package com.thustop_00;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thustop_00.model.Ticket;
import com.thustop_00.widgets.NotoTextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Ticket> historyData = new ArrayList<>();
    private Context context;
    private OnHistoryRecyclerSelectedListener selectedListener;

    HistoryRecyclerAdapter(Context context, ArrayList<Ticket> history, OnHistoryRecyclerSelectedListener listener) {
        this.historyData=history;
        this.context = context;
        this.selectedListener = listener;
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
                    selectedListener.onItemSelected(view, getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return historyData.size();
    }

    public interface OnHistoryRecyclerSelectedListener {
        void onItemSelected(View view, int position);
    }


}
