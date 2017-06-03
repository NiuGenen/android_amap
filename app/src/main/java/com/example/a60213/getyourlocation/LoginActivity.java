package com.example.a60213.getyourlocation;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private Handler handler = new Handler();

    private Button sign_in_Button;
    private String username;
    private String passwd;
    public void onLoginClick(View view){
        username = username_input.getText().toString();
        passwd = passwd_input.getText().toString();
        sign_in_Button.setBackground( ContextCompat.getDrawable(getBaseContext(), R.drawable.rectangle_lightblue) );
        sign_in_Button.setClickable(false);
        boolean is_input_empty = false;
        if(username_input.getText().length()==0){
            username_input.startAnimation(username_anima);
            is_input_empty = true;
        }
        if(passwd_input.getText().length()==0){
            passwd_input.startAnimation(passwd_anima);
            is_input_empty = true;
        }
        if(is_input_empty){
            sign_in_Button.setBackground( ContextCompat.getDrawable(getBaseContext(), R.drawable.rectangle_lightskyblue) );
            sign_in_Button.setClickable(true);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = AppServer.getInstance().Login(username, passwd);
                if(success){
                    SharedPreferences sp = getSharedPreferences("configure.ng",MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("default_username",username);
                    ed.putString("default_passwd", passwd);
                    ed.apply();

                    Intent intent = new Intent(LoginActivity.this, FlowingDrawerActivity.class);
                    startActivity(intent);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LoginActivity.this.finish();
                        }
                    }, 1000);
                }
                else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            sign_in_Button.setBackground( ContextCompat.getDrawable(getBaseContext(), R.drawable.rectangle_lightskyblue) );
                            sign_in_Button.setClickable(true);
                            username_input.startAnimation(username_anima);
                            passwd_input.startAnimation(passwd_anima);
                        }
                    });
                    Log.w("Login.onLoginClick",username + " " + passwd);
                }
            }
        }).start();
    }

    public void onRegisterClick(View view){
        FragmentTransaction mFragTransaction = getFragmentManager().beginTransaction();
        Fragment fragment =  getFragmentManager().findFragmentByTag("registerDialog");
        if(fragment!=null){
            mFragTransaction.remove(fragment);
        }
        RegisterDialogFragment dialogFragment = new RegisterDialogFragment();
        dialogFragment.show(mFragTransaction, "registerDialog");
    }

    private EditText username_input;
    private EditText passwd_input;
    private String default_username = "username";
    private String default_passwd = "passwd";
    private String default_username_configure_key = "default_username";
    private String default_passwd_configure_key = "default_passwd";
    private Button username_input_clean;
    public void onUsernameClean(View view){
        username_input.setText("");
    }
    private Button passwd_input_clean;
    public void onPasswdClean(View view){
        passwd_input.setText("");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sign_in_Button = (Button)findViewById(R.id.login_signin_button);

        SharedPreferences sp = getSharedPreferences("configure.ng",MODE_PRIVATE);

        username_input = (EditText)findViewById(R.id.login_username_input);
        default_username = sp.getString(default_username_configure_key, default_username);
        username_input.setText(default_username );
        username_input_clean = (Button)findViewById(R.id.login_username_input_clean_button);
        username_input_clean.setVisibility(View.INVISIBLE);

        username_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    username_input_clean.setVisibility(View.VISIBLE);
                }
                else{
                    username_input_clean.setVisibility(View.INVISIBLE);
                }
            }
        });

        passwd_input = (EditText)findViewById(R.id.login_passwd_input);
        default_passwd = sp.getString(default_passwd_configure_key, default_passwd);
        passwd_input.setText(default_passwd );
        passwd_input_clean = (Button)findViewById(R.id.login_passwd_input_clean_button);
        passwd_input_clean.setVisibility(View.INVISIBLE);

        passwd_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    passwd_input_clean.setVisibility(View.VISIBLE);
                }
                else{
                    passwd_input_clean.setVisibility(View.INVISIBLE);
                }
            }
        });

        createAnimation();
    }

    private AnimationSet username_anima;
    private AnimationSet passwd_anima;
    private int anima_size = 20;
    private void createAnimation(){
        username_anima = new AnimationSet(true);
        passwd_anima = new AnimationSet(true);

        float username_x = username_input.getPivotX();
        float username_y = username_input.getPivotY();
        float passwd_x = passwd_input.getPivotX();
        float passwd_y = passwd_input.getPivotY();

        Animation username_1 = new TranslateAnimation(
                username_x,username_x + anima_size,
                username_y,username_y);
        username_1.setDuration(100);
        username_1.setFillAfter (true);
        Animation username_2 = new TranslateAnimation(
                username_x + anima_size,username_x - 2*anima_size,
                username_y,username_y);
        username_2.setDuration(200);
        username_2.setFillAfter (true);
        Animation username_3 = new TranslateAnimation(
                username_x - 2*anima_size,username_x,
                username_y,username_y);
        username_3.setDuration(100);
        username_3.setFillAfter (true);
        username_anima.addAnimation( username_1 );
        username_anima.addAnimation( username_2 );
        username_anima.addAnimation( username_3 );
        //username_input.setAnimation( username_anima );

        Animation passwd_1 = new TranslateAnimation(
                passwd_x,passwd_x + anima_size,
                passwd_y,passwd_y);
        passwd_1.setDuration(100);
        passwd_1.setFillAfter (true);
        Animation passwd_2 = new TranslateAnimation(
                passwd_x + anima_size,passwd_x - 2*anima_size,
                passwd_y,passwd_y);
        passwd_2.setDuration(200);
        passwd_2.setFillAfter (true);
        Animation passwd_3 = new TranslateAnimation(
                passwd_x - 2*anima_size,passwd_x,
                passwd_y,passwd_y);
        passwd_3.setDuration(100);
        passwd_3.setFillAfter (true);
        passwd_anima.addAnimation( passwd_1 );
        passwd_anima.addAnimation( passwd_2 );
        passwd_anima.addAnimation( passwd_3 );
        //passwd_input.setAnimation( passwd_anima );
    }
}
