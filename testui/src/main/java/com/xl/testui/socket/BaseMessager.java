package com.xl.testui.socket;

import android.util.Log;

import java.nio.charset.StandardCharsets;

public abstract class BaseMessager extends Thread{

    private final String TAG = BaseMessager.class.getSimpleName();

    public static final String MSG_HEADER = "message_header";

    public static final int BUFFER_SIZE = 1024;
    public static final int PORT = 37000;
    public static final int ACCEPT_TIMEOUT = 1000*60*10;

    public MessageCallback mMessageCallback;

    public void registerMessageCallback(MessageCallback messageCallback){
        this.mMessageCallback = messageCallback;
    }

    public void unregisterMessageCallback(){
        this.mMessageCallback = null;
    }

    public void sendMsg(String jsonStr,String destName){
        Log.d(TAG, "sendMsg() called with: jsonStr = [" + jsonStr + "], destName = [" + destName + "]");
        byte[] header = MSG_HEADER.getBytes(StandardCharsets.UTF_8);
        byte[] data = jsonStr.getBytes(StandardCharsets.UTF_8);
        long length = data.length;
        byte[] dataLength = long2bytes(length);
        byte[] allData = new byte[header.length+ dataLength.length+data.length];
        Log.d(TAG, "header length: "+header.length);
        Log.d(TAG, "dataLength length: "+dataLength.length);
        System.arraycopy(header,0,allData,0,header.length);
        System.arraycopy(dataLength,0,allData,header.length,dataLength.length);
        System.arraycopy(data,0,allData,header.length+dataLength.length,data.length);
        sendMsg(allData,destName);
    }

    abstract void sendMsg(byte[] data,String destDeviceNam);

    public void callbackMessage(String content){
        if (mMessageCallback!=null){
            mMessageCallback.receiveMsg(content);
        }
    }

    public String macBytes2String(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(":").append(Integer.toHexString(0xFF&bytes[i]));
        }
        return sb.substring(1);
    }

    public byte[] long2bytes(long data){
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data&0xff);
        bytes[1] = (byte) (data>>8&0xff);
        bytes[2] = (byte) (data>>16&0xff);
        bytes[3] = (byte) (data>>24&0xff);
        bytes[4] = (byte) (data>>32&0xff);
        bytes[5] = (byte) (data>>40&0xff);
        bytes[6] = (byte) (data>>48&0xff);
        bytes[7] = (byte) (data>>56&0xff);
        return bytes;
    }
}
