package com.thustop_00;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import android.view.animation.Interpolator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerIndicator extends RecyclerView.ItemDecoration {

    public int selectedIndicator;
    public int unselectedIndicator;

    private static final float DP = Resources.getSystem().getDisplayMetrics().density;

    private int indicatorHeight;
    private int indicatorDiagram;
    private int indicatorPadding;


    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    private final Paint mPaint = new Paint();

    /***
     *  !!! 색상 넣을 때 int로 넣으면 보이지 않습니다. ex. 0x777777 <-이렇게 넣으면 컬러 설정 안됨 !!!
     *  !!! 가능한 R.color.~~로 넣어야 합니다 !!!
     *  !!! 모든 수치들은 DP값 기준으로 넣으면 됩니다!!!
     * @param selectedIndicator     : 선택된 인디케이터 색상
     * @param unselectedIndicator   : 선택되지 않은 인디케이터 색상
     * @param indicatorHeight       : 인디케이터 전체 공간 높이
     * @param indicatorDiagram      : 원형 인디케이터 지름
     * @param indicatorPadding      : 인디케이터와 인디케이터 사이의 패딩
     */

    public RecyclerIndicator(int selectedIndicator, int unselectedIndicator, int indicatorHeight, int indicatorDiagram, int indicatorPadding) {
        this.selectedIndicator=selectedIndicator;
        this.unselectedIndicator=unselectedIndicator;
        this.indicatorHeight = (int)(DP*indicatorHeight);
        this.indicatorDiagram = (int)(DP*indicatorDiagram);
        this.indicatorPadding = (int)(DP*indicatorPadding);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        //mPaint.setStrokeWidth(this.strokeWidth);
        //mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView rv, RecyclerView.State state) {

        super.onDrawOver(c, rv, state);

        int itemCount = rv.getAdapter().getItemCount();


        float totalWidth = (indicatorDiagram *itemCount) + (Math.max(0, itemCount-1)*indicatorPadding);
        float indicatorStartX = (rv.getWidth()-totalWidth)/2F;

        float indicatorPosY = rv.getHeight() - indicatorHeight/2F;


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
