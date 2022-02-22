package com.huawei.ivi.hmi.test_net.p2p;

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
     * P2P群主断开连接，释放Socket服务端
     */
    void onServiceDisconnect();

    /**
     * P2PClient已连接，创建Socket客户端通知
     *
     * @param hostIp 绑定的服务端ip
     */
    void onClientConnect(String hostIp,String macAddress);

    /**
     * P2P Client断开连接，释放Socket服务端
     */
    void onClientDisconnect();

    /**
     * 创建群组结果回调
     *
     * @param isSuccess 是否成功
     */
    void onCreateGroupResult(boolean isSuccess);

    /**
     * 已连接群组通知
     *
     */
    void onConnectedToGroupOwner();

    /**
     * 获取到群主信息
     */
    void onGetGroupOwnerInfo();

    /**
     * 客户端改变通知
     *
     * @param devices 设备列表
     */
    void onClientUpdate(List<WifiP2pDevice> devices);

    /**
     * 本机状态改变
     *
     * @param wifiP2pDevice
     */
    void onDeviceInfoChange(WifiP2pDevice wifiP2pDevice);
}
