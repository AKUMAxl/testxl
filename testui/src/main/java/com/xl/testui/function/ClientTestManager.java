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

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ClientTestManager implements View.OnClickListener{

    private Context mContext;
    private WindowManager mWindowManager;
    private View mRootView;

    private ClientTestManager() {
    }

    private static class SingletonInstance {
        private static final ClientTestManager INSTANCE = new ClientTestManager();
    }

    public static ClientTestManager getInstance() {
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
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.test_client_window,null);
        mRootView.findViewById(R.id.test_client_back).setOnClickListener(v -> {
            mWindowManager.removeView(mRootView);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.xl.testui","com.xl.testui.MainActivity"));
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
        mRootView.findViewById(R.id.test_client_navi).setOnClickListener(this::onClick);
        mRootView.findViewById(R.id.test_client_phone).setOnClickListener(this::onClick);
        mRootView.findViewById(R.id.test_client_video).setOnClickListener(this::onClick);
        mRootView.findViewById(R.id.test_client_ui_control).setOnClickListener(this::onClick);
        mWindowManager.addView(mRootView,params);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test_client_navi:
                NaviDemo.getInstance().init(mContext);
                break;
            case R.id.test_client_phone:
                PhoneDemo.getInstance().init(mContext);
                break;
            case R.id.test_client_video:
                VideoDemo.getInstance().init(mContext);
                break;
            case R.id.test_client_ui_control:
                TestUIControl testUIControl = new TestUIControl();
                testUIControl.init(mContext);
//        testUIControl.registerUIControl();
                testUIControl.registerListUIControl();
                break;
            default:
                break;
        }
    }

}