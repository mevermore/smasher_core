package com.smasher.core.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

import androidx.annotation.RequiresPermission;

public class VibratorUtil {
    /**
     * 需要加入震动权限
     * <uses-permission android:name="android.permission.VIBRATE" />
     * final Activity activity ：调用该方法的Activity实例 long milliseconds ：震动的时长，单位是毫秒
     * long[] pattern ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */
    @RequiresPermission(Manifest.permission.VIBRATE)
    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }
}
