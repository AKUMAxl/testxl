package com.xl.testqgspeech.data.voice;

import android.os.Message;

import com.qinggan.speech.VuiActionHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VoiceActionHandler implements VuiActionHandler {

    private final String TAG = this.getClass().getSimpleName();

    @Inject
    public VoiceActionHandler(){

    }

    @Override
    public boolean onProcessResult(Message message) {
        return false;
    }

}
