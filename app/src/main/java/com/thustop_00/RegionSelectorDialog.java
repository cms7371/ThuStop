package com.thustop_00;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;


public class RegionSelectorDialog extends Dialog {
    private String[] states = getContext().getResources().getStringArray(R.array.state);
    private RegionSelectorListener listener;
    private CityAdapter cityAdapter;
    private int stateFocus = 0;
    private long clickedTime = System.currentTimeMillis();
    private int clickInterval;
    private boolean isErrorOccurred = false;


    public RegionSelectorDialog(@NonNull Context context) {
        super(context);
    }

    public interface RegionSelectorListener {
        void onSelect(String state, String city);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_region_select);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        String[] States = getContext().getResources().getStringArray(R.array.state);
        StateAdapter stateAdapter = new StateAdapter(States);
        cityAdapter = new CityAdapter();
        RecyclerView rvState = findViewById(R.id.rv_state);
        RecyclerView rvCity = findViewById(R.id.rv_city);
        rvState.setAdapter(stateAdapter);
        rvCity.setAdapter(cityAdapter);
    }

    public void setDialogListener(RegionSelectorListener listener) {
        this.listener = listener;
    }

    private class StateAdapter extends RecyclerView.Adapter<StateAdapter.ListViewHolder> {
        private String[] data;

        StateAdapter(String[] states) {
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
                    clickInterval = (int) (System.currentTimeMillis() - clickedTime);
                    clickedTime = System.currentTimeMillis();
                    if (clickInterval > 50) {
                        stateFocus = position;
                        cityAdapter.updateCityList();
                        notifyDataSetChanged();
                    }
                }
            });
            if (position == stateFocus) {
                holder.tv.setTextColor(getContext().getResources().getColor(R.color.Primary));
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
                this.tv = itemView.findViewById(R.id.tv_region);
            }
        }
    }

    private class CityAdapter extends RecyclerView.Adapter<CityAdapter.ListViewHolder> {
        private String[] cities;

        CityAdapter(){
            this.cities = getCities(stateFocus);
        }


        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_region, parent, false);
            return new ListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            holder.tv.setText(cities[position]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isErrorOccurred){
                        listener.onSelect(states[stateFocus], cities[position]);
                        dismiss();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return cities.length;
        }

        public class ListViewHolder extends RecyclerView.ViewHolder {
            protected TextView tv;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                this.tv = itemView.findViewById(R.id.tv_region);
            }
        }

        public void updateCityList(){
            this.cities = getCities(stateFocus);
            notifyDataSetChanged();
        }
    }

    public String[] getCities(int position) {
        String state = states[position];
        isErrorOccurred = false;
        switch (state) {
            case "서울":
                return getStringArray(R.array.seoul);
            case "경기":
                return getStringArray(R.array.gyeonggi);
            default:
                isErrorOccurred = true;
                return getStringArray(R.array.error);
        }
    }

    public String[] getStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }
}
