package com.xl.testqgspeech;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.qinggan.ivokaui.RedFlagAnimView;
import com.xl.testqgspeech.data.IDataInterface;
import com.xl.testqgspeech.data.voice.VoiceDataProcessor;
import com.xl.testqgspeech.di.annotation.MessageData;
import com.xl.testqgspeech.di.annotation.VoiceData;
import com.xl.testqgspeech.state.IVoiceCallback;
import com.xl.testqgspeech.ui.view.AutoScrollTextView;


import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static android.widget.LinearLayout.HORIZONTAL;


@AndroidEntryPoint
public class IvokaService extends Service implements LifecycleOwner, IVoiceCallback {

    public static final String TAG = IvokaService.class.getSimpleName();

    @VoiceData
    @Inject
    IDataInterface mVoiceData;

    @MessageData
    @Inject
    IDataInterface mMessageData;

    private RedFlagAnimView mRedFlagAnimView;
    private AutoScrollTextView mTextView;

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
        ((VoiceDataProcessor)mVoiceData).setVoiceCallback(this);
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


    @Override
    public void onImageStateChange(int imageState) {
        Log.d(TAG, "onImageStateChange() called with: imageState = [" + imageState + "]");
        if (mRedFlagAnimView==null){
            Log.d(TAG, "onImageStateChange() return");
            return;
        }
        switch (imageState){
            case TO_LEFT:
                mRedFlagAnimView.toListening(RedFlagAnimView.MicDirection.FR_LEFT);
                break;
            case TO_RIGHT:
                mRedFlagAnimView.toListening(RedFlagAnimView.MicDirection.FR_RIGHT);
                break;
            case IDLE:
            default:
                mRedFlagAnimView.toIdle();
                break;
        }
    }

    @Override
    public void onTextChange(String text) {
        Log.d(TAG, "onTextChange() called with: text = [" + text + "]");
        if (mTextView!=null){
            Log.d(TAG, " text = [" + text + "]");
            mTextView.setText(text);
        }
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
                600,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2038,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP|Gravity.END;
        params.y = 160;
        View windowView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_window,null);
        mRedFlagAnimView = windowView.findViewById(R.id.red_flag_anim_view);
        mTextView = windowView.findViewById(R.id.auto_tv);
//        LinearLayout ll = windowView.findViewById(R.id.window_ll);
//        ll.setOrientation(HORIZONTAL);
//        ll.addView(getRedFlagAnimView());
//        ll.addView(getAutoScrollTextView());
//        ll.invalidate();
        mRedFlagAnimView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(Contants.EXTRA_KEY.INDEX,2);
            intent.setClass(getApplicationContext(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        mWindowManager.addView(windowView,params);
    }



}
