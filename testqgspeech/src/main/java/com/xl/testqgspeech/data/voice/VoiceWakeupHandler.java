package com.xl.testqgspeech.data.voice;

import android.util.Log;

import com.qinggan.speech.VuiVoiceWakeupHandler;
import com.xl.testqgspeech.state.VoiceStateMachine;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VoiceWakeupHandler implements VuiVoiceWakeupHandler {

    private final String TAG = this.getClass().getSimpleName();

    @Inject
    VoiceStateMachine mVoiceStateMachine;

    @Inject public VoiceWakeupHandler(){

    }

    @Override
    public void onProcessVoiceWakeup(String s, int i) {
        Log.d(TAG, "onProcessVoiceWakeup() called with: s = [" + s + "], i = [" + i + "]");
        mVoiceStateMachine.handleVoiceStart(i);
    }
}
