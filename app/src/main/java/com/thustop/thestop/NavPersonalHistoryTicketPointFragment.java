package com.thustop.thestop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.thustop.databinding.FragmentNavPersonalHistoryTicketPointBinding;
import com.thustop.thestop.model.Ticket;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavPersonalHistoryTicketPointFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavPersonalHistoryTicketPointFragment extends FragmentBase  {
    FragmentNavPersonalHistoryTicketPointBinding binding;
    private Ticket ticket;
    private Calendar start, end;
    private ArrayList<ArrayList<String>> calendarList = new ArrayList<>();
    private int year, month, day;
    private int point;
    private String refundDate;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public NavPersonalHistoryTicketPointFragment() {

    }


    public static NavPersonalHistoryTicketPointFragment newInstance(Ticket ticket) {
        NavPersonalHistoryTicketPointFragment fragment = new NavPersonalHistoryTicketPointFragment();
        Bundle args = new Bundle();
        fragment.ticket = ticket;
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

        try {
            Date startDate = dateFormat.parse(ticket.start_date);
            start.setTime(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            Date endDate = dateFormat.parse(ticket.end_date);
            end.setTime(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return binding.getRoot();
    }

    public void onDatePickerClick(View view) {

        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(getContext(), start, end);
        datePickerDialog.setDialogListener(new CustomDatePickerDialog.CustomDatePickerDialogListener() {

            @Override
            public void onOkClick(int year_picker, int month_picker, int day_picker) {

                refundDate = String.format("%d-%02d-%02d", year_picker, month_picker, day_picker);
                //임시로 포인트 처리
                point = 1300;
                binding.tvYearValue.setText(String.valueOf(year_picker));
                binding.tvMonthValue.setText(String.valueOf(month_picker));
                binding.tvDayValue.setText(String.valueOf(day_picker));
                binding.tvPointValue.setText(String.format("%d P", point));
            }
        });
        datePickerDialog.show();

    }

    public void onConfirmClick(View view) {
        TicketPointConfirmDialog ticketPointConfirmDialog = new TicketPointConfirmDialog(getContext(), year, month, day, point);
        ticketPointConfirmDialog.setDialogListener(new TicketPointConfirmDialog.TicketPointConfirmListener() {
            @Override
            public void onConfirmClick() {
                //TODO:포인트 내역 추가 하기
                _listener.setFragment(DoneFragment.newInstance("티켓 포인트 전환이 완료되었습니다", "이용내역을 확인해보세요",true));
            }
        });
        ticketPointConfirmDialog.show();
    }

}