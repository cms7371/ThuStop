package com.thustop_00;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

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