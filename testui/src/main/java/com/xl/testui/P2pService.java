package com.xl.testui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xl.testui.bean.MessageBean;
import com.xl.testui.socket.INetInterfaceImpl;
import com.xl.testui.socket.IPMapping;
import com.xl.testui.socket.MessageCallback;
import com.xl.testui.socket.MessagerManager;
import com.xl.testui.socket.P2pInfoListener;
import com.xl.testui.socket.P2pManager;

import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


/**
 * @author 27740
 */
public class P2pService extends Service implements P2pInfoListener, MessageCallback {

    public static final String TAG = P2pService.class.getSimpleName();

    public static final int P2P_SERVICE_START = 1;
    public static final int P2P_CLIENT_START = 2;
    public static final int P2P_DEVICES_UPDATE = 3;
    public static final int P2P_CREATE_GROUP_RESULT = 4;
    public static final int P2P_CLIENT_UPDATE = 5;
    public static final int RECEIVE_MSG = 6;

    private UIHandler mHandler;

    private INetInterfaceImpl mINetInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        mINetInterface = new INetInterfaceImpl(getApplicationContext());
        HandlerThread mHandlerThread = new HandlerThread("P2P_HANDLE_THREAD");
        mHandlerThread.start();
        mHandler = new UIHandler(mHandlerThread.getLooper());
        P2pManager.getInstance().init(getApplicationContext());
        P2pManager.getInstance().registerP2pInfoListener(this);
        MessagerManager.getInstance().registerMessageCallback(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mINetInterface;
    }

    private void startForeground(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getPackageName());
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = null;
        notificationChannel = new NotificationChannel(getPackageName(), getPackageName(), NotificationManager.IMPORTANCE_DEFAULT);
        nm.createNotificationChannel(notificationChannel);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        startForeground(android.os.Process.myPid(), builder.build());
    }

    @Override
    public void onDevicesUpdate(List<WifiP2pDevice> devices) {
        notifyMsg(P2P_DEVICES_UPDATE, devices);
    }

    @Override
    public void onServiceConnected() {
        notifyMsg(P2P_SERVICE_START, null);
    }

    @Override
    public void onClientConnect(String hostIp, String macAddress) {
        notifyMsg(P2P_CLIENT_START, hostIp);
    }

    @Override
    public void onCreateGroupResult(boolean isSuccess) {
        notifyMsg(P2P_CREATE_GROUP_RESULT, isSuccess);
    }

    @Override
    public void onClientUpdate(List<Pair<String, String>> macAndDeviceName) {
        IPMapping.update(macAndDeviceName);
        notifyMsg(P2P_CLIENT_UPDATE, null);
    }


    @Override
    public void receiveMsg(String content) {
        notifyMsg(RECEIVE_MSG, content);
    }

    private void notifyMsg(int msgWhat, Object param) {
        Message msg = Message.obtain();
        msg.what = msgWhat;
        if (param != null) {
            msg.obj = param;
        }
        mHandler.sendMessage(msg);
    }



    class UIHandler extends Handler {

        public UIHandler(Looper looper) {
            super(looper);
        }


        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "handleMessage() called with: msg = [" + msg.what + "]");
            switch (msg.what) {
                case P2P_SERVICE_START:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_SERVICE_START");
                    MessagerManager.getInstance().serviceStart();
                    break;
                case P2P_CLIENT_START:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_CLIENT_START");
                    MessagerManager.getInstance().clientStart((String) msg.obj);
                    break;
                case P2P_DEVICES_UPDATE:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_DEVICES_UPDATE");
                    List<WifiP2pDevice> devices = (List<WifiP2pDevice>) msg.obj;
//                    mDevicesAdapter.updateData(devices);
                    break;
                case P2P_CREATE_GROUP_RESULT:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_CREATE_GROUP_RESULT");
//                    showMsg("创建群组" + (((boolean) msg.obj) ? "成功" : "失败"));
                    break;
                case P2P_CLIENT_UPDATE:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_CLIENT_UPDATE");
//                    mClientDevicesAdapter.updateData(IPMapping.getDevicesName());
                    break;
                case RECEIVE_MSG:
                    Log.d(TAG, "handleMessage() called with: msg = RECEIVE_MSG");
                    String msgStr = (String) msg.obj;
                    Log.d(TAG, "handleMessage() called with: jsonStr = " + msgStr);
                    Gson gson = new Gson();
                    Type type = new TypeToken<MessageBean<String>>() {
                    }.getType();
                    try {
                        MessageBean<String> messageBean = gson.fromJson(msgStr, type);
                        Log.d(TAG, "handleMessage() called with: " + messageBean.getData().toString());
//                        showMsg("from:"+messageBean.getSenderName()+" -- to:"+messageBean.getReceiverName()+" data:"+messageBean.getData());
                        mINetInterface.callbackMessageReceive(messageBean.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
