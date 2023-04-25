package com.woomiao.myutils.pagenumview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.woomiao.myutils.R;

import java.util.List;

public class PageNumAdapter extends RecyclerView.Adapter<PageNumAdapter.VH> {
    private Context context;
    private List<String> list;
    IPageNum iPageNum;
    private PageNumView.ButtonType buttonType;
    private int selectColor = -1;
    private String preBtnTxt = "pre";
    private String nextBtnTxt = "next";

    public PageNumAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        buttonType = PageNumView.ButtonType.TXT;
    }

    public void setButtonType(PageNumView.ButtonType buttonType){
        this.buttonType = buttonType;
    }

    //设置选中的颜色
    public void setSelectColor(int color){
        selectColor = color;
    }

    //设置上一页按钮文字 只在按钮样式为文字样式时有效
    public void setPreButtonText(String t){
        if (TextUtils.isEmpty(t))
            return;
        preBtnTxt = t;
    }

    //设置下一页按钮文字 只在按钮样式为文字样式时有效
    public void setNextButtonText(String t){
        if (TextUtils.isEmpty(t))
            return;
        nextBtnTxt = t;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.page_num_item, parent, false);
        return new VH(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull VH holder, @SuppressLint("RecyclerView") int position) {
//        Log.i(CommonState.TAG, "onBindViewHolder: "+position+"  "+list.get(position));
        //判断显示的视图
        if (position == 0) {
            //当前是上一页按钮
            btnsUi(false, holder);
        } else if (position == list.size() - 1) {
            //当前是下一页按钮
            btnsUi(true, holder);
        } else {
            //当前是页码
            contentUi(holder, position);
        }


        //上一页按钮变化
        if (PageNumView.cPagePosition == 1) {
            //上一页按钮不能使用
            if (buttonType == PageNumView.ButtonType.IMAGE) {
                holder.pre_img.setImageResource(R.drawable.ic_back_left_24);
            } else {
                holder.pre.setBackgroundResource(R.drawable.line_border);
                holder.pre_tv.setTextColor(context.getColor(R.color.grey));
            }
        } else {
            //上一页按钮可以使用
            if (buttonType == PageNumView.ButtonType.IMAGE) {
                holder.pre_img.setImageResource(R.drawable.ic_back_left_24);
            } else {
                holder.pre.setBackgroundResource(R.drawable.line_border);
                holder.pre_tv.setTextColor(context.getColor(R.color.text_color));
            }
        }

        //下一页按钮变化
        if (PageNumView.cPagePosition == list.size() - 2) {
            //下一页按钮不能使用
            if (buttonType == PageNumView.ButtonType.IMAGE) {
                holder.next_img.setImageResource(R.drawable.ic_back_right_24);
            } else {
                holder.next.setBackgroundResource(R.drawable.line_border);
                holder.next_tv.setTextColor(context.getColor(R.color.grey));
            }
        } else {
            if (buttonType == PageNumView.ButtonType.IMAGE) {
                holder.next_img.setImageResource(R.drawable.ic_back_right_24);
            } else {
                holder.next.setBackgroundResource(R.drawable.line_border);
                holder.next_tv.setTextColor(context.getColor(R.color.text_color));
            }
        }

    }

    //按钮
    private void btnsUi(boolean isNext, @NonNull VH holder){
        //下一页按钮
        if (isNext){
            holder.tv.setVisibility(View.GONE);
            holder.more.setVisibility(View.GONE);

            //图片显示
            if (buttonType == PageNumView.ButtonType.IMAGE) {
                holder.imgPre.setVisibility(View.GONE);
                holder.imgNext.setVisibility(View.VISIBLE);
                holder.pre.setVisibility(View.GONE);
                holder.next.setVisibility(View.GONE);
                holder.imgNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (iPageNum != null) iPageNum.onNext();
                    }
                });
            }
            //文字显示
            else {
                holder.pre.setVisibility(View.GONE);
                holder.next.setVisibility(View.VISIBLE);
                holder.imgPre.setVisibility(View.GONE);
                holder.imgNext.setVisibility(View.GONE);
                holder.next_tv.setText(nextBtnTxt);
                holder.next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (iPageNum != null) iPageNum.onNext();
                    }
                });
            }
            return;
        }

        //上一页按钮
        holder.tv.setVisibility(View.GONE);
        holder.more.setVisibility(View.GONE);

        //图片显示
        if (buttonType == PageNumView.ButtonType.IMAGE) {
            holder.imgPre.setVisibility(View.VISIBLE);
            holder.imgNext.setVisibility(View.GONE);
            holder.pre.setVisibility(View.GONE);
            holder.next.setVisibility(View.GONE);

            holder.imgPre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iPageNum != null) iPageNum.onPre();
                }
            });
        }
        //文字显示
        else {
            holder.pre.setVisibility(View.VISIBLE);
            holder.next.setVisibility(View.GONE);
            holder.imgPre.setVisibility(View.GONE);
            holder.imgNext.setVisibility(View.GONE);
            holder.pre_tv.setText(preBtnTxt);

            holder.pre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iPageNum != null) iPageNum.onPre();
                }
            });
        }
    }

    //内容
    private void contentUi(@NonNull VH holder, int position){
        holder.pre.setVisibility(View.GONE);
        holder.next.setVisibility(View.GONE);
        holder.imgPre.setVisibility(View.GONE);
        holder.imgNext.setVisibility(View.GONE);

        holder.tv.setText(list.get(position));

        //省略号
        if ("...".equals(list.get(position))) {
            //省略的页码
            holder.tv.setVisibility(View.GONE);
            holder.more.setVisibility(View.VISIBLE);
        }
        //页码
        else {
            //显示的页码
            holder.tv.setVisibility(View.VISIBLE);
            holder.more.setVisibility(View.GONE);
            holder.tv.setTextColor(context.getResources().getColor(R.color.grey));
            if (position == PageNumView.cPagePosition) {
                //选中页
                holder.tv.setBackgroundColor(selectColor == -1 ? context.getResources().getColor(R.color.red) : selectColor);
                holder.tv.setTextColor(Color.WHITE);
            } else {
                //未选中页
                holder.tv.setBackgroundResource(R.drawable.line_border);
            }
        }

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iPageNum != null) {
                    iPageNum.onItemClick(position);
                }
            }
        });
    }

    public void setClick(IPageNum iPageNum) {
        this.iPageNum = iPageNum;
    }

    public void update(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView tv, more;
        LinearLayout pre, next, imgPre, imgNext;
        TextView pre_tv, next_tv;
        ImageView pre_img, next_img;

        @SuppressLint("CutPasteId")
        public VH(@NonNull View itemView) {
            super(itemView);
            more = itemView.findViewById(R.id.pageNum_item_more);
            pre = itemView.findViewById(R.id.pageNum_item_pre);
            next = itemView.findViewById(R.id.pageNum_item_next);
            imgPre = itemView.findViewById(R.id.pageNum_item_ImgPre);
            imgNext = itemView.findViewById(R.id.pageNum_item_ImgNext);
            tv = itemView.findViewById(R.id.pageNum_item_tv);
            next_tv = itemView.findViewById(R.id.pageNum_item_next_tv);
            pre_tv = itemView.findViewById(R.id.pageNum_item_pre_tv);
            next_img = itemView.findViewById(R.id.pageNum_item_next_img);
            pre_img = itemView.findViewById(R.id.pageNum_item_pre_img);
        }
    }
}
