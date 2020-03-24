package com.smasher.core.utils;

import android.util.DisplayMetrics;

import com.smasher.core.other.ApplicationContext;


/**
 * @author moyu
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率dip --> px
     *
     * @param dpValue dpValue
     */
    public static int dip2px(float dpValue) {
        final float scale = ApplicationContext.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率 px --> dp
     *
     * @param pxValue pxValue
     */
    public static int px2dip(float pxValue) {
        final float scale = ApplicationContext.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率 sp --> px
     *
     * @param sp sp
     */
    public static float sp2px(float sp) {
        final float scale = ApplicationContext.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    /**
     * 手机的分辨率getDensity
     */
    public static float getDensity() {
        return ApplicationContext.getInstance().getResources().getDisplayMetrics().density;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenW() {
        DisplayMetrics dm = ApplicationContext.getInstance().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenH() {
        DisplayMetrics dm = ApplicationContext.getInstance().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
