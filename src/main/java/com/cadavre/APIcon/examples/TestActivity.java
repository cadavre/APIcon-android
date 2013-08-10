package com.cadavre.APIcon.examples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.cadavre.APIcon.APIcon;
import com.cadavre.APIcon.ApiServer;
import com.cadavre.APIcon.R;
import com.cadavre.APIcon.oauth2.OAuth2ServerAuthorization;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TestActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        OAuth2ServerAuthorization serverAuth = new OAuth2ServerAuthorization(
            getApplicationContext(), "/oauth/v2", OAuth2ServerAuthorization.GRANT_USER_CREDENTIALS,
            "2_2vg6yqabu7ggwc4oscgswcwwogw0cc08w08k080g0koggsosgg",
            "1kybihzq182s0c4kc0c8ko44wg4o0w4ocg8cosso0o40gs4cgo"
        ).setRefreshTokenLifetime(OAuth2ServerAuthorization.DEFAULT_REFRESH_TOKEN_LIFETIME);

        serverAuth.setOnUserAuthorizationListener(new TestOnUserAuthListener(this));

        ApiServer server = new ApiServer("http://api.sonabis.com");
        server.setAuthorization(serverAuth);
        server.addServiceInterface(TestService.class);
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

        TestService api = APIcon.getService(TestService.class);
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
