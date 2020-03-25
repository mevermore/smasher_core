package com.smasher.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import java.lang.reflect.Method;

/**
 * @author matao
 * @date 2019/6/12
 */
public class NavigationBarUtil {


    //获取是否存在NavigationBar
    public boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                //不存在虚拟按键
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                //存在虚拟按键
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }


    /**
     * 隐藏虚拟按键
     */
    public static void hideBottomUIMenu(Activity activity) {
        //for new api versions.
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_IMMERSIVE |
                        // Hide the nav bar and status bar
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

    }

    /**
     * 显示虚拟按键
     */
    public static void showBottomUIMenu(Activity activity) {
        //for new api versions.
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
