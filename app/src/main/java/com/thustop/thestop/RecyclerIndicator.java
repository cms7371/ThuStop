package com.thustop.thestop;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import android.view.animation.Interpolator;

import androidx.annotation.IntDef;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerIndicator extends RecyclerView.ItemDecoration {
    public static final int HEIGHT_DP = 0;
    public static final int HEIGHT_PX = 1;

    private int selectedIndicator;
    private int unselectedIndicator;
    private int type;

    private static final float DP = Resources.getSystem().getDisplayMetrics().density;

    private float indicatorHeight;
    private int indicatorDiagram;
    private int indicatorPadding;

    private float indicatorStartX;
    private float indicatorPosY;


    @IntDef({HEIGHT_DP, HEIGHT_PX})
    public @interface Type {
    }

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    private final Paint mPaint = new Paint();

    /***
     *  !!! 색상 넣을 때 int로 넣으면 보이지 않습니다. ex. 0x777777 <-이렇게 넣으면 컬러 설정 안됨 !!!
     *  !!! getResource.getColor(R.color.~~) 또는 Color.parseColor("#000000")로 넣어야 합니다!
     *  기존 뷰 위에 그려지는 것이므로 하단에 높이만큼 패딩을 주어야함.
     * @param selectedIndicator     : 선택된 인디케이터 색상
     * @param unselectedIndicator   : 선택되지 않은 인디케이터 색상
     * @param indicatorHeight       : 인디케이터 전체 공간 높이
     * @param indicatorDiagram      : 원형 인디케이터 지름
     * @param indicatorPadding      : 인디케이터와 인디케이터 사이의 패딩
     * @param type                  : DP와 px 타입 존재(DP는 직접적으로 높이 설정, px는 전체 리사이클러 뷰에서 백분율로 높이 지정)
     */

    public RecyclerIndicator(int selectedIndicator, int unselectedIndicator, float indicatorHeight, int indicatorDiagram, int indicatorPadding, @Type int type) {
        this.selectedIndicator=selectedIndicator;
        this.unselectedIndicator=unselectedIndicator;
        this.indicatorHeight = indicatorHeight;
        this.indicatorDiagram = (int)(DP*indicatorDiagram);
        this.indicatorPadding = (int)(DP*indicatorPadding);
        this.type = type;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        //mPaint.setStrokeWidth(this.strokeWidth);
        //mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView rv, RecyclerView.State state) {

        super.onDrawOver(c, rv, state);

        int itemCount = rv.getAdapter().getItemCount();
        if(type == HEIGHT_DP) {
            indicatorHeight = (int)(DP*indicatorHeight);
            indicatorPosY  = rv.getHeight() - indicatorHeight;
        } else {
            indicatorPosY = rv.getHeight()*(1-indicatorHeight);
        }


        float totalWidth = (indicatorDiagram *itemCount) + (Math.max(0, itemCount-1)*indicatorPadding);
        indicatorStartX = (rv.getWidth()-totalWidth)/2F;


        Log.d("인디케이터 높이", String.valueOf(rv.getHeight()));


        drawIndicators(c, indicatorStartX, indicatorPosY, itemCount);

        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
        int activePosition = layoutManager.findFirstVisibleItemPosition();
        if (activePosition == RecyclerView.NO_POSITION) {
            return;
        }

        final View activeChild = layoutManager.findViewByPosition(activePosition);
        int left = activeChild.getLeft();
        int width = activeChild.getWidth();

        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation
        float progress = mInterpolator.getInterpolation(left * -1 / (float) width);

        drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount);


    }

    private void drawIndicators(Canvas c, float indicatorStartX, float indicatorPosY, int itemCount) {
        mPaint.setColor(unselectedIndicator);
        final float itemWidth = indicatorDiagram +indicatorPadding;
        float start = indicatorStartX;
        for (int i = 0; i < itemCount; i++) {
            c.drawCircle(start, indicatorPosY, indicatorDiagram/2F, mPaint);
            start += itemWidth;
        }
    }

    private void drawHighlights(Canvas c, float indicatorStartX, float indicatorPosY, int highlightPosition, float progress, int itemCount) {
        mPaint.setColor(selectedIndicator);

        final float itemWidth = indicatorDiagram+indicatorPadding;

        if (progress == 0F) {
            float highlightStart = indicatorStartX+itemWidth*highlightPosition;
            c.drawCircle(highlightStart, indicatorPosY, indicatorDiagram/2F, mPaint);
        }
        /***
        else {
            float highlightStart = indicatorStartX + itemWidth*highlightPosition;
            float partialLength = indicatorDiagram*progress+indicatorPadding*progress;
            c.drawCircle(highlightStart+partialLength, indicatorPosY, indicatorDiagram/2F, mPaint);
        }
         ***/
    }


}
