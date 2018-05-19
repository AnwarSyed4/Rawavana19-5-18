package com.iprismtech.rawvana.others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class MyCustomTextView3 extends TextView {

    public MyCustomTextView3(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public MyCustomTextView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public MyCustomTextView3(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(Values.fontPath3, context);
        setTypeface(customFont);
    }
}