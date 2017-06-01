package com.example.a60213.getyourlocation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {

    private View mWelcomeBG;
    private View mWelcomeFG;
    private Handler handler = new Handler();
    private final Runnable jump2MainActivity = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    WelcomeActivity.this.finish();
                }
            },1000);
        }
    };
    private final Runnable jump2LoginActivity = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    WelcomeActivity.this.finish();
                }
            },1000);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mWelcomeBG.setVisibility(View.VISIBLE);
            mWelcomeFG.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        mWelcomeBG = findViewById(R.id.welcome_bg);
        mWelcomeFG = findViewById(R.id.welcome_fg);

        // Theme.NoActionBar
        //ActionBar actionBar = getSupportActionBar();
        //if (actionBar != null) {
        //    actionBar.hide();
        //}

        mWelcomeBG.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        mWelcomeFG.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        handler.postDelayed(jump2LoginActivity,3000);
    }
}
