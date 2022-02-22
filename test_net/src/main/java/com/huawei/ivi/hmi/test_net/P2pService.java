package com.huawei.ivi.hmi.test_net;

import static com.huawei.ivi.hmi.test_net.bean.MessageBean.TYPE_DATA;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.ivi.hmi.messageadapter.MessageHandler;
import com.huawei.ivi.hmi.messageadapter.MessageSender;
import com.huawei.ivi.hmi.messageadapter.VideoActionCallback;
import com.huawei.ivi.hmi.test_net.p2p.P2pClientManager;
import com.huawei.ivi.hmi.test_net.p2p.P2pServiceManager;
import com.huawei.ivi.hmi.netbuslib.bean.BaseDevice;
import com.huawei.ivi.hmi.netbuslib.bean.Device;
import com.huawei.ivi.hmi.test_net.bean.MessageBean;
import com.huawei.ivi.hmi.test_net.p2p.BaseP2pManager;
import com.huawei.ivi.hmi.test_net.socket.INetInterfaceImpl;
import com.huawei.ivi.hmi.test_net.socket.MessageCallback;
import com.huawei.ivi.hmi.test_net.socket.MessagerManager;
import com.huawei.ivi.hmi.test_net.p2p.P2pInfoListener;
import com.huawei.ivi.hmi.test_net.ui.MainActivity;
import com.huawei.ivi.hmi.test_net.util.DeviceConfigUtil;
import com.huawei.ivi.hmi.netbuslib.DeviceConstant;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import static com.huawei.ivi.hmi.test_net.bean.MessageBean.TYPE_DATA;

import com.huawei.ivi.hmi.test_net.p2p.P2pServiceManager;
import com.huawei.ivi.hmi.test_net.ui.MainActivity;
import com.huawei.ivi.hmi.test_net.ui.player.PlayerActivity;
import com.huawei.ivi.hmi.test_net.bean.MessageBean;
import com.huawei.ivi.hmi.test_net.p2p.BaseP2pManager;
import com.huawei.ivi.hmi.test_net.socket.INetInterfaceImpl;
import com.huawei.ivi.hmi.test_net.socket.MessageCallback;
import com.huawei.ivi.hmi.test_net.socket.MessagerManager;
import com.huawei.ivi.hmi.test_net.util.DeviceConfigUtil;



/**
 * @author 27740
 */
public class P2pService extends Service implements P2pInfoListener, MessageCallback, MessageSender, VideoActionCallback {

    public static final String TAG = P2pService.class.getSimpleName();

    public static final int P2P_SERVICE_CONNECT = 1;
    public static final int P2P_SERVICE_DISCONNECT = 2;
    public static final int P2P_CLIENT_CONNECT = 3;
    public static final int P2P_CLIENT_DISCONNECT = 4;
    public static final int P2P_DEVICES_UPDATE = 5;
    public static final int P2P_CREATE_GROUP_RESULT = 6;
    public static final int P2P_CONNECT_GROUP_RESULT = 7;
    public static final int P2P_GET_GROUP_OWNER_INFO = 8;
    public static final int P2P_CLIENT_UPDATE = 9;
    public static final int RECEIVE_MSG = 10;
    public static final int CALLBACK_CLIENT_CHANGE = 11;
    public static final int ON_CLIENT_CHANGE = 12;
    public static final int SOCKET_CLIENT_CONNECTED = 20;
    public static final int SOCKET_CLIENT_DISCONNECTED = 21;


    private UIHandler mHandler;

    private INetInterfaceImpl mINetInterface;

    private BaseP2pManager mP2pManager;

    private Gson mGson;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate uid:"+Process.myUid()/100000+"  -  flavor:"+ BuildConfig.FLAVOR);
//        if (Objects.equals(BuildConfig.FLAVOR,"hw_host")&&Process.myUid()/100000!=10){
//            Log.e(TAG, "onCreate: hw_host uid!=10");
//        }else {
//            init();
//        }
        init();

    }

    private void init(){
        mINetInterface = new INetInterfaceImpl(getApplicationContext());
        HandlerThread mHandlerThread = new HandlerThread("P2P_HANDLE_THREAD");
        mHandlerThread.start();
        mHandler = new UIHandler(mHandlerThread.getLooper());
        mGson = new Gson();
//        ApServiceManager.getInstance().init(getApplicationContext());
        if (DeviceConfigUtil.isHost()){
            mP2pManager = P2pServiceManager.getInstance();
        }else {
            mP2pManager = P2pClientManager.getInstance();
        }
//        mP2pManager.addP2pInfoListener(this);
//        mP2pManager.init(getApplicationContext());
        MessagerManager.getInstance().serviceStart();
        MessagerManager.getInstance().registerMessageCallback(this);
        MessageHandler.getInstance().init(getApplicationContext());
        MessageHandler.getInstance().registerVideoActionCallback(this);
        MessageHandler.getInstance().setMessageSender(this::sendMsg);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        return START_STICKY;
    }

    @NonNull
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() called with: intent = [" + intent + "]");
        if (mINetInterface==null){
            Log.d(TAG, "onBind() called with: intent = [null]");
            mINetInterface = new INetInterfaceImpl(getApplicationContext());
        }
        return mINetInterface;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (DeviceConfigUtil.isHost()){
            MessagerManager.getInstance().serviceClose();
        }else {
            MessagerManager.getInstance().p2pDisconnect();
        }
    }

    private void startMainActivity(){
        Log.d(TAG, "startMainActivity() called");
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void startForeground(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getPackageName());
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = null;
            notificationChannel = new NotificationChannel(getPackageName(), getPackageName(), NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(notificationChannel);
            builder.setSmallIcon(R.drawable.ic_launcher_background);
            startForeground(Process.myPid(), builder.build());
        }
    }

    @Override
    public void onDevicesUpdate(List<WifiP2pDevice> devices) {
        notifyMsg(P2P_DEVICES_UPDATE, devices);
    }

    @Override
    public void onServiceConnected() {
        notifyMsg(P2P_SERVICE_CONNECT, null);
    }

    @Override
    public void onServiceDisconnect() {
        notifyMsg(P2P_SERVICE_DISCONNECT, null);
    }

    @Override
    public void onClientConnect(String hostIp, String macAddress) {
        Log.d(TAG, "onClientConnect: ");
        notifyMsg(P2P_CLIENT_CONNECT, hostIp);
    }

    /**
     * P2P Client断开连接，释放Socket服务端
     */
    @Override
    public void onClientDisconnect() {
        notifyMsg(P2P_CLIENT_DISCONNECT, null);
    }

    @Override
    public void onCreateGroupResult(boolean isSuccess) {
        notifyMsg(P2P_CREATE_GROUP_RESULT, isSuccess);
    }

    @Override
    public void onConnectedToGroupOwner() {
        notifyMsg(P2P_CONNECT_GROUP_RESULT,null);
    }

    @Override
    public void onGetGroupOwnerInfo() {
        notifyMsg(P2P_GET_GROUP_OWNER_INFO,null);
    }

    @Override
    public void onClientUpdate(List<WifiP2pDevice> devices) {
        notifyMsg(P2P_CLIENT_UPDATE, devices);
    }

    /**
     * 本机状态改变
     *
     * @param wifiP2pDevice
     */
    @Override
    public void onDeviceInfoChange(WifiP2pDevice wifiP2pDevice) {

    }


    @Override
    public void onMsgReceive(String content) {
        notifyMsg(RECEIVE_MSG, content);
    }

    @Override
    public void onServiceClientChange(List<Device> clients) {
        notifyMsg(CALLBACK_CLIENT_CHANGE, clients);
    }

    @Override
    public void onClientChange(BaseDevice devices) {
        notifyMsg(ON_CLIENT_CHANGE,devices);
    }

    /**
     * 客户端连接状态
     *
     * @param connect 是否连接
     */
    @Override
    public void onClientConnectState(boolean connect) {
        notifyMsg(connect?SOCKET_CLIENT_CONNECTED:SOCKET_CLIENT_DISCONNECTED,null);
    }

    private void notifyMsg(int msgWhat, Object param) {
        Message msg = Message.obtain();
        msg.what = msgWhat;
        if (param != null) {
            msg.obj = param;
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void sendMsg(String destName, String destAppPackageName, String content) {
        MessagerManager.getInstance().sendMsg(TYPE_DATA,destName,destAppPackageName,content);
    }

    @Override
    public void playNap() {
        Log.d(TAG, "playNap() called");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"nap.mp4";
        Log.d(TAG, "playNap() called path:"+path);
//        startMainActivity();
        PlayerActivity.newInstance(getApplicationContext(),path);
    }

    @Override
    public void playMuse() {
        Log.d(TAG, "playMuse() called");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"muse.mp4";
        Log.d(TAG, "playMuse() called path:"+path);
//        startMainActivity();
        PlayerActivity.newInstance(getApplicationContext(),path);

    }

    @Override
    public void playXF() {
        Log.d(TAG, "playXF() called");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"xf.mp4";
        Log.d(TAG, "playXF() called path:"+path);
//        startMainActivity();
        PlayerActivity.newInstance(getApplicationContext(),path);
    }

    @Override
    public void stop() {
//        startMainActivity();
        Log.d(TAG, "stop() called");
        stopVideoActivity(false);
    }

    private void stopVideoActivity(boolean showToast){
        if (PlayerActivity.instance==null){
            Log.d(TAG, "PlayerActivity.instance==null");
            return;
        }
        if ((DeviceConfigUtil.isRearRight()||DeviceConfigUtil.isRearLeft())&&showToast){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"网络连接中断，重新连接中...",Toast.LENGTH_SHORT).show();
                }
            });
        }
        PlayerActivity.instance.finish();
    }


    class UIHandler extends Handler {

        public UIHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "handleMessage() called with: msg = [" + msg.what + "]");
            switch (msg.what) {
                case P2P_SERVICE_CONNECT:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_SERVICE_CONNECT");
                    MessagerManager.getInstance().serviceStart();
                    mINetInterface.callbackP2pConnectState(true);
                    break;
                case P2P_SERVICE_DISCONNECT:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_SERVICE_DISCONNECT");
                    mINetInterface.callbackP2pConnectState(false);
                    break;
                case P2P_CLIENT_CONNECT:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_CLIENT_CONNECT");
                    MessagerManager.getInstance().clientStart((String) msg.obj);
                    mINetInterface.callbackP2pConnectState(true);
                    break;
                case P2P_CLIENT_DISCONNECT:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_CLIENT_DISCONNECT");
                    MessagerManager.getInstance().p2pDisconnect();
                    mINetInterface.callbackP2pConnectState(false);
//                    startMainActivity();
                    break;
                case P2P_DEVICES_UPDATE:
//                    Log.d(TAG, "handleMessage() called with: msg = P2P_DEVICES_UPDATE");
//                    List<WifiP2pDevice> devices = (List<WifiP2pDevice>) msg.obj;
//                    mDevicesAdapter.updateData(devices);
                    break;
                case P2P_CREATE_GROUP_RESULT:
//                    Log.d(TAG, "handleMessage() called with: msg = P2P_CREATE_GROUP_RESULT");
//                    showMsg("创建群组" + (((boolean) msg.obj) ? "成功" : "失败"));
                    break;
                case P2P_CONNECT_GROUP_RESULT:
//                    Log.d(TAG, "handleMessage() called with: msg = P2P_CONNECT_GROUP_RESULT");
//                    showMsg("加入群组成功"));
                    break;
                case P2P_CLIENT_UPDATE:
//                    Log.d(TAG, "handleMessage() called with: msg = P2P_CLIENT_UPDATE");
//                    mClientDevicesAdapter.updateData(IPMapping.getDevicesName());
                    break;
                case RECEIVE_MSG:
                    Log.d(TAG, "handleMessage() called with: msg = RECEIVE_MSG");
                    String msgStr = (String) msg.obj;
                    Log.d(TAG, "handleMessage() called with: jsonStr = " + msgStr);
                    Type type = new TypeToken<MessageBean<String>>() {
                    }.getType();
                    try {
                        MessageBean<String> messageBean = mGson.fromJson(msgStr, type);
                        if (Objects.equals(messageBean.getDestPackageName(),BuildConfig.APPLICATION_ID)){
                            MessageHandler.getInstance().handleMessage(messageBean.getData());
                            return;
                        }
                        mINetInterface.callbackMessageReceive(messageBean.getDestPackageName(),messageBean.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CALLBACK_CLIENT_CHANGE:
                    Log.d(TAG, "handleMessage() called with: msg = CALLBACK_CLIENT_CHANGE");
                    List<Device> deviceList = (List<Device>) msg.obj;
                    String[] clientsName = {DeviceConstant.DEVICE_NAME.HW_HOST,DeviceConstant.DEVICE_NAME.REAR_LEFT,DeviceConstant.DEVICE_NAME.REAR_RIGHT};
                    for (String clientName:clientsName) {
                        Type type1 = new TypeToken<BaseDevice>() {
                        }.getType();
                        for (Device device:deviceList){
                            BaseDevice baseDevice = new BaseDevice();
                            baseDevice.setName(device.getName());
                            baseDevice.setP2pip(device.getP2pip());
                            Log.d(TAG, "CALLBACK_CLIENT_CHANGE baseDevice = [" + baseDevice.toString() + "]");
                            String jsonStr = mGson.toJson(baseDevice, type1);
                            MessagerManager.getInstance().sendMsg(MessageBean.TYPE_DEVICES_CHANGE,clientName,BuildConfig.APPLICATION_ID,jsonStr);
                        }
                    }
                    break;
                case ON_CLIENT_CHANGE:
                    Log.d(TAG, "handleMessage() called with: msg = ON_CLIENT_CHANGE");
                    BaseDevice device = (BaseDevice) msg.obj;
                    try {
                        mINetInterface.callbackClientChange(device);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SOCKET_CLIENT_CONNECTED:
                    Log.d(TAG, "handleMessage() called with: msg = SOCKET_CLIENT_CONNECTED");
                    mINetInterface.callbackSocketConnectState(true);
                    startForeground();
                    mP2pManager.stopDiscover();
                    break;
                case SOCKET_CLIENT_DISCONNECTED:
                    Log.d(TAG, "handleMessage() called with: msg = SOCKET_CLIENT_DISCONNECTED");
                    mINetInterface.callbackSocketConnectState(false);
                    stopVideoActivity(true);
                    mP2pManager.startDiscover();
                    break;
                default:
                    break;
            }
        }
    }
}
