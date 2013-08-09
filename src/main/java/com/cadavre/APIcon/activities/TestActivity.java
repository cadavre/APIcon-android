package com.cadavre.APIcon.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.cadavre.APIcon.APIcon;
import com.cadavre.APIcon.ApiServer;
import com.cadavre.APIcon.R;
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

        ApiServer server = new ApiServer("http://api.sonabis.com"); // create a server with authorization or no
        server.addServiceInterface(SonabisService.class); // add your interfaces
        APIcon.initialize(server); // initialize APIcon

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
