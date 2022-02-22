package com.huawei.ivi.hmi.test_net;

import static com.huawei.ivi.hmi.netbuslib.MessageManager.PACKAGE_NAME;
import static com.huawei.ivi.hmi.netbuslib.MessageManager.SERVICE_NAME;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;


public class BootReceiver extends BroadcastReceiver {

    public static final String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("xLLL", "action:"+action);
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)|| "android.intent.action.FIRST_BOOT_COMPLETED".equals(action)) {
            Log.d("xLLL", "Android操作系统开机了，运行中.......");
            startP2p(context);
        } else if (Intent.ACTION_SHUTDOWN.equals(action)) {
            Log.d("xLLL", "Android操作系统关机了.......");
        } else if ("com.test.test".equals(action)){
            startP2p(context);
        }else if ("com.netbus.keep".equals(action)){
            Log.d("xLLL", "KEEP BEAT.......");
        }
    }

    public void startP2p(Context context){
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(PACKAGE_NAME, SERVICE_NAME);
        intent.setComponent(componentName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        }else {
            context.startService(intent);
        }
    }



}
