package com.woomiao.wooutilsdemo.other;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.woomiao.myutils.MyToast;
import com.woomiao.myutils.amountInputEditText.MoneyInputFilter;
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
                MyToast.getInstance(getContext()).show("点击确定", 3);
            }else {
                isConfirm = true;
                binding.btn.setText("确定");
                binding.tv.showEdit();
                MyToast.getInstance(getContext()).show("点击取消", 3);
            }
        });

        binding.marquetv.setText("1、cardBackgroundColor 设置背景色 CardView是View的子类，View一般使用Background设置背景色，为什么还要单独提取出一个属性让我们来设置背景色呢？ " +
                "为了实现阴影效果，内部已经消耗掉了 Background 属性 " +
                " " +
                "2、cardCornerRadius 设置圆角半径 " +
                " " +
                "3、contentPadding 设置内部padding " +
                " " +
                "View提供了padding设置间距，为什么还要单独提取出一个属性？ " +
                " " +
                "相同的原因，内部消耗掉了 padding 属性 " +
                " " +
                "4、cardElevation 设置阴影大小 " +
                " " +
                "5、cardUseCompatPadding " +
                " " +
                "默认为false，用于5.0及以上，true则添加额外的 padding 绘制阴影 " +
                " " +
                "6、cardPreventCornerOverlap " +
                " " +
                "默认为true，用于5.0及以下，添加额外的 padding，防止内容和圆角重叠 " +
                "———————————————— " +
                "版权声明：本文为CSDN博主「生来如风」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。 " +
                "原文链接：https://blog.csdn.net/qq_39438055/article/details/104414947");

        //9、Android EditText实现金额输入
        InputFilter[] filters = {new MoneyInputFilter()};
        binding.etAmount.setFilters(filters);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
