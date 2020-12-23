package com.thustop_00;

public interface OnFragmentInteractionListener {
    int INVISIBLE = 0;
    int GREEN_HAMBURGER = 1;
    int GREEN_BACK = 2;
    int GREEN_BACK_EXIT = 3;
    int WHITE_HAMBURGER = 4;
    int WHITE_BACK = 5;
    int WHITE_BACK_EXIT = 6;
    void addFragment(FragmentBase fr);
    void addFragmentNotBackStack(FragmentBase fr);
    void setFragment(FragmentBase fr);
    void setOnBackPressedListener(MainActivity.onBackPressedListener listener);
    void finishActivity();

    void openDrawer();
    void closeDrawer();
    void lockDrawer(boolean isLocked);
    void setToolbarStyle(int state, String title);
    void hideKeyBoard();

    void showLocationServiceSettingDialog();
    boolean checkLocationServicesStatus();
    void setGPSServiceStatus(boolean isEnabled);
    boolean getGPSServiceStatus();


}
