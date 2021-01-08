package com.thustop_00;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.FirebaseApp;
import com.kakao.util.maps.helper.Utility;
import com.pixplicity.easyprefs.library.Prefs;
import com.thustop_00.databinding.ActivityMainBinding;
import com.thustop_00.intro.IntroBaseFragment;

import java.util.Objects;


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


public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    /* Bind activity_main as variable*/
    private ActivityMainBinding binding;
    private Menu menu;
    private Toolbar toolbar;
    private ActionBar actionbar;
    /* Static variable indicates back button is available*/
    public static boolean isBackEnabled = false;
    public static boolean isExitEnabled = false;
    /* Handler for delay of splash fragment. It should be removed after loading delay added*/
    Handler H = new Handler(Looper.getMainLooper());
    //for checking first run
    private static final int LOCATION_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    //GPS 권한 허용과 위치서비스 활용이 모두 켜져 있어야 true
    private static Boolean GPSServiceStatus = null;

    private onBackPressedListener BackListener;
    private final static String TAG = "MainActivity";


    //private long pressedTime = 0;
    public interface onBackPressedListener {
        void onBack();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Link activity_main as binding, with doing setContentView(R.layout.activity_main);*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivityMain(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // edittext view layout problem
        /* Setting toolbar referred  https://blog.naver.com/qbxlvnf11/221328098468*/
        toolbar = binding.tbMain;
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        assert actionbar != null; // To prevent warning from setDisplayShowTitleEnabled
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(false);
        toolbar.setNavigationIcon(R.drawable.icon_hamburger_white);
        /* At start, display splash fragment during loading*/
        actionbar.hide();
        setFragment(SplashFragment.newInstance());
        FirebaseApp.initializeApp(this);
        Log.d(TAG, "onCreate: 로그인키 " + Prefs.getString(Constant.LOGIN_KEY, null));
        if(!(Prefs.getString(Constant.LOGIN_KEY, "").isEmpty()))
            Log.d(TAG, "onCreate: FCM 등록 시도");
            Util.registerDevice();
        Log.d(TAG, "onCreate: GPSServiceStatus is " + GPSServiceStatus);
        checkFirstRun();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GPSServiceStatus = null;
    }

    /* Toolbar hamburger button click listener*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (isBackEnabled) onBackPressed();
                else openDrawer();
                return true;
            }
            case R.id.bt_notification: {
                if (isExitEnabled) setFragment(MainFragment.newInstance());
                else Toast.makeText(getApplicationContext(), "알림버튼 눌림", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /*back button listener*/
    public void setOnBackPressedListener(onBackPressedListener listener) {
        BackListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else if (BackListener != null) {
            BackListener.onBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }

    public void onNavigationExitClick(View view) {
        closeDrawer();
    }

    public void onLoginClick(View view) {
        addFragment(LoginFragment.newInstance());
        closeDrawer();
    }

    public void onFreeTicketClick(View view) {
        closeDrawer();
        addFragment(FreeTicketIntroFragment.newInstance());
    }

    public void onNavPersonalHistoryClick(View view) {
        closeDrawer();
        addFragment(NavPersonalHistoryFragment.newInstance());
    }

    public void onNavPersonalPointClick(View view) {
        closeDrawer();
        addFragment(NavPointFragment.newInstance());
    }

    public void onNavSettingClick(View view) {
        closeDrawer();
        addFragment(NavSettingFragment.newInstance());
    }

    public void onNavFAQClick(View view) {
        closeDrawer();
        addFragment(NavServiceFAQFragment.newInstance());
    }

    public void testToolbarLogin(View view) {
        binding.clDrawerHeadGuest.setVisibility(View.GONE);
        binding.clDrawerHeadMember.setVisibility(View.VISIBLE);
        binding.clMyPage.setVisibility(View.VISIBLE);
        binding.btLogout.setVisibility(View.VISIBLE);
        binding.tvLogout.setVisibility(View.VISIBLE);
    }

    public void onCounselClick(View view) {
        addFragment(CounsellingFragment.newInstance());
    }

    public void checkFirstRun() {
        boolean isFirstRun = Prefs.getBoolean(Constant.FIRST_RUN, true);
        if (isFirstRun) {
            Prefs.putBoolean(Constant.FIRST_RUN, false);
            H.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setFragment(IntroBaseFragment.newInstance());
                }
            }, 1000);

        } else {
            H.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setFragment(MainFragment.newInstance());
                }
            }, 1000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: 권한 결과 확인 함수 호출");
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == REQUIRED_PERMISSIONS.length) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;
            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            if (check_result) {
                Log.d(TAG, "onRequestPermissionsResult: GPS 권환 허용됨, 위치 설정 체크");
                //위치 값을 가져올 수 있음
                if (!checkLocationServicesStatus()) {
                    showLocationServiceSettingDialog();
                } else {
                    setGPSServiceStatus(true);
                }
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(this, "위치 기반 서비스를 이용하실 수 없습니다. 앱을 다시 실행하여 권한을 허용해주세요.", Toast.LENGTH_LONG).show();
                    setGPSServiceStatus(false);
                } else {
                    Toast.makeText(this, "위치 기반 서비스를 이용하실 수 없습니다. 설정(앱 정보)에서 권한을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                    setGPSServiceStatus(false);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_ENABLE_REQUEST_CODE) {
            if (checkLocationServicesStatus()) {
                setGPSServiceStatus(true);
            } else {
                setGPSServiceStatus(false);
                Toast.makeText(this, "위치 서비스가 비활성화 되어 있으면 위치 기반 서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /***********************************************************************************************
     ----------------------OnFragmentInteractionLister override 메소드들------------------------------
     ***********************************************************************************************/


    /*쌓여있는 BackStack 모두 날리고 fragment 바꿀때*/
    @Override
    public void setFragment(FragmentBase fr) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction()
                    .replace(R.id.fr_main, fr)
                    .commit();
        } catch (IllegalStateException ignore) {
        }
    }
    /*BackStack 쌓으면서 fragment 바꿀 때*/

    @Override
    public void addFragment(FragmentBase fr) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fr_main, fr)
                .addToBackStack(null)
                .commit();
    }
    /*BackStack 쌓지 않고 fragment 바꿀 때(돌아올 필요가 없는 fragment 갈 때*/

    @Override
    public void addFragmentNotBackStack(FragmentBase fr) {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fr_main, fr)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }
    @Override
    public void lockDrawer(boolean isLocked) {
        if (isLocked) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }
    /**
     * setToolbarStyle 메소드
     * - toolbarState : 이름 그대로 _listener에 정의된 변수 이용하여 하면 됨
     * - title : 공백 시 없음, null 시 메인 타이틀, 입력하면 그대로 제목이 됨
     */
    @Override
    public void setToolbarStyle(int toolbarState, String title) {
        //제목을 설정하는 부분 null일 시 메인 타이틀
        if (title == null) {
            findViewById(R.id.iv_title).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_title).setVisibility(View.GONE);
        } else {
            findViewById(R.id.iv_title).setVisibility(View.GONE);
            findViewById(R.id.tv_title).setVisibility(View.VISIBLE);
            binding.tvTitle.setText(title);
        }
        //툴바 설정하는 부분
        isExitEnabled = false;
        isBackEnabled = false;
        actionbar.show();
        switch (toolbarState) {
            case INVISIBLE:
                actionbar.hide();
            case GREEN_HAMBURGER:
                toolbar.setBackground(getDrawable(R.color.Primary));
                binding.tvTitle.setTextColor(getResources().getColor(R.color.White));
                toolbar.setNavigationIcon(R.drawable.icon_hamburger_white);
                menu.getItem(0).setIcon(R.drawable.icon_notification_white);
                menu.getItem(0).setEnabled(true);
                menu.getItem(0).setVisible(true);
                break;
            case GREEN_BACK:
                toolbar.setBackground(getDrawable(R.color.Primary));
                binding.tvTitle.setTextColor(getResources().getColor(R.color.White));
                toolbar.setNavigationIcon(R.drawable.icon_back_white);
                menu.getItem(0).setEnabled(false);
                menu.getItem(0).setVisible(false);
                isBackEnabled = true;
                break;
            case GREEN_BACK_EXIT:
                toolbar.setBackground(getDrawable(R.color.Primary));
                binding.tvTitle.setTextColor(getResources().getColor(R.color.White));
                toolbar.setNavigationIcon(R.drawable.icon_back_white);
                menu.getItem(0).setIcon(R.drawable.icon_exit_white);
                menu.getItem(0).setEnabled(true);
                menu.getItem(0).setVisible(true);
                isBackEnabled = true;
                isExitEnabled = true;
                break;
            case WHITE_HAMBURGER:
                toolbar.setBackground(getDrawable(R.color.White));
                binding.tvTitle.setTextColor(getResources().getColor(R.color.Primary));
                toolbar.setNavigationIcon(R.drawable.icon_hamburger_green);
                menu.getItem(0).setIcon(R.drawable.icon_notification_green);
                menu.getItem(0).setEnabled(true);
                menu.getItem(0).setVisible(true);
                break;
            case WHITE_BACK:
                toolbar.setBackground(getDrawable(R.color.White));
                binding.tvTitle.setTextColor(getResources().getColor(R.color.Primary));
                toolbar.setNavigationIcon(R.drawable.icon_back_green);
                menu.getItem(0).setEnabled(false);
                menu.getItem(0).setVisible(false);
                isBackEnabled = true;
                break;
            case WHITE_BACK_EXIT:
                toolbar.setBackground(getDrawable(R.color.White));
                binding.tvTitle.setTextColor(getResources().getColor(R.color.Primary));
                toolbar.setNavigationIcon(R.drawable.icon_back_green);
                menu.getItem(0).setIcon(R.drawable.icon_exit);
                menu.getItem(0).setEnabled(true);
                menu.getItem(0).setVisible(true);
                isBackEnabled = true;
                isExitEnabled = true;
                break;

        }
    }

    @Override
    public void hideKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // TODO:위의 hide key board의 역할? 루트추가에서만 사용되는데 키보드 열린거 닫히진 않음, 그래서 아래 코드 추가함

    // when touch other space, hide opened keyboard
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                if (focusView instanceof EditText)
                    ((EditText)focusView).setCursorVisible(false);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //위치 권한 설정 activity 실행하는 메서드
    @Override
    public void showLocationServiceSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" + "위치 서비스 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callLocationSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callLocationSettingIntent, LOCATION_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                setGPSServiceStatus(false);
                Toast.makeText(getBaseContext(), "위치 서비스가 비활성화 되어 있으면 위치 기반 서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    @Override
    public void setGPSServiceStatus(boolean isEnabled) {
        GPSServiceStatus = isEnabled;
        Log.d(TAG, "setGPSLocationServiceStatus: 위치 설정, GPS 권한 상태 : " + isEnabled);
    }

    @Override
    public Boolean getGPSServiceStatus() {
        return GPSServiceStatus;
    }

    @Override
    public int covertDPtoPX(int dp) {
        float density = this.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    private void getKeyHash(){
        Log.d(TAG, "getKeyHash: " + Utility.getKeyHash(this));
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