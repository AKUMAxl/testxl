package com.huawei.ivi.hmi.netlib;

import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CopyOnWriteArrayList;

public class INetCallbackImpl extends INetCallback.Stub{

    private final String TAG = INetCallbackImpl.class.getSimpleName();

    private CopyOnWriteArrayList<MessageCallback> mCallbackList = new CopyOnWriteArrayList<>();
    private NetConnectListener mNetConnectListenerList;

    public void registerNetConnectListener(NetConnectListener netConnectListener){
        this.mNetConnectListenerList = netConnectListener;
    }

    public void unregisterNetConnectListener(){
        this.mNetConnectListenerList = null;
    }

    public void addMessageCallback(MessageCallback messageCallback){
        mCallbackList.add(messageCallback);
    }

     public void removeMessageCallback(MessageCallback messageCallback){
        mCallbackList.remove(messageCallback);
     }

    @Override
    public void onP2pConnectStateChange(boolean connected) throws RemoteException {
        Log.d(TAG, "onP2pConnectStateChange() called with: connected = [" + connected + "]");
        if (mNetConnectListenerList!=null){
            mNetConnectListenerList.onNetAvailableChange(connected);
        }
    }

    @Override
    public void onMessageReceive(String content) throws RemoteException {
        Log.d(TAG, "onMessageReceive() called with: content = [" + content + "]");
        for (MessageCallback messageCallback:mCallbackList) {
            messageCallback.onMessageReceived(content);
        }
    }

}
