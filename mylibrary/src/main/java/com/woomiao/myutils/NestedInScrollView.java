package com.woomiao.myutils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * @author Administrator
 * 版本：1.0
 * 创建日期：2023/4/24 0024 9:46
 * 描述：解决ScrollView嵌套ScrollView时内部ScrollView无法滑动的问题
 * Email:
 */
public class NestedInScrollView extends ScrollView {
    public NestedInScrollView(Context context) {
        super(context);
    }

    public NestedInScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedInScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NestedInScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        maxY = getChildAt(0).getMeasuredHeight() - getMeasuredHeight();
    }

    //在Y轴上可以滑动的最大距离 = 总长度 - 当前展示区域长度
    private int maxY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (getScrollY() > 0 && getScrollY() < maxY)
                    getParent().requestDisallowInterceptTouchEvent(true);
                else
                    getParent().requestDisallowInterceptTouchEvent(false);
                /*if (getScrollY()==0)
                    Log.i("kkk","滑到头了");
                if (getChildAt(0).getMeasuredHeight() == getScrollY() + getHeight())
                    Log.i("kkk","滑到底了");*/
                break;
            case MotionEvent.ACTION_UP:
                getParent().getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
