package com.example.a60213.getyourlocation;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    public void onLoginClick(View view){
        String username = username_input.getText().toString();
        String passwd = username_input.getText().toString();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginActivity.this, FlowingDrawerActivity.class);
                startActivity(intent);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoginActivity.this.finish();
                    }
                },1000);
            }
        },1000);
    }

    private EditText username_input;
    private EditText passwd_input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username_input = (EditText)findViewById(R.id.login_username_input);
        passwd_input = (EditText)findViewById(R.id.login_passwd_input);
    }
}
