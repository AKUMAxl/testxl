package com.xl.testqgspeech;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import static com.xl.testqgspeech.Contants.PACKAGE_NAME;
import static com.xl.testqgspeech.Contants.VOICE_SERVICE_CLASS_NAME;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
//            startVoiceService(context);
            startBrowser(context);
            Log.d("xLLL", "Android操作系统开机了，运行中.......");
        } else if (Intent.ACTION_SHUTDOWN.equals(action)) {
            Log.d("xLLL", "Android操作系统关机了.......");
        }
    }

//    private void startVoiceService(Context context){
//        ComponentName componentName = new ComponentName(PACKAGE_NAME,VOICE_SERVICE_CLASS_NAME);
//        Intent intent = new Intent();
//        intent.setComponent(componentName);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(intent);
//        }else {
//            context.startService(intent);
//        }
//    }

    private void startBrowser(Context context){
        Intent intent= new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("http://172.1.110.3/dsdp/");
        intent.setData(content_url);
        context.startActivity(intent);

    }
}
