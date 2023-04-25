package com.woomiao.wooutilsdemo.custom_spinner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.woomiao.myutils.customspinner.ICustomSpinner;
import com.woomiao.wooutilsdemo.databinding.FragmentCustomSpinnerBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 版本：1.0
 * 创建日期：2023/3/28 0028 17:06
 * 描述：
 * Email:
 */
public class CustomSpinnerTestFragment extends Fragment {
    private FragmentCustomSpinnerBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCustomSpinnerBinding.inflate(inflater, container, false);

        List<String> list = new ArrayList<>();
        if (list.size() == 0){
            list.add("請選擇交易狀態");
            list.add("已支付");
            list.add("未支付");
        }

        //第一个下拉列表，默认样式
        binding.spinner1.setEntries(list);
        binding.spinner1.setSelectListener(new ICustomSpinner() {
            @Override
            public void onSelect(int position, String value) {
                //position选中的下标，value选中的值
            }
        });

        //第二个下拉列表，设置选中字体大小为20sp
        binding.spinner2.setEntries(list);
        binding.spinner2.setSelectListener(new ICustomSpinner() {
            @Override
            public void onSelect(int position, String value) {
                //position选中的下标，value选中的值
            }
        });

        Log.i("下拉列表", "sp转px: 14 -> "+spToPx(14));
        Log.i("下拉列表", "sp转px: 20 -> "+spToPx(20));
        return binding.getRoot();
    }


    /**
     * sp转换为px
     * @param spValue  需要转换的值
     * @return int
     * @createtime 2022/9/16 14:05
     **/
    public float spToPx(int spValue){
        final float density = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spValue * density + 0.5f;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
