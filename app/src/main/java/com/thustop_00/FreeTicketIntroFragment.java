package com.thustop_00;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.gtomato.android.ui.transformer.WheelViewTransformer;
import com.gtomato.android.ui.widget.CarouselView;
import com.thustop_00.databinding.FragmentFreeTicketIntroBinding;
import com.thustop_00.widgets.NotoTextView;

import java.lang.reflect.Field;

public class FreeTicketIntroFragment extends FragmentBase {
    private FragmentFreeTicketIntroBinding binding;
    private float moveX = 0f;
    private float moveY = 0f;
    private float originY = 0f;
    private boolean isTouchable = true;
    private int lastFocus = 0;
    private final String TAG = "FreeTicketIntro";

    public static FreeTicketIntroFragment newInstance() {
        FreeTicketIntroFragment fragment = new FreeTicketIntroFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _listener.setToolbarStyle(_listener.WHITE_BACK, "무료탑승권");
        binding = FragmentFreeTicketIntroBinding.inflate(inflater);
        WheelViewTransformer test = new WheelViewTransformer();
        binding.cvFreeTickets.setTransformer(test);
        //여러개 넘어가는 것 방지해주는 옵션
        binding.cvFreeTickets.setEnableFling(false);
        FreeTicketAdapter adapter = new FreeTicketAdapter(getContext());
        binding.cvFreeTickets.setAdapter(adapter);


        //TODO 테스트 코드 삭제해야함
        Log.d(TAG, "onCreateView: " + originY);
        binding.tvTestFree.setOnTouchListener(((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (originY == 0f)
                        originY = view.getY();
                    moveY = view.getY() - motionEvent.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isTouchable && ((moveY + motionEvent.getRawY()) <= originY)) {
                        view.animate().y(motionEvent.getRawY() + moveY).setDuration(0).start();
                        binding.tvCurrentOffset.setText(String.format("%.3f", view.getY()));
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    isTouchable = false;
                    view.animate().y(originY).withEndAction(() -> {
                        isTouchable = true;
                    }).setDuration(500).start();
                    break;
            }
            Log.d(TAG, "onCreateView: " + motionEvent.getAction());
            return true;
        }));


        return binding.getRoot();
    }

    private class FreeTicketAdapter extends CarouselView.Adapter<FreeTicketAdapter.FreeTicketHolder> {
        private Context context;

        FreeTicketAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public FreeTicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_free_ticket_green, parent, false);

            return new FreeTicketHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull FreeTicketHolder holder, int position) {
            holder.textView.setOnTouchListener(((view, motionEvent) -> {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (holder.originY == 0f)
                            holder.originY = view.getY();
                        //터치 지점에서 Y 기준점까지  offset
                        holder.moveY = view.getY() - motionEvent.getRawY();
                        lastFocus = position;
                        //옆으로 넘어는거 멈춰줌
                        binding.cvFreeTickets.suppressLayout(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (holder.isTouchable && ((holder.moveY + motionEvent.getRawY()) <= holder.originY)) {
                            //offset + 움직인만큼 애니메이션 줘서 움직임
                            view.animate().y(motionEvent.getRawY() + holder.moveY).setDuration(0).start();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        view.performClick();
                        holder.isTouchable = false;
                        view.setClickable(false);
                        binding.cvFreeTickets.suppressLayout(false);
                        view.animate().y(holder.originY).withEndAction(() -> {
                            holder.isTouchable = true;
                        }).setDuration(300).start();
                        break;
                }
                return true;
            }));
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        private class FreeTicketHolder extends RecyclerView.ViewHolder {
            public NotoTextView textView;
            public boolean isTouchable;
            public float moveY;
            public float originY;

            public FreeTicketHolder(@NonNull View itemView) {
                super(itemView);
                this.textView = itemView.findViewById(R.id.tv_item_free_ticket);
                isTouchable = true;
                moveY = 0f;
                originY = 0f;
            }

        }
    }

}
