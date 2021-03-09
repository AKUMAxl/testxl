package com.xl.testqgspeech;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.qinggan.util.QGSpeechSystemProperties;
import com.xl.testqgspeech.data.IDataInterface;
import com.xl.testqgspeech.di.annotation.MessageData;
import com.xl.testqgspeech.di.annotation.VoiceData;

import java.security.Permission;
import java.security.Permissions;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static androidx.core.app.ActivityCompat.startActivityForResult;

@AndroidEntryPoint
public class IvokaService extends Service implements LifecycleOwner {

    @VoiceData
    @Inject
    IDataInterface mVoiceData;

    @MessageData
    @Inject
    IDataInterface mMessageData;

    private LifecycleRegistry mLifecycleRegistry;
    private WindowManager mWindowManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("xLLL", "onStartCommand()");
        mLifecycleRegistry.setCurrentState(Lifecycle.State.STARTED);
        startForeground();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.setCurrentState(Lifecycle.State.INITIALIZED);
        checkPermission();
        addView();
        mLifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
        mLifecycleRegistry.setCurrentState(Lifecycle.State.RESUMED);
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                Intent intent1 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
            }
        }
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

    private void addView(){
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2038,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP;
        params.y = 60;
        View windowView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_window,null);
        windowView.findViewById(R.id.test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Contants.EXTRA_KEY.INDEX,2);
                intent.setClass(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        mWindowManager.addView(windowView,params);
    }



}
