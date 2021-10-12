package com.xl.testui.util;

import static com.xl.testui.socket.DeviceConstant.HW_HOST;
import static com.xl.testui.socket.DeviceConstant.HW_HOST_HOST;
import static com.xl.testui.socket.DeviceConstant.HW_REAR;
import static com.xl.testui.socket.DeviceConstant.HW_REAR_HOST;
import static com.xl.testui.socket.DeviceConstant.LANTU;
import static com.xl.testui.socket.DeviceConstant.LANTU_HOST;
import static com.xl.testui.socket.DeviceConstant.P2P_GROUP_OWNER_NAME;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.xl.testui.bean.Device;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class DeviceConfigUtil {

    public static final String TAG = DeviceConfigUtil.class.getSimpleName();

    public static final String FILE_NAME = "mac_cfg.json";

    public static CopyOnWriteArrayList<Device> devices = new CopyOnWriteArrayList<>();

    public static void initMacConfig(Context context) {
        ThreadPoolUtil.getInstance().execute(() -> {
            try {
                StringBuilder sb = new StringBuilder();
                AssetManager assetManager = context.getAssets();
                BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(FILE_NAME)));
                String line;
                while ((line = bf.readLine()) != null) {
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

    public static List<Device> getDevicesInfo() {
        return devices;
    }

    public static String getP2pMac() {
        String ret = "un found";
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            Log.d(TAG, "onClick() called with: v = [" + all.size() + "]");
            for (NetworkInterface nif : all) {
//                if (!nif.getName().equalsIgnoreCase("p2p0")) {
//                    continue;
//                }
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    Log.d(TAG, "NetworkInterface called with: " + nif.getName() + " [macBytes is null]");
                    continue;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                Log.d(TAG, "NetworkInterface called with: " + nif.getName() + " [" + res1.toString() + "]");
                if (nif.getName().equals("p2p0")) {
                    ret = res1.toString();
                    break;
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    public static String getIMEI() {
        Log.d(TAG, "getIMEI: " + Build.DEVICE);
        Log.d(TAG, "getIMEI: " + Build.BOARD);
        Log.d(TAG, "getIMEI: " + Build.BRAND);
        Log.d(TAG, "getIMEI: " + Build.HARDWARE);
        Log.d(TAG, "HOST: " + Build.HOST);
        Log.d(TAG, "getIMEI: " + Build.ID);
        Log.d(TAG, "getIMEI: " + Build.MODEL);
        Log.d(TAG, "getIMEI: " + Build.PRODUCT);
        return "";
    }

    public static String getDeviceName() {
/*
30:
HWLIO-L
LIO
HUAWEI
kirin990E
cn-central-hcd-2a-21345952e1629685408656-7f665fc9fd-jzhpd
HUAWEILIO-AN00m
LIO-AN00m
40:
HWNOH
NOH
HUAWEI
kirin9000
cn-east-hcd-4a-79deed25c1627552150890-779bf7d57-bn6gz
HUAWEINOH-AN01
NOH-AN01
NOH-AN01
Android:
f0501h
f0501h
Android
freescale
njpateo-222
1.0.0-ga-rc3
F0501H-MX8Q
f0501h
*/
        if (Build.HOST.equals(HW_HOST_HOST)) {
            return HW_HOST;
        } else if (Build.HOST.equals(HW_REAR_HOST)) {
            return HW_REAR;
        } else if (Build.HOST.equals(LANTU_HOST)) {
            return LANTU;
        }
        return LANTU;
    }

    public static boolean isSocketService(){
        return getDeviceName().equals(LANTU);
    }

    public static boolean isP2pGroupOwner(String p2pDeviceName){
        boolean isOwner = p2pDeviceName.equals(P2P_GROUP_OWNER_NAME);
        Log.d(TAG, "isP2pGroupOwner() called with: p2pDeviceName = [" + p2pDeviceName + "]");
        Log.d(TAG, "isP2pGroupOwner() called with: isOwner = [" + isOwner + "]");
        return isOwner;
    }
}
