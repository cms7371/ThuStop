package com.thustop_00;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thustop_00.databinding.FragmentNavPersonalHistoryHistoryDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavPersonalHistoryHistoryDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavPersonalHistoryHistoryDetailFragment extends FragmentBase {
    FragmentNavPersonalHistoryHistoryDetailBinding binding;

    public NavPersonalHistoryHistoryDetailFragment() {
        // Required empty public constructor
    }

    public static NavPersonalHistoryHistoryDetailFragment newInstance() {
        NavPersonalHistoryHistoryDetailFragment fragment = new NavPersonalHistoryHistoryDetailFragment();
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
        binding = FragmentNavPersonalHistoryHistoryDetailBinding.inflate(inflater);
        binding.setNavPersonalHistoryHistoryDetailFrag(this);
        _listener.setToolbarStyle(_listener.WHITE_BACK, "자세히 보기");
        return binding.getRoot();
    }
}