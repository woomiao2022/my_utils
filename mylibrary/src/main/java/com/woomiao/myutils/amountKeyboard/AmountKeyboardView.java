package com.woomiao.myutils.amountKeyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.woomiao.myutils.R;


/**
 *
 * 自定义金额输入键盘
 * https://blog.csdn.net/lwh1212/article/details/125499007
 */
public class AmountKeyboardView extends KeyboardView implements KeyboardView.OnKeyboardActionListener {
    private final String TAG = "自定义键盘";
    /**
     * 数字键盘
     */
    private Keyboard keyboardNumber;

    //绑定的输入框
    private EditText mEditText;

    private int decimal = 2;//小数位数，默认小数点后两位

    public AmountKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AmountKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AmountKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        keyboardNumber = new Keyboard(getContext(), R.xml.keyboard_amount);
        //默认使用数字键盘
        setKeyboard(keyboardNumber);
        //是否启用预览
        setPreviewEnabled(false);
        Log.i(TAG, "是否棄用預覽: "+isPreviewEnabled());
        //键盘动作监听
        setOnKeyboardActionListener(this);
    }

    public void bindEditText(EditText editText){
        this.mEditText = editText;
    }


    @Override
    public void onPress(int primaryCode) {
//        if (primaryCode == -5){
//            setPreviewEnabled(false);
//        }else setPreviewEnabled(true);
    }

    @Override
    public void onRelease(int primaryCode) {
    }


    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Editable editable = mEditText.getText();
        int start = mEditText.getSelectionStart();

        //删除
        if (primaryCode == Keyboard.KEYCODE_DELETE){
            if (editable != null && editable.length() > 0 && start > 0) {
                editable.delete(start - 1, start);
            }
            return;
        }

        //數字或字符輸入
        String preStr = editable.toString();
        if (!TextUtils.isEmpty(preStr)){
            //当开头是输入的是0，后面接着不允许输入其他数字，只能输入小数点
            if (preStr.length() == 1 && (preStr.contains("0") && primaryCode != 46)){
                return;
            }

            if (preStr.contains(".")){
                //只允许有一个小数点
                if (primaryCode == 46){
                    return;
                }

                //小数点后只允许输入指定位数
                String[] strings = preStr.split("\\.");
                if (strings.length > 1 && strings[1].length() >= decimal){
                    return;
                }
            }

        }else {
            //第一个字符不能是小数点
            if (primaryCode == 46){
                return;
            }
        }
        editable.insert(start, Character.toString((char) primaryCode));
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

}
