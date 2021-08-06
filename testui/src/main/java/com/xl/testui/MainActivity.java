package com.xl.testui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.xl.testui.databinding.ActivityMainBinding;
import com.xl.testui.function.ClientTestManager;
import com.xl.testui.function.LocateManager;
import com.xl.testui.iflytek.IflytekTestManager;
import com.xl.testui.record.RecordTestManager;
import com.xl.testui.socket.SocketTestManager;
import com.xl.testui.testui.UITestManager;
import com.xl.testui.util.ImageUtil;
import com.xl.testui.vehicle.VehicleTestManager;
import com.xl.testui.voice.VoiceTestManager;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mActivityMainBinding;
    private WindowManager mWindowManager;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());


        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mActivityMainBinding.btnTestFinish.setOnClickListener(v -> {
            finish();
        });
        mActivityMainBinding.btnTestClient.setOnClickListener(v -> {
            if (checkPermission()) {
                ClientTestManager.getInstance().init(getApplicationContext(), mWindowManager);
            }
        });

        mActivityMainBinding.btnTestRecord.setOnClickListener(v -> {
            if (checkPermission()) {
                RecordTestManager.getInstance().init(getApplicationContext(), mWindowManager);
            }
        });

        mActivityMainBinding.btnTestUi.setOnClickListener(v -> {
            if (checkPermission()) {
                UITestManager.getInstance().init(getApplicationContext(), mWindowManager);
            }
        });
        mActivityMainBinding.btnTestVoice.setOnClickListener(v -> {
            if (checkPermission()) {
                VoiceTestManager.getInstance().init(getApplicationContext(), mWindowManager);
            }
        });
        mActivityMainBinding.btnTestVehicle.setOnClickListener(v -> {
            if (checkPermission()) {
                VehicleTestManager.getInstance().init(getApplicationContext(), mWindowManager);
            }
        });
        mActivityMainBinding.btnTestIflytek.setOnClickListener(v -> {
            if (checkPermission()) {
                IflytekTestManager.getInstance().init(getApplicationContext(), mWindowManager);
            }
        });
        mActivityMainBinding.btnTestLocate.setOnClickListener(v -> {
            if (checkPermission()) {
                LocateManager.getInstance().init(getApplicationContext(), mWindowManager);
            }
        });
        mActivityMainBinding.btnTestSocket.setOnClickListener(v -> {
            if (checkPermission()){
                SocketTestManager.getInstance().init(getApplicationContext(), mWindowManager);
            }
        });
        checkWPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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




}