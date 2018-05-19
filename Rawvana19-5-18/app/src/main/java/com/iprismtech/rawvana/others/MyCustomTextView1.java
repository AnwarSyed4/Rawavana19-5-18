package com.iprismtech.rawvana.others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class MyCustomTextView1 extends TextView {

    public MyCustomTextView1(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public MyCustomTextView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public MyCustomTextView1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(Values.fontPath1, context);
        setTypeface(customFont);
    }

}