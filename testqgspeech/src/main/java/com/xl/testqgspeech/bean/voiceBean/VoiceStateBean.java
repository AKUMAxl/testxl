package com.xl.testqgspeech.bean.voiceBean;

import java.util.Observable;

public class VoiceStateBean extends Observable {

    private int state;


    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

}
