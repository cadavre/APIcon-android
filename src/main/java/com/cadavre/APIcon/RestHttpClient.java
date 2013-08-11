package com.cadavre.APIcon;

import android.os.Build;
import com.cadavre.APIcon.exception.ServerAuthorizationRequiredException;
import com.cadavre.APIcon.exception.UserAuthorizationRequiredException;
import com.squareup.okhttp.OkHttpClient;
import retrofit.RetrofitError;
import retrofit.android.AndroidApacheClient;
import retrofit.client.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Very basic RestHttpClient implementation to execute Request into Response.
 * Uses best available Client existing in project.
 *
 * This Client also takes care of authorization process if ApiServerAuthorization was declared in APIcon.
 * How it works:
 * 1. On adding services to ApiServer, every @Authorization annotated method is mapped.
 * 2. If map size is greater than 0, we are checking if current path needs authorization.
 * 2a If no, we are just executing an request.
 * 2b If yes we are checking local conditions that helps to resolve if we got enough data to send request.
 * 3a If we have conditions - we are executing authorized request.
 * 3b If we don't - we are trying to get fresh auth data from server.
 * 4a If data can be received without user interaction - we do it.
 * 4b If we need user interaction - we end client execution of request,
 * BUT we are calling OnUserAuthorizationListener if it's not null.
 *
 * At the end of executed request, we are again checking it's results to handle auth errors.
 *
 * @author Seweryn Zeman
 * @version 1
 */
class RestHttpClient implements Client {

    private Client client;

    /**
     * Default constructor.
     */
    public RestHttpClient() {

        client = getBestAvailableClient();
    }

    @Override
    public Response execute(final Request request) throws IOException {

        // first check if network is available, cache if may
        if (!APIcon.getInstance().getNetworkChecker().isNetworkAvailable()) {
            Collection<Pattern> cacheEndpointPatterns = APIcon.getInstance().
                getServer().getCachedEndpoints().values();
            if (!cacheEndpointPatterns.isEmpty()) {

                RequestRebuilder rebuilder = new RequestRebuilder(request);
                URL url = rebuilder.parseUrl();
                boolean doCaching = false;
                for (Pattern pattern : cacheEndpointPatterns) {
                    if (pattern.matcher(url.getPath()).find()) {
                        Logger.d("Url " + request.getUrl() + " matched cache pattern " + pattern.pattern());
                        doCaching = true;
                        break;
                    }
                }

                if (doCaching) {
                    // not supported
                    /*if (!ObjectCacher.cacheObject(request)) {
                        throw RetrofitError.networkError(request.getUrl(), new SocketException("No network available," +
                            " couldn't cache request"));
                    }*/
                }
            }

            throw RetrofitError.networkError(request.getUrl(), new SocketException("No network available"));
        }

        boolean needAuthorization = false;
        RequestRebuilder rebuilder = null;
        ApiServerAuthorization apiAuthorization = null;

        // get endpoints that need authorization...
        Collection<Pattern> authEndpointPatterns = APIcon.getInstance().
            getServer().getAuthorizedEndpoints().values();
        // ...and if there are some - check for authorization requirement
        if (!authEndpointPatterns.isEmpty()) {

            // prepare copy of Request and parse it's url
            rebuilder = new RequestRebuilder(request);
            URL url = rebuilder.parseUrl();
            // URL: getProtocol()://getHost()getPath()?getQuery()

            // check if current endpoint needs authorization
            for (Pattern pattern : authEndpointPatterns) {
                if (pattern.matcher(url.getPath()).find()) {
                    Logger.d("Url " + request.getUrl() + " matched auth pattern " + pattern.pattern());
                    needAuthorization = true;
                    break;
                }
            }

            // since we know we need authorization - we get ApiServerAuthorization that comes with ApiServer
            if (needAuthorization) {
                apiAuthorization = APIcon.getInstance().getServer().getAuthorization();
            }
        }

        // since here we start with request executions
        Request readyRequest;
        Response response;

        // if we don't need authorization - go on - execute request...
        if (!needAuthorization) {
            Logger.d("Executing request without authorization");
            readyRequest = request;
            response = client.execute(readyRequest);
        }
        // ...but if we need authorization and we don't have tools to authorize - don't even bother
        else if (apiAuthorization == null) {
            throw new ServerAuthorizationRequiredException();
        }
        // if we need authorization - authorize request
        else {
            // if current auth data tells we will fail with request for sure
            if (!apiAuthorization.isAuthDataSufficient()) {

                Logger.d("Trying to renew auth data");
                // try to get new auth data and check its result
                boolean renewalSucceeded = apiAuthorization.tryRenewAuthData();
                if (!renewalSucceeded) {
                    Logger.d("Auth data renewal failed");
                    // if we couldn't renew - there's nothing else left to do
                    throw new UserAuthorizationRequiredException();
                }
            }

            // if we got here - either we have valid auth data or fresh, just received auth data
            rebuilder.setHeader(new Header(
                apiAuthorization.getHeaderName(),
                apiAuthorization.getHeaderValue())
            );
            Logger.d("Executing request with authorization:");
            Logger.d(apiAuthorization.getHeaderValue());
            readyRequest = rebuilder.rebuild();
            response = client.execute(readyRequest);
        }

        /*
         * In this point we've made anything we could to make a successful request becoming
         * response we've wanted to receive. Now analyze executed response...
         */

        // todo check response for handlable (O)Auth(2) data

        // just for sure
        if (response == null) {
            throw RetrofitError.unexpectedError(request.getUrl(),
                new NullPointerException("Unknown reason of Response being null."));
        }

        return response;
    }

    /**
     * Get best Client available, which is (in order):
     * - OkHttpClient by Square, Inc.
     * - UrlConnectionClient by Android (API > 8)
     * - AndroidApacheClient by Android
     *
     * @return Client
     */
    private Client getBestAvailableClient() {

        Client client;
        try {
            Class.forName("com.squareup.okhttp.OkHttpClient");
            client = okClient();
        } catch (ClassNotFoundException e) {
            Logger.d("No OkHttpClient found, using Android default implementation");
            client = defaultAndroidClient();
        }

        return client;
    }

    /**
     * Get default available Android Client.
     *
     * @return Client
     */
    private Client defaultAndroidClient() {

        Client client;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            client = new AndroidApacheClient();
        } else {
            client = new UrlConnectionClient();
        }

        return client;
    }

    /**
     * Get OkHttpClient by Square, Inc.
     *
     * @return Client
     */
    private Client okClient() {

        return new CustomOkClient();
    }

    /**
     * Custom OkClient implementation that allows to change timeouts.
     */
    private class CustomOkClient extends OkClient {

        /**
         * Timeout for associating network connection. In seconds.
         */
        private static final int CONNECTION_TIMEOUT = 5;

        /**
         * Timeout for reading data from established connection. In seconds.
         */
        private static final int READ_TIMEOUT = 10;

        private final OkHttpClient client;

        /**
         * Default constructor.
         */
        public CustomOkClient() {

            client = new OkHttpClient();
        }

        @Override
        protected HttpURLConnection openConnection(Request request) throws IOException {

            HttpURLConnection connection = client.open(new URL(request.getUrl()));
            connection.setConnectTimeout(CONNECTION_TIMEOUT * 1000);
            connection.setReadTimeout(READ_TIMEOUT * 1000);
            return connection;
        }
    }
}
