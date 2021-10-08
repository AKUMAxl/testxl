package com.huawei.ivi.hmi.netlib;

import android.os.RemoteException;
import android.util.Log;

public class INetCallbackImpl extends INetCallback.Stub{

    private final String TAG = INetCallbackImpl.class.getSimpleName();

    @Override
    public void onMessageReceive(String content) throws RemoteException {
        Log.d(TAG, "onMessageReceive() called with: content = [" + content + "]");
    }
}
