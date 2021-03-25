package com.xl.testqgspeech.state;

public class IdleState extends BaseState{

    public IdleState(VoiceStateMachine voiceStateMachine){
        this.mVoiceStateMachine = voiceStateMachine;
    }

    @Override
    public void handleVisibility(boolean show) {

    }

    @Override
    public void handleIdleText(String text) {
        callbackTextChange(text);
    }

    @Override
    public void handleVoiceStart(int i) {
        mWakeupDirection = i;
        callbackTextChange("");
        callbackVoiceImageChange(mWakeupDirection==1?IVoiceCallback.TO_LEFT:IVoiceCallback.TO_RIGHT);
        mVoiceStateMachine.setState(mVoiceStateMachine.mWakeupState);
    }

    @Override
    public void handleVoiceEnd() {

    }

    @Override
    public void handleAsrStart() {

    }

    @Override
    public void handleAsrUpdate(String text) {

    }

    @Override
    public void handleAsrEnd() {

    }

    @Override
    public void handleTtsStart(String tts) {
        callbackTextChange(tts);
        mVoiceStateMachine.setState(mVoiceStateMachine.mWakeupState);
    }

    @Override
    public void handleTtsEnd() {

    }

    @Override
    public void handleInterrupt(String error) {

    }
}
