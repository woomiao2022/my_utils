package com.woomiao.wooutilsdemo.tableview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.woomiao.myutils.tableView.ScrollTableView;
import com.woomiao.myutils.utils.ScreenUtil;
import com.woomiao.wooutilsdemo.databinding.FragmentTableviewBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Administrator
 * 版本：1.0
 * 创建日期：2023/4/17 0017 11:07
 * 描述：
 * Email:
 */
public class TableViewFragment extends Fragment {
    private FragmentTableviewBinding binding;
    private boolean isShowNo = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTableviewBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        List<UserBean> l = new ArrayList<>();
        binding.btn.setOnClickListener(v -> {
            if (isShowNo){
                isShowNo = false;
                initListView(null);
            }else {
                isShowNo = true;
                binding.rv.showContent();
                initListView(l);
            }
        });

        binding.rv.showTip("加载中...");
        for (int i = 0; i < 14; i++) {
            l.add(new UserBean("姓名"+i, "女", "12", "红红火火恍恍惚惚或fff"));
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initListView(l);
                    }
                });
            }
        }, 2000);

    }
    /**
     * 初始化列表视图
     */
    private List<UserBean> list = new ArrayList<>();//数据集
    private List<String> headerList = new ArrayList<>();//表头数据
    private List<List<String>> rowList = new ArrayList<>();//內容列表
    private void initListView(List<UserBean> list1){
        //更新分页数据
        list.clear();
        if (list1 != null) {
            list.addAll(list1);
        }
        //更新ui
        if (list == null || list.size() == 0){
            binding.rv.showTip("数据是空的");
            return;
        }else {
            binding.rv.showContent();
        }

        /**
         * 更新列表ui
         */
        //添加表頭
        if (headerList.size() == 0){
            headerList.add("姓名");
            headerList.add("性别");
            headerList.add("年龄");
            headerList.add("备注");
            headerList.add("操作");
            binding.rv.setHeaderData(headerList);
            //按鈕點擊事件監聽
            binding.rv.setListener(new ScrollTableView.IScrollTableView() {
                @Override
                public void onDel(int position) {
                }

                @Override
                public void onEdit(int position) {

                }

                @Override
                public void onDetail(int position) {

                }
            });
        }
        //添加內容
        rowList.clear();
        for (int i = 0; i < list.size(); i++) {
            //添加一行數據
            List<String> l = new ArrayList<>();
            l.add(list.get(i).getName());
            l.add(list.get(i).getSex());
            l.add(list.get(i).getAge());
            l.add(list.get(i).getRemark());
            l.add(ScrollTableView.END_ITEM_BTNS);
            rowList.add(l);
        }
        float s = ScreenUtil.getScreenWidthDp(getContext()) * 0.8f;
        Log.i("我的測試", "initListView: "+s);
        binding.rv.setFirstColumnWidth(100)
                .setLayoutWidth(s)
                .setOtherColumnWidth(0)
                .show(rowList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
