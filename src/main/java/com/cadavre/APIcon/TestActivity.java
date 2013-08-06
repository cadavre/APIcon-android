package com.cadavre.APIcon;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;

public class TestActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        callAPITest();
    }

    private void callAPITest() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(APIClient.API_URL)
                .setRequestInterceptor(new OAuth2Interceptor())
                .setClient(new TestClient())
                .setErrorHandler(new ApiErrorHandler())
                .setLog(new Logger())
                .setDebug(true)
                .build();

        APIClient.API api = restAdapter.create(APIClient.API.class);

        api.getSomething(1, "me", new Callback<List<?>>() {

            @Override
            public void success(List<?> objects, Response response) {

                Toast.makeText(TestActivity.this, "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(TestActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
