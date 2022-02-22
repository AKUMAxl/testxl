package com.huawei.ivi.hmi.messageadapter;

import static com.huawei.ivi.hmi.netbuslib.MessageConstant.MSG_TYPE_AIR_CONDITION;
import static com.huawei.ivi.hmi.netbuslib.MessageConstant.MSG_TYPE_NAVI_STATE;
import static com.huawei.ivi.hmi.netbuslib.MessageConstant.MSG_TYPE_VIDEO_INFO;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.huawei.ivi.hmi.messageadapter.handler.AirHandler;
import com.huawei.ivi.hmi.messageadapter.handler.NavHandler;
import com.huawei.ivi.hmi.messageadapter.handler.VideoHandler;
import com.huawei.ivi.hmi.netbuslib.bean.NetBusMessageBean;

import java.lang.reflect.Type;


public class MessageHandler {

    private final String TAG = MessageHandler.class.getSimpleName();

    private MessageSender mSender;

    private Context mContext;

    private NavHandler mNavHandler;

//    private AirHandler mAirHandler;

    private VideoHandler mVideoHandler;

    private MessageHandler() {
        mNavHandler = new NavHandler();
//        mAirHandler = new AirHandler();

    }

    private static class SingletonInstance {
        private static final MessageHandler INSTANCE = new MessageHandler();
    }

    public static MessageHandler getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void init(Context context){
        this.mContext = context;
        mVideoHandler = new VideoHandler(context);
    }

    public void registerVideoActionCallback(VideoActionCallback videoActionCallback){
        mVideoHandler.registerVideoActionCallback(videoActionCallback);
    }

    public void setMessageSender(MessageSender messageSender){
        this.mSender = messageSender;
//        mAirHandler.init(mSender,mContext);
    }

    public void handleMessage(String data){
        Gson gson = new Gson();
        Type t = new TypeToken<NetBusMessageBean>() {
        }.getType();
        NetBusMessageBean netBusMessageBean = gson.fromJson(data, t);
        String type = netBusMessageBean.getType();
        switch (type){
            case MSG_TYPE_NAVI_STATE:
                mNavHandler.handleNavi(data);
                break;
            case MSG_TYPE_AIR_CONDITION:
//                mAirHandler.handAir(mContext,data);
                break;
            case MSG_TYPE_VIDEO_INFO:
                mVideoHandler.handleVideo(data);
                break;
        }
    }


}