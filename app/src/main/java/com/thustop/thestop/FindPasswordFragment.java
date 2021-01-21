package com.thustop.thestop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.thustop.databinding.FragmentFindPasswordBinding;

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
        _listener.setToolbarStyle(_listener.GREEN_HAMBURGER, "비밀번호 찾기");


        return binding.getRoot();
    }
}