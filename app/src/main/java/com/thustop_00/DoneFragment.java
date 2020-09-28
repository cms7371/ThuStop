package com.thustop_00;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thustop_00.databinding.FragmentDoneBinding;
import com.thustop_00.databinding.FragmentRequestTownServiceBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoneFragment extends FragmentBase {

    FragmentDoneBinding binding;
    private static String colorBase="#565b59";
    private static String colorGreen="#15a474";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private String text1;
    private String text2;
    private boolean green;

    public DoneFragment() {
        // Required empty public constructor
    }


    public static DoneFragment newInstance(String text1, String text2, boolean green) {
        DoneFragment fragment = new DoneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, text1);
        args.putString(ARG_PARAM2, text2);
        args.putBoolean(ARG_PARAM3, green);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            text1 = getArguments().getString(ARG_PARAM1);
            text2 = getArguments().getString(ARG_PARAM2);
            green = getArguments().getBoolean(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDoneBinding.inflate(inflater);
        binding.setDonefrag(this);
        _listener.showActionBar(false);
        setText();
        return binding.getRoot();
    }

    public void setText() {
        if(text2.length() == 0)
            binding.tvContinue.setVisibility(View.GONE);
        binding.tvDone.setText(text1);
        binding.tvContinue.setText(text2);
        if(green)
            binding.tvContinue.setTextColor(Color.parseColor(colorGreen));
    }
}