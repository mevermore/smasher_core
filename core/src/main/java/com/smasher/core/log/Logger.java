package com.smasher.core.log;

import android.util.Log;

import com.smasher.core.io.FileUtil;
import com.smasher.core.other.ApplicationContext;
import com.smasher.core.thread.ThreadPool;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author moyu
 * @date 2017/3/17
 */

public class Logger {
    private static boolean IsDebug = false;
    private static String TAG = "Logger";

    public static void init(boolean isDebug, String tag) {
        IsDebug = isDebug;
        TAG = tag;
    }


    public static void i(String msg) {
        if (IsDebug) {
            Log.i(TAG, msg);
        }
    }


    public static void d(String msg) {
        if (IsDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (IsDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (IsDebug) {
            Log.w(TAG, msg);
        }
    }

    public static void wtf(String msg) {
        if (IsDebug) {
            Log.wtf(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (IsDebug) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable e) {
        if (IsDebug) {
            Log.d(tag, msg, e);
        }
    }

    public static void e(String tag, String msg) {
        if (IsDebug) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (IsDebug) {
            Log.w(tag, msg);
        }
    }

    public static void wtf(String tag, String msg) {
        if (IsDebug) {
            Log.wtf(tag, msg);
        }
    }


    public static void xml(String str) {
        if (IsDebug) {
            Log.d("xml", str);
        }
    }


    public static void json(String str) {
        if (IsDebug) {
            Log.d("demo", str);
        }
    }


    public static void exception(Throwable throwable) {
        if (throwable == null) {
            return;
        }
        throwable.printStackTrace();
        if (IsDebug) {
            Writer info = new StringWriter();
            PrintWriter printWriter = new PrintWriter(info);
            throwable.printStackTrace(printWriter);
            printWriter.close();
            saveLogToFile(info.toString());
        }
    }

    public static void saveLogToFile(final String err) {
        ThreadPool.getInstance(ThreadPool.PRIORITY_WRITE_LOG).submit(new Runnable() {
            @Override
            public void run() {
                Date now = new Date();
                String date = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(now);
                // TODO: 2020/3/24 0024 modify file path
                String path = ApplicationContext.getInstance().getFilesDir().getPath();
                String fullPath = path + date + ".txt";
                FileUtil.saveFile(new File(fullPath), err);

            }
        });
    }
}
