package com.huawei.ivi.hmi.test_net.ui;

import static com.huawei.ivi.hmi.test_net.P2pService.P2P_CLIENT_UPDATE;
import static com.huawei.ivi.hmi.test_net.P2pService.P2P_CONNECT_GROUP_RESULT;
import static com.huawei.ivi.hmi.test_net.P2pService.P2P_CREATE_GROUP_RESULT;
import static com.huawei.ivi.hmi.test_net.P2pService.P2P_DEVICES_UPDATE;
import static com.huawei.ivi.hmi.test_net.P2pService.P2P_GET_GROUP_OWNER_INFO;
import static com.huawei.ivi.hmi.test_net.P2pService.RECEIVE_MSG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.ivi.hmi.test_net.BuildConfig;
import com.huawei.ivi.hmi.test_net.R;
import com.huawei.ivi.hmi.netbuslib.bean.BaseDevice;
import com.huawei.ivi.hmi.netbuslib.bean.Device;
import com.huawei.ivi.hmi.test_net.bean.MessageBean;
import com.huawei.ivi.hmi.test_net.p2p.BaseP2pManager;
import com.huawei.ivi.hmi.test_net.p2p.P2pClientManager;
import com.huawei.ivi.hmi.test_net.p2p.P2pServiceManager;
import com.huawei.ivi.hmi.test_net.socket.MessageCallback;
import com.huawei.ivi.hmi.test_net.socket.MessagerManager;
import com.huawei.ivi.hmi.test_net.p2p.P2pInfoListener;
import com.huawei.ivi.hmi.test_net.ui.adapter.P2pDevicesAdapter;
import com.huawei.ivi.hmi.test_net.util.DeviceConfigUtil;
import com.huawei.ivi.hmi.netbuslib.DeviceConstant;
import com.huawei.ivi.hmi.netbuslib.MessageManager;
import com.huawei.ivi.hmi.netbuslib.NetConnectListener;
import com.huawei.ivi.hmi.netbuslib.bean.NetBusMessageBean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements P2pInfoListener, MessageCallback, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int REQ_CODE = 100;
    private static final String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.SYSTEM_ALERT_WINDOW};

    private RadioGroup mRG;
    private RadioButton mRbIvi;
    private RadioButton mRbPIvi;
    private RadioButton mRbRearLeft;
    private RadioButton mRbRearRight;

    private Button mBtnStartService, mBtnScan, mBtnRemove, mBtnCreateGroup, mBtnToIvi, mBtnToPIvi, mBtnToRearLeft, mBtnToRearRight;

    private UIHandle mHandler;

    private RecyclerView mDevicesRv, mClientDevicesRv;
    private P2pDevicesAdapter mDevicesAdapter, mClientDevicesAdapter;

    private BaseP2pManager mP2pManager;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate uid:" + Process.myUid() / 100000 + "  -  flavor:" + BuildConfig.FLAVOR);
//        if (Objects.equals(BuildConfig.FLAVOR,"hw_host")&&Process.myUid()/100000!=10){
//            Log.e(TAG, "onCreate: hw_host uid!=10");
//            finish();
//        }else {
//            init();
//        }
//        DeviceConfigUtil.init(getApplicationContext());
        init();
    }

    private void init() {
        getWindow().addFlags(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        requestPermission();
        mRG = findViewById(R.id.location);
        mRG = findViewById(R.id.location);
        mRbIvi = findViewById(R.id.location_ivi);
        mRbPIvi = findViewById(R.id.location_p_ivi);
        mRbRearLeft = findViewById(R.id.location_rear_left);
        mRbRearRight = findViewById(R.id.location_rear_right);
        mBtnScan = findViewById(R.id.scan_p2p);
        mBtnCreateGroup = findViewById(R.id.create_group);
        mBtnCreateGroup.setOnClickListener(this::onClick);
        mBtnStartService = findViewById(R.id.start_service);
        mBtnStartService.setOnClickListener(this::onClick);
        mBtnScan.setOnClickListener(this::onClick);
        mBtnRemove = findViewById(R.id.remove_group);
        mBtnRemove.setOnClickListener(this::onClick);
        mBtnToIvi = findViewById(R.id.msg_to_ivi);
        mBtnToIvi.setOnClickListener(this::onClick);
        mBtnToPIvi = findViewById(R.id.msg_to_p_ivi);
        mBtnToPIvi.setOnClickListener(this::onClick);
        mBtnToRearLeft = findViewById(R.id.msg_to_rear_left);
        mBtnToRearLeft.setOnClickListener(this::onClick);
        mBtnToRearRight = findViewById(R.id.msg_to_rear_right);
        mBtnToRearRight.setOnClickListener(this::onClick);

        mRG.setOnCheckedChangeListener((radioGroup, i) -> selectLocation(i));

        mDevicesRv = findViewById(R.id.rv_devices);
        mDevicesRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mDevicesAdapter = new P2pDevicesAdapter(getApplicationContext(), new ArrayList<>());
        mDevicesRv.setAdapter(mDevicesAdapter);
        mDevicesAdapter.setOnClickListener(new P2pDevicesAdapter.OnClickListener() {
            @Override
            public void onItemClick(WifiP2pDevice device) {
//                p2pConnectDevices(device);
//                P2pManager.getInstance().connectDevice(device);
//                P2pManager.getInstance().connectDevices(device);
            }
        });

        mClientDevicesRv = findViewById(R.id.rv_client_devices);
        mClientDevicesRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mClientDevicesAdapter = new P2pDevicesAdapter(getApplicationContext(), new ArrayList<>());
        mClientDevicesRv.setAdapter(mClientDevicesAdapter);
        mClientDevicesAdapter.setOnClickListener(new P2pDevicesAdapter.OnClickListener() {
            @Override
            public void onItemClick(WifiP2pDevice device) {

            }
        });

        mHandler = new UIHandle();
//        MessagerManager.getInstance().clientStart("10.10.84.56");
//        MessagerManager.getInstance().clientStart("192.168.1.102");
        MessagerManager.getInstance().serviceStart();
//        P2pManager.getInstance().startService(getApplicationContext());
//        P2pManager.getInstance().addP2pInfoListener(this);
//        if (DeviceConfigUtil.isHost()){
//            mP2pManager = P2pServiceManager.getInstance();
//        }else {
//            mP2pManager = P2pClientManager.getInstance();
//        }
//        mP2pManager.addP2pInfoListener(this);
//        mP2pManager.init(getApplicationContext());
//        MessageManager.getInstance().init(getApplicationContext(), new NetConnectListener() {
//            @Override
//            public void onServiceConnected() {
//                MessageManager.getInstance().registerINetCallback(new com.huawei.ivi.hmi.netbuslib.MessageCallback() {
//                    @Override
//                    public void onMessageReceived(String msg) {
//                        Log.d(TAG, "onMessageReceived() called with: msg = [" + msg + "]");
////                        showMsg(msg);
//                    }
//                });
//            }
//
//            @Override
//            public void onServiceDisconnected() {
//
//            }
//
//            @Override
//            public void onP2pConnectStateChange(boolean connected) {
//
//            }
//
//            @Override
//            public void onSocketConnectStateChange(boolean connected) {
//
//            }
//
//            @Override
//            public void onClientChange(List<BaseDevice> device) {
//
//            }
//
//        });
//        MessagerManager.getInstance().registerMessageCallback(this);

        initData();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
//        View view = getWindow().getDecorView();
//        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
//        lp.width = 500;
//        lp.height = 500;
//        view.setBackgroundColor(R.color.transparent);
//        getWindowManager().updateViewLayout(view,lp);
//
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, REQ_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                finish();
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void initData() {

//        int location = DeviceConfigUtil.getDeviceLocation();
        int location = 0;
        Log.d(TAG, "initData: location:" + location);
        if (location == DeviceConstant.LOCATION.UNKNOWN) {
            if (BuildConfig.devicesId != 0) {
                DeviceConfigUtil.setDeviceLocation(BuildConfig.devicesId);
                finish();
            }
        } else {
            finish();
        }
        switch (location) {
            case DeviceConstant.LOCATION.HW_HOST:
                mRbIvi.setChecked(true);
                break;
            case DeviceConstant.LOCATION.LANTU:
                mRbPIvi.setChecked(true);
                break;
            case DeviceConstant.LOCATION.REAR_LEFT:
                mRbRearLeft.setChecked(true);
                break;
            case DeviceConstant.LOCATION.REAR_RIGHT:
                mRbRearRight.setChecked(true);
                break;
        }

//        P2pManager.getInstance().startService(getApplicationContext());
//        if (DeviceConfigUtil.isHost()) {
//            P2pManager.getInstance().removeGroup();
//            P2pManager.getInstance().createGroup();
////            MessagerManager.getInstance().serviceStart();
//        } else if (DeviceConfigUtil.isPIvi()){
//            P2pManager.getInstance().removeGroup();
////            P2pManager.getInstance().requestPeers();
//            P2pManager.getInstance().discoverPeer();
//        }else {
//            P2pManager.getInstance().discoverPeer();
//        }
//        connectAP();
    }

    private void selectLocation(int id) {
        Log.d(TAG, "selectLocation() called with: id = [" + id + "]");
        int selectLocation = DeviceConstant.LOCATION.UNKNOWN;
        switch (id) {
            case R.id.location_ivi:
                selectLocation = DeviceConstant.LOCATION.HW_HOST;
                break;
            case R.id.location_p_ivi:
                selectLocation = DeviceConstant.LOCATION.LANTU;
                break;
            case R.id.location_rear_left:
                selectLocation = DeviceConstant.LOCATION.REAR_LEFT;
                break;
            case R.id.location_rear_right:
                selectLocation = DeviceConstant.LOCATION.REAR_RIGHT;
                break;
            default:
                break;
        }
        if (selectLocation == DeviceConfigUtil.getDeviceLocation()) {
            return;
        }
        Log.d(TAG, "selectLocation() called with: -- location change -> " + selectLocation);
        DeviceConfigUtil.setDeviceLocation(selectLocation);
    }


    private void sendMsg(@DeviceConstant.PARAM_DEVICE_NAME String deviceName, String content) {
        NetBusMessageBean<String> netBusMessageBean = new NetBusMessageBean<>();
        netBusMessageBean.setData(content);
        netBusMessageBean.setType("test");
        Gson gson = new Gson();
        String json = gson.toJson(netBusMessageBean);
        MessagerManager.getInstance().sendMsg(MessageBean.TYPE_DATA, deviceName, "com.huawei.ivi.hmi.netbus", json);
    }


    @Override
    public void onDevicesUpdate(List<WifiP2pDevice> devices) {
        notifyMsg(P2P_DEVICES_UPDATE, devices);
    }

    @Override
    public void onServiceConnected() {

    }

    /**
     * P2P群主断开连接，释放Socket服务端
     */
    @Override
    public void onServiceDisconnect() {

    }

    @Override
    public void onClientConnect(String hostIp, String macAddress) {
        Log.d(TAG, "onClientConnect: ");
    }

    /**
     * P2P Client断开连接，释放Socket服务端
     */
    @Override
    public void onClientDisconnect() {

    }

    @Override
    public void onCreateGroupResult(boolean isSuccess) {
        notifyMsg(P2P_CREATE_GROUP_RESULT, isSuccess);
    }

    @Override
    public void onConnectedToGroupOwner() {
        notifyMsg(P2P_CONNECT_GROUP_RESULT, null);
    }

    @Override
    public void onGetGroupOwnerInfo() {
        notifyMsg(P2P_GET_GROUP_OWNER_INFO, null);
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

    }

    @Override
    public void onClientChange(BaseDevice devices) {

    }

    /**
     * 客户端连接状态
     *
     * @param connect 是否连接
     */
    @Override
    public void onClientConnectState(boolean connect) {

    }

    private void notifyMsg(int msgWhat, Object param) {
        Message msg = Message.obtain();
        msg.what = msgWhat;
        if (param != null) {
            msg.obj = param;
        }
        mHandler.sendMessage(msg);
    }

    private void showMsg(String content) {
        Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_service:
//                MessagerManager.getInstance().clientClose();
//                rdDiscoverPeers();
                break;
            case R.id.create_group:
                initData();
                break;
            case R.id.scan_p2p:

                break;
            case R.id.remove_group:

                break;
            case R.id.msg_to_ivi:
                sendMsg(DeviceConstant.DEVICE_NAME.HW_HOST, "to host");
                break;
            case R.id.msg_to_p_ivi:
                sendMsg(DeviceConstant.DEVICE_NAME.LANTU, "to lantu");
                break;
            case R.id.msg_to_rear_left:
                sendMsg(DeviceConstant.DEVICE_NAME.REAR_LEFT, "to left");
                break;
            case R.id.msg_to_rear_right:
                sendMsg(DeviceConstant.DEVICE_NAME.REAR_RIGHT, "to right");
                break;
        }
    }

    class UIHandle extends Handler {

        public UIHandle() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "handleMessage() called with: msg = [" + msg.what + "]");
            switch (msg.what) {
                case P2P_DEVICES_UPDATE:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_DEVICES_UPDATE");
                    List<WifiP2pDevice> devices = (List<WifiP2pDevice>) msg.obj;
                    mDevicesAdapter.updateData(devices);
                    break;
                case P2P_CREATE_GROUP_RESULT:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_CREATE_GROUP_RESULT");
//                    showMsg("创建群组" + (((boolean) msg.obj) ? "成功" : "失败"));
                    break;
                case P2P_CONNECT_GROUP_RESULT:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_CONNECT_GROUP_RESULT");
//                    showMsg("加入群组成功");
                case P2P_GET_GROUP_OWNER_INFO:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_GET_GROUP_OWNER_INFO");
//                    showMsg("群组信息获取成功");
                case P2P_CLIENT_UPDATE:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_CLIENT_UPDATE");
                    List<WifiP2pDevice> clientDevices = (List<WifiP2pDevice>) msg.obj;
                    mClientDevicesAdapter.updateData(clientDevices);
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
//                        showMsg("from:" + messageBean.getSenderName() + " -- to:" + messageBean.getReceiverName() + " data:" + messageBean.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

        }
    }

    public static boolean checkFloatPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                Class cls = Class.forName("android.content.Context");
                Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                if (!(obj instanceof String)) {
                    return false;
                }
                String str2 = (String) obj;
                obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
                cls = Class.forName("android.app.AppOpsManager");
                Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                declaredField2.setAccessible(true);
                Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
                return result == declaredField2.getInt(cls);
            } catch (Exception e) {
                return false;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                if (appOpsMgr == null)
                    return false;
                int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                        .getPackageName());
                return Settings.canDrawOverlays(context) || mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
            } else {
                return Settings.canDrawOverlays(context);
            }
        }
    }

}