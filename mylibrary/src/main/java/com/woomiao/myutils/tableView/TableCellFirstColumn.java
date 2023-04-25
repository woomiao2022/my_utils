package com.woomiao.myutils.tableView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.woomiao.myutils.R;

/**
 * @author Administrator
 * 版本：1.0
 * 创建日期：2023/4/12 0012 16:19
 * 描述：第一行第一列固定不动的textview视图
 * Email:
 */
public class TableCellFirstColumn extends LinearLayout{
    private int width = -2;
    private boolean isHeader = false;
    private Context mContext;
    private TextView content_tv;
    private LinearLayout layout;
    private int mWidth = 100;

    public TableCellFirstColumn(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public TableCellFirstColumn(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public TableCellFirstColumn(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.table_item_first, this, true);
        content_tv = findViewById(R.id.scrolltableview_item_content);
        layout = findViewById(R.id.scrolltableview_layout);
    }

    public void setHeader(boolean isHeader) {
        this.isHeader = isHeader;
        if (isHeader){
            content_tv.setBackgroundColor(mContext.getResources().getColor(R.color.grey_f3f3));
        }else
            content_tv.setBackgroundColor(mContext.getResources().getColor(R.color.white));
    }

    public void setContextTxtColor(int color){
        content_tv.setTextColor(color);
    }

    public void setContentTxt(String text){
        content_tv.setText(text);
    }

    public String getContentTxt(){
        return content_tv.getText().toString().trim();
    }

    public void setContentTxtWidth(int width){
        mWidth = width;
        refreshWidth();
    }

    private void refreshWidth(){
        this.content_tv.getLayoutParams().width = mWidth;
        this.layout.getLayoutParams().width = mWidth;
        requestLayout();
    }
}
