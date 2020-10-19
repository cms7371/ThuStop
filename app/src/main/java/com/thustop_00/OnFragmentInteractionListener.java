package com.thustop_00;

public interface OnFragmentInteractionListener {
    void addFragment(FragmentBase fr);
    void addFragmentNotBackStack(FragmentBase fr);
    void setFragment(FragmentBase fr);
    void setOnBackPressedListener(MainActivity.onBackPressedListener listener);

    void showActionBar(boolean b);
    void openDrawer();
    void setTitle(boolean isMainTitle,String s);
    void setToolbar(boolean white, boolean back_en);
    void hideKeyBoard();

}
