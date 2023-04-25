package com.woomiao.wooutilsdemo.pageNum;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.woomiao.myutils.pagenumview.IPageNumView;
import com.woomiao.wooutilsdemo.databinding.FragmentPagenumBinding;

/**
 * @author Administrator
 * 版本：1.0
 * 创建日期：2023/4/17 0017 11:07
 * 描述：
 * Email:
 */
public class PageNumFragment extends Fragment {
    private FragmentPagenumBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPagenumBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        binding.pageNum.setData(50, 5);
        binding.pageNum.setClickListener(new IPageNumView() {
            @Override
            public void onJump(int page) {
                Log.i("我的测试", "onJump: "+page);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
