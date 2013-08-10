package com.cadavre.APIcon.examples;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.cadavre.APIcon.OnUserAuthorizationListener;
import com.cadavre.APIcon.R;

/**
 *
 */
public class TestOnUserAuthListener extends OnUserAuthorizationListener {

    private final Context context;

    public TestOnUserAuthListener(Context context) {

        this.context = context;
    }

    @Override
    public void onAuthorizationRequired() {

        Dialog dialog = new Dialog(context) {

            @Override
            protected void onCreate(Bundle savedInstanceState) {

                super.onCreate(savedInstanceState);
                setContentView(R.layout.dialog);

                Button btn = (Button) findViewById(R.id.ok);
                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        String username = ((EditText) findViewById(R.id.username)).getText().toString();
                        String password = ((EditText) findViewById(R.id.password)).getText().toString();

                        Bundle params = new Bundle();
                        params.putString("username", username);
                        params.putString("password", password);
                        processAuthorization(params);
                    }
                });
            }
        };
        dialog.show();
    }

    @Override
    public void onSuccess() {

        Toast.makeText(context, "success in user auth! you can now manually resend request!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure() {

        Toast.makeText(context, "failure in user auth!", Toast.LENGTH_SHORT).show();
    }
}
