package com.woomiao.wooutilsdemo.other;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.woomiao.wooutilsdemo.databinding.FragmentOtherBinding;

/**
 * @author Administrator
 * 版本：1.0
 * 创建日期：2023/4/17 0017 11:07
 * 描述：
 * Email:
 */
public class OtherFragment extends Fragment {
    private FragmentOtherBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOtherBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    private boolean isConfirm = true;
    private void initView() {
        binding.btn.setOnClickListener(v -> {
            if (isConfirm){
                isConfirm = false;
                binding.btn.setText("取消");
                binding.tv.showText();
            }else {
                isConfirm = true;
                binding.btn.setText("确定");
                binding.tv.showEdit();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
