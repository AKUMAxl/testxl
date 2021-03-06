package com.xl.testqgspeech.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class VoiceConstant {

    public static final class VoiceStatusConstant{

        public static final int IDEL = 1;
        public static final int VOICE_WAKEUP = 2;
        public static final int RECOGNITION = 3;
        public static final int RECOGNITION_MULTI_ROUND = 4;
        public static final int RECOGNITION_CLICK = 5;
        public static final int SPEECH_START = 6;
        public static final int SPEECH_ASR = 7;
        public static final int SPEECH_END = 8;
        public static final int OFFLINE_SEMANTIC = 9;
        public static final int ONLINE_SEMANTIC = 10;
        public static final int CHOOSE_SEMANTIC = 11;
        public static final int ACTION_EXECUTOR = 12;
        public static final int CONTICONV_SPEECH_START = 13;
        public static final int CONTICONV_END = 14;
        public static final int INTERRUPT = 15;

    }


    public static final class VoiceAction{
        public static final int HIDE = 0;
        public static final int SHOW = 1;
        public static final int HELP_TEXT_UPDATE = 2;
        public static final int VOICE_START = 3;
        public static final int VOICE_END = 4;
        public static final int ASR_START = 5;
        public static final int ASR_UPDATE = 6;
        public static final int ASR_END = 7;
        public static final int TTS_START = 8;
        public static final int TTS_END = 9;
        public static final int INTERRUPT = 10;
    }

    @IntDef({VoiceAction.HIDE,VoiceAction.SHOW,VoiceAction.HELP_TEXT_UPDATE,VoiceAction.VOICE_START,VoiceAction.VOICE_END,VoiceAction.ASR_START,
            VoiceAction.ASR_UPDATE, VoiceAction.ASR_END,VoiceAction.TTS_START,VoiceAction.TTS_END,VoiceAction.INTERRUPT,})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionType{}

}
