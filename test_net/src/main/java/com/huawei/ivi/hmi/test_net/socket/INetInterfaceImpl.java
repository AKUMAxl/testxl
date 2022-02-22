package com.huawei.ivi.hmi.test_net.socket;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.huawei.ivi.hmi.test_net.bean.MessageBean;
import com.huawei.ivi.hmi.netbuslib.INetCallback;
import com.huawei.ivi.hmi.netbuslib.INetInterface;
import com.huawei.ivi.hmi.netbuslib.bean.BaseDevice;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class INetInterfaceImpl extends INetInterface.Stub {

    private final String TAG = INetInterfaceImpl.class.getSimpleName();

    private Context mContext;

    private RemoteCallbackList<INetCallback> mINetCallbackList = new RemoteCallbackList<>();

    private CopyOnWriteArrayList<BaseDevice> mDeviceList = new CopyOnWriteArrayList<>();

    private boolean mP2pConnected;
    private boolean mSocketConnected;

    public INetInterfaceImpl(Context context){
        this.mContext = context;
    }

    @Override
    public void registerNetCallback(INetCallback netCallback, String packageName) throws RemoteException {
        mINetCallbackList.register(netCallback,packageName);
        callbackClientChange();
        callbackP2pConnectState();
        callbackSocketConnectState();
    }

    @Override
    public void unregisterNetCallback(INetCallback netCallback) throws RemoteException {
        mINetCallbackList.unregister(netCallback);
    }

    @Override
    public void sendMessage(String destDeviceName, String destAppPackageName, String content) throws RemoteException {
        Log.d(TAG, "sendMessage() called with: destDeviceName = [" + destDeviceName + "], destAppPackageName = [" + destAppPackageName + "], content = [" + content + "]");
        MessagerManager.getInstance().sendMsg(MessageBean.TYPE_DATA,destDeviceName,destAppPackageName,content);
    }

    public void callbackMessageReceive(String destPackageName,String content){

        int size = mINetCallbackList.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                String broadcastCookie = (String) mINetCallbackList.getBroadcastCookie(i);
                Log.d(TAG, "callbackMessageReceive: broadcastCookie:"+broadcastCookie);
                Log.d(TAG, "callbackMessageReceive: destPackageName:"+destPackageName);
                if (Objects.equals(broadcastCookie,destPackageName)){
                    mINetCallbackList.getBroadcastItem(i).onMessageReceive(content);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mINetCallbackList.finishBroadcast();
    }


    public void callbackClientChange(BaseDevice baseDevice){
        Log.d(TAG, "callbackClientChange() called with: baseDevice = [" + baseDevice + "]");
        if (mDeviceList.size()==0){
            mDeviceList.add(baseDevice);
        }else {
            boolean exist = false;
            for (BaseDevice device:mDeviceList) {
                if (Objects.equals(device.getName(),baseDevice.getName())){
                    device.setP2pip(baseDevice.getP2pip());
                    exist = true;
                    break;
                }
            }
            if (!exist){
                mDeviceList.add(baseDevice);
            }
        }
        callbackClientChange();
    }

    public void callbackP2pConnectState(boolean connect){
        if (mP2pConnected==connect){
            return;
        }
        this.mP2pConnected = connect;
        callbackP2pConnectState();
    }

    public void callbackSocketConnectState(boolean connect){
        this.mSocketConnected = connect;
        callbackSocketConnectState();
    }

    private void callbackClientChange(){
        if (mDeviceList==null||mDeviceList.size()==0){
            Log.e(TAG, "callbackClientChange: mDeviceList is null or 0");
            return;
        }
        int size = mINetCallbackList.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mINetCallbackList.getBroadcastItem(i).onSocketClientChange(mDeviceList);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mINetCallbackList.finishBroadcast();
    }

    private void callbackP2pConnectState(){
        Log.d(TAG, "callbackP2pConnectState() called");
        int size = mINetCallbackList.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mINetCallbackList.getBroadcastItem(i).onP2pConnectStateChange(mP2pConnected);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mINetCallbackList.finishBroadcast();
    }

    private void callbackSocketConnectState(){
        Log.d(TAG, "callbackSocketConnectState() called");
        int size = mINetCallbackList.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mINetCallbackList.getBroadcastItem(i).onSocketConnectStateChange(mSocketConnected);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mINetCallbackList.finishBroadcast();
    }


}
