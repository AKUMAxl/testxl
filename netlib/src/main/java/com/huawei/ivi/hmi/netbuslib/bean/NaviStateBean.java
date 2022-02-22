package com.huawei.ivi.hmi.netbuslib.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class NaviStateBean implements Cloneable, Parcelable {

    /**
     * 全程剩余距离
     */
    private int destDistance;
    /**
     * 全程剩余时间
     */
    private int destTime;
    /**
     * 是否在导航中
     */
    private boolean inNavi;
    /**
     * 目的地
     */
    private String destName;
    /**
     * 目的地所在城市
     */
    private String destCityName;
    /**
     * 目的地纬度
     */
    private double destLat;
    /**
     * 目的地经度
     */
    private double destLon;
    /**
     * 下一路径引导信息
     */
    private int guideType;
    /**
     * 预计到达时间
     */
    private String arriveTime;
    /**
     * 下一个路口距离
     */
    private String nextCrossingDistance;
    /**
     * 下一个道路名称
     */
    private String nextRoadName;
    /**
     * 当前所在车道索引
     */
    private int curLaneIndex;

    public NaviStateBean(){

    }

    protected NaviStateBean(Parcel in) {
        destDistance = in.readInt();
        destTime = in.readInt();
        inNavi = in.readByte() != 0;
        destName = in.readString();
        destCityName = in.readString();
        destLat = in.readDouble();
        destLon = in.readDouble();
        guideType = in.readInt();
        arriveTime = in.readString();
        nextCrossingDistance = in.readString();
        nextRoadName = in.readString();
        curLaneIndex = in.readInt();
    }

    public static final Creator<NaviStateBean> CREATOR = new Creator<NaviStateBean>() {
        @Override
        public NaviStateBean createFromParcel(Parcel in) {
            return new NaviStateBean(in);
        }

        @Override
        public NaviStateBean[] newArray(int size) {
            return new NaviStateBean[size];
        }
    };

    public int getDestDistance() {
        return destDistance;
    }

    public void setDestDistance(int destDistance) {
        this.destDistance = destDistance;
    }

    public int getDestTime() {
        return destTime;
    }

    public void setDestTime(int destTime) {
        this.destTime = destTime;
    }

    public boolean isInNavi() {
        return inNavi;
    }

    public void setInNavi(boolean inNavi) {
        this.inNavi = inNavi;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public double getDestLat() {
        return destLat;
    }

    public void setDestLat(double destLat) {
        this.destLat = destLat;
    }

    public double getDestLon() {
        return destLon;
    }

    public void setDestLon(double destLon) {
        this.destLon = destLon;
    }

    public String getDestCityName() {
        return destCityName;
    }

    public void setDestCityName(String destCityName) {
        this.destCityName = destCityName;
    }

    public int getGuideType() {
        return guideType;
    }

    public void setGuideType(int guideType) {
        this.guideType = guideType;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getNextCrossingDistance() {
        return nextCrossingDistance;
    }

    public void setNextCrossingDistance(String nextCrossingDistance) {
        this.nextCrossingDistance = nextCrossingDistance;
    }

    public String getNextRoadName() {
        return nextRoadName;
    }

    public void setNextRoadName(String nextRoadName) {
        this.nextRoadName = nextRoadName;
    }

    public int getCurLaneIndex() {
        return curLaneIndex;
    }

    public void setCurLaneIndex(int curLaneIndex) {
        this.curLaneIndex = curLaneIndex;
    }

    @NonNull
    @Override
    public NaviStateBean clone() throws CloneNotSupportedException {
        NaviStateBean naviStateBean;
        naviStateBean = (NaviStateBean) super.clone();
        return naviStateBean;
    }

    @Override
    public String toString() {
        return "NaviStateBean{" +
                "destDistance=" + destDistance +
                ", destTime=" + destTime +
                ", inNavi=" + inNavi +
                ", destName='" + destName + '\'' +
                ", destCityName='" + destCityName + '\'' +
                ", destLat=" + destLat +
                ", destLon=" + destLon +
                ", guideType=" + guideType +
                ", arriveTime='" + arriveTime + '\'' +
                ", nextCrossingDistance='" + nextCrossingDistance + '\'' +
                ", nextRoadName='" + nextRoadName + '\'' +
                ", curLaneIndex=" + curLaneIndex +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(destDistance);
        parcel.writeInt(destTime);
        parcel.writeByte((byte) (inNavi ? 1 : 0));
        parcel.writeString(destName);
        parcel.writeString(destCityName);
        parcel.writeDouble(destLat);
        parcel.writeDouble(destLon);
        parcel.writeInt(guideType);
        parcel.writeString(arriveTime);
        parcel.writeString(nextCrossingDistance);
        parcel.writeString(nextRoadName);
        parcel.writeInt(curLaneIndex);
    }
}
