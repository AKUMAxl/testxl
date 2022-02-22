package com.huawei.ivi.hmi.test_net.p2p;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Process;
import android.util.Log;

import com.huawei.ivi.hmi.test_net.BuildConfig;
import com.huawei.ivi.hmi.test_net.util.DeviceConfigUtil;
import com.huawei.ivi.hmi.netbuslib.DeviceConstant;

import java.util.ArrayList;
import java.util.Objects;

public class P2pClientManager extends BaseP2pManager {

    private final String TAG = BaseP2pManager.TAG + "_CLIENT";

    private P2pClientManager() {
    }

    private static class SingletonInstance {
        private static final P2pClientManager INSTANCE = new P2pClientManager();
    }

    public static P2pClientManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    @Override
    void init() {
//        if (Objects.equals(BuildConfig.FLAVOR,"hw_host")&& Process.myUid()/100000!=10){
//            Log.e(TAG, "onCreate: hw_host uid!=10");
//            return;
//        }
//        removeGroup();
//        cancelConnect();
        startDiscover();
        getGroupInfo();
        if (DeviceConfigUtil.isPIvi()) {
            setP2pDevicesName("HW_HOST");
        }
        if (DeviceConfigUtil.isRearLeft()) {
            setP2pDevicesName("HW_LEFT_PAD");
        }
        if (DeviceConfigUtil.isRearRight()) {
            setP2pDevicesName("HW_RIGHT_PAD");
        }
    }

    @Override
    void createGroupResult(boolean success) {

    }

    @Override
    public void p2pDevices(ArrayList<WifiP2pDevice> devices) {
        if (mDestDevice != null) {
            return;
        }
        for (WifiP2pDevice device : devices) {
            if (device.deviceName.equals(DeviceConstant.P2P_GROUP_OWNER_NAME)
                    ||device.deviceName.equals(DeviceConstant.P2P_GROUP_OWNER_NAME_1)
            ) {
//                ||device.deviceName.equals(DeviceConstant.P2P_GROUP_OWNER_NAME_1)
                mDestDevice = device;
                break;
            }
        }
        if (mDestDevice != null) {
            connectDevice();
        }
    }

    @Override
    void connectResult(boolean success) {
        if (success) {
            getConnectedInfo();
            getGroupInfo();
        } else {
            reconnect();
        }
    }

    @Override
    void p2pNetAvailable(boolean available) {
        Log.d(TAG, "p2pNetAvailable() called with: available = [" + available + "]");

        if (available) {
            for (P2pInfoListener listener : mP2pInfoListenerList) {
                listener.onClientConnect("192.168.49.1", null);
            }
            stopDiscover();
        } else {
            for (P2pInfoListener listener : mP2pInfoListenerList) {
                listener.onClientDisconnect();
            }
//            removeGroup();
            reconnect();
        }
    }

    @Override
    void deviceState(int state) {
        if (state != 0) {
            reconnect();
        } else {
            stopDiscover();
        }
    }

    @Override
    void deviceName(String deviceName) {

    }

    private void reconnect() {
        mDestDevice = null;
        startDiscover();
    }
}