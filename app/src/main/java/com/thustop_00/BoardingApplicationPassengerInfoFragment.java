package com.thustop_00;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.thustop_00.databinding.FragmentBoardingApplicationBinding;
import com.thustop_00.databinding.FragmentBoardingApplicationPassengerInfoBinding;
import com.thustop_00.model.Route;
import com.thustop_00.model.Ticket;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoardingApplicationPassengerInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardingApplicationPassengerInfoFragment extends FragmentBase {
    private FragmentBoardingApplicationPassengerInfoBinding binding;
    private Route route;
    private int boarding_stop_position;
    private int alighting_stop_position;
    private int year, month, day;
    private String date;
    private Ticket ticket;



    public static BoardingApplicationPassengerInfoFragment newInstance(Route route, int boarding_stop_position, int alighting_stop_position) {
        BoardingApplicationPassengerInfoFragment fragment = new BoardingApplicationPassengerInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.route = route;
        fragment.boarding_stop_position=boarding_stop_position;
        fragment.alighting_stop_position=alighting_stop_position;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBoardingApplicationPassengerInfoBinding.inflate(inflater);
        binding.setBoardingApplicationPassengerInfoFrag(this);
        binding.etvFbapiName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 1 ) {
                    if(binding.etvFbapiBoardingDate.length() > 0) {
                        binding.btFbapiOk.setBackgroundColor(getResources().getColor(R.color.Primary));
                        binding.btFbapiOk.setEnabled(true);
                    }
                } else {
                    binding.btFbapiOk.setBackgroundColor(getResources().getColor(R.color.ButtonGray));
                    binding.btFbapiOk.setEnabled(false);
                }
            }
        });
        binding.etvFbapiBoardingDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0 ) {
                    if(binding.etvFbapiName.length() > 1) {
                        binding.btFbapiOk.setBackgroundColor(getResources().getColor(R.color.Primary));
                        binding.btFbapiOk.setEnabled(true);
                    }
                } else {
                    binding.btFbapiOk.setBackgroundColor(getResources().getColor(R.color.ButtonGray));
                    binding.btFbapiOk.setEnabled(false);
                }
            }
        });
        binding.tvFbapiBoardingStop.setText(route.getBoardingStopName(boarding_stop_position));
        binding.tvFbapiAlightingStop.setText(route.getAlightingStopName(alighting_stop_position));
        _listener.setToolbarStyle(_listener.GREEN_BACK, "탑승 신청");
        return binding.getRoot();
    }

    public void onCalendarClick(View view) {
        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(getContext());
        datePickerDialog.setDialogListener(new CustomDatePickerDialog.CustomDatePickerDialogListener() {
            @Override
            public void onOkClick(int year_picker, int month_picker, int day_picker) {
                year = year_picker;
                month = month_picker+1;
                day = day_picker;
                date = String.format("%d/%d/%d", year , month, day);
                binding.etvFbapiBoardingDate.setTextColor(getResources().getColor(R.color.TextBlack));
                binding.etvFbapiBoardingDate.setText(date);

            }
        });
        datePickerDialog.show();
    }

    public void onBtOkClick(View view) {
        _listener.addFragment(BoardingApplicationCheckFragment.newInstance(route.getBoardingStopName(boarding_stop_position), route.getAlightingStopName(alighting_stop_position)));

    }


}