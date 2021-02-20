package com.thustop.thestop;

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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.thustop.R;
import com.thustop.databinding.FragmentBoardingApplicationDetailBinding;
import com.thustop.thestop.adapter.RouteDetailAdapter;
import com.thustop.thestop.model.Route;
import com.thustop.thestop.model.Ticket;

import java.util.Locale;

public class BoardingApplicationDetailFragment extends FragmentBase {
    FragmentBoardingApplicationDetailBinding binding;
    private Route route;
    private Ticket ticket;
    private int boarding_stop_num;
    private int alighting_stop_num;

    public static BoardingApplicationDetailFragment newInstance(Route route){
        BoardingApplicationDetailFragment fragment = new BoardingApplicationDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.route = route;
        fragment.boarding_stop_num = route.boarding_stops.size();
        fragment.alighting_stop_num = route.alighting_stops.size();
        return fragment;
    }

    public static BoardingApplicationDetailFragment newInstance(Ticket ticket){
        BoardingApplicationDetailFragment fragment = new BoardingApplicationDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.ticket = ticket;
        fragment.route = ticket.route_obj;
        fragment.boarding_stop_num = ticket.route_obj.boarding_stops.size();
        fragment.alighting_stop_num = ticket.route_obj.alighting_stops.size();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBoardingApplicationDetailBinding.inflate(inflater);
        binding.setRouteDetailFrag(this);
        if (ticket == null) {
            _listener.setToolbarStyle(_listener.GREEN_BACK, "노선정보");

            binding.tvBusId.setText(route.name);
            binding.tvDeparture.setText(route.getBoardingStopName(0));
            binding.tvDestination.setText(route.getAlightingStopName(alighting_stop_num - 1));
            binding.tvPassengers.setText(String.format(Locale.KOREA,"%d/%d",route.cnt_passenger,route.max_passenger));
            binding.tvDistance.setText(String.format(Locale.KOREA,"%.2fkm", route.distance));
            //RouteDetailAdapter adapter1 = new RouteDetailAdapter();
            com.thustop.thestop.adapter.RouteDetailAdapter adapter = new com.thustop.thestop.adapter.RouteDetailAdapter(getContext(), route, _listener);
            binding.rvVias.setAdapter(adapter);
        } else {
            _listener.setToolbarStyle(_listener.GREEN_BACK, "노선확인");
            binding.tvRdOk.setText("티켓관리");
            binding.tvBusId.setText(route.name);
            binding.tvDeparture.setText(route.getBoardingStopName(0));
            binding.tvDestination.setText(route.getAlightingStopName(alighting_stop_num - 1));
            binding.tvPassengers.setText(String.format(Locale.KOREA,"%d/%d",route.cnt_passenger,route.max_passenger));
            binding.tvDistance.setText(String.format(Locale.KOREA,"%.2fkm", route.distance));
            //RouteDetailAdapter adapter1 = new RouteDetailAdapter();
            com.thustop.thestop.adapter.RouteDetailAdapter adapter = new com.thustop.thestop.adapter.RouteDetailAdapter(getContext(), ticket, _listener);
            binding.rvVias.setAdapter(adapter);

        }

        return binding.getRoot();
    }

    public void onMapIconClick(View view){
        BoardingApplicationMapDialog dialog = new BoardingApplicationMapDialog(requireContext(), getActivity(), route.boarding_stops, route.alighting_stops);
        dialog.show();
    }

    public void onApplicationClick(View view){
        //TODO 로그인 안했을 때 로그인창으로 보내야함
        if(ticket == null) {
            _listener.addFragment(BoardingApplicationFragment.newInstance(route));
        } else {
            //TODO: 티켓관리에서 넘어가는 방식()
            _listener.addFragment(NavPersonalHistoryFragment.newInstance(ticket.id));
        }

    }
    /***
     * 삭제 예정
    private class RouteDetailAdapter extends RecyclerView.Adapter<RouteDetailAdapter.RouteDetailHolder>{

        @Override
        public void onBindViewHolder(@NonNull RouteDetailHolder holder, int position) {
            if (position < boarding_stop_num) {
                holder.tvTime.setText(route.boarding_stops.get(position).time);
                holder.tvStop.setText(route.getBoardingStopName(position));
                if (position == 0) {
                    holder.tvStop.setTypeface(Typeface.createFromAsset(requireContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                    holder.ivUpperLine.setVisibility(View.INVISIBLE);
                    holder.ivDot.getLayoutParams().height = _listener.covertDPtoPX(10);
                    holder.ivDot.getLayoutParams().width = _listener.covertDPtoPX(10);
                }
            } else {
                int offsetPosition = position - boarding_stop_num;
                holder.tvTime.setText(route.alighting_stops.get(offsetPosition).time);
                holder.tvStop.setText(route.getAlightingStopName(offsetPosition));
                holder.ivUpperLine.setBackground(ContextCompat.getDrawable(requireContext(), R.color.Red));
                holder.ivLowerLine.setBackground(ContextCompat.getDrawable(requireContext(), R.color.Red));
                holder.ivDot.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_via_red));
                if (offsetPosition == alighting_stop_num - 1){
                    holder.tvStop.setTypeface(Typeface.createFromAsset(requireContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                    holder.ivLowerLine.setVisibility(View.INVISIBLE);
                    holder.ivDot.getLayoutParams().height = _listener.covertDPtoPX(10);
                    holder.ivDot.getLayoutParams().width = _listener.covertDPtoPX(10);
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
    ***/
}
