package com.xl.testqgspeech.data.voice;

import android.content.Context;
import android.util.Log;

import com.qinggan.speech.UIControl;
import com.qinggan.speech.UIControlElementItem;
import com.qinggan.speech.VuiServiceMgr;
import com.qinggan.speech.customqa.CustomQAMgr;
import com.xl.testqgspeech.data.IDataInterface;
import com.xl.testqgspeech.state.IVoiceCallback;
import com.xl.testqgspeech.state.VoiceStateMachine;

import java.util.ArrayList;

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

    @Inject
    VoiceStateMachine mVoiceStateMachine;

//    @Inject
//    CustomQAMgr mCustomQAMgr;

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
        UIControlElementItem uiControlElementItem1 = new UIControlElementItem();
        uiControlElementItem1.setIdentify("test1");
        uiControlElementItem1.setWord("今天天气");
        uiControlElementItem1.setWord("今天的天气");
        UIControlElementItem uiControlElementItem2 = new UIControlElementItem();
        uiControlElementItem2.setIdentify("test1");
        uiControlElementItem2.setWord("明天天气");
        uiControlElementItem2.setWord("明天的天气");
        ArrayList<UIControlElementItem> list = new ArrayList<>();
        list.add(uiControlElementItem1);
        list.add(uiControlElementItem2);
        UIControl.getInstance().setElementUCWords(list);
    }

    public void setVoiceCallback(IVoiceCallback voiceCallback){
        mVoiceStateMachine.setVoiceCallback(voiceCallback);
    }

}
