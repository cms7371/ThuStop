package com.thustop.thestop;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.thustop.R;
import com.thustop.databinding.FragmentBoardingApplicationBinding;
import com.thustop.thestop.model.Route;
import com.thustop.thestop.model.Via;


public class BoardingApplicationFragment extends FragmentBase {
    private FragmentBoardingApplicationBinding binding;
    private Route route;
    private int boarding_stop_num;
    private int alighting_stop_num;
    private static int phase; //0이면 선택  X, 1이면 출발지 선택완료, 2면 도착지 선택 완료
    private static int start_focus;
    private static int end_focus;
    private StopSelectorAdapter adapter;
    private static final String TAG = "BoardingApplication";


    public static BoardingApplicationFragment newInstance(Route route) {
        BoardingApplicationFragment fragment = new BoardingApplicationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.route = route;
        fragment.boarding_stop_num = route.boarding_stops.size();
        fragment.alighting_stop_num = route.alighting_stops.size();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phase = 0;
        start_focus = -1;
        end_focus = -1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBoardingApplicationBinding.inflate(inflater);
        binding.setBoardingApplicationFrag(this);
        adapter = new StopSelectorAdapter();
        binding.rvFbaStopSelector.setAdapter(adapter);
        updateFragmentPhase();
        return binding.getRoot();
    }

    public void onBtIssOkClick(View view) {
        _listener.addFragment(BoardingApplicationPassengerInfoFragment.newInstance(route, start_focus, end_focus - boarding_stop_num));
    }

    private void updateFragmentPhase() {
        Log.v(TAG, String.format("Current phase %d, start %d, end %d", phase, start_focus, end_focus));
        adapter.notifyDataSetChanged();
        if (phase == 0) {
            _listener.setToolbarStyle(_listener.GREEN_BACK, "출발 위치 선택");
            binding.ivFbaLeftDots.setImageDrawable(getContext().getDrawable(R.drawable.ic_3dots_gray));
            binding.tvFbaPhase2.setBackground(getContext().getDrawable(R.drawable.bg_round12_graycf));
            binding.tvFbaBig.setText(R.string.tv_fba_big_boarding);
            colorText(binding.tvFbaBig, R.string.tv_fba_big_boarding_colored, getResources().getColor(R.color.Primary));
            binding.tvFbaSmall.setText(R.string.tv_fba_small_boarding);
            colorText(binding.tvFbaSmall, R.string.tv_fba_small_boarding, getResources().getColor(R.color.Primary));
            binding.tvFbaStage.setBackground(getContext().getDrawable(R.drawable.bg_round25_green));
            binding.tvFbaStage.setText("출발");
            binding.btFbaOk.setBackgroundColor(getResources().getColor(R.color.ButtonGray));
            binding.btFbaOk.setEnabled(false);
        } else{
            _listener.setToolbarStyle(_listener.GREEN_BACK, "도착 위치 선택");
            binding.ivFbaLeftDots.setImageDrawable(getContext().getDrawable(R.drawable.ic_3dots_green));
            binding.ivFbaRightDots.setImageDrawable(getContext().getDrawable(R.drawable.ic_3dots_gray));
            binding.tvFbaPhase2.setBackground(getContext().getDrawable(R.drawable.bg_round12_green));
            binding.tvFbaBig.setText(R.string.tv_fba_big_alighting);
            colorText(binding.tvFbaBig, R.string.tv_fba_big_alighting_colored, getResources().getColor(R.color.Red));
            binding.tvFbaSmall.setText(R.string.tv_fba_small_alighting);
            colorText(binding.tvFbaSmall, R.string.tv_fba_small_alighting, getResources().getColor(R.color.Red));
            binding.tvFbaStage.setBackground(getContext().getDrawable(R.drawable.bg_round25_red));
            binding.tvFbaStage.setText("도착");
        }
        if (phase == 2){
            binding.btFbaOk.setBackgroundColor(getResources().getColor(R.color.Primary));
            binding.btFbaOk.setEnabled(true);
        } else {
            binding.btFbaOk.setBackgroundColor(getResources().getColor(R.color.ButtonGray));
            binding.btFbaOk.setEnabled(false);
        }
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
            //리스너 할당
            holder.iv_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //아무것도 선택되지 않았을 때 -> 시작점으로 지정됨
                    if (phase == 0) {
                        //position에 따라 출발 via 도착 via 중 하나 반환하여
                        if (position < boarding_stop_num) {
                            Via currentVia = route.boarding_stops.get(position);
                            //정류장 위치 보여주는 다이어로그를 띄움
                            BoardingApplicationMapDialog dialog = new BoardingApplicationMapDialog(getContext(), getActivity(), currentVia, false);
                            dialog.setDialogListener(new BoardingApplicationMapDialog.StopMapListener() {
                                @Override
                                public void onConfirm() {
                                    //확인을 누르면 phase와 focus 바꿔주고 화면 업데이트
                                    phase = 1;
                                    start_focus = position;
                                    updateFragmentPhase();
                                }
                            });
                            dialog.show();
                        }

                        //출발지가 선택된 경우에 눌렸을 때
                    } else if (phase == 1) {
                        //출발지를 다시한번 누르면 선택 취소함
                        if (position == start_focus) {
                            phase = 0;
                            start_focus = -1;
                            updateFragmentPhase();
                            //출발지보다 뒤에 있는 정거장 눌렀을 때는 선택되도록 함
                        } else if (position >= boarding_stop_num) {
                            Via currentVia;
                            //위치에 따른 출발 정거장 또는 도착 정거장을 선택하여
                            currentVia = route.alighting_stops.get(position - boarding_stop_num);
                            //다이어로그를 띄움
                            BoardingApplicationMapDialog dialog = new BoardingApplicationMapDialog(getContext(), getActivity(), currentVia, true);
                            dialog.setDialogListener(new BoardingApplicationMapDialog.StopMapListener() {
                                //확인하면 포커스와 페이즈 업데이트
                                @Override
                                public void onConfirm() {
                                    phase = 2;
                                    end_focus = position;
                                    updateFragmentPhase();
                                }
                            });
                            dialog.show();
                        }
                    } else if (phase == 2) {
                        if (position == end_focus) {
                            phase = 1;
                            end_focus = -1;
                            updateFragmentPhase();
                        }
                    }
                }
            });
            //오동작 막기 위한 재정의
            holder.v_upper_line.setBackground(getContext().getDrawable(R.drawable.bg_round12_graycf));
            holder.v_upper_line.setVisibility(View.VISIBLE);
            holder.v_lower_line.setBackground(getContext().getDrawable(R.drawable.bg_round12_graycf));
            holder.v_lower_line.setVisibility(View.VISIBLE);
            holder.iv_checkbox.setImageDrawable(getContext().getDrawable(R.drawable.icon_stop_selector_empty));
            holder.iv_checkbox.setVisibility(View.VISIBLE);
            holder.tv_name.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "NotoSansKR-Regular-Hestia.otf"));
            holder.tv_name.setTextColor(getContext().getResources().getColor(R.color.TextBlack));

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
            //Phase에 따른 외관 구성
            if (phase == 0) {
                if (position >= boarding_stop_num) {
                    holder.iv_checkbox.setVisibility(View.GONE);
                }
            } else if (phase == 1) {
                if (position == start_focus) {
                    holder.iv_checkbox.setImageDrawable(getContext().getDrawable(R.drawable.icon_stop_selector_green));
                    holder.tv_name.setTextColor(getContext().getResources().getColor(R.color.Primary));
                } else if (position < boarding_stop_num) {
                    holder.iv_checkbox.setVisibility(View.GONE);
                }
            } else if (phase == 2) {
                if (position < start_focus) {
                    holder.iv_checkbox.setVisibility(View.GONE);
                    holder.tv_name.setTextColor(getContext().getResources().getColor(R.color.TextGray));
                } else if (position == start_focus) {
                    holder.iv_checkbox.setImageDrawable(getContext().getDrawable(R.drawable.icon_stop_selector_green));
                    holder.tv_name.setTextColor(getContext().getResources().getColor(R.color.Primary));
                    holder.v_lower_line.setBackground(getContext().getDrawable(R.color.Primary));
                } else if (position < end_focus) {
                    holder.iv_checkbox.setVisibility(View.GONE);
                    holder.v_upper_line.setBackground(getContext().getDrawable(R.color.Primary));
                    holder.v_lower_line.setBackground(getContext().getDrawable(R.color.Primary));
                    holder.tv_name.setTextColor(getContext().getResources().getColor(R.color.TextGray));
                } else if (position == end_focus) {
                    holder.iv_checkbox.setImageDrawable(getContext().getDrawable(R.drawable.icon_stop_selector_red));
                    holder.v_upper_line.setBackground(getContext().getDrawable(R.color.Primary));
                    holder.tv_name.setTextColor(getContext().getResources().getColor(R.color.Red));
                } else {
                    holder.iv_checkbox.setVisibility(View.GONE);
                    holder.tv_name.setTextColor(getContext().getResources().getColor(R.color.TextGray));
                }
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
            }
        }
    }

}
