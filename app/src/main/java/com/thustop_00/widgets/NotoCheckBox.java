package com.thustop_00.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.thustop_00.R;

public class NotoCheckBox extends AppCompatCheckBox {
    public NotoCheckBox(Context context) {
        super(context);
        setType(context, null);
    }

    public NotoCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context, attrs);
    }

    public NotoCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setType(context, attrs);
    }

    /*
     * Main method of this class.
     * From attribute(res/values/attrs) NotoStyle's notoTextStyle, set font depend on integer.
     */
    private void setType(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.NotoStyle);
        switch (attr.getInt(R.styleable.NotoStyle_notoTextStyle, 1)) {
            case 1: //When option is 'regular'
                this.setTypeface(Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Regular-Hestia.otf"));
            case 2: //When option is 'medium'
                this.setTypeface(Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Medium-Hestia.otf"));
            case 3: //When option is 'bold'
                this.setTypeface(Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Bold-Hestia.otf"));

        }
        attr.recycle();
    }
}
