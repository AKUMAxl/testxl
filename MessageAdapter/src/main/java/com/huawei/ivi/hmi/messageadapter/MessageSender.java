package com.huawei.ivi.hmi.messageadapter;

import com.huawei.ivi.hmi.netbuslib.DeviceConstant;

public interface MessageSender {

    void sendMsg(@DeviceConstant.PARAM_DEVICE_NAME String destName, String destAppPackageName, String content);

}
