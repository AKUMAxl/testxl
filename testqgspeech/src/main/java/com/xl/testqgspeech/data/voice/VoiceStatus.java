package com.xl.testqgspeech.data.voice;

import android.os.Message;
import android.util.Log;

import com.qinggan.ivokaui.RedFlagAnimView;
import com.qinggan.speech.VuiStatusListener;
import com.xl.testqgspeech.constant.VoiceConstant;
import com.xl.testqgspeech.state.VoiceStateMachine;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class VoiceStatus implements VuiStatusListener {

    private final String TAG = this.getClass().getSimpleName();

    @Inject
    VoiceStateMachine mVoiceStateMachine;

    @Inject
    public VoiceStatus(){

    }

    @Override
    public void onStart(int i) {
        Log.d(TAG, "onStart() called with: i = [" + i + "]");
        mVoiceStateMachine.handleVoiceStart(i);
    }

    @Override
    public void onNotifyDeviceId(int i) {
        Log.d(TAG, "onNotifyDeviceId() called with: i = [" + i + "]");
    }

    @Override
    public void onEnd() {
        Log.d(TAG, "onEnd() called");
        mVoiceStateMachine.handleVoiceEnd();
    }

    @Override
    public void onBeginSpeechContent(String s) {
        Log.d(TAG, "onBeginSpeechContent() called with: s = [" + s + "]");
        mVoiceStateMachine.handleTtsStart(s);
    }

    @Override
    public void onEndSpeechContent() {
        Log.d(TAG, "onEndSpeechContent() called");
        mVoiceStateMachine.handleTtsEnd();
    }

    @Override
    public void onBeginSpeech() {
        Log.d(TAG, "onBeginSpeech() called");
        mVoiceStateMachine.handleAsrStart();
    }

    @Override
    public void onEndSpeech() {
        Log.d(TAG, "onEndSpeech() called");
        mVoiceStateMachine.handleAsrEnd();
    }

    @Override
    public void onShowVolume(int i) {
//        Log.d(TAG, "onShowVolume() called with: i = [" + i + "]");
    }

    @Override
    public boolean onHandleResult(Message message) {
        Log.d(TAG, "onHandleResult() called with: message = [" + message + "]");
        return false;
    }

    @Override
    public void onRecognitionResult(String s) {
        Log.d(TAG, "onRecognitionResult() called with: s = [" + s + "]");
        mVoiceStateMachine.handleAsrUpdate(s);
    }

    @Override
    public void onPartialResult(String s) {
        Log.d(TAG, "onPartialResult() called with: s = [" + s + "]");
        mVoiceStateMachine.handleAsrUpdate(s);
    }

    @Override
    public void onVuiException(String s) {
        mVoiceStateMachine.handleInterrupt(s);
    }

    @Override
    public void onBeginSearch() {
        Log.d(TAG, "onBeginSearch() called");
    }

    @Override
    public void onSearchResult(Message message) {
        Log.d(TAG, "onSearchResult() called with: message = [" + message + "]");
    }

    @Override
    public void onEndSearch() {
        Log.d(TAG, "onEndSearch() called");
    }

    @Override
    public void onVoiceClose() {
        Log.d(TAG, "onVoiceClose() called");
    }

    @Override
    public void onShowHelpText(String s) {
        Log.d(TAG, "onShowHelpText() called with: s = [" + s + "]");
        mVoiceStateMachine.handleIdleText(s);
    }

    @Override
    public void onAllWaysListening(int i) {
        Log.d(TAG, "onAllWaysListening() called with: i = [" + i + "]");
    }

    @Override
    public void onInterrupt(int i) {
        Log.d(TAG, "onInterrupt() called with: i = [" + i + "]");
        mVoiceStateMachine.handleInterrupt(String.valueOf(i));
    }
}
