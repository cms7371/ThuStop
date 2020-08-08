package com.thustop_00;

public interface OnFragmentInteractionListener {
    void addFragment(FragmentBase fr);
    void addFragmentNotBackStack(FragmentBase fr);
    void setFragment(FragmentBase fr);

    void showActionBar(boolean b);
    void openDrawer();
    void setTitle(String s, String color);
}
