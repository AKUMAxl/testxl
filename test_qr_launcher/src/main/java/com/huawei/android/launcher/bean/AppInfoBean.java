package com.huawei.android.launcher.bean;

import android.graphics.drawable.Drawable;

public class AppInfoBean {

    String appName;
    String packageName;
    Drawable icon;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "AppInfoBean{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", icon=" + icon +
                '}';
    }
}
