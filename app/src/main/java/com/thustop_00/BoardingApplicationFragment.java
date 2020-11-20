package com.thustop_00;

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

import com.thustop_00.databinding.FragmentBoardingApplicationBinding;
import com.thustop_00.model.Route;

import butterknife.OnClick;

public class BoardingApplicationFragment extends FragmentBase {
    private FragmentBoardingApplicationBinding binding;
    private Route route;
    private int boarding_stop_num;
    private int alighting_stop_num;
    private static int phase = 0; //0이면 선택  X, 1이면 출발지 선택완료, 2면 도착지 선택 완료
    private StopSelectorAdapter adapter;


    public static BoardingApplicationFragment newInstance(Route route) {
        BoardingApplicationFragment fragment = new BoardingApplicationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.route = route;
        fragment.boarding_stop_num = route.boarding_stops.size();
        fragment.alighting_stop_num = route.alighting_stops.size();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBoardingApplicationBinding.inflate(inflater);
        binding.setBoardingApplicationFrag(this);

        _listener.setToolbarStyle(_listener.GREEN_BACK, "출발 위치 선택");
        colorText(binding.tvFbaBig, R.string.tv_fba_big_boarding_colored, getResources().getColor(R.color.Primary));
        colorText(binding.tvFbaSmall, R.string.tv_fba_small_boarding, getResources().getColor(R.color.Primary));
        adapter = new StopSelectorAdapter();
        binding.rvFbaStopSelector.setAdapter(adapter);


        return binding.getRoot();
    }

    private void updateFragmentPhase(){
        //phase에 따라서 화면이 전부 업데이트 될수록 만들 것
    }

    private class StopSelectorAdapter extends RecyclerView.Adapter<StopSelectorAdapter.StopViewHolder> {

        @NonNull
        @Override
        public StopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_stop_selection, parent, false);
            return new StopViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull StopViewHolder holder, int position) {
            //시간 이름 할당해주는 부분
            if (position < boarding_stop_num) {
                holder.tv_time.setText(route.getBoardingStopTime(position));
                holder.tv_name.setText(route.getBoardingStopName(position));
            } else {
                holder.tv_time.setText(route.getAlightingStopTime(position - boarding_stop_num));
                holder.tv_name.setText(route.getAlightingStopName(position - boarding_stop_num));
            }
            //불필요한 라인 지워주고 기점 종점 굵은 글씨로
            if (position == 0) {
                holder.v_upper_line.setVisibility(View.GONE);
                holder.tv_name.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
            } else if (position == boarding_stop_num + alighting_stop_num - 1) {
                holder.v_lower_line.setVisibility(View.GONE);
                holder.tv_name.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "NotoSansKR-Bold-Hestia.otf"));
            }
        }

        @Override
        public int getItemCount() {
            return boarding_stop_num + alighting_stop_num;
        }

        private class StopViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_time;
            public TextView tv_name;
            public View v_upper_line;
            public View v_lower_line;
            public ImageView iv_checkbox;

            public StopViewHolder(@NonNull View itemView) {
                super(itemView);
                this.tv_time = itemView.findViewById(R.id.tv_iss_time);
                this.tv_name = itemView.findViewById(R.id.tv_iss_name);
                this.v_upper_line = itemView.findViewById(R.id.v_iss_upper_line);
                this.v_lower_line = itemView.findViewById(R.id.v_iss_lower_line);
                this.iv_checkbox = itemView.findViewById(R.id.iv_iss_checkbox);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        }
    }
}
