package com.huawei.ivi.hmi.test_net.socket;

import static com.huawei.ivi.hmi.test_net.bean.MessageBean.TYPE_DEVICES_CHANGE;
import static com.huawei.ivi.hmi.test_net.bean.MessageBean.TYPE_DEVICE_INFO;

import android.os.Process;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.ivi.hmi.test_net.BuildConfig;
import com.huawei.ivi.hmi.test_net.util.ThreadPoolUtil;
import com.huawei.ivi.hmi.netbuslib.bean.BaseDevice;
import com.huawei.ivi.hmi.netbuslib.bean.Device;
import com.huawei.ivi.hmi.test_net.bean.MessageBean;
import com.huawei.ivi.hmi.test_net.util.DeviceConfigUtil;
import com.huawei.ivi.hmi.netbuslib.DeviceConstant;


import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessagerManager {

    private final String TAG = MessagerManager.class.getSimpleName();

    private ServiceMessager mServiceMessager;
    private ClientMessager mClientMessager;

    private CopyOnWriteArrayList<MessageCallback> mMessageCallbackList = new CopyOnWriteArrayList<>();

    private MessagerManager() {
    }

    private static class SingletonInstance {
        private static final MessagerManager INSTANCE = new MessagerManager();
    }

    public static MessagerManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void registerMessageCallback(MessageCallback messageCallback) {
        mMessageCallbackList.add(messageCallback);
    }

    public void removeMessageCallback(MessageCallback messageCallback){
        if (mMessageCallbackList.contains(messageCallback)){
            mMessageCallbackList.remove(messageCallback);
        }
    }

    public synchronized void serviceStart() {
        Log.d(TAG, "serviceStart() called");
        if (mServiceMessager == null) {
            Log.d(TAG, "serviceStart - > to init");
            mServiceMessager = new ServiceMessager();
            mServiceMessager.setMessageCallback(mMessageCallbackList);
            ThreadPoolUtil.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    mServiceMessager.startServiceSocket();
                }
            });
        }
    }

    public synchronized void clientStart(String hostIp) {
        Log.d(TAG, "clientStart() called with: hostIp = [" + hostIp + "]");
//        if (Objects.equals(BuildConfig.FLAVOR,"hw_host")&& Process.myUid()/100000!=10){
//            Log.e(TAG, "onCreate: hw_host uid!=10");
//            return;
//        }
        if (mClientMessager == null) {
            Log.d(TAG, "clientStart - > to init");
            mClientMessager = new ClientMessager(hostIp);
            mClientMessager.setMessageCallback(mMessageCallbackList);
            ThreadPoolUtil.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    mClientMessager.connect(new ClientMessager.ConnectServiceStateListener() {
                        @Override
                        public void onConnected() {
                            Log.d(TAG, "onConnected() called");
                            for (MessageCallback messageCallback : mMessageCallbackList){
                                messageCallback.onClientConnectState(true);
                            }
                            Gson gson = new Gson();
                            Device device = new Device();
                            device.setName(DeviceConfigUtil.getDeviceName());
                            Type type = new TypeToken<Device>() {
                            }.getType();
                            String jsonStr = gson.toJson(device, type);
                            sendMsg(MessageBean.TYPE_DEVICE_INFO,DeviceConstant.DEVICE_NAME.LANTU, BuildConfig.APPLICATION_ID,jsonStr);
                        }

                        @Override
                        public void onDisConnect() {
                            Log.d(TAG, "onDisConnect() called");
                            for (MessageCallback messageCallback : mMessageCallbackList){
                                messageCallback.onClientConnectState(false);
                            }
                        }
                    });
                }
            });

        }
    }



    public void serviceClose() {
        if (mServiceMessager != null) {
            mServiceMessager.closeService();
            mServiceMessager.cleanClient();
            mServiceMessager.clearMessageCallback();
            mServiceMessager = null;
        }
    }

    public void p2pDisconnect() {
        if (mClientMessager != null) {
            mClientMessager.p2pDisconnect();
//            mClientMessager = null;
        }
    }

    /**
     *
     * @param msgType
     * {@link com.huawei.ivi.hmi.test_net.bean.MessageBean#TYPE_DEVICE_INFO}
     * {@link com.huawei.ivi.hmi.test_net.bean.MessageBean#TYPE_DEVICES_CHANGE}
     * {@link com.huawei.ivi.hmi.test_net.bean.MessageBean#TYPE_DATA}
     * @param destName
     * @param destAppPackageName {@link com.huawei.ivi.hmi.netbuslib.DeviceConstant.DEVICE_NAME}
     * @param content
     */
    public void sendMsg(int msgType,String destName, String destAppPackageName,String content) {

        BaseMessager messager;
        if (DeviceConfigUtil.isHost()) {
            messager = getServiceMessager();
        } else {
            messager = getClientMessager();
        }
        if (messager == null) {
            Log.e(TAG, "sendMsg: messager is null,p2p connect fail");
            return;
        }
        Gson gson = new Gson();
        MessageBean<String> messageBean = new MessageBean<>();
        messageBean.setType(msgType);
        messageBean.setDestPackageName(destAppPackageName);
        messageBean.setSenderName(DeviceConfigUtil.getDeviceName());
        messageBean.setReceiverName(destName);
        messageBean.setData(content);
        Type type;
        if (msgType == TYPE_DEVICE_INFO){
            type = new TypeToken<MessageBean<Device>>() {
            }.getType();
        }else if (msgType == TYPE_DEVICES_CHANGE){
            type = new TypeToken<MessageBean<BaseDevice>>() {
            }.getType();
        }else {
            type = new TypeToken<MessageBean>() {
            }.getType();
        }
        String jsonStr = gson.toJson(messageBean, type);
        messager.sendMsg(false,jsonStr, destName);
    }

    private BaseMessager getServiceMessager(){
        serviceStart();
        return mServiceMessager;
    }

    private BaseMessager getClientMessager(){
        clientStart(ClientMessager.HOST);
        return mClientMessager;
    }

}