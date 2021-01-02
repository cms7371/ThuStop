package com.thustop_00;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
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
    private DatePicker datePicker;
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

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);



        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year_picker, int month_picker, int day_picker) {
                year = year_picker;
                month = month_picker;
                day = day_picker;
            }
        });

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
