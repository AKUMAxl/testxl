package com.huawei.ivi.hmi.messageadapter.handler;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.ivi.hmi.messageadapter.VideoActionCallback;
import com.huawei.ivi.hmi.netbuslib.bean.NaviStateBean;
import com.huawei.ivi.hmi.netbuslib.bean.NetBusMessageBean;
import com.huawei.ivi.hmi.netbuslib.bean.VideoStateBean;

import java.lang.reflect.Type;

public class VideoHandler {

    public final String TAG = VideoHandler.class.getSimpleName();

    private Context mContext;

    private VideoActionCallback mVideoActionCallback;

    public VideoHandler(Context context){
        this.mContext = context;
    }

    public void registerVideoActionCallback(VideoActionCallback videoActionCallback){
        mVideoActionCallback = videoActionCallback;
    }

    public void handleVideo(String data){

        Gson gson = new Gson();
        Type t = new TypeToken<NetBusMessageBean<VideoStateBean>>() {
        }.getType();
        NetBusMessageBean<VideoStateBean> netBusMessageBean = gson.fromJson(data, t);
        Log.d(TAG, "handleVideo() called with: netBusMessageBean = [" + netBusMessageBean.toString() + "]");
        // todo udpate aiavm

        VideoStateBean videoStateBean = netBusMessageBean.getData();
        Log.d(TAG, "handleVideo() called with: netBusMessageBean = [" + videoStateBean.toString() + "]");
        if (videoStateBean.getAction()==1){
            if (videoStateBean.getVideoName().equals("muse")){
                mVideoActionCallback.playMuse();
            }
            if (videoStateBean.getVideoName().equals("nap")){
                mVideoActionCallback.playNap();
            }
            if (videoStateBean.getVideoName().equals("xf")){
                mVideoActionCallback.playXF();
            }
        }else {
            mVideoActionCallback.stop();
        }
    }
}
