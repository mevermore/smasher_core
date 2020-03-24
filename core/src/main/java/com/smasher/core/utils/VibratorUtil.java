package com.smasher.core.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.RequiresApi;
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

    /**
     * 创建一次性振动
     *
     * @param milliseconds 震动时长（ms）
     * @param amplitude    振动强度。这必须是1到255之间的值，或者DEFAULT_AMPLITUDE
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createOneShot(long milliseconds, int amplitude) {
        VibrationEffect vibrationEffect = VibrationEffect.createOneShot(milliseconds, amplitude);
    }

    /**
     * 创建波形振动
     *
     * @param timings 交替开关定时的模式，从关闭开始。0的定时值将导致定时/振幅对被忽略。
     * @param repeat  振动重复的模式，如果您不想重复，则将索引放入计时数组中重复，或者-1。
     *                -1 为不重复
     *                0 为一直重复振动
     *                1 则是指从数组中下标为1的地方开始重复振动，重复振动之后结束
     *                2 从数组中下标为2的地方开始重复振动，重复振动之后结束
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createWaveform(long[] timings, int repeat) {
        VibrationEffect vibrationEffect = VibrationEffect.createWaveform(timings, repeat);
    }


    /**
     * 创建波形振动
     *
     * @param timings    振幅值中的定时值。定时值为0振幅可忽视的。
     * @param amplitudes 振幅值中的振幅值。振幅值必须为0和255之间，或为DEFAULT_AMPLITUDE。振幅值为0意味着断开。
     * @param repeat     振动重复的模式，如果您不想重复，则将索引放入计时数组中重复，或者-1。
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createWaveform(long[] timings, int[] amplitudes, int repeat) {
        VibrationEffect vibrationEffect = VibrationEffect.createWaveform(timings, amplitudes, repeat);
    }
}
