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
    private Calendar start, end;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd;
    private ArrayList<ArrayList<String>> calendarList = new ArrayList<>();


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
        calendarList = setCalendarList(start, end);

        return binding.getRoot();
    }

    public void onDatePickerClick(View view) {
        /***
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
        ***/


        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(getContext(), calendarList);
        datePickerDialog.setDialogListener(new CustomDatePickerDialog.CustomDatePickerDialogListener() {

            @Override
            public void onOkClick(int year_picker, int month_picker, int day_picker) {

            }
        });
        datePickerDialog.show();


    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        binding.tvYearValue.setText(String.valueOf(year));
        binding.tvMonthValue.setText(String.valueOf(monthOfYear+1));
        binding.tvDayValue.setText(String.valueOf(dayOfMonth));
        dpd = null;
    }

    public ArrayList<ArrayList<String>> setCalendarList(Calendar start, Calendar end) {
        ArrayList<ArrayList<String>> Calendars = new ArrayList<>();
        // 현재 날짜 셋팅
        Calendar c = Calendar.getInstance();
        // 시작일이 오늘 보다 이전이라면,
        if (start.before(c)) start = Calendar.getInstance();
        int startYear = start.get(Calendar.YEAR);
        int startMonth = start.get(Calendar.MONTH);
        int endYear = end.get(Calendar.YEAR);
        int endMonth = end.get(Calendar.MONTH);


        if (startMonth == endMonth) {
            c.set(startYear, startMonth, 1);
            int dayNum = c.get(Calendar.DAY_OF_WEEK);
            ArrayList<String> dayList = new ArrayList<String>();

            dayList.add("일");
            dayList.add("월");
            dayList.add("화");
            dayList.add("수");
            dayList.add("목");
            dayList.add("금");
            dayList.add("토");

            for(int i = 1; i < dayNum; i++) {
                dayList.add("");
            }
            int finalDate = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            Log.d("데이오브먼스", String.valueOf(finalDate));
            for (int i = 0; i < finalDate; i++) {
                dayList.add(String.valueOf(i + 1));
            }
            Calendars.add(dayList);
            dayList.add(startYear+"년 "+(startMonth+1)+"월");

            return Calendars;
        } else if(startYear == endYear) {

            for (int i = startMonth; i <= endMonth; i++) {
                c.set(startYear, i, 1);
                int dayNum = c.get(Calendar.DAY_OF_WEEK);
                ArrayList<String> dayList = new ArrayList<String>();

                dayList.add("일");
                dayList.add("월");
                dayList.add("화");
                dayList.add("수");
                dayList.add("목");
                dayList.add("금");
                dayList.add("토");

                for(int j = 1; j < dayNum; j++) {
                    dayList.add("");
                }
                int finalDate = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                Log.d("데이오브먼스", String.valueOf(finalDate));
                for (int j = 0; j < finalDate; j++) {
                    dayList.add(String.valueOf(j + 1));
                }
                Calendars.add(dayList);
                dayList.add(startYear+"년 "+(i+1)+"월");
            }

            return Calendars;

        }
        return Calendars;
    }

}