package com.woomiao.myutils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Administrator
 * 版本：1.0
 * 创建日期：2023/4/24 0024 10:38
 * 描述：多行输入框，即可显示多行输入框，也可以将输入结果使用文本框显示，当输入高度超过布局高度时可以滑动查看输入内容
 * Email:
 */
public class MultiLineEditText extends FrameLayout {
    private NestedInScrollView layout_input;
    private TextView tv;
    private EditText et;

    public MultiLineEditText(@NonNull Context context) {
        super(context);
    }

    public MultiLineEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public MultiLineEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public MultiLineEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.multi_line_edittext, this, true);
        tv = findViewById(R.id.multiline_text_tv);
        et = findViewById(R.id.multiline_text_et);
        layout_input = findViewById(R.id.multiline_text_layout_input);

        //自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiLineEditText);
        int index = typedArray.getInt(R.styleable.MultiLineEditText_showType, -1);
        //默認顯示多行文本輸入框
        if (index <= 0){
            showEdit();
        }else {
            //顯示文本
            showText();
        }
    }

    /**
     * 顯示多行輸入框
     */
    public void showEdit(){
        layout_input.setVisibility(VISIBLE);
        tv.setVisibility(GONE);
    }

    /**
     * 顯示文本
     */
    public void showText(){
        layout_input.setVisibility(GONE);
        tv.setVisibility(VISIBLE);
        tv.setText(et.getText().toString());
    }

    public String getInputText(){
        return et.getText().toString();
    }

    public EditText getEditTextView(){
        return et;
    }

    public void setText(String txt){
        if (tv.getVisibility() == VISIBLE){
            tv.setText(txt);
        }else if (et.getVisibility() == VISIBLE){
            et.setText(txt);
        }
    }
}
