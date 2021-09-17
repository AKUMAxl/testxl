package com.huawei.ivi.hmi.aiavm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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