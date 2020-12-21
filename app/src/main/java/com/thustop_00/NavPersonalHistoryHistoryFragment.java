package com.thustop_00;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thustop_00.databinding.FragmentNavPersonalHistoryHistoryBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavPersonalHistoryHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavPersonalHistoryHistoryFragment extends FragmentBase {
    FragmentNavPersonalHistoryHistoryBinding binding;

    public static NavPersonalHistoryHistoryFragment newInstance(String param1, String param2) {
        NavPersonalHistoryHistoryFragment fragment = new NavPersonalHistoryHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavPersonalHistoryHistoryBinding.inflate(inflater);
        binding.setNavPersonalHistoryHistoryFrag(this);

        RecyclerView historyRecycler = binding.rvHistory;
        return binding.getRoot();

    }


}