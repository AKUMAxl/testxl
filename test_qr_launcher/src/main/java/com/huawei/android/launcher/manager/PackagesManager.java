package com.huawei.android.launcher.manager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.huawei.android.launcher.bean.AppInfoBean;

import java.util.ArrayList;
import java.util.List;

public class PackagesManager {

    private final String TAG = PackagesManager.class.getSimpleName();

    private Context mContext;
    private PackageManager mPackageManager;

    private PackagesManager() {
    }

    private static class SingletonInstance {
        private static final PackagesManager INSTANCE = new PackagesManager();
    }

    public static PackagesManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void init(Context context){
        this.mContext = context;
        mPackageManager = mContext.getPackageManager();
    }

    public List<AppInfoBean> getPackageList(){
        List<AppInfoBean> appInfoBeans = new ArrayList<>();
        List<PackageInfo> packageList = mPackageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo packageInfo:packageList) {
            AppInfoBean appInfoBean = new AppInfoBean();
            appInfoBean.setAppName(packageInfo.applicationInfo.loadLabel(mPackageManager).toString());
            appInfoBean.setPackageName(packageInfo.packageName);
            appInfoBean.setIcon(packageInfo.applicationInfo.loadIcon(mPackageManager));
            appInfoBeans.add(appInfoBean);
        }
        return appInfoBeans;
    }

    public void startApp(String packageName){
        Intent intent = mPackageManager.getLaunchIntentForPackage(packageName);
        mContext.startActivity(intent);
    }

}