package com.thustop.thestop.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.thustop.R;
import com.thustop.thestop.OnFragmentInteractionListener;
import com.thustop.thestop.model.Route;
import com.thustop.thestop.model.Ticket;

public class RouteDetailAdapter extends RecyclerView.Adapter<RouteDetailAdapter.RouteDetailHolder> {
    private Route route;
    private Ticket ticket;
    private Context context;
    private int boarding_stop_num;
    private int alighting_stop_num;
    protected OnFragmentInteractionListener _listener;


    public RouteDetailAdapter(Context context, Route route, OnFragmentInteractionListener _listener) {
        this.context = context;
        this.route = route;
        this.boarding_stop_num = route.boarding_stops.size();
        this.alighting_stop_num = route.alighting_stops.size();
        this._listener = _listener;
    }

    public RouteDetailAdapter(Context context, Ticket ticket, OnFragmentInteractionListener _listener) {
        this.context = context;
        this.route = ticket.route_obj;
        this.ticket = ticket;
        this.boarding_stop_num = route.boarding_stops.size();
        this.alighting_stop_num = route.alighting_stops.size();
        this._listener = _listener;
    }


    @Override
    public void onBindViewHolder(@NonNull RouteDetailAdapter.RouteDetailHolder holder, int position) {
        if (position < boarding_stop_num) {
            holder.tvTime.setText(route.boarding_stops.get(position).time);
            holder.tvStop.setText(route.getBoardingStopName(position));
            if (position == 0) {
                holder.tvStop.setTypeface(Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                holder.ivUpperLine.setVisibility(View.INVISIBLE);
                holder.ivDot.getLayoutParams().height = _listener.covertDPtoPX(10);
                holder.ivDot.getLayoutParams().width = _listener.covertDPtoPX(10);
            }
        } else {
            int offsetPosition = position - boarding_stop_num;
            holder.tvTime.setText(route.alighting_stops.get(offsetPosition).time);
            holder.tvStop.setText(route.getAlightingStopName(offsetPosition));
            holder.ivUpperLine.setBackground(ContextCompat.getDrawable(context, R.color.Red));
            holder.ivLowerLine.setBackground(ContextCompat.getDrawable(context, R.color.Red));
            holder.ivDot.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_via_red));
            if (offsetPosition == alighting_stop_num - 1) {
                holder.tvStop.setTypeface(Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                holder.ivLowerLine.setVisibility(View.INVISIBLE);
                holder.ivDot.getLayoutParams().height = _listener.covertDPtoPX(10);
                holder.ivDot.getLayoutParams().width = _listener.covertDPtoPX(10);
            }
        }

        if (ticket != null) {
            if(position < boarding_stop_num && route.boarding_stops.get(position).id == ticket.start_via) {
                holder.tvStop.setTypeface(Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                holder.tvStop.setTextColor(ContextCompat.getColor(context, R.color.Primary));
                holder.tvTime.setTextColor(ContextCompat.getColor(context, R.color.Primary));
            } else if ( position >= boarding_stop_num && route.alighting_stops.get(position - boarding_stop_num).id == ticket.end_via) {
                holder.tvStop.setTypeface(Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                holder.tvStop.setTextColor(ContextCompat.getColor(context, R.color.Red));
                holder.tvTime.setTextColor(ContextCompat.getColor(context, R.color.Red));
            }
        }
    }

    @Override
    public int getItemCount() {
        return boarding_stop_num + alighting_stop_num;
    }

    public class RouteDetailHolder extends RecyclerView.ViewHolder {
        public TextView tvStop;
        public TextView tvTime;
        public ImageView ivDot;
        public View ivUpperLine;
        public View ivLowerLine;

        public RouteDetailHolder(@NonNull View itemView) {
            super(itemView);
            this.tvStop = itemView.findViewById(R.id.tv_rd_name);
            this.tvTime = itemView.findViewById(R.id.tv_rd_time);
            this.ivDot = itemView.findViewById(R.id.iv_via);
            this.ivUpperLine = itemView.findViewById(R.id.v_upper_line);
            this.ivLowerLine = itemView.findViewById(R.id.v_lower_line);
        }
    }

    @NonNull
    @Override
    public RouteDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_via_list, parent, false);
        return new RouteDetailHolder(itemView);
    }
}