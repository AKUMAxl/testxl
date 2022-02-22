package com.huawei.ivi.hmi.test_net.socket;

import com.huawei.ivi.hmi.netbuslib.bean.BaseDevice;
import com.huawei.ivi.hmi.netbuslib.bean.Device;

import java.util.List;

public interface MessageCallback {

    void onMsgReceive(String content);

    /**
     * 服务端通知客户端，全部客户端信息
     *
     * @param clients client info
     */
    void onServiceClientChange(List<Device> clients);

    /**
     * 客户端收到所有客户端信息
     *
     * @param devices client info
     */
    void onClientChange(BaseDevice devices);

    /**
     * 客户端连接状态
     *
     * @param connect 是否连接
     */
    void onClientConnectState(boolean connect);
}
