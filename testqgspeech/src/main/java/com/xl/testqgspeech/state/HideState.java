package com.xl.testqgspeech.state;

public class HideState extends BaseState{

    public HideState(VoiceStateMachine voiceStateMachine){
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

    }

    @Override
    public void handleTtsEnd() {

    }

    @Override
    public void handleInterrupt(String error) {

    }
}
