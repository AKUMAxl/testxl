package com.xl.testui.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.xl.testui.bean.Device;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class MacConfigUtil {

    public static final String FILE_NAME = "mac_cfg.json";

    public static CopyOnWriteArrayList<Device> devices = new CopyOnWriteArrayList<>();

    public static void initMacConfig(Context context){
        ThreadPoolUtil.getInstance().execute(() -> {
            try {
                StringBuilder sb = new StringBuilder();
                AssetManager assetManager = context.getAssets();
                BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(FILE_NAME)));
                String line;
                while ((line = bf.readLine())!=null){
                    sb.append(line);
                }
                String strManCfg = sb.toString();
                Gson gson = new Gson();
//                Device device = gson.fromJson(strManCfg, Device.class);
//                devices.addAll(device.getDevices());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static List<Device> getDevicesInfo(){
        return devices;
    }


}
