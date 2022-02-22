// INetInterface.aidl
package com.huawei.ivi.hmi.netbuslib;

import com.huawei.ivi.hmi.netbuslib.INetCallback;

interface INetInterface {

    void registerNetCallback(in INetCallback netCallback,in String packageName);

    void unregisterNetCallback(in INetCallback netCallback);

    void sendMessage(in String destDeviceName,in String destAppPackageName,in String content);

}