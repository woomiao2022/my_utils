package com.woomiao.wooutilsdemo.amount_keyboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.woomiao.wooutilsdemo.databinding.FragmentAmountKeyboardBinding;

/**
 * @author Administrator
 * 版本：1.0
 * 创建日期：2023/3/28 0028 17:06
 * 描述：
 * Email:
 */
public class AmountKeyboardTestFragment extends Fragment {
    private FragmentAmountKeyboardBinding binding;
    private TranslateAnimation translateAniShow, translateAniHide;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAmountKeyboardBinding.inflate(inflater, container, false);

        //初始化金额输入键盘
        initAmountKeyboard();

        return binding.getRoot();
    }

    //初始化金额输入键盘
    private void initAmountKeyboard(){
        binding.keyboardView.bindEditText(binding.keyboardEt);
        binding.keyboardEt.bindAmountKeyboardView(binding.keyboardView);

        //收起键盘
        binding.keyboardviewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.keyboardviewLayout.getVisibility() == View.VISIBLE){
                    binding.keyboardEt.clearFocus();
                }
            }
        });

        //输入框焦点改变监听
        binding.keyboardEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    //弹出键盘
                    if (binding.keyboardviewLayout.getVisibility() == View.GONE){
                        binding.keyboardviewLayout.startAnimation(translateAniShow);
                        binding.keyboardviewLayout.setVisibility(View.VISIBLE);
                    }
                }else {
                    //收起键盘
                    if (binding.keyboardviewLayout.getVisibility() == View.VISIBLE){
                        binding.keyboardviewLayout.startAnimation(translateAniHide);
                        translateAniHide.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                binding.keyboardviewLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }
            }
        });

        //初始化键盘弹出和收起的动画
        translateAnimation();
    }

    //位移动画
    private void translateAnimation() {
        //向上位移显示动画  从自身位置的最下端向上滑动了自身的高度
        translateAniShow = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,//RELATIVE_TO_SELF表示操作自身
                0,//fromXValue表示开始的X轴位置
                Animation.RELATIVE_TO_SELF,
                0,//fromXValue表示结束的X轴位置
                Animation.RELATIVE_TO_SELF,
                1,//fromXValue表示开始的Y轴位置
                Animation.RELATIVE_TO_SELF,
                0);//fromXValue表示结束的Y轴位置
        translateAniShow.setRepeatMode(Animation.REVERSE);
        translateAniShow.setDuration(300);

        //向下位移隐藏动画  从自身位置的最上端向下滑动了自身的高度
        translateAniHide = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,//RELATIVE_TO_SELF表示操作自身
                0,//fromXValue表示开始的X轴位置
                Animation.RELATIVE_TO_SELF,
                0,//fromXValue表示结束的X轴位置
                Animation.RELATIVE_TO_SELF,
                0,//fromXValue表示开始的Y轴位置
                Animation.RELATIVE_TO_SELF,
                1);//fromXValue表示结束的Y轴位置
        translateAniHide.setRepeatMode(Animation.REVERSE);
        translateAniHide.setDuration(300);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
