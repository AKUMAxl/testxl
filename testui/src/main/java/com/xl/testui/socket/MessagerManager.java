package com.xl.testui.socket;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xl.testui.bean.Device;
import com.xl.testui.bean.MessageBean;
import com.xl.testui.util.DeviceConfigUtil;

import java.lang.reflect.Type;

import static com.xl.testui.socket.DeviceConstant.HW_HOST;

public class MessagerManager {

    private final String TAG = MessagerManager.class.getSimpleName();

    private ServiceMessager mServiceMessager;
    private ClientMessager mClientMessager;

    private MessageCallback mMessageCallback;

    private MessagerManager() {
    }

    private static class SingletonInstance {
        private static final MessagerManager INSTANCE = new MessagerManager();
    }

    public static MessagerManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void registerMessageCallback(MessageCallback messageCallback) {
        this.mMessageCallback = messageCallback;
    }

    public void serviceStart() {
        if (mServiceMessager == null && mMessageCallback != null) {
            mServiceMessager = new ServiceMessager();
            mServiceMessager.registerMessageCallback(mMessageCallback);
            mServiceMessager.start();
        }
    }

    public void clientStart(String hostIp) {
        if (mClientMessager == null && mMessageCallback != null) {
            mClientMessager = new ClientMessager(hostIp);
            mClientMessager.registerMessageCallback(mMessageCallback);
            mClientMessager.start();
            sendMsg(HW_HOST, null);
        }
    }

    public void sendMsg(String destName, String content) {
        String senderName = DeviceConfigUtil.getDeviceName();
        Log.d(TAG, "senderDeviceName: " + senderName);
        BaseMessager messager;
        if (DeviceConfigUtil.isSocketService()) {
            messager = mServiceMessager;
        } else {
            messager = mClientMessager;
        }
        if (messager == null) {
            Log.e(TAG, "sendMsg: messager is null,p2p connect fail");
            return;
        }
        Gson gson = new Gson();
        if (TextUtils.isEmpty(content)) {
            Device device = new Device();
            device.setName(Build.DEVICE);
            device.setP2pmac(DeviceConfigUtil.getP2pMac());
            MessageBean<Device> messageBean = new MessageBean<>();
            messageBean.setType(MessageBean.TYPE_DEVICE_INFO);
            messageBean.setLength(321);
            messageBean.setSenderName(senderName);
            messageBean.setReceiverName(destName);
            Type type = new TypeToken<MessageBean<Device>>() {
            }.getType();
            String jsonStr = gson.toJson(messageBean, type);
            messager.sendMsg(jsonStr, destName);
        } else {
            MessageBean<String> messageBean = new MessageBean<>();
            messageBean.setType(MessageBean.TYPE_DATA);
            messageBean.setLength(321);
            messageBean.setSenderName(senderName);
            messageBean.setReceiverName(destName);
            messageBean.setData(content);
            Type type = new TypeToken<MessageBean<Device>>() {
            }.getType();
            String jsonStr = gson.toJson(messageBean, type);
            messager.sendMsg(jsonStr, destName);
        }
    }


    public void serviceClose() {
        if (mServiceMessager != null) {
            mServiceMessager.closeService();
            mServiceMessager.cleanClient();
            mServiceMessager.unregisterMessageCallback();
            mServiceMessager = null;
        }
    }

    public void clientClose() {
        if (mClientMessager != null) {
            mClientMessager.closeClient();
            mClientMessager.unregisterMessageCallback();
            mClientMessager = null;
        }
    }
}