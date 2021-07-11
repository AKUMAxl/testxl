package com.xl.testui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.qinggan.speech.VuiServiceMgr;
import com.qinggan.util.QGSpeechSystemProperties;
import com.xl.testui.databinding.ActivityMainBinding;
import com.xl.testui.record.TestRecord;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mActivityMainBinding;
    private WindowManager mWindowManager;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private String cur_path;
    VuiServiceMgr vuiServiceMgr;
    TestUIControl testUIControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());

        testUIControl = new TestUIControl();
        testUIControl.init(getApplicationContext());
//        testUIControl.registerUIControl();
        testUIControl.registerListUIControl();

        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        mActivityMainBinding.btnAddView.setOnClickListener(v -> {
            if (checkPermission()){
                addTestUIWindow();
            }
        });

        mActivityMainBinding.btnTestRecord.setOnClickListener(v -> {
            if (checkPermission()){
                addTestRecordView();
            }
        });

        mActivityMainBinding.btnTestC100.setOnClickListener(v -> {
            if (checkPermission()){
                addC100TestView();
            }
        });
        mActivityMainBinding.btnTestVehicle.setOnClickListener(v -> {
            if (checkPermission()){
                addC100TesTVehicletView();
            }
        });

        checkWPermission();
        vuiServiceMgr = VuiServiceMgr.getInstance(getApplication(), new VuiServiceMgr.VuiConnectionCallback() {
            @Override
            public void onServiceConnected() {
                Log.d("xLLL","on service connected");
                testUIControl.setVuiServiceMgr(vuiServiceMgr);
            }

            @Override
            public void onServiceDisconnect() {
                Log.d("xLLL","on service disconnected");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        testUIControl.unRegisterUIControl();
    }

    private void checkWPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ||ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ||ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, REQUEST_WRITE_EXTERNAL_STORAGE);

        } else {
            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
            Log.e("xLLL", "checkPermission: 已经授权！");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE){

        }
    }


    private boolean checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            }else {
                Log.d("xLLL","has voerlay permission");
            }
        }
        return true;
    }

    private void addC100TestView(){
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
        View windowView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_window,null);
        AtomicReference<TestRecord> record = new AtomicReference<>();
        Button btn1 = windowView.findViewById(R.id.test1);
        btn1.setText("启动IVOKA");
        btn1.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.qinggan.ivoka","com.qinggan.ivoka.service.WindowService"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            }
        });
        Button btn2 = windowView.findViewById(R.id.test2);
        btn2.setText("模拟语音按键");
        btn2.setOnClickListener(v -> {
            Intent intent = new Intent("com.qinggan.test.voice_hardkey");
            sendBroadcast(intent);

        });
        Button btn3 = windowView.findViewById(R.id.test3);
        btn3.setText("IGN ON");
        btn3.setOnClickListener(v -> {
            Intent intent = new Intent("com.qinggan.test.ign");
            intent.putExtra("status",1);
            sendBroadcast(intent);
        });
        Button btn4 = windowView.findViewById(R.id.test4);
        btn4.setText("IGN OFF");
        btn4.setOnClickListener(v -> {
            Intent intent = new Intent("com.qinggan.test.ign");
            intent.putExtra("status",2);
            sendBroadcast(intent);
        });
        mWindowManager.addView(windowView,params);
    }

    private void addC100TesTVehicletView(){
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
        View windowView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_window,null);
        AtomicReference<TestRecord> record = new AtomicReference<>();
        Button btn1 = windowView.findViewById(R.id.test1);
        btn1.setText("蓝牙");
        btn1.setOnClickListener(v -> {
            VehicleTest.getInstance().init(getApplicationContext());
        });
        Button btn2 = windowView.findViewById(R.id.test2);
        btn2.setText("模拟语音按键");
        btn2.setOnClickListener(v -> {
            Intent intent = new Intent("com.qinggan.test.voice_hardkey");
            sendBroadcast(intent);

        });
        Button btn3 = windowView.findViewById(R.id.test3);
        btn3.setText("IGN ON");
        btn3.setOnClickListener(v -> {
            Intent intent = new Intent("com.qinggan.test.ign");
            intent.putExtra("status",1);
            sendBroadcast(intent);
        });
        Button btn4 = windowView.findViewById(R.id.test4);
        btn4.setText("IGN OFF");
        btn4.setOnClickListener(v -> {
            Intent intent = new Intent("com.qinggan.test.ign");
            intent.putExtra("status",2);
            sendBroadcast(intent);
        });
        mWindowManager.addView(windowView,params);
    }

    private void addTestRecordView(){
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
        View windowView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_window,null);
        AtomicReference<TestRecord> record = new AtomicReference<>();
        Button btn1 = windowView.findViewById(R.id.test1);
        btn1.setText("初始化录音");
        btn1.setOnClickListener(v -> {
            record.set(new TestRecord(this));
        });
        Button btn2 = windowView.findViewById(R.id.test2);
        btn2.setText("开始录音");
        btn2.setOnClickListener(v -> {
            record.get().startRecord();

        });
        Button btn3 = windowView.findViewById(R.id.test3);
        btn3.setText("结束录音");
        btn3.setOnClickListener(v -> {
            record.get().stopRecord();
        });
        windowView.findViewById(R.id.test4).setOnClickListener(v -> {
            finish();
        });
        mWindowManager.addView(windowView,params);
    }

    private void addTestUIWindow(){
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
        View windowView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_window,null);
        windowView.findViewById(R.id.test1).setOnClickListener(v -> finish());
        windowView.findViewById(R.id.test2).setOnClickListener(v -> {
            showImageSelectView();
//            showImage();
        });
        windowView.findViewById(R.id.test3).setOnClickListener(v -> showImage());
        windowView.findViewById(R.id.test4).setOnClickListener(v -> {
            Log.i("xLLL","cancel Request");
            vuiServiceMgr.cancelRequest();
//            private static final String AUDIO_SAVE_SOURCE        = "source";
//            private static final String AUDIO_SAVE_PROCESS       = "process";
//            private static final String AUDIO_SAVE_FEED          = "feed";
//            private static final String AUDIO_SAVE_ALL           = "all";
//            private static final String AUDIO_SAVE_NONE          = "none";
            QGSpeechSystemProperties.set("persist.sys.va.drive_mode","all");
        });
        mWindowManager.addView(windowView,params);
    }

    private void showImage(){
        if (TextUtils.isEmpty(cur_path)){
            Toast.makeText(getApplicationContext(),"先选择图片啊",Toast.LENGTH_LONG).show();
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
        View imageView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_image,null);
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

    private void showImageSelectView(){
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                2038,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        SelectImageView selectImageView = new SelectImageView(getApplicationContext(),null);
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
}