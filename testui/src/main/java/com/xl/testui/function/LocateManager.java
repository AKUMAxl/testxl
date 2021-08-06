package com.xl.testui.function;

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
import com.xl.testui.util.LocationHelp;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class LocateManager {

    private Context mContext;
    private WindowManager mWindowManager;
    private View mRootView;

    private static volatile LocateManager singleton;

    private LocationHelp mlocationHelp;

    private LocateManager() {
    }

    public static LocateManager getInstance() {
        if (singleton == null) {
            synchronized (LocateManager.class) {
                if (singleton == null) {
                    singleton = new LocateManager();
                }
            }
        }
        return singleton;
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
        params.y = 260;
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.test_locate_window,null);
        mRootView.findViewById(R.id.test_client_back).setOnClickListener(v -> {
            mWindowManager.removeView(mRootView);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.xl.testui","com.xl.testui.MainActivity"));
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
        mRootView.findViewById(R.id.test_locate_init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlocationHelp = new LocationHelp();
                mlocationHelp.init(mContext.getApplicationContext());
            }
        });
        mRootView.findViewById(R.id.test_locate_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlocationHelp.getLatitude();
                mlocationHelp.getLongitude();
                mlocationHelp.getCity();
                mlocationHelp.getProviderName();
            }
        });
        mRootView.findViewById(R.id.test_locate_test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mWindowManager.addView(mRootView,params);

    }
}