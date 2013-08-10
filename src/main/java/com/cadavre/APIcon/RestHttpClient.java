package com.cadavre.APIcon;

import android.os.Build;
import com.cadavre.APIcon.exception.ServerAuthorizationRequiredException;
import com.cadavre.APIcon.exception.UserAuthorizationRequiredException;
import com.squareup.okhttp.OkHttpClient;
import retrofit.android.AndroidApacheClient;
import retrofit.client.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Very basic RestHttpClient implementation to execute Request into Response.
 * Uses best available Client existing in project.
 *
 * @author Seweryn Zeman
 * @version alpha
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

        boolean needAuthorization = false;
        RequestRebuilder rebuilder = null;
        ApiServerAuthorization apiAuthorization = null;

        // get endpoints that need authorization...
        Collection<Pattern> authEndpointPatterns = APIcon.getInstance().
            getServer().getEndpointsRequiringAuthorization().values();
        // ...and if there are some - check for authorization requirement
        if (!authEndpointPatterns.isEmpty()) {
            Logger.d("Found endpoints with authorization required");

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
        Response response = null;

        // if we don't need authorization - go on - execute request...
        if (!needAuthorization) {
            readyRequest = request;
            response = client.execute(readyRequest);
        }
        // ...but if we need authorization and we don't have tools to authorize - don't even bother
        else if (apiAuthorization == null) {
            throw new ServerAuthorizationRequiredException();
        }
        // if we need authorization - authorize request
        else if (needAuthorization) {
            // if current auth data tells we will fail with request for sure
            if (!apiAuthorization.isAuthDataSufficient()) {

                // try to get new auth data and check its result
                boolean renewalSucceeded = apiAuthorization.tryRenewAuthData();
                if (!renewalSucceeded) {
                    // if we couldn't renew - there's nothing else left to do
                    throw new UserAuthorizationRequiredException();
                }
            }

            // if we got here - either we have valid auth data or fresh, just received auth data
            rebuilder.setHeader(new Header(
                apiAuthorization.getHeaderName(),
                apiAuthorization.getHeaderValue())
            );
            readyRequest = rebuilder.rebuild();
            response = client.execute(readyRequest);
        }

        /*
         * In this point we've made anything we could to make a successful request becoming
         * response we've wanted to receive. Now analyze executed response...
         */

        // tutaj sprawdzić czy Response nie ma wybrakowanego albo starego OAuth2,
        // jeśli tak zrobić request w międzyczasie i wysłać request jeszcze raz
        // otrzymując nowego Bearera, które można użyć w ponownym wykonaniu execute()

        // if (isOAuth2Error && !isOAuth2InProgress) {
        /*if (response.getStatus() == 401) {
            SonabisService.OAuth2 oauth = TestActivity.restAdapter.create(SonabisService.OAuth2.class);
            oauth.getTokensWithUserCredentials(
                    "2_2vg6yqabu7ggwc4oscgswcwwogw0cc08w08k080g0koggsosgg",
                    "1kybihzq182s0c4kc0c8ko44wg4o0w4ocg8cosso0o40gs4cgo",
                    "abba",
                    "abba",
                    new Callback<OAuth2ResponseData>() {

                        @Override
                        public void success(OAuth2ResponseData data, Response response) {

                            // podmiana headera z autoryzacją
                            List<Header> newHeaders = new ArrayList<Header>();
                            newHeaders.addAll(request.getHeaders());

                            for (Header header : newHeaders) {
                                if (header.getName().equals("Authorization")) {
                                    newHeaders.remove(header);
                                    break;
                                }
                            }
                            newHeaders.add(new Header("Authorization", "Bearer " + data.getAccessToken()));

                            Request newRequest = new Request(request.getMethod(), request.getUrl(), newHeaders,
                                    request.getBody());
                            try {
                                Response newResponse = client.execute(newRequest);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });

        }*/

        if (response == null) {
            throw new RuntimeException("Unknown reason of Response being null");
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
