package com.smasher.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.smasher.core.path.Path;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File files = getFilesDir();
        Log.d(TAG, "FilesDir: " + getFilesDir().getPath());
        if (files.isDirectory()) {
            Log.d(TAG, "FilesDir: is directory");
        }
        if (files.exists()) {
            Log.d(TAG, "FilesDir: is exists");
        }

        File cache = getCacheDir();
        Log.d(TAG, "CacheDir: " + getCacheDir().getPath());
        if (cache.isDirectory()) {
            Log.d(TAG, "CacheDir: is directory");
        }
        if (cache.exists()) {
            Log.d(TAG, "CacheDir: is exists");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d(TAG, "DataDir: " + getDataDir().getPath());
        }

        File documents = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (documents != null) {
            Log.d(TAG, "documents: documents " + documents.getPath());
        } else {
            Log.d(TAG, "documents: documents is null");
        }

        File externalPath = Environment.getExternalStorageDirectory();
        if (externalPath != null) {
            Log.d(TAG, "externalPath: " + externalPath.getPath());
        } else {
            Log.d(TAG, "externalPath is null");
        }

        Log.d(TAG, "onCreate: +++++++++++++++++++++++++++++++++++++");
        Log.d(TAG, "onCreate: =====================================");

        Log.d(TAG, "Cache: " + Path.getCachePath());
        Log.d(TAG, "Download: " + Path.getDownloadPath());

        Log.d(TAG, "Fonts: " + Path.getFontsPath());
        Log.d(TAG, "Image: " + Path.getImagePath());
        Log.d(TAG, "Log: " + Path.getLogPath());
        Log.d(TAG, "Picture: " + Path.getPicturePath());

    }
}
