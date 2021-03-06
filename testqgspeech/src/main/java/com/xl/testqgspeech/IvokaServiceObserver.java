package com.xl.testqgspeech;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class IvokaServiceObserver implements LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void init(@ApplicationContext Context context){
//        ContextCompat.getSystemService(context, Intent.)
    }


}
