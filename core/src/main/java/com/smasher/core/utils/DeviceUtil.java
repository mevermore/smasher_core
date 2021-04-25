package com.smasher.core.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import androidx.annotation.RequiresPermission;

import com.smasher.core.log.Logger;
import com.smasher.core.other.ApplicationContext;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * @author moyu
 * @date 2017/4/6
 */
public class DeviceUtil {
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 0;

    public static String getCPUSerial() {
        String str = "", strCPU = "", cpuAddress = "0000000000000000";
        try {
            // 读取CPU信息
            Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            // 查找CPU序列号
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    // 查找到序列号所在行
                    if (str.contains("Serial")) {
                        // 提取序列号
                        strCPU = str.substring(str.indexOf(":") + 1, str.length());
                        // 去空格
                        cpuAddress = strCPU.trim();
                        break;
                    }
                } else {
                    // 文件结尾
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cpuAddress;
    }

    /**
     * 获取CPU名字
     *
     * @return String
     */
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String cpuName = br.readLine();
            br.close();
            return cpuName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "arm";
    }

    /**
     * 判断CPU
     *
     * @return boolean
     */
    public static boolean isARM() {
        String cpuName = getCpuType();
        if (!TextUtils.isEmpty(cpuName)) {
            if (cpuName.toLowerCase().contains("x86")) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取cpu信息
     *
     * @return String
     */
    public static String getCpuString() {
        if ("x86".equalsIgnoreCase(Build.CPU_ABI)) {
            return "Intel";
        }
        String strInfo = "";
        try {
            byte[] bs = new byte[1024];
            RandomAccessFile reader = new RandomAccessFile("/proc/cpuinfo", "r");
            reader.read(bs);
            String ret = new String(bs);
            int index = ret.indexOf(0);
            if (index != -1) {
                strInfo = ret.substring(0, index);
            } else {
                strInfo = ret;
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return strInfo;
    }

    /**
     * 获取cpu类型
     *
     * @return String
     */
    public static String getCpuType() {
        String strInfo = getCpuString();
        String strType = null;
        if (strInfo.contains("ARMv5")) {
            strType = "armv5";
        } else if (strInfo.contains("ARMv6")) {
            strType = "armv6";
        } else if (strInfo.contains("ARMv7")) {
            strType = "armv7";
        } else if (strInfo.contains("Intel")) {
            strType = "x86";
        } else {
            strType = "unknown";
            return strType;
        }
        if (strInfo.contains("neon")) {
            strType += "_neon";
        } else if (strInfo.contains("vfpv3")) {
            strType += "_vfpv3";
        } else if (strInfo.contains(" vfp")) {
            strType += "_vfp";
        } else {
            strType += "_none";
        }
        return strType;
    }

    public static String getSystemVersion() {
        String[] version = {"null", "null", "null", "null"};
        String str1 = "/proc/version";
        String str2;
        String[] arrayOfString;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");

            // KernelVersion
            version[0] = arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // firmware version
        version[1] = Build.VERSION.RELEASE;

        // model
        version[2] = Build.MODEL;

        // system version
        version[3] = Build.DISPLAY;
        String e = "";
        for (String t : version) {
            e = e + t;
        }
        return e.replaceAll("\\|", "_");
    }

    /**
     * WifiMac
     *
     * @return String
     */
    public static String getWifiMac() {
        try {
            WifiManager wm = (WifiManager) ApplicationContext.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            return wm.getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * AndroidId
     *
     * @return String
     */
    public static String getAndroidId() {
        try {
            return Settings.Secure.getString(ApplicationContext.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * IMEI
     *
     * @return String
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static String getIMEI() {
        String id = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) ApplicationContext.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager.getDeviceId() != null) {
                id = telephonyManager.getDeviceId();
            } else {
                //android.provider.Settings;
                id = getAndroidId();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    /**
     * IMSI
     *
     * @return String
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static String getIMSI() {
        String id = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) ApplicationContext.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getSubscriberId();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    /**
     * SimSerial
     *
     * @return String
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static String getSimSerial() {
        String id = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) ApplicationContext.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getSimSerialNumber();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    /**
     * SDK
     *
     * @return String
     */
    public static String getSDK() {
        return Build.VERSION.RELEASE;
    }

    /**
     * Model
     *
     * @return String
     */
    public static String getPhoneModel() {
        return Build.MODEL != null ? Build.MODEL.replaceAll("\\|", "_") : "";
    }

    /**
     * OS
     *
     * @return String
     */
    public static String getPhoneOS() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    /**
     * Brand
     *
     * @return String
     */
    public static String getPhoneBrand() {
        return Build.BRAND != null ? Build.BRAND.replaceAll("\\|", "_") : "";
    }

    /**
     * VersionCode
     *
     * @return int
     */
    public static long getVersionCode() {
        try {
            PackageInfo info = ApplicationContext.getInstance().getPackageManager().getPackageInfo(ApplicationContext.getInstance().getPackageName(), 0);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return info.getLongVersionCode();
            } else {
                return info.versionCode;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * VersionName
     *
     * @return String
     */
    public static String getVersionName() {
        try {
            PackageInfo info = ApplicationContext.getInstance().getPackageManager().getPackageInfo(ApplicationContext.getInstance().getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * ScreenHeight
     *
     * @return int
     */
    public static int getScreenHeight() {
        try {
            DisplayMetrics metric = new DisplayMetrics();
            WindowManager wm = (WindowManager) ApplicationContext.getInstance().getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metric);
            return metric.heightPixels;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * ScreenWidth
     *
     * @return int
     */
    public static int getScreenWidth() {
        try {
            DisplayMetrics metric = new DisplayMetrics();
            WindowManager wm = (WindowManager) ApplicationContext.getInstance().getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metric);
            return metric.widthPixels;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * Density
     *
     * @return int
     */
    public static int getDensity() {
        try {
            DisplayMetrics metric = new DisplayMetrics();
            WindowManager wm = (WindowManager) ApplicationContext.getInstance().getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metric);
            return (int) (160 * metric.density);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据上下文获取屏幕宽度，单位：像素
     *
     * @return int
     **/
    public static int getScreenWidthInPixels() {
        try {
            return ApplicationContext.getInstance().getResources().getDisplayMetrics().widthPixels;
        } catch (Exception ex) {
            Logger.exception(ex);
            return 0;
        }
    }

    /**
     * 根据上下文获取屏幕高度，单位：像素
     *
     * @return int
     **/
    public static int getScreenHeightInPixels() {
        try {
            return ApplicationContext.getInstance().getResources().getDisplayMetrics().heightPixels;
        } catch (Exception ex) {
            Logger.exception(ex);
            return 0;
        }
    }

    /**
     * 获取手机横竖屏
     *
     * @return int
     */
    public static int getScreenOrientation() {
        if (ApplicationContext.getInstance().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return ORIENTATION_PORTRAIT;
        } else {
            return ORIENTATION_LANDSCAPE;
        }
    }

    /**
     * 判断底部是否有虚拟导航栏 （true：虚拟导航栏，false：物理导航栏）
     *
     * @return boolean
     */
    public static boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = ApplicationContext.getInstance().getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * 获取NavigationBar的高度
     *
     * @return int
     */
    public static int getNavigationBarHeight() {
        Resources resources = ApplicationContext.getInstance().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 是否显示虚拟键
     *
     * @param activity activity
     * @return boolean
     */
    public static boolean isNavigationBarShow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                // 竖屏
                return realSize.y != size.y;
            } else {
                //横屏
                return realSize.x != size.x;
            }
        } else {
            boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !menu && !back;
        }
    }

    /**
     * 获取手机状态栏高度
     *
     * @return int
     */
    public static int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;

        //通常这个值会是38
        int statusBarHeight = 38;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = ApplicationContext.getInstance().getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获取系统语言
     *
     * @return String
     */
    public static String getLanguage() {
        String l = Locale.getDefault().getCountry().toLowerCase();
        if ("tw".equals(l) || "hk".equals(l)) {
            return "tw";
        } else {
            return "cn";
        }
    }

    /**
     * 是否6.0以上
     *
     * @return boolean
     */
    public static boolean isM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 是否5.0以下
     *
     * @return boolean
     */
    public static boolean isKitKat() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 是否5.0
     *
     * @return boolean
     */
    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP;
    }
}
