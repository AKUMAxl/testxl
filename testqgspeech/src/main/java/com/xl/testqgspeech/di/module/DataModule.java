package com.xl.testqgspeech.di.module;

import com.xl.testqgspeech.data.IDataInterface;
import com.xl.testqgspeech.data.message.MessageDataProcessor;
import com.xl.testqgspeech.data.voice.VoiceDataProcessor;
import com.xl.testqgspeech.di.annotation.MessageData;
import com.xl.testqgspeech.di.annotation.VoiceData;


import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;


@InstallIn(SingletonComponent.class)
@Module
public abstract class DataModule {

    @VoiceData
    @Binds
    abstract IDataInterface bindSpeechData(VoiceDataProcessor speechDataProcessor);

    @MessageData
    @Binds
    abstract IDataInterface bindMessageData(MessageDataProcessor messageDataProcessor);


}
