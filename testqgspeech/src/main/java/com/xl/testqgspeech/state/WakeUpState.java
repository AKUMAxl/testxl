package com.xl.testqgspeech.state;


public class WakeUpState extends BaseState{

    public WakeUpState(VoiceStateMachine voiceStateMachine){
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
    }

    @Override
    public void handleVoiceEnd() {

    }

    @Override
    public void handleAsrStart() {
        callbackVoiceImageChange(mWakeupDirection==1?IVoiceCallback.TO_LEFT:IVoiceCallback.TO_RIGHT);
        callbackTextChange("");
        mVoiceStateMachine.setState(mVoiceStateMachine.mAsrState);
    }

    @Override
    public void handleAsrUpdate(String text) {

    }

    @Override
    public void handleAsrEnd() {

    }

    @Override
    public void handleTtsStart(String tts) {
        callbackVoiceImageChange(mWakeupDirection==1?IVoiceCallback.TO_LEFT:IVoiceCallback.TO_RIGHT);
        callbackTextChange(tts);
        mVoiceStateMachine.setState(mVoiceStateMachine.mTtsState);
    }

    @Override
    public void handleTtsEnd() {

    }

    @Override
    public void handleInterrupt(String error) {
        callbackVoiceImageChange(IVoiceCallback.IDLE);
        callbackTextChange("");
        mVoiceStateMachine.setState(mVoiceStateMachine.mIdle);
    }
}
