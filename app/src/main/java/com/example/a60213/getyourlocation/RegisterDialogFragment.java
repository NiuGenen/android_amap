package com.example.a60213.getyourlocation;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by 60213 on 2017/6/3.
 */

public class RegisterDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_register, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        final EditText username_input = (EditText)view.findViewById(R.id.reg_username_input);
        final EditText passwd_input = (EditText)view.findViewById(R.id.reg_passwd_input);
        final Button username_clean = (Button)view.findViewById(R.id.reg_username_input_clean_button);
        final Button passwd_clean = (Button)view.findViewById(R.id.reg_passwd_input_clean_button);
        final Button reg_now_button = (Button)view.findViewById(R.id.reg_now_button);

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
                Log.i(RegisterDialogFragment.class.toString(),
                        "username = " + username_input.getText().toString() +
                                " passwd = " + passwd_input.getText().toString()
                );
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
