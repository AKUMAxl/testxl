package com.xl.testui.record;

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

import java.util.concurrent.atomic.AtomicReference;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class RecordTestManager {

    private Context mContext;
    private WindowManager mWindowManager;
    private View mRootView;

    private RecordTestManager() {
    }

    private static class SingletonInstance {
        private static final RecordTestManager INSTANCE = new RecordTestManager();
    }

    public static RecordTestManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void init(Context context,WindowManager windowManager){
        this.mContext = context;
        this.mWindowManager = windowManager;
        initView();
    }

    public void initView(){
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
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.test_record_window,null);
        AtomicReference<TestRecord> record = new AtomicReference<>();
        mRootView.findViewById(R.id.test_record_back).setOnClickListener(v -> {
            mWindowManager.removeView(mRootView);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.xl.testui","com.xl.testui.MainActivity"));
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
        mRootView.findViewById(R.id.test_record_init).setOnClickListener(v -> {
            record.set(new TestRecord(mContext));
        });
        mRootView.findViewById(R.id.test_record_start).setOnClickListener(v -> {
            record.get().startRecord();

        });
        mRootView.findViewById(R.id.test_record_stop).setOnClickListener(v -> {
            record.get().stopRecord();
        });
        mWindowManager.addView(mRootView,params);
    }

}