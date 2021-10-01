package com.xl.testui;

import android.app.Application;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;


import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.xl.testui.socket.P2pManager;
import com.xl.testui.socket.P2pReceiver;
import com.xl.testui.util.DeviceConfigUtil;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StringBuffer param = new StringBuffer();
        param.append("appid="+getString(R.string.app_id));
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(App.this, param.toString());

        P2pReceiver p2pReceiver = new P2pReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        registerReceiver(p2pReceiver, intentFilter);

        BootReceiver bootReceiver = new BootReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BOOT_COMPLETED");
//        filter.addAction("android.intent.action.FIRST_BOOT_COMPLETED");
        filter.addAction("com.android.internal.intent.action.REQUEST_SHUTDOWN");
        filter.addAction("com.test.test");
        getApplicationContext().registerReceiver(bootReceiver,filter);

        DeviceConfigUtil.initMacConfig(getApplicationContext());
        Log.d("xLLL","application onCreate");
    }
}
