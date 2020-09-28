package com.thustop_00.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.thustop_00.R;

/*
 * This class is for customizing Button.
 * Through the function 'setType', change the font of the button into NotoSansKR
 */
public class NotoEditText extends AppCompatEditText {
    /* Below 3 methods are essential declarations with new function 'setType'*/
    public NotoEditText(Context context) {
        super(context);
        setType(context, null);
    }

    public NotoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context, attrs);
    }

    public NotoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setType(context, attrs);
    }

    /*
     * Main method of this class.
     * From attribute(res/values/attrs) NotoStyle's notoTextStyle, set font depend on integer.
     */
    private void setType(Context context, AttributeSet attrs) {
        //TODO : 글씨체 안바뀌는 이유 찾기
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
