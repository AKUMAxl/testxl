package com.xl.testui.vehicle;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.xl.testui.R;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class VehicleTestManager {

    private Context mContext;
    private WindowManager mWindowManager;
    private View mRootView;

    private VehicleTestManager() {
    }

    private static class SingletonInstance {
        private static final VehicleTestManager INSTANCE = new VehicleTestManager();
    }

    public static VehicleTestManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void init(Context context, WindowManager windowManager){
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
        params.y = 60;
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.test_vehicle_window,null);
        mRootView.findViewById(R.id.test_vehicle_back).setOnClickListener(v -> {
            mWindowManager.removeView(mRootView);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.xl.testui","com.xl.testui.MainActivity"));
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
        mWindowManager.addView(mRootView,params);
    }
}
