package com.thustop.thestop;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.thustop.R;
import com.thustop.databinding.FragmentUnsubscribeBinding;

public class UnsubscribeFragment extends FragmentBase {
    FragmentUnsubscribeBinding binding;


    public UnsubscribeFragment() {
        // Required empty public constructor
    }

    //TODO:아이디 정보 연결
    public static UnsubscribeFragment newInstance() {
        UnsubscribeFragment fragment = new UnsubscribeFragment();
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
        binding = FragmentUnsubscribeBinding.inflate(inflater);

        binding.cbUnsubs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    binding.tvUnsubsNext.setBackgroundColor(getContext().getResources().getColor(R.color.Primary));
                    binding.tvUnsubsNext.setClickable(true);
                } else {
                    binding.tvUnsubsNext.setBackgroundColor(getContext().getResources().getColor(R.color.AchCF));
                    binding.tvUnsubsNext.setClickable(false);
                }
            }
        });


        binding.tvUnsubsNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _listener.addFragment(UnsubscribeReasonFragment.newInstance());
            }
        });

        binding.tvUnsubsNext.setClickable(false);
        _listener.setToolbarStyle(_listener.WHITE_BACK_EXIT, "탈퇴하기");



        return binding.getRoot();
    }


}