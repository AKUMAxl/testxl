package com.qinggan.voice_demo;

import android.content.Context;
import android.os.Message;

import com.qinggan.speech.VuiServiceMgr;
import com.qinggan.speech.VuiStatusListener;
import com.qinggan.speech.VuiVoiceWakeupHandler;

public class VoiceStateManager {

    private VuiServiceMgr mVuiServiceMgr;

    private VoiceStateManager() {
    }

    private static class SingletonInstance {
        private static final VoiceStateManager INSTANCE = new VoiceStateManager();
    }

    public static VoiceStateManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void init(Context context){
        VuiServiceMgr.getInstance(context, new VuiServiceMgr.VuiConnectionCallback() {
            @Override
            public void onServiceConnected() {
                mVuiServiceMgr.addNotifyCallback(mVuiStatusListener);
                mVuiServiceMgr.addVoiceWakeupHandler(mVuiVoiceWakeupHandler);
            }

            @Override
            public void onServiceDisconnect() {

            }
        });
    }

    private VuiStatusListener mVuiStatusListener = new VuiStatusListener() {

        // 开始语音交互
        @Override
        public void onStart(int i) {

        }

        // 无需关注
        @Override
        public void onNotifyDeviceId(int i) {

        }

        // 语音交互结束
        @Override
        public void onEnd() {

        }

        // 开始TTS
        @Override
        public void onBeginSpeechContent(String s) {

        }

        // TTS结束
        @Override
        public void onEndSpeechContent() {

        }

        // 开始识别
        @Override
        public void onBeginSpeech() {

        }

        // 识别结束
        @Override
        public void onEndSpeech() {

        }

        // 无需关注
        @Override
        public void onShowVolume(int i) {

        }

        // 无需关注
        @Override
        public boolean onHandleResult(Message message) {
            return false;
        }

        // 识别结果
        @Override
        public void onRecognitionResult(String s) {

        }

        // 识别中间结果
        @Override
        public void onPartialResult(String s) {

        }

        // 语音异常结束
        @Override
        public void onVuiException(String s) {

        }

        // 无需关注
        @Override
        public void onBeginSearch() {

        }
        // 无需关注
        @Override
        public void onSearchResult(Message message) {

        }

        // 无需关注
        @Override
        public void onEndSearch() {

        }

        // 无需关注
        @Override
        public void onVoiceClose() {

        }

        // 语音提示词
        @Override
        public void onShowHelpText(String s) {

        }

        // 无需关注
        @Override
        public void onAllWaysListening(int i) {

        }

        // 中断语音
        @Override
        public void onInterrupt(int i) {

        }
    };

    private VuiVoiceWakeupHandler mVuiVoiceWakeupHandler = new VuiVoiceWakeupHandler() {

        // 唤醒回调
        @Override
        public void onProcessVoiceWakeup(String s, int i) {

        }
    };

}