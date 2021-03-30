package com.xl.testqgspeech.state;


public abstract class BaseState implements IVoiceState{

    VoiceStateMachine mVoiceStateMachine;

    int mWakeupDirection;


    void callbackVoiceImageChange(@IVoiceCallback.IMAGE_STATE int imageState){
        if(mVoiceStateMachine.getVoiceCallback()==null){
            return;
        }
        mVoiceStateMachine.getVoiceCallback().onImageStateChange(imageState);
    }

    void callbackTextChange(String text, @IVoiceCallback.TEXT_TYPE int textType){
        if(mVoiceStateMachine.getVoiceCallback()==null){
            return;
        }
        mVoiceStateMachine.getVoiceCallback().onTextChange(text,textType);
    }
}
