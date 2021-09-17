package com.huawei.ivi.hmi.launcher;

import android.app.ActivityOptions;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class LaunchService extends Service {

    private final String TAG = this.getClass().getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate() called");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        String str;
        if (intent == null) {
            str = null;
        } else {
            str = intent.getAction();
        }
        startMain(getApplicationContext());
        startFreeForm(getApplicationContext());
        Log.i(TAG,"action :"+str);
        if (str == null){
            Log.e(TAG,"action is null");
            return START_STICKY;
        }

        if (str.equalsIgnoreCase("dsv.launcher.action.launch")){
            Intent intent1 = (Intent) intent.getParcelableExtra("launch_target");
            Rect rect = (Rect) intent.getParcelableExtra("window_rect");
            if (intent1!=null){
                ComponentName componentName = intent1.getComponent();
                String packageName = componentName.getPackageName();
                String className = componentName.getClassName();
                Log.e(TAG,"packageName:"+packageName);
                Log.e(TAG,"className:"+className);

            }
            return START_STICKY;
        }else if (!str.equalsIgnoreCase("dsv.launcher.action.enter")){
            return START_STICKY;
        }else {
            int i = intent.getIntExtra("launch_target", 0);
            return START_STICKY;
        }

    }

    private void startMain(Context context){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
        intent.setClassName("com.huawei.ivi.hmi.launcher","com.huawei.ivi.hmi.launcher.MainActivity");
        context.startActivity(intent);
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

}
