package com.thustop_00;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.thustop_00.FAQRecyclerAdapter.FAQRecyclerListener;
import com.thustop_00.databinding.FragmentNavServiceFaqBinding;
import com.thustop_00.widgets.NotoTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavServiceFAQFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavServiceFAQFragment extends FragmentBase implements FAQRecyclerListener{
    FragmentNavServiceFaqBinding binding;
    private NotoTextView preTap;
    private Context context;
    private int[] nums = {1,2,3,4,5};
    private int heightPx, strokePx, marginPx;
    private boolean isOpen = false;
    private int expandedQuestionPosition = -1;

    public NavServiceFAQFragment() {
        // Required empty public constructor
    }

    public static NavServiceFAQFragment newInstance() {
        NavServiceFAQFragment fragment = new NavServiceFAQFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavServiceFaqBinding.inflate(inflater);
        binding.setNavServiceFAQFrag(this);
        context = getContext();
        FAQRecyclerAdapter mAdapter = new FAQRecyclerAdapter(getContext(),nums[0], this);
        binding.rvFaq.setAdapter(mAdapter);
        _listener.setToolbarStyle(_listener.WHITE_BACK_EXIT,"FAQ");
        preTap=binding.tvFaqPrice;
        heightPx=_listener.covertDPtoPX(49);
        strokePx=_listener.covertDPtoPX(1);
        marginPx=_listener.covertDPtoPX(20);
        binding.rvFaq.addItemDecoration(new fillSpace(heightPx, strokePx, marginPx));
        return binding.getRoot();
    }

    public void onPriceClick(View view) {
        selectTap(preTap, binding.tvFaqPrice);
        preTap=binding.tvFaqPrice;
        FAQRecyclerAdapter mAdapter = new FAQRecyclerAdapter(getContext(),nums[0],this);
        expandedQuestionPosition = -1;
        binding.rvFaq.setAdapter(mAdapter);
    }
    public void onLateClick(View view) {
        selectTap(preTap, binding.tvFaqLate);
        preTap=binding.tvFaqLate;
        FAQRecyclerAdapter mAdapter = new FAQRecyclerAdapter(getContext(),nums[1],this);
        expandedQuestionPosition = -1;
        binding.rvFaq.setAdapter(mAdapter);
    }
    public void onAccidentClick(View view) {
        selectTap(preTap, binding.tvFaqAccident);
        preTap=binding.tvFaqAccident;
        FAQRecyclerAdapter mAdapter = new FAQRecyclerAdapter(getContext(),nums[2],this);
        expandedQuestionPosition = -1;
        binding.rvFaq.setAdapter(mAdapter);
    }
    public void onServiceUseClick(View view) {
        selectTap(preTap,binding.tvFaqServiceUse);
        preTap=binding.tvFaqServiceUse;
        FAQRecyclerAdapter mAdapter = new FAQRecyclerAdapter(getContext(),nums[3],this);
        expandedQuestionPosition = -1;
        binding.rvFaq.setAdapter(mAdapter);
    }
    public void onSecurityClick(View view) {
        selectTap(preTap, binding.tvFaqSecurity);
        preTap=binding.tvFaqSecurity;
        FAQRecyclerAdapter mAdapter = new FAQRecyclerAdapter(getContext(),nums[4],this);
        expandedQuestionPosition = -1;
        binding.rvFaq.setAdapter(mAdapter);
    }

    public void selectTap(NotoTextView preTap, NotoTextView curTap) {
        Log.d("클릭", "클릭됨");
        preTap.setBackgroundColor(Color.parseColor("#00000000"));
        preTap.setTextColor(context.getResources().getColor(R.color.TextBlack));
        curTap.setBackground(context.getDrawable(R.drawable.bg_tap_top));
        curTap.setTextColor(context.getResources().getColor(R.color.Primary));
    }

    @Override
    public void onItemClick(int position, boolean isExpanded) {
        if (isExpanded) {
            if (expandedQuestionPosition != -1){
                FAQRecyclerAdapter.FAQHolder holder = (FAQRecyclerAdapter.FAQHolder)binding.rvFaq.getChildViewHolder(binding.rvFaq.getChildAt(expandedQuestionPosition));
                if (holder.isExpanded)
                    holder.switchFoldedState();
            }
            expandedQuestionPosition = position;
        } else {
            expandedQuestionPosition = -1;
        }
    }

    public class fillSpace extends RecyclerView.ItemDecoration{
        private int heightPx;
        private int strokePx;
        private int marginPx;
        private final Paint mPaint = new Paint();

        public fillSpace (int heightPx, int strokePx, int marginPx) {
            this.heightPx = heightPx;
            this.strokePx = strokePx;
            this.marginPx = marginPx;
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(this.strokePx);
            mPaint.setStrokeCap(Paint.Cap.SQUARE);
        }

        /***
        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView rv, @NonNull RecyclerView.State state) {
            super.onDraw(c, rv, state);
            int itemCount = rv.getAdapter().getItemCount();
            int filledSpace = heightPx *itemCount;
            int width = rv.getWidth();
            int height = rv.getHeight();


            drawLine(c, heightPx, filledSpace, width-marginPx, height, marginPx);


        }
         ***/
        private void drawLine(Canvas c, float heightPx, float filledSpace, int width, int height, int marginPx) {
            mPaint.setColor(context.getResources().getColor(R.color.Stroke));
            float start =filledSpace;
            start += heightPx;

            while (start < height) {
                c.drawLine(marginPx, start, width, start, mPaint);
                start += heightPx;
            }
        }


        @Override
        public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
            int itemCount = parent.getChildCount();
            View lastView = parent.getChildAt(itemCount-1);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) lastView.getLayoutParams();
            int height = lastView.getBottom();
            int width = lastView.getWidth();
            int parentHeight = parent.getHeight();

            drawLine(c, heightPx, height, width-marginPx, parentHeight, marginPx);

        }
    }


}