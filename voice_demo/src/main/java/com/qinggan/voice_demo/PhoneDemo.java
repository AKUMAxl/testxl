package com.qinggan.voice_demo;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.speech.tts.Voice;
import android.util.Log;

import com.qinggan.dcs.DcsDataWrapper;
import com.qinggan.dcs.bean.DcsBean;
import com.qinggan.dcs.bean.socialization.TelContactBean;
import com.qinggan.speech.VoiceActionID;
import com.qinggan.speech.VoiceParam;
import com.qinggan.speech.VuiActionHandler;
import com.qinggan.speech.VuiServiceMgr;
import com.qinggan.speech.VuiTtsObj;
import com.qinggan.speech.internal.IVuiTtsProcessHandler;
import com.qinggan.speech.internal.VuiTtsProcessHandler;

import java.util.ArrayList;

public class PhoneDemo {

    public static final String TAG = "PHONE_DEMO";

    public static final String PACKAGE_NAME = "com.qinggan.voice_demo";

    private VuiServiceMgr mVuiServiceMgr;

    public static final int[] mActions = {

            /******************* 电话技能 **********************/
            VoiceActionID.ACTION_DCS_SOCIETY_PHONE_CONTACTS_QUERY,
    };

    private PhoneDemo() {
    }

    private static class SingletonInstance {
        private static final PhoneDemo INSTANCE = new PhoneDemo();
    }

    public static PhoneDemo getInstance() {
        return PhoneDemo.SingletonInstance.INSTANCE;
    }

    public void init(Context context){
        // 初始化
        mVuiServiceMgr = VuiServiceMgr.getInstance(context, new VuiServiceMgr.VuiConnectionCallback() {
            @Override
            public void onServiceConnected() {
                registerVoiceSkill();
                Log.d(TAG, "onServiceConnected() called");
            }

            @Override
            public void onServiceDisconnect() {
                Log.d(TAG, "onServiceDisconnect() called");
            }
        });
    }

    /**
     * 注册语音技能
     */
    private void registerVoiceSkill() {
        mVuiServiceMgr.registerHandler(VuiServiceMgr.AppServiceType.mbBTPhone.ordinal(), mActions, new VuiActionHandler() {
            @Override
            public boolean onProcessResult(Message message) {
                Log.d(TAG, "onProcessResult() called with: message = [" + message.what + "]");
                if (message.what==VoiceActionID.ACTION_DCS_SOCIETY_PHONE_CONTACTS_QUERY){
                    Bundle data = message.getData();
                    DcsDataWrapper wrapper = data.getParcelable(VoiceParam.VOICE_PARAM_DCS_DISPLAY);
                    if (wrapper != null) {
                        ArrayList<DcsBean> beans = wrapper.getDcsBeenList();
                        for (DcsBean dcsBean:beans) {
                            TelContactBean telContactBean = (TelContactBean) dcsBean;
                            Log.d(TAG, "telContactBean = [" + telContactBean.toString() + "]");
                        }
                    }
                    // 如需播放TTS 调用此方法
                    TtsSpeak("好的");
                    return true;
                }

                return false;
            }
        });

    }

    /**
     * TTS
     * @param content 播放内容
     */
    public void TtsSpeak(String content) {
        Log.d(TAG, "TtsSpeak  " + content);
        VuiTtsObj ttsObj = new VuiTtsObj(PACKAGE_NAME, VuiTtsObj.TtsContentType.TEXT);
        ttsObj.setSpeakContent(content);
//        ttsObj.setDisplayText(content);
        mVuiServiceMgr.sendTtsNotify(ttsObj.toIntent(), iVuiTtsProcessHandler);
    }

    /**
     * tts播放状态回调
     */
    private IVuiTtsProcessHandler iVuiTtsProcessHandler = new IVuiTtsProcessHandler() {
        @Override
        public void onStart() {
            // 播放开始
            Log.d(TAG, "video tts onStart  ");
        }

        @Override
        public void onStop() {
            // 播放停止（不一定会播放完成，有可能是被中断回调）
            Log.d(TAG, "video tts onStop  ");
        }

        @Override
        public void onDone() {
            // 播放完成
            Log.d(TAG, "video tts onDone  ");
        }

        @Override
        public void onError() {
            // 出现异常
            Log.d(TAG, "video tts onError  ");
        }
    };}
