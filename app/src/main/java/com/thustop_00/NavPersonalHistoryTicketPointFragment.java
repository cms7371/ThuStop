package com.thustop_00;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.thustop_00.databinding.FragmentNavPersonalHistoryTicketBinding;
import com.thustop_00.databinding.FragmentNavPersonalHistoryTicketPointBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavPersonalHistoryTicketPointFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavPersonalHistoryTicketPointFragment extends FragmentBase {
    FragmentNavPersonalHistoryTicketPointBinding binding;
    int year, month, day;


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

        //DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, 2020,11,15);
        /*dialog.setContentView(R.id.datetime_dialog);
        DatePicker datePicker = dialog.findViewById(R.id.dialogDatePicker);
        LinearLayout datePickerHeader = (LinearLayout) datePicker.getChildAt(0);
        datePickerHeader.setVisibility(View.GONE);
        dialog.setCustomTitle(binding.stroke);*/
        //dialog.show();

    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(getActivity().getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
        }
    };


}