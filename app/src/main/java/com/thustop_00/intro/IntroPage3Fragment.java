package com.thustop_00.intro;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.thustop_00.FragmentBase;
import com.thustop_00.R;
import com.thustop_00.databinding.FragmentIntroPage3Binding;

public class IntroPage3Fragment extends FragmentBase {
    /* Bind fragment_intro_page3 as variable*/
    FragmentIntroPage3Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro_page3, container, false);

        colorText(binding.tvIntroPage3, R.string.tv_intro_page3_green1, "#64bb74");
        colorText(binding.tvIntroPage3, R.string.tv_intro_page3_green2, "#64bb74");

        return binding.getRoot();
    }

    /*This method returns new instance of this fragment*/
    public static IntroPage3Fragment newInstance() {
        Bundle args = new Bundle();
        IntroPage3Fragment fragment = new IntroPage3Fragment();
        fragment.setArguments(args);
        return fragment;
    }

}
