package com.huawei.ivi.hmi.netbuslib.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class NetBusMessageBean<T> implements Parcelable {

    private String type;
    private T data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NetBusMessageBean{" +
                "type='" + type + '\'' +
                ", data=" + data +
                '}';
    }

    public NetBusMessageBean(){

    }

    protected NetBusMessageBean(Parcel in) {
        type = in.readString();
    }

    public static final Creator<NetBusMessageBean> CREATOR = new Creator<NetBusMessageBean>() {
        @Override
        public NetBusMessageBean createFromParcel(Parcel in) {
            return new NetBusMessageBean(in);
        }

        @Override
        public NetBusMessageBean[] newArray(int size) {
            return new NetBusMessageBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
    }
}
