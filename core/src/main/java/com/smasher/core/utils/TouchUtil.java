package com.smasher.core.utils;

import android.app.KeyguardManager;
import android.content.Context;

/**
 * @author moyu
 */
public class TouchUtil {
    /**
     * 为了防止用户或者测试疯狂的点击某个button，写个方法防止按钮连续点击
     */
    private static long mLastClickTime;
    private static final long GAP = 500L;

    /**
     * 判断连续点击
     */
    public static boolean isFastTouch() {
        long time = System.currentTimeMillis();
        if (time - mLastClickTime < GAP) {
            return true;
        }
        mLastClickTime = time;
        return false;
    }

    /**
     * 判断屏幕锁屏
     */
    public static boolean isLockScreen(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
            return true;
        }
        return false;
    }

}
