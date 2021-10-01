package com.xl.testui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class P2pService extends Service {

    public static final String TAG = P2pService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startForeground(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getPackageName());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = null;
            notificationChannel = new NotificationChannel(getPackageName(), getPackageName(), NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(notificationChannel);
            builder.setSmallIcon(R.drawable.ic_launcher_background);
            startForeground(android.os.Process.myPid(), builder.build());
        }else {
            builder.setSmallIcon(R.mipmap.ic_launcher);
            Notification notification = builder.build();
            startForeground(android.os.Process.myPid(),notification);
        }
    }
}
