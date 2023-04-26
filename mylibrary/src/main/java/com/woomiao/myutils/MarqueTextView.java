package com.woomiao.myutils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 跑马灯功能的TextView
 */
public class MarqueTextView extends androidx.appcompat.widget.AppCompatTextView {

    public MarqueTextView(@NonNull Context context) {
        super(context);
    }

    public MarqueTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        //就是把这里返回true即可
        return true;
    }
}
