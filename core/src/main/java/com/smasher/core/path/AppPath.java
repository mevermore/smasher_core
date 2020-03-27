package com.smasher.core.path;

import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.smasher.core.other.ApplicationContext;

import java.io.File;

/**
 * @author moyu
 * @date 2017/3/13
 */

public class AppPath {

    private static final String TAG = "AppPath";

    public static void init() {
        String path = getRootPath();
        Log.d(TAG, "init: " + path);
    }

    private static String getRootPath() {

        String result = "";
        File dir = null;
        Application app = ApplicationContext.getInstance();
        if (app == null) {
            return result;
        }

        try {
            String state = Environment.getExternalStorageState();
            if (!TextUtils.isEmpty(state) && Environment.MEDIA_MOUNTED.equals(state)) {

                /*/storage/emulated/0/Android/data/packagename/files/*/
                File file = app.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                if (file != null) {
                    result = file.getParent();
//                    File gap = file.getParentFile();
//                    if (gap != null) {
//                        result = gap.getParent();
//                    }
                }
            }

            if (!TextUtils.isEmpty(result)) {
                dir = new File(result);
            }

            if (dir == null) {
                return result;
            }

            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getSubPath(String subName) {

        if (TextUtils.isEmpty(getRootPath())) {
            return "";
        }

        String result = getRootPath() + File.separator + subName;
        File dir = new File(result);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }


    protected static String getCachePath() {
        File file = ApplicationContext.getInstance().getExternalCacheDir();
        if (file != null) {
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        return null;
    }


    /**
     * Environment.DIRECTORY_PICTURES
     *
     * @param type The type of files directory to return. May be {@code null}
     *             for the root of the files directory or one of the following
     *             constants for a subdirectory:
     *             {@link android.os.Environment#DIRECTORY_MUSIC},
     *             {@link android.os.Environment#DIRECTORY_PODCASTS},
     *             {@link android.os.Environment#DIRECTORY_RINGTONES},
     *             {@link android.os.Environment#DIRECTORY_ALARMS},
     *             {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *             {@link android.os.Environment#DIRECTORY_PICTURES}, or
     *             {@link android.os.Environment#DIRECTORY_MOVIES}.
     * @return ExternalPath
     */
    protected static String getExternalPath(String type) {
        File file = ApplicationContext.getInstance().getExternalFilesDir(type);
        if (file != null) {
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        return null;
    }


    protected static String getFontsPath() {
        return getSubPath("fonts");
    }

    protected static String getImagePath() {
        return getSubPath("image");
    }

    protected static String getLogPath() {
        return getSubPath("log");
    }


}
