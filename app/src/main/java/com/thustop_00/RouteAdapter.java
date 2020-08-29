package com.thustop_00;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thustop_00.model.Route;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Route> data;
    private static final int VIEW_TYPE_BUTTON = 0;
    private static final int VIEW_TYPE_TITLE = 1;
    private static final int VIEW_TYPE_ROUTE= 2;

    RouteAdapter(List<Route> in, Context ct) {
        this.data = in;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_BUTTON) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_route, parent, false);
            return new ButtonViewHolder(itemView);
        } else if (viewType == VIEW_TYPE_TITLE) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
            return new TitleViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);
            return new RouteViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_BUTTON;
        } else if (position == 1) {
            return VIEW_TYPE_TITLE;
        } else {
            return VIEW_TYPE_ROUTE;
        }
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 5;
        } else {
            return data.size() + 1;
        }
    }

    static class ButtonViewHolder extends RecyclerView.ViewHolder {
        protected Button button;
        protected OnFragmentInteractionListener _listener;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            this.button = (Button) itemView.findViewById(R.id.bt_new_route);
            _listener = (OnFragmentInteractionListener) itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //_listener.setFragment();
                }
            });

        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;


        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.tv_route_title);
        }
    }

    static class RouteViewHolder extends RecyclerView.ViewHolder {
       public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
