package com.thustop_00;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.thustop_00.widgets.NotoTextView;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.Delayed;

public class FAQRecyclerAdapter extends RecyclerView.Adapter<FAQRecyclerAdapter.FAQHolder> {

    private Context context;
    private int prePosition = -1;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private ArrayList<Integer> viewHeight = new ArrayList<>();
    private int num;

    public interface FAQRecyclerListener {
        void onItemClick();
    }

    FAQRecyclerAdapter(Context context, int num) {
        this.context = context;
        this.num = num;
    }

    @NonNull
    @Override
    public FAQHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq_top,parent,false);
        return new FAQHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQRecyclerAdapter.FAQHolder holder, int position) {
        holder.onBind(position, selectedItems, viewHeight);
        holder.setFAQRecyclerListener(new FAQRecyclerListener() {
            @Override
            public void onItemClick() {
                Log.d("클릭", "클릭됨");
                if (selectedItems.get(position)) {
                    // 펼쳐진 Item을 클릭 시
                    selectedItems.delete(position);
                } else {
                    Log.d("프리포지션", String.valueOf(prePosition));
                    // 직전의 클릭됐던 Item의 클릭상태를 지움
                    selectedItems.delete(prePosition);

                    // 클릭한 Item의 position을 저장
                    selectedItems.put(position, true);
                }
                // 해당 포지션의 변화를 알림
                if (prePosition != -1) notifyItemChanged(prePosition);
                notifyItemChanged(position);
                // 클릭된 position 저장
                prePosition = position;

            }
        });

    }




    @Override
    public int getItemCount() {
        return num;
    }

    public class FAQHolder extends RecyclerView.ViewHolder {

        private FAQRecyclerListener faqRecyclerListener;
        private NotoTextView tvQuestion;
        private NotoTextView tvAnswer;
        private ImageView ivDown;
        private ConstraintLayout cl;
        public FAQHolder(@NonNull View itemView) {
            super(itemView);
            this.tvQuestion=itemView.findViewById(R.id.tv_faq_question);
            this.tvAnswer=itemView.findViewById(R.id.tv_faq_answer);
            this.ivDown=itemView.findViewById(R.id.iv_faq_down);
            this.cl = itemView.findViewById(R.id.cl_faq);
            viewHeight.add(tvAnswer.getLayoutParams().height);
            tvAnswer.setVisibility(View.GONE);
            cl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    faqRecyclerListener.onItemClick();
                }
            });

        }
        public void onBind(int position, SparseBooleanArray selectedItems, ArrayList<Integer> viewHeight){
            changeVisibility(selectedItems.get(position), viewHeight.get(position));
        }

        private void changeVisibility(final boolean isExpanded, int height) {
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            va.setDuration(500);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // imageView의 높이 변경
                    tvAnswer.getLayoutParams().height = (int) animation.getAnimatedValue();
                    tvAnswer.requestLayout();
                    // imageView가 실제로 사라지게하는 부분
                    tvAnswer.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                    ivDown.setImageDrawable(isExpanded ? context.getDrawable(R.drawable.icon_down) : context.getDrawable(R.drawable.icon_down_gray));
                }
            });
            // Animation start
            va.start();
        }

        public void setFAQRecyclerListener(FAQRecyclerListener faqRecyclerListener) {
            this.faqRecyclerListener = faqRecyclerListener;
        }
    }
}


