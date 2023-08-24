package com.woomiao.myutils.customspinner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.woomiao.myutils.R;
import com.woomiao.myutils.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义下拉列表
 *
 */
public class CustomSpinner extends LinearLayout {
    private static final String TAG = "下拉列表";
    private TextView tv_selected;
    private LinearLayout spinner_layout;
    private List<String> entries = new ArrayList<>();
    private View popupView;
    private CustomSpinnerAdapter adapter;
    private PopupWindow popupWindow;
    private ICustomSpinner iCustomSpinner;
    private Drawable popupBackgroundResource = null;//下拉弹窗背景资源
    private Drawable backgroundResource = null;//选中框背景资源

    public CustomSpinner(Context context) {
        super(context);
    }

    public CustomSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CustomSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @SuppressLint({"ResourceType", "NonConstantResourceId"})
    private void initView(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.custom_spinner, this, true);
        tv_selected = findViewById(R.id.customSpinner_content_tv);
        spinner_layout = findViewById(R.id.customSpinner_spinner_layout);

        //自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSpinner);
        //默认14sp大小
        float selectTxtSize = typedArray.getDimension(R.styleable.CustomSpinner_selected_txt_size, 26);
        float listTxtSize = typedArray.getDimension(R.styleable.CustomSpinner_content_txt_size, 26);
        int popupLocation = typedArray.getInt(R.styleable.CustomSpinner_popupLocation, -1);//弹窗显示位置，默认显示在底部
        popupBackgroundResource = typedArray.getDrawable(R.styleable.CustomSpinner_popupBackgroundResource);
        backgroundResource = typedArray.getDrawable(R.styleable.CustomSpinner_backgroundResource);
        //设置内边距
        int paddingHorizontal = typedArray.getDimensionPixelSize(R.styleable.CustomSpinner_paddingHorizontal, -1);
        int paddingVertical = typedArray.getDimensionPixelSize(R.styleable.CustomSpinner_paddingVertical, -1);

        //设置选中字体大小
//        Log.i(TAG, "选中字体大小: "+selectTxtSize+"  "+listTxtSize);
        tv_selected.getPaint().setTextSize(selectTxtSize);
        typedArray.recycle();
        //设置背景
        if (backgroundResource != null){
            spinner_layout.setBackgroundDrawable(backgroundResource);
        }
        //设置内边距
        if (paddingHorizontal > 0 || paddingVertical > 0){
            if (paddingHorizontal == -1){
                paddingHorizontal = 10;
            }
            if (paddingVertical == -1){
                paddingVertical = 5;
            }
            spinner_layout.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        }


        //点击展开/关闭下拉框
        spinner_layout.setOnClickListener(v -> {
            if (entries.size() != 0) {
                if (popupLocation <= 0){
                    openBottom();
                }else {
                    openTop();
                }
            }
        });

        //初始化下拉列表弹窗视图
        popupView = LayoutInflater.from(context).inflate(R.layout.custom_popup, null);
        ListView listView = popupView.findViewById(R.id.customSpinner_popupview_lv);
        adapter = new CustomSpinnerAdapter(context, entries, listTxtSize);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                tv_selected.setText(entries.get(position));
                if (iCustomSpinner != null)
                    iCustomSpinner.onSelect(position, entries.get(position));
            }
        });
    }

    //设置下拉列表的列表内容
    public void setEntries(List<String> entries) {
        this.entries = entries;
        if (entries != null && entries.size() > 0){
            tv_selected.setText(entries.get(0));
            adapter.update(entries);
        }
    }

    /**
     * 设置选中项
     *
     * @param position 选中项下标
     */
    public void setSelected(int position) {
        if (position >= 0 && position < entries.size()){
            tv_selected.setText(entries.get(position));
        }
    }

    //设置下拉列表选中监听
    public void setSelectListener(ICustomSpinner iCustomSpinner) {
        this.iCustomSpinner = iCustomSpinner;
    }

    //设置选中项的文字颜色
    public void setSelectedTextColor(int color) {
        tv_selected.setTextColor(color);
    }

    //列表框位于组件底部弹出
    private void openBottom() {
        popupWindow = new PopupWindow(popupView, spinner_layout.getWidth(), entries.size() > 3 ? ScreenUtil.getScreenHeight(getContext()) / 5 : ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //下拉列表背景设置
        if (popupBackgroundResource != null){
            popupView.setBackgroundDrawable(popupBackgroundResource);
        }else {
            popupView.setBackgroundColor(getContext().getResources().getColor(R.color.grey_bg));
        }

        popupWindow.showAsDropDown(spinner_layout);
    }

    //列表框位于组件顶部弹出
    private void openTop(){
        popupWindow = new PopupWindow(popupView, spinner_layout.getWidth(), entries.size() > 3 ? ScreenUtil.getScreenHeight(getContext()) / 5 : ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //下拉列表背景设置
        if (popupBackgroundResource != null){
            popupView.setBackgroundDrawable(popupBackgroundResource);
        }else {
            popupView.setBackgroundColor(getContext().getResources().getColor(R.color.grey_bg));
        }
        popupView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        int popupWidth = popupView.getMeasuredWidth();
        int popupHeight = popupView.getMeasuredHeight();
        int[] location = new int[2];

        // 获得位置
        spinner_layout.getLocationOnScreen(location);
        popupWindow.showAtLocation(spinner_layout, Gravity.NO_GRAVITY, (location[0] + spinner_layout.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
    }
}
