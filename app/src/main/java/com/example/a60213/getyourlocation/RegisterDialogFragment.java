package com.example.a60213.getyourlocation;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by 60213 on 2017/6/3.
 */

public class RegisterDialogFragment extends DialogFragment {

    private Handler handler = new Handler();

    private AnimationSet username_anima;
    private AnimationSet passwd_anima;
    private int anima_size = 20;

    private void create_anima_username(float x,float y){
        username_anima = new AnimationSet(true);
        Animation a1 = new TranslateAnimation(
                x,x + anima_size,
                y,y);
        a1.setDuration(100);
        a1.setFillAfter (true);
        Animation a2 = new TranslateAnimation(
                x + anima_size,x - 2*anima_size,
                y,y);
        a2.setDuration(200);
        a2.setFillAfter (true);
        Animation a3 = new TranslateAnimation(
                x - 2*anima_size,x,
                y,y);
        a3.setDuration(100);
        a3.setFillAfter (true);
        username_anima.addAnimation( a1 );
        username_anima.addAnimation( a2 );
        username_anima.addAnimation( a3 );
    }
    private void create_anima_passwd(float x,float y){
        passwd_anima = new AnimationSet(true);
        Animation a1 = new TranslateAnimation(
                x,x + anima_size,
                y,y);
        a1.setDuration(100);
        a1.setFillAfter (true);
        Animation a2 = new TranslateAnimation(
                x + anima_size,x - 2*anima_size,
                y,y);
        a2.setDuration(200);
        a2.setFillAfter (true);
        Animation a3 = new TranslateAnimation(
                x - 2*anima_size,x,
                y,y);
        a3.setDuration(100);
        a3.setFillAfter (true);
        passwd_anima.addAnimation( a1 );
        passwd_anima.addAnimation( a2 );
        passwd_anima.addAnimation( a3 );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_register, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        final EditText username_input = (EditText)view.findViewById(R.id.reg_username_input);
        final EditText passwd_input = (EditText)view.findViewById(R.id.reg_passwd_input);
        final Button username_clean = (Button)view.findViewById(R.id.reg_username_input_clean_button);
        final Button passwd_clean = (Button)view.findViewById(R.id.reg_passwd_input_clean_button);
        final Button reg_now_button = (Button)view.findViewById(R.id.reg_now_button);

        create_anima_username(username_input.getX(),username_input.getY() );
        //username_input.setAnimation( username_anima );
        create_anima_passwd(passwd_input.getX(),passwd_input.getY() );
        //passwd_input.setAnimation( passwd_anima );

        username_clean.setVisibility(View.INVISIBLE);
        passwd_clean.setVisibility(View.INVISIBLE);

        username_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    username_clean.setVisibility(View.VISIBLE);
                }
                else{
                    username_clean.setVisibility(View.INVISIBLE);
                }
            }
        });
        username_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_input.setText("");
            }
        });

        passwd_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    passwd_clean.setVisibility(View.VISIBLE);
                }
                else{
                    passwd_clean.setVisibility(View.INVISIBLE);
                }
            }
        });
        passwd_clean.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                passwd_input.setText("");
            }
        });

        reg_now_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = username_input.getText().toString();
                final String passwd = passwd_input.getText().toString();
                Log.i(RegisterDialogFragment.class.toString(),
                        "username = " + username + " passwd = " + passwd
                );
                if(username.length()==0 || passwd.length()==0){
                    username_input.startAnimation(username_anima);
                    passwd_input.startAnimation(passwd_anima);
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean reg = AppServer.getInstance().Register(username,passwd);
                        if(reg){
                            getDialog().dismiss();
                        }
                        else{
                            Log.d("RegDialog.Reg",username +" " +passwd);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    username_input.startAnimation(username_anima);
                                    passwd_input.startAnimation(passwd_anima);
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        return view;
    }
/*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View registerView = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_register, null);

        Dialog dia = new Dialog(getActivity());
        dia.setTitle("Register");
        dia.setContentView(registerView);
        return dia;
    }
    */
}
