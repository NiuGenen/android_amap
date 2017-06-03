package com.example.a60213.getyourlocation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import java.util.Calendar;

/**
 * Created by 60213 on 2017/6/3.
 */

public class DatePickerDialogFragment extends DialogFragment {

    public interface DatePickerDialogImpl{
        DatePickerDialog.OnDateSetListener getDatePickerDialogListener();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Calendar ca = Calendar.getInstance();
        return new DatePickerDialog(getActivity(),
                ((DatePickerDialogImpl)getActivity()).getDatePickerDialogListener(),
                ca.get(Calendar.YEAR),
                ca.get(Calendar.MONTH),
                ca.get(Calendar.DAY_OF_MONTH));
    }
}
