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
import com.thustop_00.databinding.FragmentIntroPage3Binding;
import com.thustop_00.widgets.NotoTextView;

public class IntroPage3Fragment extends Fragment {
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

    /* This method colors specific words in TextView*/
    public void colorText(NotoTextView textView, int strAddress, String color) {
        /* Get the string of the view to be colored */
        String str = textView.getText().toString();
        /* Get string to be colored from address */
        String coloredStr = getString(strAddress);
        /* Instantiate spannable to color view's string*/
        Spannable span = (Spannable)textView.getText();
        /* Find position of start and end of coloredStr on str */
        int index_s=str.indexOf(coloredStr);
        int index_e = index_s+coloredStr.length();
        /* Color the view's string with upper variables */
        span.setSpan(new ForegroundColorSpan(Color.parseColor(color)),index_s,index_e, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    }


}
