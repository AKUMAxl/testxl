package com.xl.testqgspeech;

import android.app.Application;
import android.content.IntentFilter;

import com.xl.testqgspeech.data.IDataInterface;
import com.xl.testqgspeech.di.annotation.SpeechData;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;
import dagger.internal.ProviderOfLazy;

@HiltAndroidApp
public class App extends Application {





    @Override
    public void onCreate() {
        super.onCreate();
        BootReceiver bootReceiver = new BootReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BOOT_COMPLETED");
        filter.addAction("com.android.internal.intent.action.REQUEST_SHUTDOWN");
        getApplicationContext().registerReceiver(bootReceiver,filter);

    }
}
