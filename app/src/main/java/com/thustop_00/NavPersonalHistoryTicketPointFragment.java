package com.thustop_00;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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
public class NavPersonalHistoryTicketPointFragment extends FragmentBase  {
    FragmentNavPersonalHistoryTicketPointBinding binding;
    private Calendar start, end;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd;
    private ArrayList<ArrayList<String>> calendarList = new ArrayList<>();
    private int year, month, day;


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
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.set(2020, 0, 15);
        end.set(2021,1,26);


        return binding.getRoot();
    }

    public void onDatePickerClick(View view) {

        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(getContext(), start, end);
        datePickerDialog.setDialogListener(new CustomDatePickerDialog.CustomDatePickerDialogListener() {

            @Override
            public void onOkClick(int year_picker, int month_picker, int day_picker) {
                year = year_picker;
                month = month_picker;
                day = day_picker;
                binding.tvYearValue.setText(String.valueOf(year));
                binding.tvMonthValue.setText(String.valueOf(month));
                binding.tvDayValue.setText(String.valueOf(day));
            }
        });
        datePickerDialog.show();


    }

}