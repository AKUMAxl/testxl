package com.xl.testui.socket;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class IPMapping {


    private static ConcurrentHashMap<String,String> mMacDeviceNameMapping = new ConcurrentHashMap<>();

    public static void addDevice(String macAddress,String deviceName){
        if (mMacDeviceNameMapping.containsKey(macAddress)){
            return;
        }
        mMacDeviceNameMapping.put(macAddress,deviceName);
    }

    public static void removeDevice(String macAddress){
        if (!mMacDeviceNameMapping.containsKey(macAddress)){
            return;
        }
        mMacDeviceNameMapping.remove(macAddress);
    }

    public static String getDeviceName(String macAddress){
        if (!mMacDeviceNameMapping.containsKey(macAddress)){
            return "";
        }
        return mMacDeviceNameMapping.get(macAddress);
    }

    public static String getMacAddress(String deviceName){
        for (String macAddress:mMacDeviceNameMapping.keySet()) {
            if (mMacDeviceNameMapping.get(macAddress).equalsIgnoreCase(deviceName)){
                return macAddress;
            }
        }
        return "";
    }

    public static List<String> getDevicesName(){
        return new ArrayList<>(mMacDeviceNameMapping.values());
    }

    public static void update(List<Pair<String,String>> macAndDeviceName){
        mMacDeviceNameMapping.clear();
        for (Pair<String,String> info:macAndDeviceName) {
            mMacDeviceNameMapping.put(info.first,info.second);
        }
    }

}
