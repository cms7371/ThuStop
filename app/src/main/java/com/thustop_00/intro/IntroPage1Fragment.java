package com.thustop_00.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.thustop_00.R;
import com.thustop_00.databinding.FragmentIntroPage1Binding;

public class IntroPage1Fragment extends Fragment {

    FragmentIntroPage1Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro_page1, container, false);
        return binding.getRoot();
    }

    /*This method returns new instance of this fragment*/
    public static IntroPage1Fragment newInstance() {
        Bundle args = new Bundle();
        IntroPage1Fragment fragment = new IntroPage1Fragment();
        fragment.setArguments(args);
        return fragment;
    }

}
