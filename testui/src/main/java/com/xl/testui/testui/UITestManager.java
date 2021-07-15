package com.xl.testui.testui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;


import com.xl.testui.R;

import java.io.File;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class UITestManager {

    private Context mContext;
    private WindowManager mWindowManager;
    private View mRootView;
    private String cur_path;

    private UITestManager() {
    }

    private static class SingletonInstance {
        private static final UITestManager INSTANCE = new UITestManager();
    }

    public static UITestManager getInstance() {
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
        View windowView = LayoutInflater.from(mContext).inflate(R.layout.test_ui_window,null);
        windowView.findViewById(R.id.test_ui_back).setOnClickListener(v -> {
            mWindowManager.removeView(windowView);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.xl.testui","com.xl.testui.MainActivity"));
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
        windowView.findViewById(R.id.test_ui_select).setOnClickListener(v -> {
            showImageSelectView();
        });
        windowView.findViewById(R.id.test_ui_show).setOnClickListener(v -> showImage());
//            private static final String AUDIO_SAVE_SOURCE        = "source";
//            private static final String AUDIO_SAVE_PROCESS       = "process";
//            private static final String AUDIO_SAVE_FEED          = "feed";
//            private static final String AUDIO_SAVE_ALL           = "all";
//            private static final String AUDIO_SAVE_NONE          = "none";
//        QGSpeechSystemProperties.set("persist.sys.va.drive_mode","all");
        mWindowManager.addView(windowView,params);
    }

    private void showImageSelectView(){
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                2038,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        SelectImageView selectImageView = new SelectImageView(mContext,null);
        selectImageView.setSelectResultListener(new SelectResultCallback() {
            @Override
            public void selectResult(String path) {
                mWindowManager.removeView(selectImageView);
                Log.d("xLLL","select result:"+path);
                cur_path = path;
            }
        });
        mWindowManager.addView(selectImageView,params);
    }

    private void showImage(){
        if (TextUtils.isEmpty(cur_path)){
            Toast.makeText(mContext,"先选择图片啊",Toast.LENGTH_LONG).show();
            return;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                2038,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        View imageView = LayoutInflater.from(mContext).inflate(R.layout.view_image,null);
        ImageView image = imageView.findViewById(R.id.image);
        imageView.findViewById(R.id.dismiss).setOnClickListener(v -> mWindowManager.removeView(imageView));
        image.setImageURI(Uri.fromFile(new File(cur_path)));
        ((SeekBar)imageView.findViewById(R.id.seekBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                image.setAlpha(progress*0.01f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mWindowManager.addView(imageView,params);
    }

}