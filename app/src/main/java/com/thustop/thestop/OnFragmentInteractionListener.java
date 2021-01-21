package com.thustop.thestop;

import androidx.annotation.IntDef;

public interface OnFragmentInteractionListener {

    @IntDef({INVISIBLE, GREEN_HAMBURGER, GREEN_BACK, GREEN_BACK_EXIT, WHITE_HAMBURGER, WHITE_BACK, WHITE_BACK_EXIT})
    public @interface ToolbarType {
    }

    public final static int INVISIBLE = 0;
    public final static int GREEN_HAMBURGER = 1;
    public final static int GREEN_BACK = 2;
    public final static int GREEN_BACK_EXIT = 3;
    public final static int WHITE_HAMBURGER = 4;
    public final static int WHITE_BACK = 5;
    public final static int WHITE_BACK_EXIT = 6;

    void addFragment(FragmentBase fr);

    void addFragmentNotBackStack(FragmentBase fr);

    void setFragment(FragmentBase fr);

    void setOnBackPressedListener(MainActivity.onBackPressedListener listener);

    void finishActivity();

    void openDrawer();

    void closeDrawer();

    void lockDrawer(boolean isLocked);

    void setToolbarStyle(@ToolbarType int state, String title);

    void hideKeyBoard();

    void showLocationServiceSettingDialog();

    boolean checkLocationServicesStatus();

    void setGPSServiceStatus(boolean isEnabled);

    Boolean getGPSServiceStatus();

    int covertDPtoPX(int dp);


}
