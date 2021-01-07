package com.thustop_00;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thustop_00.databinding.FragmentNavPersonalHistoryHistoryBinding;
import com.thustop_00.model.Ticket;
import com.thustop_00.widgets.NotoTextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavPersonalHistoryHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavPersonalHistoryHistoryFragment extends FragmentBase {
    FragmentNavPersonalHistoryHistoryBinding binding;
    private ArrayList<Ticket> historyData = new ArrayList<>();

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
        HistoryRecyclerAdapter historyAdapter = new HistoryRecyclerAdapter(getContext(), historyData);
        binding.rvHistory.setAdapter(historyAdapter);

        return binding.getRoot();

    }


    private class HistoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<Ticket> historyData = new ArrayList<>();
        private Context context;

        HistoryRecyclerAdapter(Context context, ArrayList<Ticket> history) {
            this.historyData=history;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
            return new historyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        public class historyViewHolder extends RecyclerView.ViewHolder{
            public NotoTextView tvCondition;
            public NotoTextView tvDeparture;
            public NotoTextView tvAlighting;
            public NotoTextView tvTime;
            public historyViewHolder(@NonNull View itemView) {
                super(itemView);
                this.tvCondition = itemView.findViewById(R.id.tv_condition);
                this.tvAlighting = itemView.findViewById(R.id.tv_alighting);
                this.tvDeparture = itemView.findViewById(R.id.tv_departure);
                this.tvTime = itemView.findViewById(R.id.tv_time);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        _listener.addFragment(NavPersonalHistoryHistoryDetailFragment.newInstance());
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }

    }



}