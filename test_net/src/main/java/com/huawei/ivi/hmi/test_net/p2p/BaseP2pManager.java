package com.huawei.ivi.hmi.test_net.p2p;

import static android.net.wifi.p2p.WifiP2pConfig.GROUP_OWNER_INTENT_MAX;
import static android.net.wifi.p2p.WifiP2pConfig.GROUP_OWNER_INTENT_MIN;

import static com.huawei.ivi.hmi.netbuslib.DeviceConstant.P2P_GROUP_OWNER_NAME;
import static com.huawei.ivi.hmi.netbuslib.DeviceConstant.P2P_GROUP_OWNER_NAME_1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;

import com.huawei.ivi.hmi.test_net.BuildConfig;
import com.huawei.ivi.hmi.test_net.ui.MainActivity;
import com.huawei.ivi.hmi.test_net.util.DeviceConfigUtil;
import com.huawei.ivi.hmi.netbuslib.RetryTimer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class BaseP2pManager {

    static final String TAG = BaseP2pManager.class.getSimpleName();

    public Context mContext;
    public WifiP2pManager mWifiP2pManager;
    public WifiManager mWifiManager;
    public WifiP2pManager.Channel mChannel;

    public CopyOnWriteArrayList<P2pInfoListener> mP2pInfoListenerList = new CopyOnWriteArrayList<>();

    public boolean mNeedDiscover;
    public WifiP2pDevice mDestDevice;

    private RetryTimer mRetryCreateGroupTimer;
    private RetryTimer mRetryDiscoverTimer;

    private boolean mInitOver;

    private boolean mDiscovering;

    @SuppressLint("ServiceCast")
    public void init(Context context) {
//        if (Objects.equals(BuildConfig.FLAVOR,"hw_host")&& Process.myUid()/100000!=10){
//            Log.e(TAG, "onCreate: hw_host uid!=10");
//            return;
//        }
        if (mContext != null) {
            Log.d(TAG, "P2p already");
            return;
        }
        this.mContext = context;
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mWifiP2pManager = (WifiP2pManager) mContext.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mWifiP2pManager.initialize(mContext, Looper.getMainLooper(), () -> {
            Log.d(TAG, "init() called with: context = [" + context + "]");
        });
        Log.d(TAG, "init() called with: mChannel = [" + mChannel + "]");
        if (mChannel == null) {
            mWifiP2pManager = null;
        }
        mInitOver = true;
        init();

    }

    public void addP2pInfoListener(P2pInfoListener p2pInfoListener) {
        mP2pInfoListenerList.add(p2pInfoListener);
    }

    public void removeP2pInfoListener(P2pInfoListener p2pInfoListener) {
        if (mP2pInfoListenerList.contains(p2pInfoListener)) {
            mP2pInfoListenerList.remove(p2pInfoListener);
        }
    }

    public void setDiscoverState(boolean isDiscovering) {
        mDiscovering = isDiscovering;
        if (!isDiscovering) {
            discoverPeers();
        }
    }

    public void peersChanges() {
        if (!mInitOver) {
            return;
        }
        if (mDestDevice == null) {
            requestPeerInfo();
        }
    }

    public void connectStateChange(WifiP2pInfo wifiP2pInfo) {
//        if (wifiP2pInfo.groupFormed){
//            p2pNetAvailable(true);
//        }else {
//            p2pNetAvailable(false);
//        }
    }

    public void groupStateChange() {
        if (!mInitOver) {
            return;
        }
        getGroupInfo();
    }

    public void deviceStateChange(WifiP2pDevice wifiP2pDevice) {
        if (!mInitOver) {
            return;
        }
        for (P2pInfoListener listener : mP2pInfoListenerList) {
            listener.onDeviceInfoChange(wifiP2pDevice);
        }
        deviceState(wifiP2pDevice.status);
        deviceName(wifiP2pDevice.deviceName);
    }

    public void startDiscover() {
        mNeedDiscover = true;
        discoverPeers();
    }

    public void stopDiscover() {
        mNeedDiscover = false;
        stopDiscoverPeers();
    }

    @SuppressLint("MissingPermission")
    void createGroup() {
        Log.d(TAG, "createGroup() called");
//        WifiP2pConfig config = new WifiP2pConfig();
        mWifiP2pManager.removeGroup(mChannel, null);
        mWifiP2pManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "createGroup onSuccess() called");
                createGroupResult(true);
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "createGroup onFailure() called with: i = [" + i + "]");
                parseActionListenerOnFailure(i);
                createGroupResult(false);
            }
        });
    }


    @SuppressLint("MissingPermission")
    void connectDevice() {
        Log.d(TAG, "connectDevice() called:" + mDestDevice.deviceName);
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = mDestDevice.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        config.groupOwnerIntent = DeviceConfigUtil.isHost() ? GROUP_OWNER_INTENT_MAX : GROUP_OWNER_INTENT_MIN;
        mWifiP2pManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "connectDevice onSuccess() called");
                connectResult(true);
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "connectDevice onFailure() called");
                parseActionListenerOnFailure(i);
                connectResult(false);
            }
        });
    }

    void removeGroup() {
        Log.d(TAG, "removeGroup() called");
        mWifiP2pManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "removeGroup onSuccess() called");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "removeGroup onFailure() called");
                parseActionListenerOnFailure(i);
            }
        });
    }

    void cancelConnect() {
        Log.d(TAG, "cancelConnect() called");
        mWifiP2pManager.cancelConnect(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "cancelConnect onSuccess() called");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "cancelConnect onFailure() called");
                parseActionListenerOnFailure(i);
            }
        });
    }


    void getConnectedInfo() {
        Log.d(TAG, "getConnectedInfo() called");
        mWifiP2pManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                Log.d(TAG, "onConnectionInfoAvailable() called with: wifiP2pInfo = [" + wifiP2pInfo + "]");
//                if (wifiP2pInfo.groupFormed){
//                    p2pNetAvailable(true);
//                }else {
//                    p2pNetAvailable(false);
//                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    void getGroupInfo() {
        Log.d(TAG, "getGroupInfo() called");
        mWifiP2pManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup) {
                Log.d(TAG, "onGroupInfoAvailable() called with: wifiP2pGroup = [" + wifiP2pGroup + "]");
                List<WifiP2pDevice> devices;
                if (wifiP2pGroup == null) {
//                    p2pNetAvailable(false);
//                    startMainActivity();
                    devices = new ArrayList<>();
                    for (P2pInfoListener listener : mP2pInfoListenerList) {
                        listener.onClientUpdate(devices);
                    }
                } else {
                    String networkName = wifiP2pGroup.getNetworkName();
                    String groupOwnerName = wifiP2pGroup.getOwner().deviceName;
                    Log.d(TAG, "networkName: " + networkName);
                    Log.d(TAG, "groupOwnerName: " + groupOwnerName);

//                     ||networkName.contains(P2P_GROUP_OWNER_NAME_1)||Objects.equals(groupOwnerName,P2P_GROUP_OWNER_NAME_1)
                    if (networkName.contains(P2P_GROUP_OWNER_NAME) || Objects.equals(groupOwnerName, P2P_GROUP_OWNER_NAME)
                            || networkName.contains(P2P_GROUP_OWNER_NAME_1) || Objects.equals(groupOwnerName, P2P_GROUP_OWNER_NAME_1)
                    ) {
                        p2pNetAvailable(true);
                        Log.d(TAG, "getClientList: " + wifiP2pGroup.getClientList().toString());
                        devices = new ArrayList<>(wifiP2pGroup.getClientList());
                        for (P2pInfoListener listener : mP2pInfoListenerList) {
                            listener.onClientUpdate(devices);
                        }
                    } else {
                        removeGroup();
                        p2pNetAvailable(false);
                        devices = new ArrayList<>();
                        for (P2pInfoListener listener : mP2pInfoListenerList) {
                            listener.onClientUpdate(devices);
                        }
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void discoverPeers() {
        Log.d(TAG, "discoverPeers() called");
        if (mWifiP2pManager == null) {
            return;
        }
        if (mDiscovering) {
            Log.d(TAG, "discovering");
            return;
        }
        mWifiP2pManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "discoverPeers onSuccess() called");
                stopRetryDiscover();
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "discoverPeers onFailure() called");
                startRetryDiscover();
                parseActionListenerOnFailure(i);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void stopDiscoverPeers() {
        Log.d(TAG, "stopDiscoverPeers() called");
        stopRetryDiscover();
        mWifiP2pManager.stopPeerDiscovery(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "stopPeerDiscovery onSuccess() called");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "stopPeerDiscovery onFailure() called");
                parseActionListenerOnFailure(i);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void requestPeerInfo() {
        mWifiP2pManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                if (wifiP2pDeviceList==null){
                    Log.e(TAG, "onPeersAvailable: wifiP2pDeviceList==null");
                    return;
                }
                if (wifiP2pDeviceList.getDeviceList()==null){
                    Log.e(TAG, "onPeersAvailable: wifiP2pDeviceList.getDeviceList==null");
                    return;
                }

                ArrayList<WifiP2pDevice> devices = new ArrayList<>();
                for (WifiP2pDevice device : wifiP2pDeviceList.getDeviceList()) {
                    Log.d(TAG, "onPeersAvailable: "+device.deviceName);
                    devices.add(device);
                }
                p2pDevices(devices);
                for (P2pInfoListener listener : mP2pInfoListenerList) {
                    listener.onDevicesUpdate(devices);
                }
            }
        });
    }

    void setP2pDevicesName(String p2pDeviceName) {
        Log.d(TAG, "setP2pDevicesName() called with: p2pDeviceName = [" + p2pDeviceName + "]");
        try {
            Class<?> clz = mWifiP2pManager.getClass();
            Method method = clz.getMethod("setDeviceName", WifiP2pManager.Channel.class, String.class, WifiP2pManager.ActionListener.class);
            method.invoke(mWifiP2pManager, mChannel, p2pDeviceName, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "setP2pDeviceName success");
                }

                @Override
                public void onFailure(int i) {
                    Log.d(TAG, "setP2pDeviceName failure:" + i);
                    parseActionListenerOnFailure(i);
                }
            });
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    void startRetryCreateGroup() {
        if (mRetryCreateGroupTimer == null) {
            mRetryCreateGroupTimer = new RetryTimer();
            mRetryCreateGroupTimer.startTimer(new Runnable() {
                @Override
                public void run() {
                    createGroup();
                }
            },10,10);
        }
    }

    void stopRetryCreateGroup() {
        if (mRetryCreateGroupTimer != null) {
            mRetryCreateGroupTimer.stopTimer();
            mRetryCreateGroupTimer = null;
        }
    }

    void startRetryDiscover() {
        if (mRetryDiscoverTimer == null) {
            mRetryDiscoverTimer = new RetryTimer();
            mRetryDiscoverTimer.startTimer(new Runnable() {
                @Override
                public void run() {
                    discoverPeers();
                }
            },15,15);
        }
    }

    void stopRetryDiscover() {
        if (mRetryDiscoverTimer != null) {
            mRetryDiscoverTimer.stopTimer();
            mRetryDiscoverTimer = null;
        }
    }

    abstract void init();

    abstract void createGroupResult(boolean success);

    abstract void p2pDevices(ArrayList<WifiP2pDevice> devices);

    abstract void connectResult(boolean success);

    abstract void p2pNetAvailable(boolean available);

    abstract void deviceState(int state);

    abstract void deviceName(String deviceName);

    @SuppressLint("MissingPermission")
    void parseActionListenerOnFailure(int reason) {
        switch (reason) {
            case WifiP2pManager.ERROR:
                Log.e(TAG, "failure error");
                reStartWifi();
                break;
            case WifiP2pManager.P2P_UNSUPPORTED:
                Log.e(TAG, "failure p2p unsupported");
                break;
            case WifiP2pManager.BUSY:
                Log.e(TAG, "failure busy");
                reStartWifi();
                break;
            default:
                Log.e(TAG, "failure unknown");
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mWifiP2pManager.requestDeviceInfo(mChannel, new WifiP2pManager.DeviceInfoListener() {
                @Override
                public void onDeviceInfoAvailable(@Nullable WifiP2pDevice wifiP2pDevice) {
                    if (wifiP2pDevice!=null){
                        Log.d(TAG, "本机 deviceName:" + wifiP2pDevice.deviceName);
                        Log.d(TAG, "本机 status:" + stateConvert(wifiP2pDevice.status));
                    }else {
                        Log.e(TAG, "onDeviceInfoAvailable wifiP2pDevice is null");
                        reStartWifi();
                    }

                }
            });
        }
    }

    private String stateConvert(int code) {
        String ret = "4 - UNAVAILABLE - 不可用";
        switch (code) {
            case WifiP2pDevice.CONNECTED:
                ret = "0 - " + "CONNECTED" + " - 已连接";
                break;
            case WifiP2pDevice.INVITED:
                ret = "1 - " + "INVITED" + " - 已邀请";
                break;
            case WifiP2pDevice.FAILED:
                ret = "2 - " + "FAILED" + " - 邀请失败";
                break;
            case WifiP2pDevice.AVAILABLE:
                ret = "3 - " + "AVAILABLE" + " -  可用";
                break;
            case WifiP2pDevice.UNAVAILABLE:
                ret = "4 - " + "UNAVAILABLE" + " - 不可用";
                break;
            default:
                break;
        }
        return ret;
    }

    private void startMainActivity(){
        Log.d(TAG, "startMainActivity() called");
        Intent intent = new Intent();
        intent.setClass(mContext.getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void reStartWifi(){

//        int curState = mWifiManager.getWifiState();
//        Log.d(TAG, "reStartWifi() called curState:"+curState);
//        if (curState==WifiManager.WIFI_STATE_ENABLED){
//            mWifiManager.setWifiEnabled(false);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mWifiManager.setWifiEnabled(true);
//                }
//            },5*1000);
//        }

    }

}
