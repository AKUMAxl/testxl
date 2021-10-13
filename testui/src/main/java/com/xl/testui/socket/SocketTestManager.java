package com.xl.testui.socket;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xl.testui.R;
import com.xl.testui.bean.Device;
import com.xl.testui.bean.MessageBean;
import com.xl.testui.util.DeviceConfigUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SocketTestManager implements P2pInfoListener, MessageCallback {

    public static final String TAG = SocketTestManager.class.getSimpleName();

    public static final int P2P_SERVICE_START = 1;
    public static final int P2P_CLIENT_START = 2;
    public static final int P2P_DEVICES_UPDATE = 3;
    public static final int P2P_CREATE_GROUP_RESULT = 4;
    public static final int P2P_CLIENT_UPDATE = 5;
    public static final int RECEIVE_MSG = 6;

    private Context mContext;
    private WindowManager mWindowManager;
    private View mRootView;

    private ServiceMessager mServiceMessager;
    private ClientMessager mClientMessager;

    private RecyclerView mDevicesRv, mClientDevicesRv;
    private P2pDevicesAdapter mDevicesAdapter;
    private P2pClientDevicesAdapter mClientDevicesAdapter;

    private UIHandler mHandler;

    private SocketTestManager() {
    }


    private static class SingletonInstance {
        private static final SocketTestManager INSTANCE = new SocketTestManager();
    }

    public static SocketTestManager getInstance() {
        return SingletonInstance.INSTANCE;
    }


    public void init(Context context, WindowManager windowManager) {
        this.mContext = context;
        this.mWindowManager = windowManager;
        mHandler = new UIHandler();
        P2pManager.getInstance().init(context);
        P2pManager.getInstance().registerP2pInfoListener(this);
        initView();
    }

    private void initView() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2038,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.END;
        params.y = 260;
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.test_socket_window, null);
        mWindowManager.addView(mRootView, params);

        mDevicesRv = mRootView.findViewById(R.id.rv_devices);
        mDevicesRv.setLayoutManager(new LinearLayoutManager(mContext));
        mDevicesAdapter = new P2pDevicesAdapter(mContext, new ArrayList<>());
        mDevicesRv.setAdapter(mDevicesAdapter);
        mDevicesAdapter.setOnClickListener(new P2pDevicesAdapter.OnClickListener() {
            @Override
            public void onItemClick(WifiP2pDevice device) {
                p2pConnectDevices(device);
            }
        });

        mClientDevicesRv = mRootView.findViewById(R.id.rv_client_devices);
        mClientDevicesRv.setLayoutManager(new LinearLayoutManager(mContext));
        mClientDevicesAdapter = new P2pClientDevicesAdapter(mContext, new ArrayList<>());
        mClientDevicesRv.setAdapter(mClientDevicesAdapter);
        mClientDevicesAdapter.setOnClickListener(new P2pClientDevicesAdapter.OnClickListener() {
            @Override
            public void onItemClick(String deviceName) {
                showMsg(deviceName + " -- " + IPMapping.getMacAddress(deviceName));
                serviceSendMsg("service send msg to " + deviceName, IPMapping.getMacAddress(deviceName));
            }
        });

        mRootView.findViewById(R.id.test_socket_back).setOnClickListener(v -> {
            P2pManager.getInstance().stopService();
            P2pManager.getInstance().unregisterP2pInfoListener();
            serviceClose();
            clientClose();
            mWindowManager.removeView(mRootView);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.xl.testui", "com.xl.testui.MainActivity"));
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
        mRootView.findViewById(R.id.test_socket_start_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceStart();
            }
        });
        mRootView.findViewById(R.id.test_socket_start_client).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientStart("");
            }
        });
        mRootView.findViewById(R.id.test_socket_close_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceClose();
            }
        });
        mRootView.findViewById(R.id.test_socket_close_client).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientClose();
            }
        });
        mRootView.findViewById(R.id.test_socket_service_send_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceSendMsg("this msg from service", "");
            }
        });
        mRootView.findViewById(R.id.test_socket_client_send_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientSendMsg();
            }
        });
        mRootView.findViewById(R.id.test_p2p_service_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p2pServiceStart();
            }
        });
        mRootView.findViewById(R.id.test_p2p_create_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p2pCreateGroup();
            }
        });
        mRootView.findViewById(R.id.test_p2p_group_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p2pGroupInfo();
            }
        });
        mRootView.findViewById(R.id.test_p2p_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p2pScanDevices();
            }
        });
        mRootView.findViewById(R.id.test_p2p_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                p2pConnectDevices();
//                MacConfigUtil.getP2pIp();
                DeviceConfigUtil.getIMEI();
            }
        });
        mRootView.findViewById(R.id.test_p2p_cancel_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p2pCancelConnect();
            }
        });
        mRootView.findViewById(R.id.test_p2p_service_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p2pServiceStop();
            }
        });
        mRootView.findViewById(R.id.test_p2p_search_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p2pSearchService();
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

    /**
     * Socket是TCP客户端API，通常用于connect到远程主机。
     * ServerSocket是TCP服务器API，通常来自客户端套接字的accept连接。
     * DatagramSocket是UDP端点API，用于send和receive datagram packets 。
     * MulticastSocket是处理多播组时使用的DatagramSocket的子类。
     */
    private void serviceStart() {
        if (mServiceMessager == null) {
            mServiceMessager = new ServiceMessager();
            mServiceMessager.registerMessageCallback(this);
            mServiceMessager.start();
        }
    }

    ;

    private void clientStart(String hostIp) {
        if (mClientMessager == null) {
            mClientMessager = new ClientMessager(hostIp);
            mClientMessager.registerMessageCallback(this);
            mClientMessager.start();
        }
    }

    ;

    private void serviceSendMsg(String content, String destMacAddress) {
        if (mServiceMessager != null) {
            mServiceMessager.sendMsg(content, destMacAddress);
        }
    }

    private void clientSendMsg() {
        if (mClientMessager != null) {
//            mClientMessager.sendMsg("this is client");
            Gson gson = new Gson();
            Device device = new Device();
            device.setName("test");
            device.setP2pmac("1234");
            MessageBean<Device> messageBean = new MessageBean<>();
            messageBean.setData(device);
            messageBean.setType(1);
//            mClientMessager.sendMsg(gson.toJson(messageBean));
        }
    }


    private void serviceClose() {
        if (mServiceMessager != null) {
            mServiceMessager.closeService();
            mServiceMessager.cleanClient();
            mServiceMessager.unregisterMessageCallback();
            mServiceMessager = null;
        }
    }

    private void clientClose() {
        if (mClientMessager != null) {
            mClientMessager.closeClient();
            mClientMessager.unregisterMessageCallback();
            mClientMessager = null;
        }
    }

    private void p2pServiceStart() {
        P2pManager.getInstance().startP2pService();
    }

    private void p2pCreateGroup() {
        // 创建群组之前清理之前的P2P服务，避免返回onFailure WifiP2pManager.BUSY
        P2pManager.getInstance().stopService();
        P2pManager.getInstance().createGroup();
    }

    private void p2pGroupInfo() {
        P2pManager.getInstance().requestGroupInfo();
    }

    private void p2pScanDevices() {
        P2pManager.getInstance().discoverPeer();
    }

    private void p2pConnectDevices(WifiP2pDevice wifiP2pDevice) {
        P2pManager.getInstance().connectDevice(wifiP2pDevice);
    }

    private void p2pCancelConnect() {
        P2pManager.getInstance().cancelConnect();
        clientClose();
    }

    private void p2pServiceStop() {
        P2pManager.getInstance().stopService();
        serviceClose();
    }

    private void p2pSearchService() {
        P2pManager.getInstance().discoverService();
    }

    private void showMsg(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
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
                    serviceStart();
                    break;
                case P2P_CLIENT_START:
                    Log.d(TAG, "handleMessage() called with: msg = P2P_CLIENT_START");
                    clientStart((String) msg.obj);
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