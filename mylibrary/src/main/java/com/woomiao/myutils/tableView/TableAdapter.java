package com.woomiao.myutils.tableView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.woomiao.myutils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 版本：1.0
 * 创建日期：2023/4/12 0012 16:16
 * 描述：
 * Email:
 */
public class TableAdapter extends RecyclerView.Adapter<TableAdapter.MyViewHolder> {
    private final Context mContext;
    private List<String> mItemList;
    private int itemWidth = 360;
    private boolean isHeader = false;
    private boolean isFirstColumn = false;
    private int textColor = 0x0ff61686E;
    private Click click;
    private boolean showEdit, showDetail, shoDel;
    private int column = 4;//除去第一列以外，总共多少列

    private int editBtnTvColor = -1;
    private int delBtnTvColor = -1;
    private int detailBtnTvColor = -1;

    public TableAdapter(Context context) {
        this.mContext = context;
        this.mItemList = new ArrayList<>();
        this.shoDel = false;
        this.showEdit = false;
        this.showDetail = false;
    }

    public TableAdapter(Context context, boolean showEdit, boolean shoDel, boolean showDetail,
                        int editBtnTvColor,
                        int delBtnTvColor,
                        int detailBtnTvColor) {
        this.mContext = context;
        this.mItemList = new ArrayList<>();
        this.shoDel = shoDel;
        this.showEdit = showEdit;
        this.showDetail = showDetail;
        this.editBtnTvColor = editBtnTvColor;
        this.delBtnTvColor = delBtnTvColor;
        this.detailBtnTvColor = detailBtnTvColor;
    }

    public void setItemWidth(int width) {
        this.itemWidth = width;
        this.notifyDataSetChanged();
    }

    public void setTextColor(int headerColor) {
        this.textColor = headerColor;
        this.notifyDataSetChanged();
    }

    public void setItemList(List<String> itemList) {
        this.mItemList = itemList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.table_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.itemView.getLayoutParams().width = itemWidth;//解決宽度断裂问题
        if (ScrollTableView.END_ITEM_BTNS.equals(mItemList.get(position))) {
            //删除、详情、编辑按钮
            changeUiForBtns(holder, position);
        } else {
            //文字
            if (isFirstColumn) {
                changeUiForFirstColumn(holder, position);
            } else {
                changeUiForContentOrHeader(holder, position);
            }
        }

    }

    //显示编辑、详情、删除按钮的ui
    private void changeUiForBtns(MyViewHolder holder, int position) {
        holder.btns_l.getLayoutParams().width = itemWidth;
        holder.tv_content.setVisibility(View.GONE);
        holder.btns_l.setVisibility(View.VISIBLE);
        holder.tv_content_f.setVisibility(View.GONE);
        holder.edit_btn.setOnClickListener(v -> {
            if (click != null) {
                click.onEdit(getRealPosition(position));
            }
        });
        holder.detail_btn.setOnClickListener(v -> {
            if (click != null)
                click.onDetail(getRealPosition(position));
        });
        holder.del_btn.setOnClickListener(v -> {
            if (click != null)
                click.onDel(getRealPosition(position));
        });

        if (editBtnTvColor != mContext.getResources().getColor(R.color.themeColor_700)){
            holder.edit_btn.setTextColor(editBtnTvColor);
        }
        if (delBtnTvColor != mContext.getResources().getColor(R.color.red)){
            holder.del_btn.setTextColor(delBtnTvColor);
        }
        if (detailBtnTvColor != mContext.getResources().getColor(R.color.themeColor_700)){
            holder.detail_btn.setTextColor(detailBtnTvColor);
        }

        if (!shoDel) {
            holder.del_btn.setVisibility(View.GONE);
        }
        if (!showDetail) {
            holder.detail_btn.setVisibility(View.GONE);
        }
        if (!showEdit) {
            holder.edit_btn.setVisibility(View.GONE);
        }
        int real = getRealPosition(position);
        if ((real & 1) == 1) {
            holder.btns_l.setBackgroundColor(mContext.getResources().getColor(R.color.grey_f3f3));
        } else {
            holder.btns_l.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
    }

    //显示正文内容或表头的ui
    private void changeUiForContentOrHeader(MyViewHolder holder, int position) {
        holder.tv_content.getLayoutParams().width = itemWidth;
        holder.btns_l.setVisibility(View.GONE);
        holder.tv_content.setVisibility(View.VISIBLE);
        holder.tv_content_f.setVisibility(View.GONE);

        String item = mItemList.get(position);
        holder.tv_content.setText(item);
        if (isHeader) {
            holder.tv_content.setBackgroundColor(mContext.getResources().getColor(R.color.grey_f3f3));
        } else {
            int real = getRealPosition(position);
            if ((real & 1) == 1) {
                holder.tv_content.setBackgroundColor(mContext.getResources().getColor(R.color.grey_f3f3));
            } else {
                holder.tv_content.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
        }
    }

    /**
     * 显示第一列的ui（不包括表头）
     *
     * @param holder
     * @param position 此处下标就是真实列表的下标
     */
    private void changeUiForFirstColumn(MyViewHolder holder, int position) {
        holder.tv_content_f.getLayoutParams().width = itemWidth;
        holder.btns_l.setVisibility(View.GONE);
        holder.tv_content.setVisibility(View.GONE);
        holder.tv_content_f.setVisibility(View.VISIBLE);
        holder.tv_content_f.setText(mItemList.get(position));
        holder.tv_content_f.setTextColor(textColor);
        if ((position & 1) == 1) {
            holder.tv_content_f.setBackgroundColor(mContext.getResources().getColor(R.color.grey_f3f3));
        } else {
            holder.tv_content_f.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
    }

    //获取内容所在的真实下标
    public int getRealPosition(int position) {
        return position / column;
    }

    public void setClick(Click click) {
        this.click = click;
    }

    public void setColumnCountForExceptTheFirst(int column) {
        this.column = column;
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void isHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }

    public void isFirstColumn(boolean isFirstColumn) {
        this.isFirstColumn = isFirstColumn;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_content, tv_content_f;
        private LinearLayout btns_l;
        private TextView edit_btn, detail_btn, del_btn;

        MyViewHolder(View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.scrolltableview_item_content);
            btns_l = itemView.findViewById(R.id.scrolltableview_item_btns);
            edit_btn = itemView.findViewById(R.id.scrolltableview_item_edit);
            detail_btn = itemView.findViewById(R.id.scrolltableview_item_detail);
            del_btn = itemView.findViewById(R.id.scrolltableview_item_del);
            tv_content_f = itemView.findViewById(R.id.scrolltableview_item_content_f);
        }
    }

    public interface Click {
        void onDel(int position);

        void onEdit(int position);

        void onDetail(int position);
    }
}
