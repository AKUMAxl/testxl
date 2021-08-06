package com.xl.testui.function;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.qinggan.dcs.DcsDataWrapper;
import com.qinggan.dcs.bean.DcsBean;
import com.qinggan.dcs.bean.video.VideoScreenControlBean;
import com.qinggan.speech.VoiceActionID;
import com.qinggan.speech.VoiceParam;
import com.qinggan.speech.VuiActionHandler;
import com.qinggan.speech.VuiServiceMgr;

import java.util.Objects;

public class VideoDemo {

    public static final String TAG = "VideoDemo";

    private VuiServiceMgr mVuiServiceMgr;



    private VideoDemo() {
    }

    private static class SingletonInstance {
        private static final VideoDemo INSTANCE = new VideoDemo();
    }

    public static VideoDemo getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void init(Context context){
        mVuiServiceMgr = VuiServiceMgr.getInstance(context, new VuiServiceMgr.VuiConnectionCallback() {
            @Override
            public void onServiceConnected() {
                bindVoiceService();
                Log.d(TAG, "onServiceConnected() called");
            }

            @Override
            public void onServiceDisconnect() {
                Log.d(TAG, "onServiceDisconnect() called");
            }
        });
    }

    public static final int[] mActions = {
            VoiceActionID.ACTION_VIDEO_CONTROL_SCREEN
    };

    private void bindVoiceService() {
        mVuiServiceMgr.registerHandler(VuiServiceMgr.AppServiceType.mbDiscVideo.ordinal(), mActions, new VuiActionHandler() {
            @Override
            public boolean onProcessResult(Message message) {
                Log.d(TAG, "onProcessResult() called with: message = [" + message.what + "]");
                if (message.what==VoiceActionID.ACTION_VIDEO_CONTROL_SCREEN){
                    Bundle data = message.getData();
                    DcsDataWrapper wrapper = data.getParcelable(VoiceParam.VOICE_PARAM_DCS_DISPLAY);
                    DcsBean dcsBean = wrapper.getDcsBean();
                    if (dcsBean instanceof VideoScreenControlBean){
                        VideoScreenControlBean videoScreenControlBean = (VideoScreenControlBean) dcsBean;
                        Log.d(TAG, "onProcessResult: "+videoScreenControlBean.toString());
                        String type = videoScreenControlBean.getStateControlType();
                        if ("FULL_SCREEN".equalsIgnoreCase(type)){
                            // 进入全屏
                        }
                        if ("EXIT_FULL_SCREEN".equalsIgnoreCase(type)){
                            // 退出全屏
                        }
                    }
                }

                return true;
            }
        });
    }

}