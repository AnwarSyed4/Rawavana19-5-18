package com.iprismtech.rawvana.others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by udaykumar on 15-03-2018.
 */
@SuppressLint("AppCompatCustomView")
public class MyCustomTextView2 extends TextView {

    public MyCustomTextView2(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public MyCustomTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public MyCustomTextView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(Values.fontPath2, context);
        setTypeface(customFont);
    }

}