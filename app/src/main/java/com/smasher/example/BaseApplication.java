package com.smasher.example;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.smasher.core.other.ApplicationContext;

/**
 * @author Smasher
 * on 2020/3/26 0026
 */
public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        ApplicationContext.setApplicationContext(this);
    }
}
