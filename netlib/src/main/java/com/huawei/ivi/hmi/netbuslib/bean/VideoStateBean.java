package com.huawei.ivi.hmi.netbuslib.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class VideoStateBean implements Cloneable, Parcelable {

    private int action;
    private String videoName;

    public VideoStateBean(){

    }

    protected VideoStateBean(Parcel in) {
        action = in.readInt();
        videoName = in.readString();
    }

    public static final Creator<VideoStateBean> CREATOR = new Creator<VideoStateBean>() {
        @Override
        public VideoStateBean createFromParcel(Parcel in) {
            return new VideoStateBean(in);
        }

        @Override
        public VideoStateBean[] newArray(int size) {
            return new VideoStateBean[size];
        }
    };

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    @Override
    public String toString() {
        return "VideoStateBean{" +
                "action=" + action +
                ", videoName='" + videoName + '\'' +
                '}';
    }

    @NonNull
    @Override
    public VideoStateBean clone() throws CloneNotSupportedException {
        VideoStateBean naviStateBean;
        naviStateBean = (VideoStateBean) super.clone();
        return naviStateBean;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(action);
        dest.writeString(videoName);
    }
}
