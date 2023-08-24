package com.woomiao.myutils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义Toast 不带图标的吐司
 *
 */
public class MyToast extends Toast {
    private Toast toast;
    public static MyToast myToast;
    private View toastView;

    public static MyToast getInstance(Context context) {
        myToast = new MyToast(context);
        return myToast;
    }

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public MyToast(Context context) {
        super(context);
        toastView = LayoutInflater.from(context).inflate(R.layout.layout_mytoast, null);

        if (null == toast) {
            toast = new Toast(context);
        }
    }


    /**
     *
     * @param str
     * @param duration
     */
    public void show(String str,int duration){
        toast.setDuration(duration);
        TextView textView = toastView.findViewById(R.id.tv_ls_toast);
        textView.setText(str);
        toast.setView(toastView);
        toast.show();
    }
}
