// INetInterface.aidl
package com.huawei.ivi.hmi.netlib;

import com.huawei.ivi.hmi.netlib.INetCallback;

interface INetInterface {

    void init();

    void registerNetCallback(in INetCallback netCallback);

    void unregisterNetCallback(in INetCallback netCallback);

    void sendMessage(in String destDeviceName,in String destAppPackageName,in String content);

}