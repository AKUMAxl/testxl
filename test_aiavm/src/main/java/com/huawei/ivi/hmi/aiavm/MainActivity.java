package com.huawei.ivi.hmi.aiavm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean a = false;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    a = isInMultiWindowMode();
                    Log.d("xLLL","-------:"+a);
                }
            }
        });
    }

    private void requestPinWidget(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AppWidgetManager appWidgetManager = getApplicationContext().getSystemService(AppWidgetManager.class);
            ComponentName componentName = new ComponentName(getApplicationContext(),AiavmWidgetProvider.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (appWidgetManager.isRequestPinAppWidgetSupported()){
                    Intent pinnedWidgetCallbackIntent = new Intent();
                    PendingIntent successCallback = PendingIntent.getBroadcast(getApplicationContext(),0,pinnedWidgetCallbackIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                    appWidgetManager.requestPinAppWidget(componentName,null,successCallback);
                }
            }
        }
    }
}