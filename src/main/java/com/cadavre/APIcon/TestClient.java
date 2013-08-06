package com.cadavre.APIcon;

import android.os.Build;
import retrofit.android.AndroidApacheClient;
import retrofit.client.*;

import java.io.IOException;

/**
 * APIcon class
 *
 * @author Seweryn Zeman
 */
public class TestClient implements Client {

    @Override
    public Response execute(Request request) throws IOException {

        Client client = okClient();

        return client.execute(request);
    }

    Client defaultClient() {

        final Client client;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            client = new AndroidApacheClient();
        } else {
            client = new UrlConnectionClient();
        }

        return client;
    }

    Client okClient() {

        return new OkClient();
    }
}
