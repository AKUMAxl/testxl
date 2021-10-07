package com.xl.testui.socket;

import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.huawei.ivi.hmi.netlib.INetCallback;
import com.huawei.ivi.hmi.netlib.INetInterface;
import com.xl.testui.util.DeviceConfigUtil;

public class INetInterfaceImpl extends INetInterface.Stub {

    private Context mContext;

    private RemoteCallbackList<INetCallback> mINetCallbackList = new RemoteCallbackList<>();


    public INetInterfaceImpl(Context context){
        this.mContext = context;
    }

    @Override
    public void init() throws RemoteException {
        P2pManager.getInstance().init(mContext);
        if (DeviceConfigUtil.isSocketService(DeviceConfigUtil.getDeviceName())){
            P2pManager.getInstance().createGroup();
        }else {
            P2pManager.getInstance().discoverPeer();
        }
    }

    @Override
    public void registerNetCallback(INetCallback netCallback) throws RemoteException {
        mINetCallbackList.register(netCallback);
    }

    @Override
    public void unregisterNetCallback(INetCallback netCallback) throws RemoteException {
        mINetCallbackList.unregister(netCallback);
    }

    @Override
    public void sendMessage(String destDeviceName, String destAppPackageName, String content) throws RemoteException {
        MessagerManager.getInstance().sendMsg(destDeviceName,content);
    }

    public void callbackMessageReceive(String content){
        int size = mINetCallbackList.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mINetCallbackList.getBroadcastItem(i).onMessageReceive(DeviceConfigUtil.getDeviceName(),"",content);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mINetCallbackList.finishBroadcast();
    }

}
