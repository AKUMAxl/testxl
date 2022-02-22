package com.huawei.ivi.hmi.netlib;

interface INetCallback {

     void onP2pConnectStateChange(in boolean connected);

     void onMessageReceive(in String content);

}