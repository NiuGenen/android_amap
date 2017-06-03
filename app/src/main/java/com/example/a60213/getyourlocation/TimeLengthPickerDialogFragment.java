package com.example.a60213.getyourlocation;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by 60213 on 2017/6/3.
 */

public class TimeLengthPickerDialogFragment extends DialogFragment {

    public interface TimeLengthPickerDialogImpl{
        void onTimeLengthPickerSet(int min,int sec);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view  = inflater.inflate(R.layout.dialog_timelength, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        final EditText interval_time_input_sec_text = (EditText) view.findViewById(R.id.interval_time_input_sec_text);
        final EditText interval_time_input_min_text = (EditText) view.findViewById(R.id.interval_time_input_min_text);
        final Button set_now_button = (Button) view.findViewById(R.id.set_now_button);

        set_now_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int min = 0, sec = 0;
                Editable min_text = interval_time_input_min_text.getText();
                Editable sec_text = interval_time_input_sec_text.getText();
                if(min_text.length() > 0)
                    min = Integer.parseInt( min_text.toString() );
                if(sec_text.length() > 0)
                    sec = Integer.parseInt( sec_text.toString() );
                ((TimeLengthPickerDialogImpl)getActivity()).onTimeLengthPickerSet(min,sec);
                dismiss();
            }
        });

        return view;
    }
}
