package com.cadavre.APIcon;

import android.os.Build;
import com.squareup.okhttp.OkHttpClient;
import retrofit.android.AndroidApacheClient;
import retrofit.client.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Very basic RestHttpClient implementation to execute Request into Response.
 * Uses best available Client existing in project.
 *
 * @author Seweryn Zeman
 * @version alpha
 */
class RestHttpClient implements Client {

    private Client client;
    //private OAuth2Helper oAuth2Helper;

    private static boolean isOAuth2InProgress = false;

    public RestHttpClient() {

        client = getBestAvailableClient();
        //this.oAuth2Helper = APIcon.getInstance().getOAuth2Helper();
    }

    @Override
    public Response execute(final Request request) throws IOException {

        RequestRebuilder rebuilder = new RequestRebuilder(request);
        URL url = rebuilder.parseUrl();

        // todo DO THIS MORE UNIVERSAL

        // check for OAuth2 availability and set proper headers
        /*if (!oAuth2Helper.isOAuth2DataAvailable() || oAuth2Helper.isRefreshTokenExpired()) {
            //request.addHeader("X-OAuth2-Helper", "Reauthorize");
        } else if (oAuth2Helper.isAccessTokenExpired()) {
            //request.addHeader("X-OAuth2-Helper", "Refresh");
        } else {
            // todo check if endpoint needs authorization
            //request.addHeader("Authorization", "Bearer " + oAuth2Helper.getAccessToken());
        }*/

        /**
         * protocol :// host path ? query
         * ==
         * protocol :// host filename
         */

        /*
        reason = request.getHeaders().contain("X-OAuth2-Unauthorized")
        if (reason == "Wstępna autoryzacja wymagana") {
            oauth.authorize();
            isOAuth2InProgress = true;
        } else if (reason == "Pobierz access_token z refresh_token'a") {
            oauth.getAccessTokenFromRefreshToken();
            isOAuth2InProgress = true;
        }

        // tu jeszcze wstawić Bearer'a w headery i usunąć "X-OAuth2-401"
         */

        Response response = client.execute(request);
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
            Logger.i("No okHttpClient found, using Android default implementation");
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
