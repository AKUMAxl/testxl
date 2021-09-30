package com.xl.testui;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xl.testui.bean.Device;
import com.xl.testui.bean.MessageBean;
import com.xl.testui.socket.IPMapping;
import com.xl.testui.socket.MessageCallback;
import com.xl.testui.socket.P2pClientDevicesAdapter;
import com.xl.testui.socket.P2pDevicesAdapter;
import com.xl.testui.socket.P2pInfoListener;
import com.xl.testui.socket.P2pManager;
import com.xl.testui.socket.SocketTestManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NetActivity extends Activity implements P2pInfoListener, MessageCallback {

    public static final String TAG = SocketTestManager.class.getSimpleName();

    public static final int P2P_SERVICE_START = 1;
    public static final int P2P_CLIENT_START = 2;
    public static final int P2P_DEVICES_UPDATE = 3;
    public static final int P2P_CREATE_GROUP_RESULT = 4;
    public static final int P2P_CLIENT_UPDATE = 5;
    public static final int RECEIVE_MSG = 6;

    Button btnStartDnssd,
            btnCloseDnssd,
            btnDiscoverDnssd,
            btnStopDisconver,
            btnCreateGroup,
            btnExitGroup,
            btnScanDevice,
            btnStopScanDevice,
            btnQueryClient;

    private RecyclerView mDevicesRv, mClientDevicesRv;
    private P2pDevicesAdapter mDevicesAdapter;
    private P2pClientDevicesAdapter mClientDevicesAdapter;

    private UIHandler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        mHandler = new UIHandler();
        P2pManager.getInstance().init(getApplicationContext());
        P2pManager.getInstance().registerP2pInfoListener(this);
        btnStartDnssd = findViewById(R.id.btn_start_dnssd);
        btnCloseDnssd = findViewById(R.id.btn_close_dnssd);
        btnDiscoverDnssd = findViewById(R.id.btn_discover_dnssd);
        btnStopDisconver = findViewById(R.id.btn_stop_discover);
        btnCreateGroup = findViewById(R.id.btn_create_group);
        btnExitGroup = findViewById(R.id.btn_exit_group);
        btnScanDevice = findViewById(R.id.btn_scan_device);
        btnStopScanDevice = findViewById(R.id.btn_stop_scan_device);
        btnQueryClient = findViewById(R.id.btn_query_client);


        mDevicesRv = findViewById(R.id.rv_devices);
        mDevicesRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mDevicesAdapter = new P2pDevicesAdapter(getApplicationContext(), new ArrayList<>());
        mDevicesRv.setAdapter(mDevicesAdapter);
        mDevicesAdapter.setOnClickListener(new P2pDevicesAdapter.OnClickListener() {
            @Override
            public void onItemClick(WifiP2pDevice device) {
//                p2pConnectDevices(device);
                P2pManager.getInstance().connectDevice(device);
            }
        });

        mClientDevicesRv = findViewById(R.id.rv_client_devices);
        mClientDevicesRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mClientDevicesAdapter = new P2pClientDevicesAdapter(getApplicationContext(), new ArrayList<>());
        mClientDevicesRv.setAdapter(mClientDevicesAdapter);
        mClientDevicesAdapter.setOnClickListener(new P2pClientDevicesAdapter.OnClickListener() {
            @Override
            public void onItemClick(String deviceName) {
                showMsg(deviceName + " -- " + IPMapping.getMacAddress(deviceName));
//                serviceSendMsg("service send msg to " + deviceName, IPMapping.getMacAddress(deviceName));
            }
        });

        btnStartDnssd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P2pManager.getInstance().startP2pService();
            }
        });
        btnCloseDnssd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P2pManager.getInstance().closeDnssdUpnpService();
            }
        });
        btnDiscoverDnssd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P2pManager.getInstance().discoverService();
            }
        });
        btnStopDisconver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P2pManager.getInstance().stopDiscoverService();
            }
        });
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P2pManager.getInstance().createGroup();
            }
        });
        btnExitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P2pManager.getInstance().exitGroup();
            }
        });
        btnScanDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P2pManager.getInstance().discoverPeer();
            }
        });
        btnStopScanDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P2pManager.getInstance().stopRequestPeer();
            }
        });
        btnQueryClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P2pManager.getInstance().requestGroupInfo();
            }
        });
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

    private void showMsg(String content) {
        Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }

    class UIHandler extends Handler {

        public UIHandler() {
            super(Looper.getMainLooper());
        }


        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "handleMessage() called with: msg = [" + msg.what + "]");
            switch (msg.what) {
                case P2P_SERVICE_START:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_SERVICE_START");

                    break;
                case P2P_CLIENT_START:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_CLIENT_START");

                    break;
                case P2P_DEVICES_UPDATE:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_DEVICES_UPDATE");
                    List<WifiP2pDevice> devices = (List<WifiP2pDevice>) msg.obj;
                    mDevicesAdapter.updateData(devices);
                    break;
                case P2P_CREATE_GROUP_RESULT:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_CREATE_GROUP_RESULT");
                    showMsg("创建群组" + (((boolean) msg.obj) ? "成功" : "失败"));
                    break;
                case P2P_CLIENT_UPDATE:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_CLIENT_UPDATE");
                    mClientDevicesAdapter.updateData(IPMapping.getDevicesName());
                    break;
                case RECEIVE_MSG:
                    Log.d(TAG, "handleMessage() called with: msg = RECEIVE_MSG");
                    String msgStr = (String) msg.obj;
                    Log.d(TAG, "handleMessage() called with: jsonStr = "+msgStr);
                    Gson gson = new Gson();
                    Type type = new TypeToken<MessageBean<Device>>() {}.getType();
                    try {
                        MessageBean messageBean = gson.fromJson(msgStr,type);
                        Log.d(TAG, "handleMessage() called with: "+messageBean.getData().toString());
                        if (messageBean.getType()==1){
                            Log.d(TAG, "handleMessage() called with: getLength = [" + messageBean.getLength() + "]");
                            Device device = (Device) messageBean.getData();
                            Log.d(TAG, "handleMessage() called with: getLength = [" + device.getName() + "]");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
