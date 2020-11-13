package com.thustop_00;

import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thustop_00.databinding.FragmentNavPersonalHistoryTicketBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavPersonalHistoryTicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavPersonalHistoryTicketFragment extends FragmentBase {

    FragmentNavPersonalHistoryTicketBinding binding;

    public NavPersonalHistoryTicketFragment() {
        // Required empty public constructor
    }


    public static NavPersonalHistoryTicketFragment newInstance(String param1, String param2) {
        NavPersonalHistoryTicketFragment fragment = new NavPersonalHistoryTicketFragment();
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
        binding=FragmentNavPersonalHistoryTicketBinding.inflate(inflater);
        binding.setNavPersonalHistoryTicketFrag(this);

        return binding.getRoot();

    }

    public void onTransformInfoClick(View view) {
        Log.d("ㅇㅇ","눌렸음");
        NoticeTicketPointDialog noticeTicketPointDialog = new NoticeTicketPointDialog(getContext());
        noticeTicketPointDialog.show();
    }


}