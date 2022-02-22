package com.huawei.ivi.hmi.netbuslib.bean;

import java.net.Socket;

public class Device extends BaseDevice {


    private Socket socket = null;

    public Device(){}


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    @Override
    public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                ", p2pip='" + p2pip + '\'' +
                ", socket=" + socket +
                '}';
    }
}
