package com.xl.testui.voice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.xl.testui.R;
import com.xl.testui.vehicle.c100.BtTest;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class VoiceTestManager {

    private Context mContext;
    private WindowManager mWindowManager;
    private View mRootView;

    private VoiceTestManager() {
    }

    private static class SingletonInstance {
        private static final VoiceTestManager INSTANCE = new VoiceTestManager();
    }

    public static VoiceTestManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void init(Context context,WindowManager windowManager){
        this.mContext = context;
        this.mWindowManager = windowManager;
        initView();
    }

    private void initView(){
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2038,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP|Gravity.END;
        params.y = 260;
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.test_voice_window,null);
        mRootView.findViewById(R.id.test_voice_back).setOnClickListener(v -> {
            mWindowManager.removeView(mRootView);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.xl.testui","com.xl.testui.MainActivity"));
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
        mRootView.findViewById(R.id.test_voice_start_ivoka).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.qinggan.ivoka","com.qinggan.ivoka.service.WindowService"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mContext.startForegroundService(intent);
            }
        });
        mRootView.findViewById(R.id.test_voice_bt).setOnClickListener(v -> {
            BtTest.getInstance().init(mContext);
        });
        mRootView.findViewById(R.id.test_voice_hard_key).setOnClickListener(v -> {
            Intent intent = new Intent("com.qinggan.test.voice_hardkey");
            mContext.sendBroadcast(intent);

        });
        mRootView.findViewById(R.id.test_voice_ign_on).setOnClickListener(v -> {
            Intent intent = new Intent("com.qinggan.test.ign");
            intent.putExtra("status",1);
            mContext.sendBroadcast(intent);
        });
        mRootView.findViewById(R.id.test_voice_ign_off).setOnClickListener(v -> {
            Intent intent = new Intent("com.qinggan.test.ign");
            intent.putExtra("status",2);
            mContext.sendBroadcast(intent);
        });
        mWindowManager.addView(mRootView,params);
    }
}