package com.huawei.ivi.hmi.netbuslib;

import com.huawei.ivi.hmi.netbuslib.BaseDevice;


interface INetCallback {

     void onP2pConnectStateChange(in boolean connected);

     void onSocketConnectStateChange(in boolean connected);

     void onSocketClientChange(in List<BaseDevice> device);

     void onMessageReceive(in String content);

     
}