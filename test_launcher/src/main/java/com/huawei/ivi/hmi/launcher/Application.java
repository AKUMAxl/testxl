package com.huawei.ivi.hmi.launcher;

import android.content.IntentFilter;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TestReceiver testReceiver = new TestReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.test.test");
        getApplicationContext().registerReceiver(testReceiver,filter);
    }
}
