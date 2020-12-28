package com.thustop_00;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.thustop_00.databinding.FragmentNavPersonalHistoryTicketBinding;
import com.thustop_00.model.Ticket;
import com.thustop_00.widgets.NotoTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavPersonalHistoryTicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavPersonalHistoryTicketFragment extends FragmentBase {

    FragmentNavPersonalHistoryTicketBinding binding;
    private NavTicketAdapter mAdapter;
    private ArrayList<Ticket> mArrayList;

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
        mAdapter = new NavTicketAdapter(mArrayList);
        binding.rvTicket.setAdapter(mAdapter);

        return binding.getRoot();

    }
    public void onTicketPointClick(View view) {
        _listener.addFragment(NavPersonalHistoryTicketPointFragment.newInstance());
    }
    public void onTransformInfoClick(View view) {
        Log.d("ㅇㅇ","눌렸음");
        NoticeTicketPointDialog noticeTicketPointDialog = new NoticeTicketPointDialog(getContext());
        noticeTicketPointDialog.show();
    }

    private class NavTicketAdapter extends RecyclerView.Adapter<NavTicketAdapter.TicketViewHolder> {
        private ArrayList<Ticket> tickets;
        NavTicketAdapter(ArrayList<Ticket> in) {
            this.tickets = in;
        }

        @NonNull
        @Override
        public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_ticket, parent, false);
            TicketViewHolder viewHolder = new TicketViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return tickets.size();
        }

        public class TicketViewHolder extends RecyclerView.ViewHolder {
            protected NotoTextView busCode;
            protected NotoTextView departureTime;
            protected NotoTextView departure;
            protected NotoTextView destination;
            protected NotoTextView destinationTime;
            protected NotoTextView capacity;
            protected NotoTextView ticketState;
            protected ProgressBar pb_capacity;


            public TicketViewHolder(@NonNull View itemView) {
                super(itemView);
                this.busCode = (NotoTextView) itemView.findViewById(R.id.tv_ticket_bus_code);
                this.departure = (NotoTextView) itemView.findViewById(R.id.tv_departure);
                this.departureTime = (NotoTextView) itemView.findViewById(R.id.tv_departure_time);
                this.destination = (NotoTextView) itemView.findViewById(R.id.tv_destination);
                this.destinationTime = (NotoTextView) itemView.findViewById(R.id.tv_departure_time);
                this.capacity = (NotoTextView) itemView.findViewById(R.id.tv_capacity);
                this.ticketState = (NotoTextView) itemView.findViewById(R.id.tv_ticket_state);
                this.pb_capacity = (ProgressBar) itemView.findViewById(R.id.pb_personnel);

            }
        }
    }


}