package com.thustop_00;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.thustop_00.widgets.NotoTextView;

public class RegionGridAdapter extends BaseAdapter {
    LayoutInflater inf;
    private Context context;
    private String[] regions;
    private int width;

    RegionGridAdapter(Context context, String[] regionsIn, int width) {
        this.context = context;
        this.regions = regionsIn;
        this.width = width;

    }

    @Override
    public int getCount() {
        return regions.length;
    }

    @Override
    public Object getItem(int position) {
        return regions[position];
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        NotoTextView btRegion = new NotoTextView(this.context);
        btRegion.setText(regions[position]);
        btRegion.setBackgroundResource(R.drawable.button_local);
        btRegion.setGravity(Gravity.CENTER);
        btRegion.setTextColor(context.getResources().getColor(R.color.TextGray));
        btRegion.setLayoutParams(new GridView.LayoutParams(width,width));
        btRegion.setTextSize(13);
        return btRegion;
    }
}
