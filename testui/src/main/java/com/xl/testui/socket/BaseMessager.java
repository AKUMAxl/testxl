package com.xl.testui.socket;

public class BaseMessager extends Thread{

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
}
