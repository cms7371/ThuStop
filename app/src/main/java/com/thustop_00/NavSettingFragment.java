package com.thustop_00;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thustop_00.databinding.FragmentNavSettingBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavSettingFragment extends FragmentBase {
    FragmentNavSettingBinding binding;


    public static NavSettingFragment newInstance() {
        NavSettingFragment fragment = new NavSettingFragment();
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
        binding=FragmentNavSettingBinding.inflate(inflater);
        binding.setNavSettingFrag(this);
        _listener.setToolbarStyle(_listener.WHITE_BACK, "설정");
        return binding.getRoot();
    }
}