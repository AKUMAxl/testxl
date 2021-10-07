package com.huawei.ivi.hmi.netlib;

interface INetCallback {

     void onMessageReceive(in String srcDeviceName,in String srcAppPackageName,in String content);
}