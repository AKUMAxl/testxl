package com.xl.testqgspeech.state;


import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VoiceStateMachine implements IVoiceState{

    HideState mHideState;
    IdleState mIdle;
    WakeUpState mWakeupState;
    AsrState mAsrState;
    TtsState mTtsState;

    private IVoiceState mCurState;

    private IVoiceCallback mVoiceCallback;

    @Inject
    public VoiceStateMachine(){
        mHideState = new HideState(this);
        mIdle = new IdleState(this);
        mWakeupState = new WakeUpState(this);
        mAsrState = new AsrState(this);
        mTtsState = new TtsState(this);
        mCurState = mIdle;
    }

    @Override
    public void handleVisibility(boolean show){
        mCurState.handleVisibility(show);
    }

    @Override
    public void handleIdleText(String text) {
        mCurState.handleIdleText(text);
    }

    @Override
    public void handleVoiceStart(int i) {
        mCurState.handleVoiceStart(i);
    }

    @Override
    public void handleVoiceEnd(){
        mCurState.handleVoiceEnd();
    }

    @Override
    public void handleAsrStart(){
        mCurState.handleAsrStart();
    }

    @Override
    public void handleAsrUpdate(String text){
        mCurState.handleAsrUpdate(text);
    }

    @Override
    public void handleAsrEnd(){
        mCurState.handleAsrEnd();
    }

    @Override
    public void handleTtsStart(String tts){
        mCurState.handleTtsStart(tts);
    }

    @Override
    public void handleTtsEnd(){
        mCurState.handleTtsEnd();
    }

    @Override
    public void handleInterrupt(String error){
        mCurState.handleInterrupt(error);
    }

    void setState(IVoiceState state){
        mCurState = state;
    }

    public void setVoiceCallback(IVoiceCallback voiceCallback){
        this.mVoiceCallback = voiceCallback;
    }

    public IVoiceCallback getVoiceCallback(){
        return mVoiceCallback;
    }
}
