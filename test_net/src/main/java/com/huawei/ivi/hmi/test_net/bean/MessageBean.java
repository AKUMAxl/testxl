package com.huawei.ivi.hmi.test_net.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageBean<T> implements Parcelable{

    /**
     * client信息通知给service
     */
    public static final int TYPE_DEVICE_INFO = 1;
    /**
     * service中所有client信息通知各个client
     */
    public static final int TYPE_DEVICES_CHANGE = 2;
    /**
     * normal msg
     */
    public static final int TYPE_DATA = 3;

    private int type;
    private String senderName;
    private String receiverName;
    private String destPackageName;
    private T data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getDestPackageName() {
        return destPackageName;
    }

    public void setDestPackageName(String destPackageName) {
        this.destPackageName = destPackageName;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public MessageBean(){}

    protected MessageBean(Parcel in) {
        type = in.readInt();
        senderName = in.readString();
        receiverName = in.readString();
        destPackageName = in.readString();
    }

    public static final Creator<MessageBean> CREATOR = new Creator<MessageBean>() {
        @Override
        public MessageBean createFromParcel(Parcel in) {
            return new MessageBean(in);
        }

        @Override
        public MessageBean[] newArray(int size) {
            return new MessageBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(type);
        parcel.writeString(senderName);
        parcel.writeString(receiverName);
        parcel.writeString(destPackageName);
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "type=" + type +
                ", senderName='" + senderName + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", destPackageName='" + destPackageName + '\'' +
                ", data=" + data +
                '}';
    }
}
