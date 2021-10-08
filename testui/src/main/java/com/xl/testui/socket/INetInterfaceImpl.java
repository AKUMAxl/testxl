package com.xl.testui.socket;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.huawei.ivi.hmi.netlib.INetCallback;
import com.huawei.ivi.hmi.netlib.INetInterface;
import com.xl.testui.util.DeviceConfigUtil;

public class INetInterfaceImpl extends INetInterface.Stub {

    private final String TAG = INetInterfaceImpl.class.getSimpleName();

    private Context mContext;

    private RemoteCallbackList<INetCallback> mINetCallbackList = new RemoteCallbackList<>();


    public INetInterfaceImpl(Context context){
        this.mContext = context;
    }


    @Override
    public void registerNetCallback(INetCallback netCallback, String packageName) throws RemoteException {
        mINetCallbackList.register(netCallback,packageName);
    }

    @Override
    public void unregisterNetCallback(INetCallback netCallback) throws RemoteException {
        mINetCallbackList.unregister(netCallback);
    }

    @Override
    public void sendMessage(String destDeviceName, String destAppPackageName, String content) throws RemoteException {
        Log.d(TAG, "sendMessage() called with: destDeviceName = [" + destDeviceName + "], destAppPackageName = [" + destAppPackageName + "], content = [" + content + "]");
        MessagerManager.getInstance().sendMsg(destDeviceName,content);
    }

    public void callbackMessageReceive(String destPackageName,String content){
        int size = mINetCallbackList.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                String broadcastCookie = (String) mINetCallbackList.getBroadcastCookie(i);
                String registeredCallbackCookie = (String) mINetCallbackList.getRegisteredCallbackCookie(i);
                Log.d(TAG, "callbackMessageReceive: broadcastCookie"+broadcastCookie);
                Log.d(TAG, "callbackMessageReceive: registeredCallbackCookie"+registeredCallbackCookie);
                mINetCallbackList.getBroadcastItem(i).onMessageReceive(content);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mINetCallbackList.finishBroadcast();
    }

}
