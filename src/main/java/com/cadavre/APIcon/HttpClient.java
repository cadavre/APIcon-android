package com.cadavre.APIcon;

import android.os.Build;
import retrofit.android.AndroidApacheClient;
import retrofit.client.*;

import java.io.IOException;

/**
 * Very basic HttpClient implementation to execute Request into Response.
 * Uses best available Client existing in project.
 *
 * @author Seweryn Zeman
 * @version 1
 */
public final class HttpClient implements Client {

    private Client client;

    public HttpClient() {

        client = getBestAvailableClient();
    }

    @Override
    public Response execute(Request request) throws IOException {

        return client.execute(request);
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

        // if necessary can be passed as an argument to new OkClient()
        // OkHttpClient rawClient = new OkHttpClient();

        return new OkClient();
    }
}
