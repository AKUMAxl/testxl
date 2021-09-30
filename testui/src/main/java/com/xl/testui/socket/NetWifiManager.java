package com.xl.testui.socket;

import android.content.Context;
import android.net.wifi.WifiManager;

public class NetWifiManager {

    private Context mContext;
    private WifiManager mWifiManager;


    private NetWifiManager() {
    }

    private static class SingletonInstance {
        private static final NetWifiManager INSTANCE = new NetWifiManager();
    }

    public static NetWifiManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void init(Context context){
        this.mContext = context;
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
    }

}