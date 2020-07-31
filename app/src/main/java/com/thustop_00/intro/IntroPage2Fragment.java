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
import com.thustop_00.databinding.FragmentIntroPage2Binding;

public class IntroPage2Fragment extends Fragment {
    /* Bind fragment_intro_page2 as variable*/
    FragmentIntroPage2Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro_page2, container, false);
        return binding.getRoot();
    }

    /*This method returns new instance of this fragment*/
    public static IntroPage2Fragment newInstance() {
        Bundle args = new Bundle();
        IntroPage2Fragment fragment = new IntroPage2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

}
