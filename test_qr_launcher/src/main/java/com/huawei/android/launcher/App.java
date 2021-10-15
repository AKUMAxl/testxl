package com.huawei.android.launcher;

import android.app.Application;

import com.huawei.android.launcher.manager.PackagesManager;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PackagesManager.getInstance().init(getApplicationContext());
    }
}
