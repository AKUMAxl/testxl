package com.xl.testui.socket;

import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;

/**
 * P2p状态改变监听
 *
 * @author xl
 * @date 2021.10.1
 */
public interface P2pStateListener {

    /**
     * P2p是否可用
     *
     * @param enable 是否可用
     */
    void p2pEnableStateChange(boolean enable);

    /**
     * P2p Client 发生改变
     */
    void onPeerChange();

    /**
     * P2p 链接状态改变
     *
     * @param networkInfo 当前网络信息
     * @param wifiP2pInfo 链接的设备信息
     */
    void onConnectDeviceChange(NetworkInfo networkInfo, WifiP2pInfo wifiP2pInfo);

    /**
     * P2p 本机状态改变
     *
     * @param wifiP2pDevice 本地P2p信息
     */
    void onDeviceStateChange(WifiP2pDevice wifiP2pDevice);

    /**
     * 扫描P2p设备状态
     *
     * @param scanning 是否正在扫描中
     */
    void onScanPeer(boolean scanning);


}
