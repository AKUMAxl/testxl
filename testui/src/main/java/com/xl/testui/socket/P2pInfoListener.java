package com.xl.testui.socket;


import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Pair;

import java.util.List;

public interface P2pInfoListener {

    /**
     * 扫描到的P2P设备
     *
     * @param devices 设备列表
     */
    void onDevicesUpdate(List<WifiP2pDevice> devices);

    /**
     * P2P群主已连接，创建Socket服务端
     */
    void onServiceConnected();

    /**
     * P2PClient已连接，创建Socket客户端通知
     *
     * @param hostIp 绑定的服务端ip
     */
    void onClientConnect(String hostIp,String macAddress);

    /**
     * 创建群组结果回调
     *
     * @param isSuccess 是否成功
     */
    void onCreateGroupResult(boolean isSuccess);

    /**
     * 客户端改变通知
     *
     * @param macAndDeviceName 客户端mac地址和设备名列表
     */
    void onClientUpdate(List<Pair<String,String>> macAndDeviceName);

}
