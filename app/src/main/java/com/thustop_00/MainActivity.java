package com.thustop_00;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.thustop_00.databinding.ActivityMainBinding;
import com.thustop_00.intro.IntroBaseFragment;


/*
 * Copyright (c) 2019 by ThusTop INC. all rights reserved
 * - Project name : ThuStop
 * - First written by Minseok Chae and Suwon Lee students of SungKyunKwan Univ, College of
 *   Information and Communication Engineering(ICE) and developers of ThusTop INC
 * - Created on 2020.07.28 / Last revision on 0000.00.00
 * - Version Beta 00
 * - Description : Android application for TheStop beta service.
 * - Update history(Please enter when other developer modifies this project.)
 *   - 20.07.28. Created as version Beta 00
 */


public class MainActivity extends AppCompatActivity {
    /* Bind activity_main as variable*/
    private ActivityMainBinding binding;
    /* Handler for delay of splash fragment. It should be removed after loading delay added*/
    Handler H = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Link activity_main as binding, with doing setContentView(R.layout.activity_main);*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        /* At start, display splash fragment during loading*/
        setFragment(SplashFragment.newInstance());

        H.postDelayed(new Runnable() {
            @Override
            public void run() {
                setFragment(IntroBaseFragment.newInstance());
            }
        }, 1000);
    }


    /* Change fragment of frame in activity_main.*/
    public void setFragment(Fragment fr) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction()
                    .replace(R.id.frMain, fr)
                    .commit();
        } catch (IllegalStateException ignore) { }
    }
}