package com.xl.testqgspeech.data.voice;

import android.os.Message;
import android.speech.tts.Voice;
import android.util.Log;

import com.qinggan.speech.VuiStatusListener;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VoiceStatus implements VuiStatusListener {

    private final String TAG = this.getClass().getSimpleName();

    @Inject
    public VoiceStatus(){

    }

    @Override
    public void onStart(int i) {
        Log.d(TAG, "onStart() called with: i = [" + i + "]");
    }

    @Override
    public void onNotifyDeviceId(int i) {
        Log.d(TAG, "onNotifyDeviceId() called with: i = [" + i + "]");
    }

    @Override
    public void onEnd() {
        Log.d(TAG, "onEnd() called");
    }

    @Override
    public void onBeginSpeechContent(String s) {
        Log.d(TAG, "onBeginSpeechContent() called with: s = [" + s + "]");
    }

    @Override
    public void onEndSpeechContent() {
        Log.d(TAG, "onEndSpeechContent() called");
    }

    @Override
    public void onBeginSpeech() {
        Log.d(TAG, "onBeginSpeech() called");
    }

    @Override
    public void onEndSpeech() {
        Log.d(TAG, "onEndSpeech() called");
    }

    @Override
    public void onShowVolume(int i) {
        Log.d(TAG, "onShowVolume() called with: i = [" + i + "]");
    }

    @Override
    public boolean onHandleResult(Message message) {
        Log.d(TAG, "onHandleResult() called with: message = [" + message + "]");
        return false;
    }

    @Override
    public void onRecognitionResult(String s) {
        Log.d(TAG, "onRecognitionResult() called with: s = [" + s + "]");
    }

    @Override
    public void onPartialResult(String s) {
        Log.d(TAG, "onPartialResult() called with: s = [" + s + "]");
    }

    @Override
    public void onVuiException(String s) {
        Log.d(TAG, "onVuiException() called with: s = [" + s + "]");
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
    }

    @Override
    public void onAllWaysListening(int i) {
        Log.d(TAG, "onAllWaysListening() called with: i = [" + i + "]");
    }

    @Override
    public void onInterrupt(int i) {
        Log.d(TAG, "onInterrupt() called with: i = [" + i + "]");
    }
}
