package com.thustop_00;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

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


public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener{
    /* Bind activity_main as variable*/
    private ActivityMainBinding binding;
    private Menu menu;
    private Toolbar toolbar;
    private ActionBar actionbar;
    /* Static variable indicates back button is available*/
    private static boolean isbackenabled = false;
    /* Handler for delay of splash fragment. It should be removed after loading delay added*/
    Handler H = new Handler(Looper.getMainLooper());

    //private onBackPressedListener BackListener;
    //private long pressedTime = 0;
    /*public interface onBackPressedListener {
        public void onBack();
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Link activity_main as binding, with doing setContentView(R.layout.activity_main);*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        /* Setting toolbar referred  https://blog.naver.com/qbxlvnf11/221328098468*/
        toolbar = binding.tbMain;
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        assert actionbar != null; // To prevent warning from setDisplayShowTitleEnabled
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(false); //Set back button invisible to make hamburger button visible
        actionbar.setHomeAsUpIndicator(R.drawable.icon_back_green);
        toolbar.setNavigationIcon(R.drawable.icon_hamburger_white);

        /* At start, display splash fragment during loading*/
        actionbar.hide();
        setFragment(SplashFragment.newInstance());

        H.postDelayed(new Runnable() {
            @Override
            public void run() {
                setFragment(IntroBaseFragment.newInstance());
            }
        }, 1000);
    }

    /* Toolbar hamburger button click listener*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                if(isbackenabled) onBackPressed();
                else openDrawer();
                return true;
            }
            case R.id.bt_notification : {
                Toast.makeText(getApplicationContext(), "알림버튼 눌림", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    /*back button listener*/
    /*
    public void setOnBackPressedListener(onBackPressedListener listener) {
        BackListener = listener;
    }

    @Override
    public void onBackPressed() {
        if(BackListener != null) {
            BackListener.onBack();
        } else { // 두번 누르면 어플종료
            if(pressedTime == 0) {
                Toast.makeText(this,"한번 더 누르면 종료됩니다",Toast.LENGTH_SHORT);
                pressedTime=System.currentTimeMillis();
            } else {
                int seconds = (int) (System.currentTimeMillis()-pressedTime);
                if (seconds > 2000) {
                    pressedTime = 0;
                } else {
                    super.onBackPressed();;
                    finish();
                    android.os.Process.killProcess((android.os.Process.myPid()));
                }
            }
        }
    }*/

    /* This method links */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }
    /*method of OnFragmentInteractionLister*/
    /* Change fragment of frame in activity_main.*/
    @Override
    public void setFragment(FragmentBase fr) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction()
                    .replace(R.id.frMain, fr)
                    .commit();
        } catch (IllegalStateException ignore) { }
    }

    @Override
    public void addFragment(FragmentBase fr) {
    }

    @Override
    public void addFragmentNotBackStack(FragmentBase fr) {

    }

    @Override
    public void showActionBar(boolean b) {
        if(b)
            actionbar.show();
        else
            actionbar.hide();
    }

    @Override
    public void openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void setTitle(String s) {
        getSupportActionBar().show();
        binding.tvTitle.setText(s);
    }

    /* This method changes color and status of toolbar
     * If 'white' is true, background color will be white, else, green. Buttons colors will also change depend on background.
     * If 'back_en' is true, back button is enabled, hamburger button and notification button will disappear.
     * If 'main_title' is true, TextView title will be disable, and main title will be visible*/
    @Override
    public void setToolbar(boolean white, boolean back_en, boolean main_title) {
        if (white) {
            toolbar.setBackground(getDrawable(R.color.colorWhite));
            binding.tvTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
            actionbar.setHomeAsUpIndicator(R.drawable.icon_back_green);
            if (back_en) {
                actionbar.setDisplayHomeAsUpEnabled(true);
                menu.getItem(0).setEnabled(false);
                menu.getItem(0).setVisible(false);
            } else {
                actionbar.setDisplayHomeAsUpEnabled(false);
                menu.getItem(0).setEnabled(true);
                menu.getItem(0).setVisible(true);
                toolbar.setNavigationIcon(R.drawable.icon_hamburger_green);
                menu.getItem(0).setIcon(R.drawable.icon_notification_green);
            }
        } else {
            toolbar.setBackground(getDrawable(R.color.colorPrimary));
            binding.tvTitle.setTextColor(getResources().getColor(R.color.colorWhite));
            actionbar.setHomeAsUpIndicator(R.drawable.icon_back_white);
            if (back_en) {
                actionbar.setDisplayHomeAsUpEnabled(true);
                menu.getItem(0).setEnabled(false);
                menu.getItem(0).setVisible(false);
            } else {
                actionbar.setDisplayHomeAsUpEnabled(false);
                menu.getItem(0).setEnabled(true);
                menu.getItem(0).setVisible(true);
                toolbar.setNavigationIcon(R.drawable.icon_hamburger_white);
                menu.getItem(0).setIcon(R.drawable.icon_notification_white);
            }
        }
        isbackenabled = back_en;
        if (main_title) {
            findViewById(R.id.iv_title).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_title).setVisibility(View.GONE);
        } else {
            findViewById(R.id.iv_title).setVisibility(View.GONE);
            findViewById(R.id.tv_title).setVisibility(View.VISIBLE);
        }
    }


//해시키 필요할 때
/*    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            Log.e("name not found", e.toString());
        }
    }*/
}