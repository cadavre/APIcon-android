package com.cadavre.APIcon.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.cadavre.APIcon.*;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TestActivity extends Activity {

    public static RestAdapter restAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callAPITest();
            }
        });
    }

    private void callAPITest() {

        restAdapter = new RestAdapter.Builder()
                .setLog(new Logger())
                .setDebug(true)
                .setServer(SonabisService.BASE_URL)
                .setClient(new HttpClient())
                .setRequestInterceptor(new OAuth2Interceptor())
                .setConverter(new SymfonyGsonConverter())
                        // .setErrorHandler(new RetrofitErrorHandler()) works only with synchronous Requests
                .build();

        SonabisService.API api = restAdapter.create(SonabisService.API.class);
        SonabisService.OAuth2 oauth = restAdapter.create(SonabisService.OAuth2.class);

        api.getGame(new Callback<User>() {

            @Override
            public void success(User user, Response response) {

                Toast.makeText(TestActivity.this, "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(TestActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
