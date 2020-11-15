package com.thustop_00;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;


import androidx.annotation.NonNull;

import com.thustop_00.widgets.NotoButton;
import com.thustop_00.widgets.NotoTextView;

import java.util.Calendar;
import java.util.Objects;

public class CustomDatePickerDialog extends Dialog {
    private CustomDatePickerDialogListener customDatePickerDialogListener;
    private Context context;
    private NotoTextView btOk;
    private NotoTextView btCancel;
    private CalendarView datePicker;
    private int year, month, day;

    public CustomDatePickerDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public void setDialogListener(CustomDatePickerDialog.CustomDatePickerDialogListener customDatePickerDialogListener){
        this.customDatePickerDialogListener = customDatePickerDialogListener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date_picker);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btOk = findViewById(R.id.tv_ok);
        btCancel = findViewById(R.id.tv_cancel);
        datePicker = findViewById(R.id.date_picker);
        //LinearLayout datePickerHeader = (LinearLayout) datePicker.getChildAt(0);
        //datePickerHeader.setVisibility(View.GONE);
        datePicker.setMinDate(System.currentTimeMillis());

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        datePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int picked_year, int picked_month, int picked_day) {
                year = picked_year;
                month = picked_month;
                day = picked_day;
            }
        } );

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDatePickerDialogListener.onOkClick(year,month,day);
                dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface CustomDatePickerDialogListener {
        void onOkClick(int year, int month, int day);
    }


}
