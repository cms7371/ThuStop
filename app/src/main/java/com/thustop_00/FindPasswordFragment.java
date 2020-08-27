package com.thustop_00;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thustop_00.databinding.FragmentFindPasswordBinding;
import com.thustop_00.databinding.FragmentRegisterBinding;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindPasswordFragment extends FragmentBase {
    private FragmentFindPasswordBinding binding;

    public FindPasswordFragment() {
        // Required empty public constructor
    }


    public static FindPasswordFragment newInstance() {
        FindPasswordFragment fragment = new FindPasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFindPasswordBinding.inflate(inflater);
        ButterKnife.bind(this,binding.getRoot());
        //_listener.showActionBar(true);
        _listener.setToolbar(true, false, false);
        _listener.setTitle("비밀번호 찾기");


        return binding.getRoot();
    }
}