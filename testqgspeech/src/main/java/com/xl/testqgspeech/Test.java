package com.xl.testqgspeech;


import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import dagger.hilt.components.SingletonComponent;


public class Test {


    @Inject public Test(){

    }

    public String get(){
        Log.d("xLLL","get");
        return "test";
    }
}
