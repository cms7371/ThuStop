package com.thustop_00.intro;


import android.content.Context;
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
import com.thustop_00.databinding.FragmentIntroPage3Binding;
import com.thustop_00.widgets.NotoButton;
import com.thustop_00.widgets.NotoTextView;

public class IntroPage3Fragment extends Fragment {
    private Context mContext;
    FragmentIntroPage3Binding binding;
    public void setContext(Context context){
        this.mContext = context;
    }
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

    public void colorText(NotoTextView textView, int strAddress, String color) {
        String str = textView.getText().toString();
        String coloredStr = getString(strAddress);

        Spannable span = (Spannable)textView.getText();
        int index_s=str.indexOf(coloredStr);

        int index_e = index_s+coloredStr.length();

        span.setSpan(new ForegroundColorSpan(Color.parseColor(color)),index_s,index_e, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


}
