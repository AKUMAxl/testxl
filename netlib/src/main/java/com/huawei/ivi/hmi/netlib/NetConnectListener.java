package com.huawei.ivi.hmi.netlib;

public interface NetConnectListener {

    void onNetConnected();

    void onNetDisconnected();

    void onNetAvailableChange(boolean available);

}
