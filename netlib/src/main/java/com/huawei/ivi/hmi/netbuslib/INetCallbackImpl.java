package com.huawei.ivi.hmi.netbuslib;

import android.os.RemoteException;
import android.util.Log;

import com.huawei.ivi.hmi.netbuslib.bean.BaseDevice;
import com.huawei.ivi.hmi.netbuslib.bean.Device;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class INetCallbackImpl extends INetCallback.Stub {

    private final String TAG = INetCallbackImpl.class.getSimpleName();

    private CopyOnWriteArrayList<MessageCallback> mCallbackList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<NetConnectListener> mNetConnectListenerList = new CopyOnWriteArrayList<>();


    public void addNetConnectListener(NetConnectListener netConnectListener) {
        mNetConnectListenerList.add(netConnectListener);

    }

    public void unregisterNetConnectListener(NetConnectListener netConnectListener) {
        mNetConnectListenerList.remove(netConnectListener);
    }

    public void addMessageCallback(MessageCallback messageCallback) {
        mCallbackList.add(messageCallback);
    }

    public void removeMessageCallback(MessageCallback messageCallback) {
        mCallbackList.remove(messageCallback);
    }



    @Override
    public void onP2pConnectStateChange(boolean connected) throws RemoteException {
        Log.d(TAG, "onP2pConnectStateChange() called with: connected = [" + connected + "]");
        for (NetConnectListener netConnectListener: mNetConnectListenerList) {
            netConnectListener.onP2pConnectStateChange(connected);
        }
    }

    @Override
    public void onSocketConnectStateChange(boolean connected) throws RemoteException {
        Log.d(TAG, "onSocketConnectStateChange() called with: connected = [" + connected + "]");
        for (NetConnectListener netConnectListener: mNetConnectListenerList) {
            netConnectListener.onSocketConnectStateChange(connected);
        }
    }

    @Override
    public void onSocketClientChange(List<BaseDevice> device) throws RemoteException {
        Log.d(TAG, "onSocketClientChange() called with: device = [" + device + "]");
        if (device==null||device.size()==0){
            Log.d(TAG, "onSocketClientChange() device size is 0 or null");
            return;
        }
        for (NetConnectListener netConnectListener: mNetConnectListenerList) {
            netConnectListener.onClientChange(device);
        }
    }


    @Override
    public void onMessageReceive(String content) throws RemoteException {
        Log.d(TAG, "onMessageReceive() called with: content = [" + content + "]");
        for (MessageCallback messageCallback : mCallbackList) {
            messageCallback.onMessageReceived(content);
        }
    }

}
