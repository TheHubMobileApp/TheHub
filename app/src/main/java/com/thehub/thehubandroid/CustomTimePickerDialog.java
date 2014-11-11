package com.thehub.thehubandroid;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CustomTimePickerDialog extends TimePickerDialog {

    private final static int TIME_PICKER_INTERVAL = 5;
    private TimePicker timePicker;
    private final OnTimeSetListener callback;

    public CustomTimePickerDialog(Context context, OnTimeSetListener callBack,
                                  int hourOfDay, int minute, boolean is24HourView) {
        super(context, callBack, hourOfDay, minute / TIME_PICKER_INTERVAL,
                is24HourView);
        this.callback = callBack;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (callback != null && timePicker != null) {
            timePicker.clearFocus();
            callback.onTimeSet(timePicker, timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
        }
    }

    @Override
    protected void onStop() {
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            this.timePicker = (TimePicker) findViewById(timePickerField
                    .getInt(null));
            Field field = classForid.getField("minute");

            NumberPicker mMinuteSpinner = (NumberPicker) timePicker
                    .findViewById(field.getInt(null));
            mMinuteSpinner.setMinValue(0);
            mMinuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<String>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            mMinuteSpinner.setDisplayedValues(displayedValues
                    .toArray(new String[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


//public class CustomTimePickerDialog extends TimePickerDialog {
//
//    public static final int TIME_PICKER_INTERVAL = 5;
//    private boolean mIgnoreEvent=false;
//
//    public CustomTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
//        super(context, callBack, hourOfDay, minute, is24HourView);
//    }
//
//    @Override
//    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
//        super.onTimeChanged(timePicker, hourOfDay, minute);
//        if (!mIgnoreEvent){
//            minute = getRoundedMinute(minute);
//            mIgnoreEvent=true;
//            timePicker.setCurrentMinute(minute);
//            mIgnoreEvent=false;
//        }
//    }
//
//    public static  int getRoundedMinute(int minute){
//        if(minute % TIME_PICKER_INTERVAL != 0){
//            int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
//            minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
//            if (minute == 60)  minute=0;
//        }
//
//        return minute;
//    }
//
//    //    @Override
////    public void onAttachedToWindow() {
////        super.onAttachedToWindow();
////        try {
////            Class<?> classForid = Class.forName("com.android.internal.R$id");
////            Field timePickerField = classForid.getField("timePicker");
////            this.timePicker = (TimePicker) findViewById(timePickerField
////                    .getInt(null));
////            Field field = classForid.getField("minute");
////
////            NumberPicker mMinuteSpinner = (NumberPicker) timePicker
////                    .findViewById(field.getInt(null));
////            mMinuteSpinner.setMinValue(0);
////            mMinuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
////            List<String> displayedValues = new ArrayList<String>();
////            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
////                displayedValues.add(String.format("%02d", i));
////            }
////            mMinuteSpinner.setDisplayedValues(displayedValues
////                    .toArray(new String[0]));
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////    }
//}