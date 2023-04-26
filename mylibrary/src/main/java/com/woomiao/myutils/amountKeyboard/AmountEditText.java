package com.woomiao.myutils.amountKeyboard;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

/**
 *
 * 自定义金额输入键盘
 * https://blog.csdn.net/lwh1212/article/details/125499007
 */
public class AmountEditText extends AppCompatEditText {
    private AmountKeyboardView mAmountKeyboardView;
    public AmountEditText(@NonNull Context context) {
        super(context);
        init();
    }

    public AmountEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AmountEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setInputType(InputType.TYPE_NULL);
    }

    //绑定键盘
    public void bindAmountKeyboardView(AmountKeyboardView mAmountKeyboardView) {
        this.mAmountKeyboardView = mAmountKeyboardView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //按下时绑定当前的EditText
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (mAmountKeyboardView!=null){
                mAmountKeyboardView.bindEditText(this);
            }
        }
        return super.onTouchEvent(event);
    }

}
