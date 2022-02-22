package com.huawei.ivi.hmi.test_net.p2p;

import static com.huawei.ivi.hmi.netbuslib.DeviceConstant.P2P_GROUP_OWNER_NAME;

import android.annotation.SuppressLint;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.huawei.ivi.hmi.test_net.BuildConfig;
import com.huawei.ivi.hmi.test_net.util.DeviceConfigUtil;
import com.huawei.ivi.hmi.netbuslib.DeviceConstant;
import com.huawei.ivi.hmi.netbuslib.RetryTimer;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class P2pServiceManager extends BaseP2pManager{

    private final String TAG = BaseP2pManager.TAG+"_SERVICE";




    private boolean mCrateGroupSuccess;
    private boolean mDestExist;

    private P2pServiceManager() {
    }

    private static class SingletonInstance {
        private static final P2pServiceManager INSTANCE = new P2pServiceManager();
    }

    public static P2pServiceManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    @Override
    protected void init() {
//        if (Objects.equals(BuildConfig.FLAVOR,"hw_host")&& Process.myUid()/100000!=10){
//            Log.e(TAG, "onCreate: hw_host uid!=10");
//            return;
//        }
//        stopDiscover();
//        stopConnect();
//        cancelConnect();
//        removeGroup();
        setP2pDevicesName(P2P_GROUP_OWNER_NAME);
        createGroup();
//        startDiscover();
    }

    @Override
    void createGroupResult(boolean success) {
        mCrateGroupSuccess = success;
        if (success){
            stopRetryCreateGroup();
            startDiscover();
        }else {
            startRetryCreateGroup();
        }
    }

    @Override
    public void p2pDevices(ArrayList<WifiP2pDevice> devices) {
//        if (!mCrateGroupSuccess){
//            Log.d(TAG, "p2pDevices: mCrateGroup unSuccess return");
//            return;
//        }
        if (mDestExist){
            Log.d(TAG, "p2pDevices: mDestExist return");
            return;
        }
        if (mDestDevice!=null){
            Log.d(TAG, "p2pDevices: mDestDevice == null return");
            return;
        }
        for (WifiP2pDevice device:devices) {
            if (device.deviceName.equals(DeviceConstant.P2P_GROUP_MEMBER_NAME1)
                    ||device.deviceName.equals(DeviceConstant.P2P_GROUP_MEMBER_NAME2)
                    ||device.deviceName.equals(DeviceConstant.P2P_GROUP_MEMBER_NAME3)){
                Log.d(TAG, "find lantu  device = [" + device + "]");
                mDestDevice = device;
                break;
            }
        }
        if (mDestDevice!=null){
            mDestExist = true;
            connectDevice();
        }
    }

    @Override
    void connectResult(boolean success) {
        if (success){
            getConnectedInfo();
            getGroupInfo();
        }else {
            reconnect();
        }
    }

    @Override
    void p2pNetAvailable(boolean available) {
        Log.d(TAG, "p2pNetAvailable() called with: available = [" + available + "]");
        if (available){
//            stopDiscover();
            stopRetryCreateGroup();
            for (P2pInfoListener listener:mP2pInfoListenerList) {
                listener.onServiceConnected();
            }
        }else {
            for (P2pInfoListener listener:mP2pInfoListenerList) {
                listener.onServiceDisconnect();
            }
            startRetryCreateGroup();
//            removeGroup();
//            reconnect();
        }
    }

    @Override
    void deviceState(int state) {

    }

    @Override
    void deviceName(String deviceName) {
        if (!Objects.equals(deviceName,P2P_GROUP_OWNER_NAME)){
            setP2pDevicesName(P2P_GROUP_OWNER_NAME);
        }
    }

    private void reconnect(){
        mDestExist = false;
        mDestDevice = null;
        startDiscover();
    }





}