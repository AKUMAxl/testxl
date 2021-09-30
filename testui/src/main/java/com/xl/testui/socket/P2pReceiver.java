package com.xl.testui.socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED;

public class P2pReceiver extends BroadcastReceiver {

    public static final String TAG = P2pReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive() called with: context = [" + context + "], action = [" + action + "]");

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // p2p状态
            Log.d(TAG, "P2P 状态改变：");
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
                Log.d(TAG, "P2P 状态改变 ——> 可用");
            } else {
                // Wi-Fi P2P is not enabled
                Log.d(TAG, "P2P 状态改变 ——> 不可用");
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // p2p设备变化监听
            Log.d(TAG, "P2P 设备列表改变 --> 获取P2P设备");
            P2pManager.getInstance().requestPeers();
        }else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            // p2p设备连接
            Log.d(TAG, "P2P 连接设备状态改变 --> 获取连接设备信息，开启Socket");
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            WifiP2pInfo wifiP2pInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
            Log.d(TAG, "P2P 连接设备状态改变 --> "+networkInfo.toString());
            Log.d(TAG, "P2P 连接设备状态改变 --> "+wifiP2pInfo.toString());
            if (networkInfo.isConnected()){
                P2pManager.getInstance().requestConnectedDeviceInfo();
                P2pManager.getInstance().requestGroupInfo();
            }
        }else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
            Log.d(TAG, "P2P 本机状态改变 -->");
            WifiP2pDevice wifiP2pDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            Log.d(TAG, "wifiP2pDevice deviceName:"+wifiP2pDevice.deviceName);
            Log.d(TAG, "wifiP2pDevice deviceAddress:"+wifiP2pDevice.deviceAddress);
            Log.d(TAG, "wifiP2pDevice primaryDeviceType:"+wifiP2pDevice.primaryDeviceType);
            Log.d(TAG, "wifiP2pDevice secondaryDeviceType:"+wifiP2pDevice.secondaryDeviceType);
            Log.d(TAG, "wifiP2pDevice status:"+stateConvert(wifiP2pDevice.status));
            /*
              public static final int CONNECTED   = 0;
              public static final int INVITED     = 1;
              public static final int FAILED      = 2;
              public static final int AVAILABLE   = 3;
              public static final int UNAVAILABLE = 4;
              HotSpotManager.getInstance().requestDeviceInfo();
             */
        }else if (WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION.equals(action)){
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE,0);
            if (state==WIFI_P2P_DISCOVERY_STOPPED){
                Log.d(TAG, "P2P 扫描状态 --> STOPPED");
            }
            if (state==WIFI_P2P_DISCOVERY_STARTED){
                Log.d(TAG, "P2P 扫描状态 --> STARTED");
            }
        }
    }

    private String stateConvert(int code){
        String ret = "4 - UNAVAILABLE - 不可用";
        switch (code){
            case WifiP2pDevice.CONNECTED:
                ret = "0 - "+"CONNECTED"+" - 已连接";
                break;
            case WifiP2pDevice.INVITED:
                ret = "1 - "+"INVITED"+" - 已邀请";
                break;
            case WifiP2pDevice.FAILED:
                ret = "2 - "+"FAILED"+" - 邀请失败";
                break;
            case WifiP2pDevice.AVAILABLE:
                ret = "3 - "+"AVAILABLE"+" -  可用";
                break;
            case WifiP2pDevice.UNAVAILABLE:
                ret = "4 - "+"UNAVAILABLE"+" - 不可用";
                break;
            default:
                break;
        }
        return ret;
    }
}
