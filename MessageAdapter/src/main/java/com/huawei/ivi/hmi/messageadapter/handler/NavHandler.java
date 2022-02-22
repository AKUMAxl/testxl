package com.huawei.ivi.hmi.messageadapter.handler;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.ivi.hmi.netbuslib.bean.NaviStateBean;
import com.huawei.ivi.hmi.netbuslib.bean.NetBusMessageBean;

import java.lang.reflect.Type;

public class NavHandler {

    public final String TAG = NavHandler.class.getSimpleName();

    public void handleNavi(String data){
        Gson gson = new Gson();
        Type t = new TypeToken<NetBusMessageBean<NaviStateBean>>() {
        }.getType();
        NetBusMessageBean<NaviStateBean> netBusMessageBean = gson.fromJson(data, t);
        Log.d(TAG, "handleNavi() called with: netBusMessageBean = [" + netBusMessageBean.toString() + "]");
        // todo udpate aiavm

        NaviStateBean naviStateBean = netBusMessageBean.getData();
        naviStateBean.getGuideType();
        naviStateBean.getNextCrossingDistance();
        naviStateBean.getNextRoadName();
    }
}
