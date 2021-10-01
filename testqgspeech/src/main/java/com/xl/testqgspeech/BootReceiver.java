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
        Log.e("xLLL", "action:"+action);
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            startVoiceService(context);
            Log.d("xLLL", "Android操作系统开机了，运行中.......");
        } else if (Intent.ACTION_SHUTDOWN.equals(action)) {
            Log.d("xLLL", "Android操作系统关机了.......");
        } else if ("com.test.test".equals(action)){
            startVoiceService(context);
        }else if ("android.intent.action.FIRST_BOOT_COMPLETED".equals(action)){

        }
    }

    private void startVoiceService(Context context){
        ComponentName componentName = new ComponentName(context.getPackageName(),VOICE_SERVICE_CLASS_NAME);
        Intent intent = new Intent();
        intent.setComponent(componentName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        }else {
            context.startService(intent);
        }
    }

}
