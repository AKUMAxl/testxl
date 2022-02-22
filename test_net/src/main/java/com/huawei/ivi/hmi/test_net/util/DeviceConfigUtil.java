package com.huawei.ivi.hmi.test_net.util;

import static com.huawei.ivi.hmi.netbuslib.DeviceConstant.IVI_NAME;
import static com.huawei.ivi.hmi.netbuslib.DeviceConstant.KEY_LOCATION;
import static com.huawei.ivi.hmi.netbuslib.DeviceConstant.KEY_P2P_OWNER_DEVICE_NAME;
import static com.huawei.ivi.hmi.netbuslib.DeviceConstant.P2P_GROUP_OWNER_NAME;
import static com.huawei.ivi.hmi.netbuslib.DeviceConstant.PIVI_NAME;
import static com.huawei.ivi.hmi.netbuslib.DeviceConstant.REAR_LEFT;
import static com.huawei.ivi.hmi.netbuslib.DeviceConstant.REAR_RIGHT;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.huawei.ivi.hmi.netbuslib.DeviceConstant;
import com.tencent.mmkv.MMKV;

public class DeviceConfigUtil {

    public static final String TAG = DeviceConfigUtil.class.getSimpleName();

    private static SharedPreferences sp;

    public static void init(Context context){
        sp = context.getSharedPreferences("sp_data",Context.MODE_PRIVATE);
    }

    public static void setDeviceLocation(@DeviceConstant.PARAM_LOCATION_TYPE int location){
        if (sp==null){
            Log.e(TAG, "setDeviceLocation: sp==null ");
        }else {
            sp.edit().putInt(KEY_LOCATION,location).commit();
        }

//        MMKV kv = MMKV.defaultMMKV();
//        kv.encode(KEY_LOCATION,location);
    }

    public static int getDeviceLocation(){
        return sp.getInt(KEY_LOCATION,DeviceConstant.LOCATION.UNKNOWN);
//        MMKV kv = MMKV.defaultMMKV();
//        return kv.decodeInt(KEY_LOCATION, DeviceConstant.LOCATION.UNKNOWN);
    }

    public static void setP2pGroupOwnerName(String ownerDevicesName){
        MMKV kv = MMKV.defaultMMKV();
        kv.encode(KEY_P2P_OWNER_DEVICE_NAME,ownerDevicesName);
    }

    public static String getP2pGroupOwnerName(){
        MMKV kv = MMKV.defaultMMKV();
        return kv.decodeString(KEY_P2P_OWNER_DEVICE_NAME,"");
    }


    public static String getDeviceName() {
        int location = DeviceConfigUtil.getDeviceLocation();
        if (location == DeviceConstant.LOCATION.HW_HOST) {
            return DeviceConstant.DEVICE_NAME.HW_HOST;
        } else if (location == DeviceConstant.LOCATION.LANTU) {
            return DeviceConstant.DEVICE_NAME.LANTU;
        } else if (location == DeviceConstant.LOCATION.REAR_LEFT) {
            return DeviceConstant.DEVICE_NAME.REAR_LEFT;
        } else if(location == DeviceConstant.LOCATION.REAR_RIGHT){
            return DeviceConstant.DEVICE_NAME.REAR_RIGHT;
        }
        return DeviceConstant.DEVICE_NAME.HW_HOST;
    }

    public static boolean isHost(){
        return getDeviceName().equals(IVI_NAME);
//        return getDeviceName().equals(PIVI_NAME);
    }

    public static boolean isPIvi(){
//        return getDeviceName().equals(IVI_NAME);
        return getDeviceName().equals(PIVI_NAME);
    }

    public static boolean isRearLeft(){
        return getDeviceName().equals(REAR_LEFT);
    }

    public static boolean isRearRight(){
        return getDeviceName().equals(REAR_RIGHT);
    }

    public static boolean isP2pGroupOwner(String p2pDeviceName){
        boolean isOwner = p2pDeviceName.equals(P2P_GROUP_OWNER_NAME);
        Log.d(TAG, "isP2pGroupOwner() called with: p2pDeviceName = [" + p2pDeviceName + "]");
        Log.d(TAG, "isP2pGroupOwner() called with: isOwner = [" + isOwner + "]");
        return isOwner;
    }
}
