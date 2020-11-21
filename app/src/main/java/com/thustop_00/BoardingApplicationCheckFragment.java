package com.thustop_00;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thustop_00.databinding.FragmentBoardingApplicationCheckBinding;
import com.thustop_00.databinding.FragmentBoardingApplicationPassengerInfoBinding;
import com.thustop_00.model.Route;
import com.thustop_00.model.Ticket;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoardingApplicationCheckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardingApplicationCheckFragment extends FragmentBase {
    private FragmentBoardingApplicationCheckBinding binding;
    private String boarding_stop;
    private String alighting_stop;

    public static BoardingApplicationCheckFragment newInstance(String boarding_stop, String alighting_stop) {
        BoardingApplicationCheckFragment fragment = new BoardingApplicationCheckFragment();
        Bundle args = new Bundle();
        fragment.boarding_stop = boarding_stop;
        fragment.alighting_stop = alighting_stop;
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBoardingApplicationCheckBinding.inflate(inflater);
        binding.setBoardingApplicationCheckFrag(this);
        binding.tvFbacBoardingStop.setText(boarding_stop);
        binding.tvFbacAlightingStop.setText(alighting_stop);

        _listener.setToolbarStyle(_listener.GREEN_BACK, "");
        return binding.getRoot();
    }

    public void onBtOkClick(View view) {
        _listener.setFragment(DoneFragment.newInstance("탑승 신청이 완료되었습니다.", "배차가 확정되면 푸시알림, 또는 문자를 드립니다.",true));
    }
}