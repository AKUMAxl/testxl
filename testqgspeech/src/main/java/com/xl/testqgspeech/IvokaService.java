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

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.qinggan.ivokaui.RedFlagAnimView;
import com.xl.testqgspeech.data.IDataInterface;
import com.xl.testqgspeech.data.voice.VoiceDataProcessor;
import com.xl.testqgspeech.di.annotation.MessageData;
import com.xl.testqgspeech.di.annotation.VoiceData;
import com.xl.testqgspeech.state.IVoiceCallback;
import com.xl.testqgspeech.ui.view.AutoScrollTextView;


import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;



@AndroidEntryPoint
public class IvokaService extends Service implements IVoiceCallback {

    public static final String TAG = IvokaService.class.getSimpleName();

    @VoiceData
    @Inject
    IDataInterface mVoiceData;

    @MessageData
    @Inject
    IDataInterface mMessageData;

    public static final int FRAGMENT_INDEX_HELP = -1;
    public static final int FRAGMENT_INDEX_SKILL_CENTER = 0;
    public static final int FRAGMENT_INDEX_IMAGE = 1;
    public static final int FRAGMENT_INDEX_VOICE = 2;
    public static final int FRAGMENT_INDEX_SETTING = 3;


    private RedFlagAnimView mRedFlagAnimView;
    private AutoScrollTextView mTextView;

    private WindowManager mWindowManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("xLLL", "onStartCommand()");
        startForeground();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ((VoiceDataProcessor)mVoiceData).setVoiceCallback(this);
        checkPermission();
        addView();

    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    public void onTextChange(String text,int textType) {
        Log.d(TAG, "onTextChange() called with: text = [" + text + "]");
        if (mTextView!=null){
            Log.d(TAG, " text = [" + text + "]");
            if (textType==DEFAULT){
                mTextView.setText(text);
            }else {
                mTextView.setVeticalText(text);
            }
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
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2038,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP|Gravity.END;
        params.y = 110;
        View windowView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_window,null);
        mRedFlagAnimView = windowView.findViewById(R.id.red_flag_anim_view);
        mTextView = windowView.findViewById(R.id.auto_tv);
        mTextView.setDefaultContentWidth(1730);
        mTextView.setScrollInterval(10000);
        mTextView.setMaxLine(1);
        mTextView.setVeticalText(Contants.DEFAULT_TEXT.WAKE_UP_KEYWORD);
        mRedFlagAnimView.setOnClickListener(v -> {
            showView(FRAGMENT_INDEX_HELP);
        });
        mWindowManager.addView(windowView,params);
    }

    private void showView(int index){
        Intent intent = new Intent();
        intent.putExtra(Contants.EXTRA_KEY.INDEX,index);
        intent.setClass(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
