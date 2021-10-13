package com.xl.testui.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageBean<T> implements Parcelable{

    public static final int TYPE_DEVICE_INFO = 1;
    public static final int TYPE_DATA = 2;

    private int type;
    private String senderName;
    private String receiverName;
    private T data;

    public MessageBean(){

    }

    protected MessageBean(Parcel in) {
        type = in.readInt();
        senderName = in.readString();
        receiverName = in.readString();
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "type=" + type +
                ", senderName=" + senderName +
                ", receiverName=" + receiverName +
                ", data=" + data +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(senderName);
        dest.writeString(receiverName);
    }
}
