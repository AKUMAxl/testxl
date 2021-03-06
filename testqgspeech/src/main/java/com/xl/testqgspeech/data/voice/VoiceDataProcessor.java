package com.xl.testqgspeech.data.voice;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.qinggan.speech.VuiServiceMgr;
import com.xl.testqgspeech.Test;
import com.xl.testqgspeech.data.IDataInterface;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

import static com.xl.testqgspeech.data.voice.VoiceAction.MainRegisterAction;

public class VoiceDataProcessor implements IDataInterface {

    private final String TAG = this.getClass().getSimpleName();

    @Inject
    VoiceStatus mVoiceStatus;

    @Inject
    VoiceWakeupHandler mVuiVoiceWakeupHandler;

    @Inject
    VoiceActionHandler mVuiActionHandler;


    private VuiServiceMgr mVuiServiceMgr;

    private boolean mServiceConnected;


    @Inject
    public VoiceDataProcessor(@ApplicationContext Context context){
        mVuiServiceMgr = VuiServiceMgr.getInstance(context, new VuiServiceMgr.VuiConnectionCallback() {
            @Override
            public void onServiceConnected() {
                Log.d(TAG, "onServiceConnected() called");
                mServiceConnected = true;
                mVuiServiceMgr.addNotifyCallback(mVoiceStatus);
                mVuiServiceMgr.addVoiceWakeupHandler(mVuiVoiceWakeupHandler);
                mVuiServiceMgr.registerHandler(VuiServiceMgr.AppServiceType.mbiVoka.ordinal(),MainRegisterAction,mVuiActionHandler);
            }

            @Override
            public void onServiceDisconnect() {
                Log.d(TAG, "onServiceDisconnect() called");
                mServiceConnected = false;
            }
        });
    }


    @Override
    public void sendRequest() {

    }

}
