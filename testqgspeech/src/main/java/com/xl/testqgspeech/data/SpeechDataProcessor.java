package com.xl.testqgspeech.data;

import android.content.Context;

import com.qinggan.speech.VuiServiceMgr;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;


public class SpeechDataProcessor implements IDataInterface{

    private VuiServiceMgr mVuiServiceMgr;

    @Inject
    public SpeechDataProcessor(@ApplicationContext Context context){
        mVuiServiceMgr = VuiServiceMgr.getInstance(context, new VuiServiceMgr.VuiConnectionCallback() {
            @Override
            public void onServiceConnected() {

            }

            @Override
            public void onServiceDisconnect() {

            }
        });
    }


    @Override
    public void sendRequest() {

    }


}
