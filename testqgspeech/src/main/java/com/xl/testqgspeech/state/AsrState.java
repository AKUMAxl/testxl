package com.xl.testqgspeech.state;


public class AsrState extends BaseState{

    public AsrState(VoiceStateMachine voiceStateMachine){
        this.mVoiceStateMachine = voiceStateMachine;
    }

    @Override
    public void handleVisibility(boolean show) {

    }

    @Override
    public void handleIdleText(String text) {

    }

    @Override
    public void handleVoiceStart(int i) {
        mWakeupDirection = i;
        callbackTextChange("",IVoiceCallback.DEFAULT);
        mVoiceStateMachine.setState(mVoiceStateMachine.mWakeupState);
    }

    @Override
    public void handleVoiceEnd() {
        callbackVoiceImageChange(IVoiceCallback.IDLE);
        callbackTextChange("",IVoiceCallback.DEFAULT);
        mVoiceStateMachine.setState(mVoiceStateMachine.mIdle);
    }

    @Override
    public void handleAsrStart() {

    }

    @Override
    public void handleAsrUpdate(String text) {
        callbackTextChange(text,IVoiceCallback.DEFAULT);
    }

    @Override
    public void handleAsrEnd() {

    }

    @Override
    public void handleTtsStart(String tts) {
        callbackVoiceImageChange(mWakeupDirection==1?IVoiceCallback.TO_LEFT:IVoiceCallback.TO_RIGHT);
        callbackTextChange(tts,IVoiceCallback.DEFAULT);
        mVoiceStateMachine.setState(mVoiceStateMachine.mTtsState);
    }

    @Override
    public void handleTtsEnd() {

    }

    @Override
    public void handleInterrupt(String error) {
        callbackVoiceImageChange(IVoiceCallback.IDLE);
        callbackTextChange("",IVoiceCallback.DEFAULT);
        mVoiceStateMachine.setState(mVoiceStateMachine.mIdle);
    }
}
