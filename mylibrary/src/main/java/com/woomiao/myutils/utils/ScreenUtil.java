package com.woomiao.myutils.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

public class ScreenUtil {
    /**
     * 获取屏幕实际宽度和高度
     * @param activity
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int[] getScreenWH(Activity activity){
        Point point = new Point();
        getDispaly(activity).getRealSize(point);
        int[] screen = {point.x,point.y};
        return screen;
    }

    private static Display getDispaly(Activity activity){
        return activity.getWindowManager().getDefaultDisplay();
    }

    private static Point sPoint;

    public static int getScreenWidth(Context context) {
        if (sPoint == null) {
            sPoint = new Point();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getSize(sPoint);
        }
        return sPoint.x;
    }

    public static int getScreenHeight(Context context) {
        if (sPoint == null) {
            sPoint = new Point();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getSize(sPoint);
        }
        return sPoint.y;
    }



    private static int screenWidthPx; //屏幕宽 px
    private static int screenhightPx; //屏幕高 px
    private static float density;//屏幕密度
    private static int densityDPI;//屏幕密度
    private static float screenWidthDip;//  dp单位
    private static float screenHightDip;//  dp单位
    public static int getScreenWidthPx(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        density = dm.density;
        densityDPI = dm.densityDpi;
        screenWidthPx = dm.widthPixels;
        screenhightPx = dm.heightPixels;
        screenWidthDip = px2dip(context, dm.widthPixels);
        screenHightDip = px2dip(context, dm.heightPixels);
        return screenWidthPx;
    }

    public static float getScreenWidthDp(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        density = dm.density;
        densityDPI = dm.densityDpi;
        screenWidthPx = dm.widthPixels;
        screenhightPx = dm.heightPixels;
        screenWidthDip = px2dip(context, dm.widthPixels);
        screenHightDip = px2dip(context, dm.heightPixels);
        return screenWidthDip;
    }



    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
