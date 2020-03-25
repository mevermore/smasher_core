package com.smasher.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: " + getFilesDir().getPath());
        Log.d(TAG, "onCreate: " + getCacheDir().getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d(TAG, "onCreate: " + getDataDir().getPath());
        }

        File documents = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (documents != null) {
            Log.d(TAG, "onCreate: " + documents.getPath());
        } else {
            Log.d(TAG, "onCreate: documents is null");
        }

    }
}
