package com.xl.testqgspeech.state;


public interface IVoiceState {

    void handleVisibility(boolean show);

    void handleIdleText(String text);

    void handleVoiceStart(int i);

    void handleVoiceEnd();

    void handleAsrStart();

    void handleAsrUpdate(String text);

    void handleAsrEnd();

    void handleTtsStart(String tts);

    void handleTtsEnd();

    void handleInterrupt(String error);

}
