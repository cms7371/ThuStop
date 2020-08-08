package com.thustop_00;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.thustop_00.databinding.FragmentAgreeTermsBinding;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Agree_TermsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Agree_TermsFragment extends FragmentBase {
    FragmentAgreeTermsBinding binding;
    @OnClick(R.id.bt_agree)
    void goLogin() {
        _listener.setFragment(RegisterFragment.newInstance());
    }

    public Agree_TermsFragment() {
        // Required empty public constructor
    }


    public static Agree_TermsFragment newInstance() {
        Agree_TermsFragment fragment = new Agree_TermsFragment();
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
        binding = FragmentAgreeTermsBinding.inflate(inflater);
        ButterKnife.bind(this,binding.getRoot());
        _listener.showActionBar(false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}