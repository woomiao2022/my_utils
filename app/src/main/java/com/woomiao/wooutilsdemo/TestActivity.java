package com.woomiao.wooutilsdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.woomiao.wooutilsdemo.amount_keyboard.AmountKeyboardTestFragment;
import com.woomiao.wooutilsdemo.custom_spinner.CustomSpinnerTestFragment;
import com.woomiao.wooutilsdemo.databinding.ActivityTestBinding;
import com.woomiao.wooutilsdemo.other.OtherFragment;
import com.woomiao.wooutilsdemo.pageNum.PageNumFragment;
import com.woomiao.wooutilsdemo.tableview.TableViewFragment;

public class TestActivity extends AppCompatActivity {
    private ActivityTestBinding binding;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();
        initView();
    }

    private void initView(){
        String title = getIntent().getStringExtra("name");

        switch (title){
            case "amountKey":
                binding.titleTv.setText("金额输入键盘");
                replace(new AmountKeyboardTestFragment());
                break;
            case "customSpinner":
                binding.titleTv.setText("自定义下拉列表");
                replace(new CustomSpinnerTestFragment());
                break;
            case "pageNum":
                binding.titleTv.setText("页码");
                replace(new PageNumFragment());
                break;
            case "tableView":
                binding.titleTv.setText("自定义Excel样式表格");
                replace(new TableViewFragment());
                break;
            case "other":
                binding.titleTv.setText("其他工具");
                replace(new OtherFragment());
                break;
        }
    }


    private void replace(Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_l, fragment);
        fragmentTransaction.commit();
    }
}