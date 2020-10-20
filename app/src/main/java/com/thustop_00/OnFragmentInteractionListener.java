package com.thustop_00;

public interface OnFragmentInteractionListener {
    void addFragment(FragmentBase fr);
    void addFragmentNotBackStack(FragmentBase fr);
    void setFragment(FragmentBase fr);
    void setOnBackPressedListener(MainActivity.onBackPressedListener listener);

    void showToolbarVisibility(boolean b);
    void openDrawer();
    void closeDrawer();
    void lockDrawer(boolean isLocked);
    void setTitle(boolean isMainTitle,String s);
    void setToolbarStyle(boolean white, boolean back_en);
    void hideKeyBoard();

}
