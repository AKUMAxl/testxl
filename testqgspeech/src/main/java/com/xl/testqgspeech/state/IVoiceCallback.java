package com.xl.testqgspeech.state;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface IVoiceCallback {

    public static final int IDLE = 0;
    public static final int TO_LEFT = 1;
    public static final int TO_RIGHT = 2;

    public static final int DEFAULT = 0;
    public static final int SCROLL = 1;

    @IntDef({IDLE,TO_LEFT,TO_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    @interface IMAGE_STATE {}

    @IntDef({DEFAULT,SCROLL})
    @Retention(RetentionPolicy.SOURCE)
    @interface TEXT_TYPE{}

    void onImageStateChange(@IMAGE_STATE int imageState);

    void onTextChange(String text,@TEXT_TYPE int textType);





}
