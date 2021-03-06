package com.xl.testqgspeech.bean.voiceBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/***
 * 帮助数据类
 */
public class HelpDataNewBean implements Parcelable {

    private String title;
    private String tabID;
    private String videoName;
    private ArrayList<String> contents;

    private HelpDataNewBean(Parcel in) {
        title = in.readString();
        tabID = in.readString();
        videoName = in.readString();
        contents = in.createStringArrayList();
    }

    public static final Creator<HelpDataNewBean> CREATOR = new Creator<HelpDataNewBean>() {
        @Override
        public HelpDataNewBean createFromParcel(Parcel in) {
            return new HelpDataNewBean(in);
        }

        @Override
        public HelpDataNewBean[] newArray(int size) {
            return new HelpDataNewBean[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTabID() {
        return tabID;
    }

    public void setTabID(String tabID) {
        this.tabID = tabID;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public ArrayList<String> getContents() {
        return contents;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(tabID);
        dest.writeString(videoName);
        dest.writeStringList(contents);
    }

    @Override
    public String toString() {
        return "HelpDataNewBean{" +
                "title='" + title + '\'' +
                ", tabID='" + tabID + '\'' +
                ", videoName='" + videoName + '\'' +
                ", contents=" + contents +
                '}';
    }
}
