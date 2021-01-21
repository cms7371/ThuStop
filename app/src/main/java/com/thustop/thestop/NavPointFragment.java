package com.thustop.thestop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.thustop.R;
import com.thustop.databinding.FragmentNavPointBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavPointFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavPointFragment extends FragmentBase implements MainActivity.onBackPressedListener{
    FragmentNavPointBinding binding;

    public NavPointFragment() {
        // Required empty public constructor
    }


    public static NavPointFragment newInstance() {
        NavPointFragment fragment = new NavPointFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavPointBinding.inflate(inflater);
        binding.setNavPointFrag(this);
        _listener.setToolbarStyle(_listener.WHITE_BACK_EXIT, "포인트");

        _listener.setOnBackPressedListener(this);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_point, container, false);
    }

    @Override
    public void onBack() {
        _listener.setFragment(MainFragment.newInstance());
        // TODO : 딜레이 넣어서 자연스럽게 만들기
        _listener.openDrawer();
    }
}