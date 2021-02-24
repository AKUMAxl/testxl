package com.xl.testqgspeech.di.module;

import android.content.Context;

import com.qinggan.speech.VuiServiceMgr;
import com.xl.testqgspeech.data.IDataInterface;
import com.xl.testqgspeech.data.MessageDataProcessor;
import com.xl.testqgspeech.data.SpeechDataProcessor;
import com.xl.testqgspeech.di.annotation.MessageData;
import com.xl.testqgspeech.di.annotation.SpeechData;


import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;


@InstallIn(ApplicationComponent.class)
@Module
public abstract class DataModule {

    @SpeechData
    @Binds
    abstract IDataInterface bindSpeechData(SpeechDataProcessor speechDataProcessor);

    @MessageData
    @Binds
    abstract IDataInterface bindMessageData(MessageDataProcessor messageDataProcessor);



}
