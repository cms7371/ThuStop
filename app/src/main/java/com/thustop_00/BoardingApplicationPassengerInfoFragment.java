package com.thustop_00;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.thustop_00.databinding.FragmentBoardingApplicationPassengerInfoBinding;
import com.thustop_00.model.Route;
import com.thustop_00.model.Ticket;

import java.util.Calendar;

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
        fragment.boarding_stop_position = boarding_stop_position;
        fragment.alighting_stop_position = alighting_stop_position;
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
                if (editable.length() > 1) {
                    if (binding.etvFbapiBoardingDate.length() > 0) {
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
                if (editable.length() > 0) {
                    if (binding.etvFbapiName.length() > 1) {
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

    //TODO: 데이트피커 변경중. 나중에 재적용 필요

    public void onCalendarClick(View view) {
        // 임시 적용 중
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.set(2021,1,27);
        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(getContext(), start, end);
        datePickerDialog.setDialogListener(new CustomDatePickerDialog.CustomDatePickerDialogListener() {
            @Override
            public void onOkClick(int year_picker, int month_picker, int day_picker) {
                year = year_picker;
                month = month_picker + 1;
                day = day_picker;
                date = String.format("%d/%d/%d", year, month, day);
                binding.etvFbapiBoardingDate.setTextColor(getResources().getColor(R.color.TextBlack));
                binding.etvFbapiBoardingDate.setText(date);
            }
        });
        datePickerDialog.show();
    }


    public void onBtOkClick(View view) {
        // TODO 확인 화면 다이얼로그로 바꾸고 결제화면 연결해야함

        BoardingApplicationCheckDialog dialog = new BoardingApplicationCheckDialog(getContext() ,route.getBoardingStopName(boarding_stop_position), route.getAlightingStopName(alighting_stop_position));
        dialog.setListener(new BoardingApplicationCheckDialog.BoardingCheckDialogListener() {
            @Override
            public void onBoardingConfirm() {
                //TODO 결제 프로세스 연결 또는 탑승 신청 완료로 연결
                _listener.setFragment(DoneFragment.newInstance("탑승 신청이 완료되었습니다.", "배차가 확정되면 푸시알림, 또는 문자를 드립니다.",true));
                Handler H = new Handler(Looper.getMainLooper());
                H.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 500);
            }
        });
        dialog.show();
    }
}