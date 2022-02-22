package com.huawei.ivi.hmi.netbuslib.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseDevice implements Parcelable {

    String name = "";
    String p2pip = null;


    public BaseDevice(){}

    protected BaseDevice(Parcel in) {
        name = in.readString();
        p2pip = in.readString();
    }

    public static final Creator<BaseDevice> CREATOR = new Creator<BaseDevice>() {
        @Override
        public BaseDevice createFromParcel(Parcel in) {
            return new BaseDevice(in);
        }

        @Override
        public BaseDevice[] newArray(int size) {
            return new BaseDevice[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getP2pip() {
        return p2pip;
    }

    public void setP2pip(String p2pip) {
        this.p2pip = p2pip;
    }

    @Override
    public String toString() {
        return "BaseDevice{" +
                "name='" + name + '\'' +
                ", p2pip='" + p2pip + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(p2pip);
    }
}
