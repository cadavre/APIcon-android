package com.cadavre.APIcon;

import android.os.Build;
import com.cadavre.APIcon.activities.SonabisService;
import com.cadavre.APIcon.activities.TestActivity;
import com.squareup.okhttp.OkHttpClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.android.AndroidApacheClient;
import retrofit.client.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Very basic HttpClient implementation to execute Request into Response.
 * Uses best available Client existing in project.
 *
 * @author Seweryn Zeman
 * @version alpha
 */
public class HttpClient implements Client {

    private Client client;

    private static boolean isOAuth2InProgress = false;

    public HttpClient() {

        client = getBestAvailableClient();
    }

    @Override
    public Response execute(final Request request) throws IOException {

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
        if (response.getStatus() == 401) {
            SonabisService.OAuth2 oauth = TestActivity.restAdapter.create(SonabisService.OAuth2.class);
            oauth.getTokenWithUserCredentials(
                    "2_2vg6yqabu7ggwc4oscgswcwwogw0cc08w08k080g0koggsosgg",
                    "1kybihzq182s0c4kc0c8ko44wg4o0w4ocg8cosso0o40gs4cgo",
                    "password",
                    "abba",
                    "abba",
                    new Callback<OAuth2Data>() {

                        @Override
                        public void success(OAuth2Data data, Response response) {

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

    private class CustomOkClient extends OkClient {

        private final OkHttpClient client;

        public CustomOkClient() {

            client = new OkHttpClient();
        }

        @Override
        protected HttpURLConnection openConnection(Request request) throws IOException {

            HttpURLConnection connection = client.open(new URL(request.getUrl()));
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            return connection;
        }
    }
}
