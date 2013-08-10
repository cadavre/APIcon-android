package com.cadavre.APIcon.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.cadavre.APIcon.*;
import retrofit.RestAdapter;

public class TestActivity extends Activity {

    public static RestAdapter restAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /*ApiServer server = new ApiServer("http://api.sonabis.com"); // create a server with authorization or no
        server.addServiceInterface(SonabisService.class); // add your interfaces
        APIcon.initialize(server); // initialize APIcon*/

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ApiServer server = new ApiServer("http://api.sonabis.com/app_dev.php/oauth/v2");
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

        OAuth2Service service = APIcon.getInstance().getService(OAuth2Service.class);

        OAuth2ResponseData data = service.getTokensWithClientCredentials(
            "5_11mvv7ktaa40owgsk44gwwgw0o8c4ck000cw4wos0gokkcco4g",
            "3ga5gbwk4r8ko4wckckswwsw44080w8wgkwgscwsgko844w8ks"
        );
        Log.i("APIcon", "actk=" + data.getAccessToken());

        /*SonabisService api = APIcon.getInstance().getService(SonabisService.class);
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
        });*/
    }
}
