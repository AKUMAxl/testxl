package com.huawei.ivi.hmi.netbuslib;

import com.huawei.ivi.hmi.netbuslib.bean.BaseDevice;
import com.huawei.ivi.hmi.netbuslib.bean.Device;

import java.util.List;
import java.util.Map;

public interface NetConnectListener {

    void onServiceConnected();

    void onServiceDisconnected();

    void onP2pConnectStateChange(boolean connected);

    void onSocketConnectStateChange(boolean connected);

    void onClientChange(List<BaseDevice> device);

}
