package com.thustop_00;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.thustop_00.databinding.FragmentBoardingApplicationBinding;
import com.thustop_00.model.Route;

public class BoardingApplicationFragment extends FragmentBase{
    private FragmentBoardingApplicationBinding binding;
    private Route route;
    private int boarding_stop_num;
    private int alighting_stop_num;


    public static BoardingApplicationFragment newInstance(Route route){
        BoardingApplicationFragment fragment = new BoardingApplicationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.route = route;
        fragment.boarding_stop_num = route.boarding_stops.size();
        fragment.alighting_stop_num = route.alighting_stops.size();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBoardingApplicationBinding.inflate(inflater);
        binding.setBoardingApplicationFrag(this);

        return binding.getRoot();
    }
}
