package com.thustop_00.intro;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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

        String str = binding.tvIntroPage1.getText().toString();
        String coloredStr = getString(R.string.tv_intro_page1_green);
        Spannable span = (Spannable)binding.tvIntroPage1.getText();
        int index_s = str.indexOf(coloredStr);
        int index_e = index_s+coloredStr.length();

        span.setSpan(new ForegroundColorSpan(Color.parseColor("#64bb74")),index_s,index_e, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

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
