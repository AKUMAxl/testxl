package com.huawei.ivi.hmi.launcher;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;


public class TestReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("xLLL", "action:"+action);
        if ("com.test.test".equals(action)){
            String test_extra_value = intent.getStringExtra("test_extra_key");
            Log.d("xLLL", "test_extra_value:"+test_extra_value);

            switch (test_extra_value){
                case "freeform":
                    startFreeForm(context);
                    break;
                case "launcher":
                    startMain(context);
                    break;
                default:
                    break;
            }
        }
    }

    private void startFreeForm(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ActivityOptions activityOptions = ActivityOptions.makeBasic();
            activityOptions.setLaunchBounds(new Rect(0,0,350,350));
            Bundle bundle = activityOptions.toBundle();
            bundle.putInt("android.activity.windowingMode",102);
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName("com.huawei.ivi.hmi.aiavm","com.huawei.ivi.hmi.aiavm.MainActivity");
            context.startActivity(intent);
        }
    }

    private void startMain(Context context){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
        intent.setClassName("com.huawei.ivi.hmi.launcher","com.huawei.ivi.hmi.launcher.MainActivity");
        context.startActivity(intent);
    }

}
