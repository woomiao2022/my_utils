package com.woomiao.myutils.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
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

    public static final int SA_NORMAL = 0;//标准
    public static final int SA_MIN1 = 1;//较小
    public static final int SA_MIN2 = 2;//最小
    public static int sAdapterType = -1;//屏幕适配等级
    public static void screenAdapter(Activity activity){
        if (sAdapterType != -1)
            return;
        //寬度：2000 标准
        //寬度：1920 销售单据查询页面，顶部有些挤着，可把按钮方至第二行
        //寬度：1600 销售单据查询页面，顶部有些挤着，可把按钮方至第二行
        //寬度：1280 同上、会员管理顶部可隐藏电话和地址、选择数据弹窗顶部可将尾部空格部分缩短、销售退货顶部可将尾部空格部分缩短、销售出库顶部可将尾部空格部分缩短
        //寬度：960 同上、
        Point point = new Point();
        getDispaly(activity).getRealSize(point);
        int w = point.x;
        if (w <= 1280){
            sAdapterType = SA_MIN2;
        }else if (w <= 1600){
            sAdapterType = SA_MIN1;
        }else {
            sAdapterType = SA_NORMAL;
        }
    }
}
