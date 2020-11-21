package com.thustop_00;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.thustop_00.databinding.FragmentRouteDetailBinding;
import com.thustop_00.model.Route;

public class RouteDetailFragment extends FragmentBase {
    FragmentRouteDetailBinding binding;
    private Route route;
    private int boarding_stop_num;
    private int alighting_stop_num;


    public static RouteDetailFragment newInstance(Route route){
        RouteDetailFragment fragment = new RouteDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.route = route;
        fragment.boarding_stop_num = route.boarding_stops.size();
        fragment.alighting_stop_num = route.alighting_stops.size();
        return fragment;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRouteDetailBinding.inflate(inflater);
        binding.setRouteDetailFrag(this);
        _listener.setToolbarStyle(_listener.GREEN_HAMBURGER, null);

        binding.tvBusId.setText(route.id);
        binding.tvDeparture.setText(route.getBoardingStopName(0));
        binding.tvDestination.setText(route.getAlightingStopName(alighting_stop_num - 1));
        binding.tvCapacity.setText(String.format("%d인승", route.spec));
        binding.tvPassengers.setText(String.format("%d/%d",route.cnt_passenger,route.max_passenger));
        binding.tvDistance.setText(String.format("%.2fkm", route.distance));
        RouteDetailAdapter adapter = new RouteDetailAdapter();
        binding.rvVias.setAdapter(adapter);
        return binding.getRoot();
    }

    public void onMapIconClick(View view){
        RouteStopMapDialog dialog = new RouteStopMapDialog(getContext(), getActivity(), route.boarding_stops, route.alighting_stops);
        dialog.show();
    }

    public void onApplicationClick(View view){
        //TODO 로그인 안했을 때 로그인창으로 보내야함
        _listener.addFragment(BoardingApplicationFragment.newInstance(route));
    }


    private class RouteDetailAdapter extends RecyclerView.Adapter<RouteDetailAdapter.RouteDetailHolder>{
        int dp10ToPixel = (int) (10 * getContext().getResources().getDisplayMetrics().density + 0.5);

        @Override
        public void onBindViewHolder(@NonNull RouteDetailHolder holder, int position) {
            if (position < boarding_stop_num) {
                holder.tvTime.setText(route.boarding_stops.get(position).time);
                holder.tvStop.setText(route.getBoardingStopName(position));
                if (position == 0) {
                    holder.tvStop.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                    holder.ivUpperLine.setVisibility(View.INVISIBLE);
                    holder.ivDot.getLayoutParams().height = dp10ToPixel;
                    holder.ivDot.getLayoutParams().width = dp10ToPixel;
                }
            } else {
                int offsetPosition = position - boarding_stop_num;
                holder.tvTime.setText(route.alighting_stops.get(offsetPosition).time);
                holder.tvStop.setText(route.getAlightingStopName(offsetPosition));
                holder.ivUpperLine.setBackground(getContext().getDrawable(R.color.Red));
                holder.ivLowerLine.setBackground(getContext().getDrawable(R.color.Red));
                holder.ivDot.setImageDrawable(getContext().getDrawable(R.drawable.icon_via_red));
                if (offsetPosition == alighting_stop_num - 1){
                    holder.tvStop.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                    holder.ivLowerLine.setVisibility(View.INVISIBLE);
                    holder.ivDot.getLayoutParams().height = dp10ToPixel;
                    holder.ivDot.getLayoutParams().width = dp10ToPixel;
                }
            }
        }

        @Override
        public int getItemCount() {
            return boarding_stop_num + alighting_stop_num;
        }

        private class RouteDetailHolder extends RecyclerView.ViewHolder{
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
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_via_list, parent, false);
            return new RouteDetailHolder(itemView);
        }
    }
}
