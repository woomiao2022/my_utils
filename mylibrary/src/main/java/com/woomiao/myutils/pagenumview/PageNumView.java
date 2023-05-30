package com.woomiao.myutils.pagenumview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.woomiao.myutils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义页码
 *
 */
public class PageNumView extends LinearLayout {
    public static String PAGE_FLAG_PRE = "left";//左箭头标志
    public static String PAGE_FLAG_NEXT = "right";//右箭头标志
    private RecyclerView recyclerView;
    private TextView tv_total;
    private PageNumAdapter adapter;
    private static final ButtonType[] sButtonTypeArray = {
            ButtonType.IMAGE,
            ButtonType.TXT
    };

    /**
     * 页码显示状态：
     * 数量不够分区时
     * 1234
     * 数量够分区显示时
     * 123...10  最前面显示middle个页码
     * 1234...10 最前面显示max个页码
     * 1...567...10 最前面显示min个页码
     */
    private int min = 1;//最少显示1个页码  1...567...10
    private int middle = 3;// 123...10
    private int max = 4;//最多显示4个页码  1234...10
    public int totalPage = 6;//总页数 如总共3页
    private int size;//列表总数
    private int pageNum;//一页显示的数量

    public static int cPagePosition = 1;//当前页码的下标 默认指向页码1的下标
    private List<String> list = new ArrayList<>();

    private IPageNumView iPageNumView;

    public PageNumView(Context context) {
        super(context);
    }

    public PageNumView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView(context, attrs);
    }

    public PageNumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.page_num, this, true);
        recyclerView = findViewById(R.id.pageNum_recyclerview);
        tv_total = findViewById(R.id.pageNum_tv_total);

        //自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PageNumView);
        //上一页和下一页按钮是否是图标样式，默认是图片样式
        int index = typedArray.getInt(R.styleable.PageNumView_buttonsType, -1);
        if (index < 0){
            index = 0;
        }
        //页码选中时的背景颜色
        int selectColor = typedArray.getColor(R.styleable.PageNumView_selectedColor, context.getResources().getColor(R.color.themeColor_700));
        //总页数文字颜色
        int totalTvColor = typedArray.getColor(R.styleable.PageNumView_totalTvColor, context.getResources().getColor(R.color.text_color));
        //是否显示总页数视图
        boolean isShowTotalView = typedArray.getBoolean(R.styleable.PageNumView_isShowTotalView, true);
        if (!isShowTotalView) {
            tv_total.setVisibility(GONE);
        } else {
            tv_total.setVisibility(VISIBLE);
            tv_total.setTextColor(totalTvColor);
        }
        //上一页、下一页按钮文字 显示为文字样式时生效
        String preBtnT = typedArray.getString(R.styleable.PageNumView_preButtonText);
        String nextBtnT = typedArray.getString(R.styleable.PageNumView_nextButtonText);

        //初始化数据
        list.add(PAGE_FLAG_PRE);
        initData();
        list.add(PAGE_FLAG_NEXT);

        //初始化视图
        adapter = new PageNumAdapter(context, list);
        adapter.setButtonType(sButtonTypeArray[index]);
        adapter.setSelectColor(selectColor);
        adapter.setPreButtonText(preBtnT);
        adapter.setNextButtonText(nextBtnT);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setClick(new IPageNum() {
            @Override
            public void onPre() {
                pre();
            }

            @Override
            public void onNext() {
                next();
            }

            @Override
            public void onItemClick(int position) {
                itemJump(position);
            }
        });
    }

    /**
     * @param pageIndex 定位到的页数
     */
    public void setPageIndex(int pageIndex) {
        if (pageIndex == cPagePosition) return;
        cPagePosition = pageIndex;
        adapter.notifyDataSetChanged();
    }
    /**
     * 指定選中頁 不更新ui
     * @param cPage 選中的頁碼
     */
    public void setPage(int cPage){
        cPagePosition = cPage;
    }

    /**
     * @param totalPage 总页数
     */
    public void setData(int totalPage) {
        this.totalPage = totalPage;
        list.clear();
        list.add(PAGE_FLAG_PRE);
        initData();
        list.add(PAGE_FLAG_NEXT);
        //重置后ui定位到第一页
        cPagePosition = 1;
        adapter.update(list);
    }

    /**
     * @param size    所有数据的总数量
     * @param pageNum 一页显示的数量
     */
    public void setData(int size, int pageNum) {
        if (this.size == size && this.pageNum == pageNum) {
            return;
        }
        this.size = size;
        this.pageNum = pageNum;
        tv_total.setText("共" + size + "項");

        this.totalPage = size / pageNum;
        if (this.totalPage == 0) {
            this.totalPage = 1;
        } else if (size % pageNum != 0) {
            this.totalPage += 1;
        }

        list.clear();
        list.add(PAGE_FLAG_PRE);
        initData();
        list.add(PAGE_FLAG_NEXT);
        adapter.update(list);
    }

    public void setMax(int max) {
        middle = max;
        this.max = max + 1;
    }

    public void setClickListener(IPageNumView iPageNumView) {
        this.iPageNumView = iPageNumView;
    }


    private void initData() {
        //1-3\4个数据
        if (totalPage <= middle + 1) {
            for (int i = 1; i <= totalPage; i++) {
                list.add(i + "");
            }
        } else {
            //4-5个数据 或者 6个以上
            for (int i = 1; i <= middle; i++) {
                list.add(i + "");
            }
            list.add("...");
            list.add(totalPage + "");
        }
    }

    /**
     * 点击指定页码跳转
     *
     * @param position 点击的页码下标
     */
    private void itemJump(int position) {
        int pageNum = Integer.parseInt(list.get(position));
        if ("...".equals(list.get(position - 1))) {
            //往左移动
            int start = pageNum - 1;
            int end = pageNum + middle - 2;
            if (totalPage - pageNum > middle) {
                //位于中间段
                if (pageNum == middle) {
                    addP2(-1, max, pageNum);
                } else addP3(start, end, pageNum);
            } else {
                //位于末尾段
                int num = totalPage - pageNum + 1;
                if (num < middle && pageNum > middle) {
                    //补齐middle个
                    addP2(totalPage - middle + 1, max, pageNum);
                } else if (num < max && pageNum > middle) {
                    //补齐max个
                    addP2(totalPage - max + 1, max, pageNum);
                } else {
                    //分裂
                    if (totalPage > max + 1) {
                        //可分三段
                        if (pageNum > middle) {
                            addP3(start, end, pageNum);
                        } else addP2(-1, max, pageNum);
                    } else {
                        addP1(pageNum);
                    }
                }
            }
        } else if ("...".equals(list.get(position + 1))) {
            //往右移动
            if (pageNum == min) {
                addP2(-1, middle, pageNum);
            } else if (pageNum == middle) {
                if (totalPage > max + 1) {
                    addP2(-1, max, pageNum);
                } else {
                    addP1(pageNum);
                }
            } else {
                if (totalPage - pageNum > 2) {
                    //分三段
                    addP3(pageNum - 1, pageNum + middle - 2, pageNum);
                } else {
                    addP2(pageNum - 1, max, pageNum);
                }
            }
        } else if (pageNum == middle - 1) {
            //将一段缩成middle个 变成两大段
            if (position + 2 > list.size() - 1) {
                cPagePosition = position;
            } else {
                try {
                    //如果第max个页码是数字---> 去掉这数字，改成省略号
                    Integer.parseInt(list.get(position + 2));
                    if (totalPage > max) {
                        addP2(-1, middle, pageNum);
                    } else {
                        cPagePosition = position;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    cPagePosition = position;
                }
            }
        } else if (pageNum == middle) {
            //第一段增加到max
            if (totalPage > max + 1) {
                addP2(-1, max, pageNum);
            } else {
                cPagePosition = position;
            }
        } else {
            int num = totalPage - pageNum + 1;
            if (num < middle && pageNum > max) {
                //处于最后一段-----补齐middle个
                addP2(totalPage - middle + 1, max, pageNum);
            } else if (num < max && pageNum > max) {
                //处于最后一段-----补齐max个
                addP2(totalPage - max + 1, max, pageNum);
            } else {
                cPagePosition = position;
            }
        }

        adapter.update(list);

        if (iPageNumView != null) {
            iPageNumView.onJump(pageNum);
        }
    }

    /**
     * 跳转到指定页码
     *
     * @param pageNum 页码
     */
    public void jumpTo(int pageNum) {
        if (totalPage <= middle + 1) {
            //最多可以分成一个区段 1-3、4个数据
            addP1(pageNum);
        } else if (totalPage <= max + 1) {
            //最多可以分成两个区段 5个数据
            if (pageNum < middle) {
                //123...5
                addP2(-1, middle, pageNum);
            } else {
                //12345
                addP1(pageNum);
            }
        } else {
            //最多可以分成三个区段 6~
            if (pageNum < middle) {
                //123...10
                addP2(-1, middle, pageNum);
            } else if (pageNum == middle) {
                //1234...10
                addP2(-1, max, pageNum);
            } else {
                if (pageNum >= totalPage - middle + 1) {
                    //totalPage-middle+1 的值为最后一段的从后往前数第middle个页码值
                    //在最后一段，如：当前页码为7/8，显示为：1...678 或者 当前页码为6，显示为：1...5678
                    int start = totalPage - middle + 1;//当前页码是最后一个页码
                    if (pageNum < totalPage) {
                        //当前页码不是最后一个页码
                        start = pageNum - 1;
                    }
                    addP2(start, -1, pageNum);
                } else {
                    //1...345...10
                    addP3(pageNum - 1, pageNum + 1, pageNum);
                }
            }
        }

        adapter.update(list);

        if (iPageNumView != null) {
            iPageNumView.onJump(pageNum);
        }
    }

    private void pre() {
        int oldPageIndex = cPagePosition;
        if (oldPageIndex <= 1) return;

        int newPageNum = 0;//上一页页码
        try {
            newPageNum = Integer.parseInt(list.get(oldPageIndex)) - 1;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        if (totalPage <= middle + 1) {
            //最多可以分成一个区段 1-3、4个数据
            cPagePosition--;
        } else if (totalPage <= max + 1) {
            //最多可以分成两个区段 5个数据
            if (newPageNum == middle - 1 && list.contains((middle + 1) + "")) {
                addP2(-1, middle, newPageNum);
            } else {
                cPagePosition--;
            }
        } else {
            //最多可以分成三个区段 6~ 个数据

            if (newPageNum == middle - 1 && !"...".equals(list.get(oldPageIndex + 1))) {
                addP2(-1, middle, newPageNum);
            }
            //点击第middle个页码----> 三段居中/两端居后 需要往前进1-->回到两段
            else if (newPageNum == middle) {
                if (list.contains((max + 1) + "")) {
                    //居中/居后
                    addP2(-1, max, newPageNum);
                } else {
                    //居前
                    list.remove(max);
                    cPagePosition--;
                }
            }
            //点击第二段的第一个页码 三段居中/两端居后 需要往前进1-->回到三段/两段
            else if (newPageNum >= max && oldPageIndex == 3 + min) {
                boolean isTwo = true;//是否是两段
                for (int i = oldPageIndex; i < list.size(); i++) {
                    if ("...".equals(list.get(i))) {
                        isTwo = false;
                        break;
                    }
                }

                if (isTwo) {
                    if (list.size() - oldPageIndex == middle) {
                        //最后一段只显示了middle个，增加到max个
                        addP2(newPageNum - 1, max, newPageNum);
                    } else if (list.size() - oldPageIndex > middle) {
                        //最后一段显示了max个，分裂成三段
                        addP3(newPageNum - 1, newPageNum + middle - 2, newPageNum);
                    } else {
                        cPagePosition--;
                    }
                } else {
                    addP3(newPageNum - 1, newPageNum + middle - 2, newPageNum);
                }
            } else {
                cPagePosition--;
            }
        }
        adapter.update(list);

        if (iPageNumView != null) {
            iPageNumView.onJump(newPageNum);
        }
        Log.i("PageNumView", "pre: " + cPagePosition + "-----" + list.get(cPagePosition));
    }

    private void next() {
        int oldPageIndex = cPagePosition;
        if (oldPageIndex >= list.size() - 2) return;

        int newPageNum = 0;//下一页页码
        try {
            newPageNum = Integer.parseInt(list.get(oldPageIndex)) + 1;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        if (totalPage <= middle) {
            // 1-3个数据
            cPagePosition++;
        } else if (totalPage <= max + 1) {
            //4-5个数据
            //完整显示成一段
            if (newPageNum == middle) {
                addP1(newPageNum);
            } else {
                cPagePosition++;
            }
        } else {
            // 6~ 个数据
            if (newPageNum == middle) {
                //点击第一段的中间页码值----》第一段增加到最大页码数
                addP2(-1, max, newPageNum);
            } else if (newPageNum == max) {
                //点击第一段的最后一个页码---》分成三段或维持两段
                if (totalPage - newPageNum <= 2) {
                    //6个数据时，只能分成两段，最后一段多出一个
                    addP2(newPageNum - 1, max, newPageNum);
                } else {
                    //可以分成三段
                    addP3(newPageNum - 1, newPageNum + middle - 2, newPageNum);
                }
            } else {
                //点击中间段的最后一个页码
                if (newPageNum >= 2 * middle - 1) {
                    if (totalPage - newPageNum > 2) {
                        //后面还有
                        addP3(newPageNum - middle + 2, newPageNum + 1, newPageNum);
                    } else {
                        //后面没有了
                        boolean isThree = false;
                        for (int i = oldPageIndex; i < list.size(); i++) {
                            if ("...".equals(list.get(i))) {
                                isThree = true;
                                break;
                            }
                        }
                        if (isThree) {
                            //合并成两个
                            addP2(newPageNum - 1, max, newPageNum);
                        } else {
                            //剃掉最后一段超过middle数量的数
                            if (list.size() > 3 + min + middle) {
                                addP2(totalPage - middle + 1, max, newPageNum);
                            } else {
                                cPagePosition++;
                            }
                        }
                    }
                } else {
                    cPagePosition++;
                }
            }
        }
        adapter.update(list);

        if (iPageNumView != null) {
            iPageNumView.onJump(newPageNum);
        }
        Log.i("PageNumView", "next:  当前页下标" + cPagePosition + "  当前页码" + list.get(cPagePosition));
    }

    /**
     * 只有一段的数据  1234
     *
     * @param cPage 当前选中的页码值
     */
    private void addP1(int cPage) {
        list.clear();
        list.add(PageNumView.PAGE_FLAG_PRE);
        for (int i = 1; i <= totalPage; i++) {
            list.add(i + "");
        }
        list.add(PageNumView.PAGE_FLAG_NEXT);
        cPagePosition = cPage;
    }

    /**
     * 有两段的数据
     * 集中在前面显示：1234...8
     * 集中在后面显示： 123...6789
     *
     * @param start  集中在后面显示时的第一个页码值
     * @param maxEnd 集中在前面显示时的最后一个页码值
     * @param cPage  当前选中的页码值
     */
    private void addP2(int start, int maxEnd, int cPage) {
        list.clear();
        list.add(PageNumView.PAGE_FLAG_PRE);
        if (start != -1) {
            //集中在后面显示
            for (int i = 1; i <= min; i++) {
                list.add(i + "");
            }
            list.add("...");
            for (int i = start, index = list.size(); i <= totalPage; i++, index++) {
                list.add(i + "");
                if (i == cPage) {
                    cPagePosition = index;
                }
            }
        } else {
            //集中在前面显示
            for (int i = 1; i <= maxEnd; i++) {
                list.add(i + "");
            }
            list.add("...");
            list.add(totalPage + "");
            cPagePosition = cPage;
        }
        list.add(PageNumView.PAGE_FLAG_NEXT);
    }

    /**
     * 有三段的数据  123...789...12
     *
     * @param start 中间一段的第一个页码值
     * @param end   中间一段的最后一个页码值
     * @param cPage 当前选中的页码值
     */
    private void addP3(int start, int end, int cPage) {
        list.clear();
        list.add(PageNumView.PAGE_FLAG_PRE);
        for (int i = 1; i <= min; i++) {
            list.add(i + "");
        }
        list.add("...");
        for (int i = start, index = list.size(); i <= end; i++, index++) {
            list.add(i + "");
            if (i == cPage) {
                cPagePosition = index;
            }
        }
        list.add("...");
        list.add(totalPage + "");
        list.add(PageNumView.PAGE_FLAG_NEXT);
    }

    public enum ButtonType {
        TXT(0),
        IMAGE(1);

        ButtonType(int ni) {
            nativeInt = ni;
        }

        final int nativeInt;
    }
}
