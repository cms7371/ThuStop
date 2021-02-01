package com.thustop.thestop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.PagerSnapHelper;

import com.thustop.R;
import com.thustop.thestop.adapter.TicketRecyclerAdapter;
import com.thustop.databinding.FragmentNavPersonalHistoryTicketBinding;
import com.thustop.thestop.model.Ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavPersonalHistoryTicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavPersonalHistoryTicketFragment extends FragmentBase {
    public static final int RECRUITING = 0;
    public static final int WAITING_FOR_PAYMENT = 1;
    public static final int WAITING_FOR_OPERATION = 2;
    public static final int IN_OPERATION = 3;

    FragmentNavPersonalHistoryTicketBinding binding;
    private TicketRecyclerAdapter mAdapter;
    private ArrayList<Ticket> mArrayList;
    private ArrayList<Integer> type;
    private int current_position = 0;
    private List<Ticket> tickets;

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
        binding = FragmentNavPersonalHistoryTicketBinding.inflate(inflater);
        binding.setNavPersonalHistoryTicketFrag(this);
        mArrayList = new ArrayList<>();
        tickets = _listener.getTickets();
        mAdapter = new TicketRecyclerAdapter(getContext(), tickets, false);
        //TODO: 타입 어떻게 받을 지 결정 필요
        type = new ArrayList<Integer>();
        type.add(RECRUITING);
        Log.d("1", String.valueOf(type.get(0)));
        type.add(WAITING_FOR_PAYMENT);
        Log.d("1", String.valueOf(type.get(1)));
        type.add(WAITING_FOR_OPERATION);
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
                current_position = position;
            }
        });

        binding.rvTicket.addOnScrollListener(mListener);

        setIndicator(mAdapter.getItemCount());
        return binding.getRoot();
    }

    public void setIndicator(int numTicket) {
        if (numTicket > 1) {
            //binding.rvTicket.addItemDecoration(new RecyclerIndicator());
            binding.rvTicket.addItemDecoration(
                    new RecyclerIndicator(ContextCompat.getColor(requireContext(), R.color.Ach97),
                            ContextCompat.getColor(requireContext(), R.color.AchCF),
                            16, 6, 10, RecyclerIndicator.HEIGHT_DP));
        } else {
            binding.rvTicket.setPadding(0, 0, 0, 0);
        }
    }

    public void onStopChangeClick(View view) {
        _listener.addFragment(BoardingApplicationFragment.newInstance(tickets.get(current_position)));
    }

    public void onTicketPointClick(View view) {
        _listener.addFragment(NavPersonalHistoryTicketPointFragment.newInstance(tickets.get(current_position)));
    }

    public void onTransformInfoClick(View view) {
        Log.d("ㅇㅇ", "눌렸음");
        TicketPointNoticeDialog ticketPointNoticeDialog = new TicketPointNoticeDialog(getContext());
        ticketPointNoticeDialog.show();
    }

    public void settingView(int position) {
        Log.d("겟", String.valueOf(type.get(position)));
        switch (type.get(position)) {
            case RECRUITING:
                Log.d("됨", "0");
                binding.clRecruit.setVisibility(View.VISIBLE);
                binding.clBeforePayment.setVisibility(View.GONE);
                binding.clCompletePayment.setVisibility(View.GONE);
                break;
            case WAITING_FOR_PAYMENT:
                binding.clRecruit.setVisibility(View.GONE);
                binding.clBeforePayment.setVisibility(View.VISIBLE);
                binding.clCompletePayment.setVisibility(View.GONE);
                Log.d("됨", "1");
                break;
            case WAITING_FOR_OPERATION:
                binding.clRecruit.setVisibility(View.GONE);
                binding.clBeforePayment.setVisibility(View.GONE);
                binding.clCompletePayment.setVisibility(View.VISIBLE);
                Log.d("됨", "2");
                break;
        }
    }


}