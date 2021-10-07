package com.huawei.ivi.hmi.netlib;

import android.os.RemoteException;

public class INetCallbackImpl extends INetCallback.Stub{
    @Override
    public void onMessageReceive(String srcDeviceName, String srcAppPackageName, String content) throws RemoteException {

    }
}
