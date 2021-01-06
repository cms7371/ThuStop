package com.thustop_00;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Switch;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.thustop_00.databinding.FragmentNavPersonalHistoryTicketBinding;
import com.thustop_00.model.Ticket;
import com.thustop_00.widgets.NotoTextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavPersonalHistoryTicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavPersonalHistoryTicketFragment extends FragmentBase {
    public static final int  모집중= 0;
    public static final int 대기중 = 1;
    public static final int 운행중 = 2;

    FragmentNavPersonalHistoryTicketBinding binding;
    private TicketRecyclerAdapter mAdapter;
    private ArrayList<Ticket> mArrayList;
    private ArrayList<Integer> type;

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
        mArrayList = new ArrayList<>();
        mAdapter = new TicketRecyclerAdapter(getContext(), mArrayList, false);
        //TODO: 타입 어떻게 받을 지 결정 필요
        type = new ArrayList<Integer>();
        type.add(모집중);
        Log.d("1", String.valueOf(type.get(0)));
        type.add(대기중);
        Log.d("1", String.valueOf(type.get(1)));
        type.add(운행중);
        Log.d("1", String.valueOf(type.get(2)));

        binding.rvTicket.setAdapter(mAdapter);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(binding.rvTicket);
        if (mAdapter.getItemCount() != 0)
        settingView(0);

        SnapPagerListener mListener = new SnapPagerListener(pagerSnapHelper, SnapPagerListener.ON_SETTLED, false, new SnapPagerListener.OnChangeListener() {
            @Override
            public void onSnapped(int position) {
                settingView(position);
            }
        });

        binding.rvTicket.addOnScrollListener(mListener);

        setIndicator(binding.rvTicket.getAdapter().getItemCount());
        return binding.getRoot();
    }

    public void setIndicator(int numTicket) {
        if (numTicket > 1) {
            //binding.rvTicket.addItemDecoration(new RecyclerIndicator());
            binding.rvTicket.addItemDecoration(new RecyclerIndicator(R.color.Ach97, R.color.AchCF, 32, 6, 10));
        } else {
            binding.rvTicket.setPadding(0,0,0,0);
        }
    }
    public void onTicketPointClick(View view) {
        _listener.addFragment(NavPersonalHistoryTicketPointFragment.newInstance());
    }
    public void onTransformInfoClick(View view) {
        Log.d("ㅇㅇ","눌렸음");
        NoticeTicketPointDialog noticeTicketPointDialog = new NoticeTicketPointDialog(getContext());
        noticeTicketPointDialog.show();
    }

    public void settingView(int position){
        Log.d("겟", String.valueOf(type.get(position)));
        switch (type.get(position)) {
            case 0 :
                Log.d("됨", "0");
                binding.clRecruit.setVisibility(View.VISIBLE);
                binding.clBeforePayment.setVisibility(View.GONE);
                binding.clCompletePayment.setVisibility(View.GONE);
                break;
            case 1 :
                binding.clRecruit.setVisibility(View.GONE);
                binding.clBeforePayment.setVisibility(View.VISIBLE);
                binding.clCompletePayment.setVisibility(View.GONE);
                Log.d("됨", "1");
                break;
            case 2 :
                binding.clRecruit.setVisibility(View.GONE);
                binding.clBeforePayment.setVisibility(View.GONE);
                binding.clCompletePayment.setVisibility(View.VISIBLE);
                Log.d("됨", "2");
                break;
        }
    }




}