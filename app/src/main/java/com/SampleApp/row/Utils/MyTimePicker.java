package com.SampleApp.row.Utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by USER on 10-02-2016.
 */
public class MyTimePicker {
    TimePickerDialog mTimePickerDialog;

    public interface onTimeSet {
        public void onTime(TimePicker view, int hourOfDay, int minute);
    }

    onTimeSet mOnTimeSet;

    public void setTimeListener(onTimeSet onTimeset) {
        mOnTimeSet = onTimeset;
    }

    public MyTimePicker(Context ctx) {


        mTimePickerDialog = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                mOnTimeSet.onTime(view, hourOfDay, minute);

            }
        }, 1, 1, true);
    }

    public void show() {
        Calendar c = Calendar.getInstance();
        mTimePickerDialog.updateTime(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));
        mTimePickerDialog.show();
    }
}
