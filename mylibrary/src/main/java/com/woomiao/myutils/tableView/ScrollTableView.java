package com.woomiao.myutils.tableView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.woomiao.myutils.R;
import com.woomiao.myutils.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Administrator
 * 版本：1.0
 * 创建日期：2023/4/12 0012 16:15
 * 描述：仿Excel样式的列表；
 *      第一列固定，可上下、左右滚动；
 *      一行的最后一个数据可显示为按钮（编辑、详情、删除按钮）；
 *      showTip()：显示提示内容，隐藏表格【如数据为空时，显示提示内容：“没有数据”】
 *      showContent()：显示表格，隐藏提示
 * Email:
 */
public class ScrollTableView extends LinearLayout {
    private final Context mContext;
    private RecyclerView rvHeader, rvFirstColumn, rvItems;
    private TableCellFirstColumn tvFirstHeader;
    private TableAdapter headerAdapter, fistColumnAdapter, itemAdapter;
    private List<String> headerList = new ArrayList<>();
    private final List<String> firstColumnList = new ArrayList<>();
    private final List<String> itemList = new ArrayList<>();
    private final int headerColor = 0x0ff262a2d;
    private int mdx = 0, mdy = 0;
    private IScrollTableView iScrollTableView;
    public static final String END_ITEM_BTNS = "end_item_btns";
    //第一列宽度
    private int firstColumnWidth = 50;
    //其他列宽度
    private int otherColumnWidth = 200;
    private int layoutWidth;//列表總寬度，默認為屏幕寬度

    public static final int FIRST_COLUMN_WIDTH_AUTO = 1;//第一列寬度設置后還是會根據縂寬度進行調整
    public static final int FIRST_COLUMN_WIDTH_FIXED = 2;//第一列宽度设置后不随总宽度进行调整
    private int firstColumnWidthModel;

    private LinearLayout content_layout;//表格最外层
    private TextView tip_tv;//提示文本

    public ScrollTableView(Context context) {
        this(context, null, 0);
    }

    public ScrollTableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        //默认显示表格，提示文本默认隐藏
        this.setOrientation(VERTICAL);
        content_layout = new LinearLayout(mContext);
        content_layout.setOrientation(HORIZONTAL);
        content_layout.setLayoutParams(new LayoutParams(-1,-1));
        content_layout.addView(getRowHeader());
        content_layout.addView(getFirstColumnAndItemLayout());
        this.addView(content_layout);
        this.addView(getTipView(false));

        //第一行表头数据
        headerAdapter = new TableAdapter(mContext);
        headerAdapter.setTextColor(headerColor);
        rvHeader.setAdapter(headerAdapter);
        tvFirstHeader.setHeader(true);
        headerAdapter.isHeader(true);

        //第一列固定不动数据
        fistColumnAdapter = new TableAdapter(mContext);
        fistColumnAdapter.setItemWidth(100);
        fistColumnAdapter.isFirstColumn(true);
        fistColumnAdapter.setTextColor(headerColor);
        rvFirstColumn.setAdapter(fistColumnAdapter);

        //内容
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ScrollTableView);
        boolean showEdit = typedArray.getBoolean(R.styleable.ScrollTableView_showEditBtn, false);
        boolean showDel = typedArray.getBoolean(R.styleable.ScrollTableView_showDelBtn, false);
        boolean showDetail = typedArray.getBoolean(R.styleable.ScrollTableView_showDetailBtn, false);

        int editBtnTvColor = typedArray.getColor(R.styleable.ScrollTableView_editBtnTvColor, -1);
        int delBtnTvColor = typedArray.getColor(R.styleable.ScrollTableView_delBtnTvColor, -1);
        int detailBtnTvColor = typedArray.getColor(R.styleable.ScrollTableView_detailBtnTvColor, -1);

        int tipTvColor = typedArray.getColor(R.styleable.ScrollTableView_tipTvColor, -1);
        if (tipTvColor != -1){
            tip_tv.setTextColor(tipTvColor);
        }

        //第一列除外的總列數
        itemAdapter = new TableAdapter(
                mContext,
                showEdit, showDel, showDetail,
                editBtnTvColor, delBtnTvColor, detailBtnTvColor);
        rvItems.setAdapter(itemAdapter);
        itemAdapter.setClick(new TableAdapter.Click() {
            @Override
            public void onDel(int position) {
                if (iScrollTableView != null)
                    iScrollTableView.onDel(position);
            }

            @Override
            public void onEdit(int position) {
                if (iScrollTableView != null) {
                    iScrollTableView.onEdit(position);
                }
            }

            @Override
            public void onDetail(int position) {
                if (iScrollTableView != null)
                    iScrollTableView.onDetail(position);
            }
        });


        /**
         * 2.滑动处理
         *
         * 横向滑动处理：第一行（除第一个单元格）和表格内容，由一个横向的HorizonScrollView包含，因此横向滑动由HorizonScrollView控制。
         *
         * 竖向滑动处理：第一列除第一个单元格外，是一个RecyclerView，表格内容为一个GridlayoutManager的RecyclerView。下滑时，将这两个RecyclerView的位置统一即可。
         * ————————————————
         * 版权声明：本文为CSDN博主「mojo001」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
         * 原文链接：https://blog.csdn.net/mojo001/article/details/126014355
         */
        rvFirstColumn.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mdx = dx;
                mdy = dy;
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    rvItems.scrollBy(dx, dy);
                }
            }
        });
        rvItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mdx = dx;
                mdy = dy;
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    rvFirstColumn.scrollBy(dx, dy);
                }
            }
        });
    }

    //获取提示文本视图，默认隐藏
    private TextView getTipView(boolean visibility){
        if (tip_tv == null){
            tip_tv = new TextView(mContext);
            tip_tv.setLayoutParams(new LayoutParams(-1,-1));
            tip_tv.setText("no data");
            tip_tv.setGravity(Gravity.CENTER);
            tip_tv.setTextColor(mContext.getResources().getColor(R.color.text_color));
        }
        tip_tv.setVisibility(visibility ? VISIBLE : GONE);
        return tip_tv;
    }

    //显示提示
    public void showTip(String tip){
        tip_tv.setVisibility(VISIBLE);
        content_layout.setVisibility(GONE);
        tip_tv.setText(tip);
    }

    //显示内容
    public void showContent(){
        tip_tv.setVisibility(GONE);
        content_layout.setVisibility(VISIBLE);
    }

    //获取第一列
    private LinearLayout getRowHeader() {
        LinearLayout lyHeader = new LinearLayout(mContext);
        lyHeader.setOrientation(VERTICAL);
        //参数：WRAP_CONTENT = -2  MATCH_PARENT = -1
        lyHeader.setLayoutParams(new LayoutParams(-2, -2));

        //第一行第一列的TextView，因为左右上下滑动时，这个都不动，因此固定
        tvFirstHeader = new TableCellFirstColumn(mContext);
        tvFirstHeader.setContextTxtColor(headerColor);
        //第一列，一个竖向的RecyclerView
        lyHeader.addView(tvFirstHeader);
        rvFirstColumn = new RecyclerView(mContext);
        rvFirstColumn.setLayoutManager(new LinearLayoutManager(mContext));
        lyHeader.addView(rvFirstColumn);
        return lyHeader;
    }

    //获取第一行以及内容表
    private HorizontalScrollView getFirstColumnAndItemLayout() {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(VERTICAL);
        //参数：WRAP_CONTENT = -2  MATCH_PARENT = -1
        layout.setLayoutParams(new LayoutParams(-2, -1));

        //添加第一行，即表头，横向的RecyclerView
        rvHeader = new RecyclerView(mContext);
        rvHeader.setLayoutParams(new LayoutParams(-1, -2));
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        rvHeader.setLayoutManager(manager);
        layout.addView(rvHeader);

        //添加内容表
        rvItems = new RecyclerView(mContext);
        rvItems.setLayoutParams(new LayoutParams(-2, -2));
        layout.addView(rvItems);

        HorizontalScrollView scrollView = new HorizontalScrollView(mContext);
        scrollView.setLayoutParams(new LayoutParams(-1, -1));
        scrollView.addView(layout);
        scrollView.setFillViewport(true);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);//取消滑到顶端的阴影
        scrollView.setHorizontalScrollBarEnabled(false);
        return scrollView;
    }

    //设置表头数据
    public void setHeaderData(List<String> headerData) {
        if (headerData.isEmpty()) return;
        //
        headerAdapter.setColumnCountForExceptTheFirst(headerData.size() - 1);
        fistColumnAdapter.setColumnCountForExceptTheFirst(headerData.size() - 1);
        itemAdapter.setColumnCountForExceptTheFirst(headerData.size() - 1);

        this.headerList = new ArrayList<>(headerData);
        List<String> headers = new ArrayList<>(headerData);
        tvFirstHeader.setContentTxt(headers.get(0));
        headers.remove(0);
        headerAdapter.setItemList(headers);
        rvItems.setLayoutManager(new GridLayoutManager(mContext, headerList.size() - 1));
    }

    public void setListener(IScrollTableView iScrollTableView) {
        this.iScrollTableView = iScrollTableView;
    }

    private int getMaxWidth(List<String> list) {
        int width = 0;
        Paint paint = new Paint();
        paint.setTextSize(14);
        for (String s : list) {
            width = (int) Math.max(width, paint.measureText(s));
        }
        return dip2px(mContext, width) + 60;
    }

    public int getItemCount() {
        return firstColumnList.size() * headerList.size();
    }

    public interface IScrollTableView {
        void onDel(int position);

        void onEdit(int position);

        void onDetail(int position);
    }



    //设置表头数据
    public ScrollTableView setHeaderData1(List<String> headerData){
        if (headerData.isEmpty()) return null;
        //
        headerAdapter.setColumnCountForExceptTheFirst(headerData.size() - 1);
        fistColumnAdapter.setColumnCountForExceptTheFirst(headerData.size() - 1);
        itemAdapter.setColumnCountForExceptTheFirst(headerData.size() - 1);

        this.headerList = new ArrayList<>(headerData);
        List<String> headers = new ArrayList<>(headerData);
        tvFirstHeader.setContentTxt(headers.get(0));
        headers.remove(0);
        headerAdapter.setItemList(headers);
        rvItems.setLayoutManager(new GridLayoutManager(mContext, headerList.size() - 1));
        return this;
    }

    /**
     * 设置表格所占空间的总宽度
     *         需在 setRowData() 之前调用
     * @param w
     */
    public ScrollTableView setLayoutWidth(int w){
        if (w > 0){
            layoutWidth = w;
        }else {
            layoutWidth = ScreenUtil.getScreenWidth(mContext);
        }
        return this;
    }

    /**
     * 设置第一列宽度模式
     *         需在 setRowData() 之前调用
     * @param model
     *      FIRST_COLUMN_WIDTH_AUTO 第一列寬度設置后還是會根據縂寬度進行調整
     *      FIRST_COLUMN_WIDTH_FIXED 第一列宽度设置后不随总宽度进行调整
     */
    public ScrollTableView setFirstColumnWidthModel(int model){
        firstColumnWidthModel = model;
        return this;
    }

    //设置第一列宽度
    public ScrollTableView setFirstColumnWidth(int w){
        if (w > 0) {
            firstColumnWidth = w;
        }
        return this;
    }

    //设置其他列宽度
    public ScrollTableView setOtherColumnWidth(int w){
        if (w > 0){
            otherColumnWidth = w;
        }
        return this;
    }

    /**
     * 显示表格
     * 使用连串设置参数时，此方法必须调用
     * @param rowDataList 行数据
     */
    public void show(List<List<String>> rowDataList){
        if (headerList.isEmpty() || rowDataList.isEmpty()) return;
        firstColumnList.clear();
        itemList.clear();

        //添加数据
        addRowData(rowDataList);
    }

    private void addRowData(List<List<String>> rowDataList) {
        List<List<String>> list = new ArrayList<>(rowDataList);
        for (List<String> rowData : list) {
            List<String> row = new ArrayList<>(rowData);
            if (row.size() > 0) {
                firstColumnList.add(row.get(0));
                row.remove(0);
                itemList.addAll(row);
            }
        }

        fistColumnAdapter.setItemList(firstColumnList);
        itemAdapter.setItemList(itemList);

        rvFirstColumn.scrollBy(mdx, mdy);
        rvItems.scrollBy(mdx, mdy);

        List<String> fist = new ArrayList<>(firstColumnList);
        fist.add(tvFirstHeader.getContentTxt());

        //----宽度设置----
        int firstW = firstColumnWidth;
        int otherW = otherColumnWidth;
        if (layoutWidth <= 0){
            layoutWidth = ScreenUtil.getScreenWidth(mContext);
        }

//        Log.i("我的测试", "始: " + firstW + "  " + otherW+"  屏幕总宽度："+layoutWidth);
        //实际总宽度
        int sum = otherW * headerAdapter.getItemCount() + firstW;

        if (sum < layoutWidth) {
            //如果ScrollTableView小于屏幕宽度，则按比例设置宽度，宽度占满屏幕
            int sw = dip2px(mContext, layoutWidth);
//            Log.i("我的测试", "屏幕总宽度："+sw+"  实际总宽度："+sum);
            firstW = handleFirstWidth(sw, sum);
            otherW = handleOtherWidth(sw);
        }

//        Log.i("我的测试", "終: " + firstW + "  " + otherW);
        tvFirstHeader.setContentTxtWidth(firstW);
        fistColumnAdapter.setItemWidth(firstW);
        headerAdapter.setItemWidth(otherW);
        itemAdapter.setItemWidth(otherW);
    }

    /**
     * 处理第一列宽度
     * @param sw 屏幕空间宽度
     * @param sum 所有列宽之和
     * @return
     */
    private int handleFirstWidth(int sw, int sum){
        if (firstColumnWidthModel == FIRST_COLUMN_WIDTH_AUTO){
            return (int) (firstColumnWidth * 1.0f / sum * sw);
        }
        else if (firstColumnWidthModel == FIRST_COLUMN_WIDTH_FIXED){
            return firstColumnWidth;
        }
        return firstColumnWidth;
    }

    /**
     * 处理其他列宽度
     * @return
     */
    private int handleOtherWidth(int sw){
        return (sw - firstColumnWidth) / headerAdapter.getItemCount();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
