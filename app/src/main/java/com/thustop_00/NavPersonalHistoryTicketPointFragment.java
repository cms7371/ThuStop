package com.thustop_00;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.thustop_00.databinding.FragmentNavPersonalHistoryTicketBinding;
import com.thustop_00.databinding.FragmentNavPersonalHistoryTicketPointBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavPersonalHistoryTicketPointFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavPersonalHistoryTicketPointFragment extends FragmentBase implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    FragmentNavPersonalHistoryTicketPointBinding binding;
    int year, month, day;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd;


    public NavPersonalHistoryTicketPointFragment() {
        // Required empty public constructor
    }


    public static NavPersonalHistoryTicketPointFragment newInstance() {
        NavPersonalHistoryTicketPointFragment fragment = new NavPersonalHistoryTicketPointFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentNavPersonalHistoryTicketPointBinding.inflate(inflater);
        binding.setNavPersonalHistoryTicketPointFrag(this);

        return binding.getRoot();
    }

    public void onDatePickerClick(View view) {
        Calendar now = Calendar.getInstance();
        if (dpd == null) {
            dpd = DatePickerDialog.newInstance(
                    NavPersonalHistoryTicketPointFragment.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        } else {
            dpd.initialize(
                    NavPersonalHistoryTicketPointFragment.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        }

        // restrict to weekdays only
        ArrayList<Calendar> weekdays = new ArrayList<Calendar>();
        Calendar day = Calendar.getInstance();
        for (int i = 0; i < 30 ; i++) {
            if (day.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && day.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                Calendar d = (Calendar) day.clone();
                weekdays.add(d);
            }
            day.add(Calendar.DATE, 1);
        }
        Calendar[] weekdayDays = weekdays.toArray(new Calendar[weekdays.size()]);
        dpd.setSelectableDays(weekdayDays);

        dpd.setOnCancelListener(dialog -> {
            Log.d("DatePickerDialog", "Dialog was cancelled");
            dpd = null;
        });
        dpd.show(getParentFragmentManager(), "Datepickerdialog");


        /***
        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(getContext());
        datePickerDialog.setDialogListener(new CustomDatePickerDialog.CustomDatePickerDialogListener() {

            @Override
            public void onOkClick(int year_picker, int month_picker, int day_picker) {
                year = year_picker;
                month = month_picker+1;
                day = day_picker;
                binding.tvYearValue.setText(String.valueOf(year));
                binding.tvMonthValue.setText(String.valueOf(month));
                binding.tvDayValue.setText(String.valueOf(day));
            }
        });
        datePickerDialog.show();
        ***/

    }




    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        binding.tvYearValue.setText(String.valueOf(year));
        binding.tvMonthValue.setText(String.valueOf(monthOfYear+1));
        binding.tvDayValue.setText(String.valueOf(dayOfMonth));
        dpd = null;
    }
}