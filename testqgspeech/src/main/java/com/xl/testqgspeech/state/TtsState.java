package com.xl.testqgspeech.state;


public class TtsState extends BaseState{

    public TtsState(VoiceStateMachine voiceStateMachine){
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
        callbackTextChange("");
        mVoiceStateMachine.setState(mVoiceStateMachine.mWakeupState);
    }

    @Override
    public void handleVoiceEnd() {
        callbackVoiceImageChange(IVoiceCallback.IDLE);
        mVoiceStateMachine.setState(mVoiceStateMachine.mIdle);
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
        callbackTextChange(tts);
    }

    @Override
    public void handleTtsEnd() {
        callbackVoiceImageChange(IVoiceCallback.IDLE);
        callbackTextChange("");
        mVoiceStateMachine.setState(mVoiceStateMachine.mIdle);
    }

    @Override
    public void handleInterrupt(String error) {
        callbackVoiceImageChange(IVoiceCallback.IDLE);
        callbackTextChange("");
        mVoiceStateMachine.setState(mVoiceStateMachine.mIdle);
    }
}
