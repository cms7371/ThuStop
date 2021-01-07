package com.thustop_00.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatTextView;

import com.thustop_00.R;

/*
 * This class is for customizing TextView.
 * Through the function 'setType', change the font of the button into NotoSansKR
 */
public class NotoTextView extends AppCompatTextView {
    /* Below 3 methods are essential declarations with new function 'setType'*/

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public NotoTextView(Context context) {
        super(context);
        setType(context, null);
    }

    public NotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context, attrs);
    }

    public NotoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
                break;
            case 2: //When option is 'medium'
                this.setTypeface(Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Medium-Hestia.otf"));
                break;
            case 3: //When option is 'bold'
                this.setTypeface(Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Bold-Hestia.otf"));
                break;
        }
        attr.recycle();

    }

}
