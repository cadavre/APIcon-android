package com.cadavre.APIcon.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.cadavre.APIcon.APIcon;
import com.cadavre.APIcon.ApiServer;
import com.cadavre.APIcon.OAuth2ServerAuthorization;
import com.cadavre.APIcon.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TestActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        OAuth2ServerAuthorization serverAuth = new OAuth2ServerAuthorization(
            getApplicationContext(), "/oauth/v2", OAuth2ServerAuthorization.GRANT_CLIENT_CREDENTIALS,
            "5_11mvv7ktaa40owgsk44gwwgw0o8c4ck000cw4wos0gokkcco4g",
            "3ga5gbwk4r8ko4wckckswwsw44080w8wgkwgscwsgko844w8ks"
        ).setRefreshTokenLifetime(OAuth2ServerAuthorization.DEFAULT_REFRESH_TOKEN_LIFETIME);

        ApiServer server = new ApiServer("http://api.sonabis.com");
        server.setAuthorization(serverAuth);
        server.addServiceInterface(SonabisService.class);
        APIcon.initialize(server);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                callAPITest();
            }
        });
    }

    private void callAPITest() {

        SonabisService api = APIcon.getInstance().getService(SonabisService.class);
        api.getScore(1, "me", new Callback<User>() {

            @Override
            public void success(User user, Response response) {

                Toast.makeText(TestActivity.this, "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {

                Log.e("APIcon", "ERROR", error);
                Toast.makeText(TestActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
