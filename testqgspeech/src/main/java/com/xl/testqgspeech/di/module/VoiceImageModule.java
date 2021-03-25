package com.xl.testqgspeech.di.module;


import com.xl.testqgspeech.TestView;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewComponent;

@InstallIn(ViewComponent.class)
@Module
public class VoiceImageModule {

    @Provides
    TestView providesTestView(){
        return new TestView();
    }



}
