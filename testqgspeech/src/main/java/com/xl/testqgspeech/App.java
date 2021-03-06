package com.xl.testqgspeech;

import android.app.Application;
import android.content.IntentFilter;

import com.xl.testqgspeech.data.IDataInterface;
import com.xl.testqgspeech.di.annotation.MessageData;
import com.xl.testqgspeech.di.annotation.VoiceData;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class App extends Application {


    @VoiceData
    @Inject
    IDataInterface mVoiceData;

    @MessageData
    @Inject
    IDataInterface mMessageData;

    @Inject
    Test test;

    @Override
    public void onCreate() {
        super.onCreate();
        test.get();
        BootReceiver bootReceiver = new BootReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BOOT_COMPLETED");
        filter.addAction("com.android.internal.intent.action.REQUEST_SHUTDOWN");
        filter.addAction("com.test.test");
        getApplicationContext().registerReceiver(bootReceiver,filter);

    }
}
