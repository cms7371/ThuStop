package com.thustop_00;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CustomTimePicker extends TimePickerDialog {
    private final static int TIME_PICKER_INTERVAL = 10;
    private TimePicker timePicker;
    private final OnTimeSetListener callback;
    public CustomTimePicker(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, listener, hourOfDay, minute/TIME_PICKER_INTERVAL, is24HourView);
        this.callback = listener;}
    public CustomTimePicker(Context context, int Res,  OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, Res, listener, hourOfDay, minute/TIME_PICKER_INTERVAL, is24HourView);
        this.callback = listener;}

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

    private boolean autoTitle = true;

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
    {
        if (autoTitle)
        {
            // Super will call a private updateTitle() method, so lets make
            // sure it has the right minute value.
            super.onTimeChanged(view, hourOfDay, minute * TIME_PICKER_INTERVAL);
        }
        else
        {
            // do nothing
        }
    }

    @Override
    public void setTitle(int id)
    {
        super.setTitle(id);
        autoTitle = (id == 0);
    }
}

