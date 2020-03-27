package com.smasher.core.path;


import android.os.Environment;

/**
 * @author lin
 * @date 2017/7/5
 */

public class Path {

    static {
        AppPath.init();
    }


    public static String getLogPath() {
        return AppPath.getLogPath();
    }

    public static String getDownloadPath() {
        return AppPath.getExternalPath(Environment.DIRECTORY_DOWNLOADS);
    }

    public static String getCachePath() {
        return AppPath.getCachePath();
    }

    public static String getFontsPath() {
        return AppPath.getFontsPath();
    }

    public static String getImagePath() {
        return AppPath.getImagePath();
    }

}
