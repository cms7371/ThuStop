package com.thustop_00;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CustomRegionSelectorDialog extends Dialog {
    StateAdapter stateAdapter;


    public CustomRegionSelectorDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.region_select_dialog);
        String[] States = getContext().getResources().getStringArray(R.array.state);
        stateAdapter = new StateAdapter(States);
        RecyclerView rv = findViewById(R.id.rv_state);
        rv.setAdapter(stateAdapter);
    }

    private class StateAdapter extends RecyclerView.Adapter<StateAdapter.ListViewHolder>{
        private String[] data;
        private int focus = -1;

        StateAdapter(String[] states){
            this.data = states;
        }
        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_region, parent, false);
            return new ListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            holder.tv.setText(data[position]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    focus = position;
                    notifyDataSetChanged();
                }
            });
            // TODO : 딜레이 줘서 오동작 방지하기
            if (position == focus) {
                holder.tv.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                holder.tv.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
            }
        }


        @Override
        public int getItemCount() {
            return data.length;
        }

        public class ListViewHolder extends RecyclerView.ViewHolder {
            protected TextView tv;
            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                this.tv = (TextView) itemView.findViewById(R.id.tv_region);
            }
        }
    }
}
