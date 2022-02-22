package com.huawei.ivi.hmi.test_net;

import android.app.Application;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Process;
import android.util.Log;


import com.huawei.ivi.hmi.test_net.p2p.P2pReceiver;
import com.huawei.ivi.hmi.test_net.util.DeviceConfigUtil;
import com.tencent.mmkv.BuildConfig;

import java.util.Objects;


public class App extends Application {

    private final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        DeviceConfigUtil.init(getApplicationContext());
        Log.d(TAG, "onCreate uid:"+Process.myUid()/100000+"  -  flavor:"+BuildConfig.FLAVOR);
//        if (Objects.equals(BuildConfig.FLAVOR,"hw_host")&&Process.myUid()/100000!=10){
//            Log.e(TAG, "onCreate: hw_host uid!=10");
//            Process.killProcess(Process.myPid());
//        }else {
//            initReceive();
//        }
//        String rootDir = MMKV.initialize(this);
//        Log.d(TAG,"mmkv root: " + rootDir);

    }

    private void initReceive(){
        P2pReceiver p2pReceiver = new P2pReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        intentFilter.addAction("android.net.wifi.p2p.PERSISTENT_GROUPS_CHANGED");
        registerReceiver(p2pReceiver, intentFilter);

        BootReceiver bootReceiver = new BootReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BOOT_COMPLETED");
        filter.addAction("android.intent.action.FIRST_BOOT_COMPLETED");
        filter.addAction("com.android.internal.intent.action.REQUEST_SHUTDOWN");
        filter.addAction("com.test.test");
        filter.addAction("com.netbus.keep");
        getApplicationContext().registerReceiver(bootReceiver,filter);
    }
}
