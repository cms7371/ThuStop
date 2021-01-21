package com.thustop.thestop.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.thustop.R;
import com.thustop.thestop.widgets.NotoTextView;

public class FAQRecyclerAdapter extends RecyclerView.Adapter<FAQRecyclerAdapter.FAQHolder> {

    private Context context;
    private int num;
    private FAQRecyclerListener faqRecyclerListener;

    public interface FAQRecyclerListener {
        void onItemClick(int position, boolean isExpanded);
    }

    public FAQRecyclerAdapter(Context context, int num, FAQRecyclerListener listener) {
        this.context = context;
        this.num = num;
        this.faqRecyclerListener = listener;
    }

    @NonNull
    @Override
    public FAQHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq_top, parent, false);
        return new FAQHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQRecyclerAdapter.FAQHolder holder, int position) {
        holder.cl.setOnClickListener((view)-> {
            holder.switchFoldedState();
            faqRecyclerListener.onItemClick(position, holder.isExpanded);
        });
    }

    @Override
    public int getItemCount() {
        return num;
    }

    public class FAQHolder extends RecyclerView.ViewHolder {
        private NotoTextView tvQuestion;
        private NotoTextView tvAnswer;
        private ImageView ivDown;
        private TransitionDrawable downAnimation;
        private ConstraintLayout cl;
        public boolean isExpanded = false;

        public FAQHolder(@NonNull View itemView) {
            super(itemView);
            this.tvQuestion = itemView.findViewById(R.id.tv_faq_question);
            this.tvAnswer = itemView.findViewById(R.id.tv_faq_answer);
            this.ivDown = itemView.findViewById(R.id.iv_faq_down);
            this.cl = itemView.findViewById(R.id.cl_faq);
            tvAnswer.getLayoutParams().height = 1;
            tvAnswer.setVisibility(View.GONE);
            Drawable[] layers = new Drawable[2];
            layers[0] = context.getDrawable(R.drawable.ic_down_gray);
            layers[1] = context.getDrawable(R.drawable.ic_down);
            downAnimation = new TransitionDrawable(layers);
            ivDown.setImageDrawable(downAnimation);
        }

        public void switchFoldedState() {
            if (isExpanded) {
                collapse(tvAnswer);
                isExpanded = false;
            } else {
                expand(tvAnswer);
                isExpanded = true;
            }
        }

        //소스 https://stackoverflow.com/questions/4946295/android-expand-collapse-animation
        public void expand(final View v) {
            int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
            int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
            final int targetHeight = v.getMeasuredHeight();

            // Older versions of android (pre API 21) cancel animations for views with a height of 0.
            v.getLayoutParams().height = 1;
            v.setVisibility(View.VISIBLE);
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    v.getLayoutParams().height = interpolatedTime == 1
                            ? ConstraintLayout.LayoutParams.WRAP_CONTENT
                            : (int) (targetHeight * interpolatedTime);
                    v.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            // Expansion speed of 1dp/ms
            a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
            v.startAnimation(a);
            downAnimation.startTransition(300);
        }

        public void collapse(final View v) {
            final int initialHeight = v.getMeasuredHeight();

            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if (interpolatedTime == 1) {
                        v.setVisibility(View.GONE);
                    } else {
                        v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                        v.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            // Collapse speed of 1dp/ms
            a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
            v.startAnimation(a);
            downAnimation.reverseTransition(300);
        }



    }
}


