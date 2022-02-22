package com.huawei.ivi.hmi.test_net.ui.player;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.huawei.ivi.hmi.test_net.R;

public class PlayerActivity extends AppCompatActivity {

    private static final String TAG = PlayerActivity.class.getSimpleName();

    private PlayerLayout mPlayer;

    public static PlayerActivity instance;

    private Gson gson = new Gson();

    public static void newInstance(Context context,String path){
        Log.d(TAG, "newInstance() called with: context = [" + context + "], path = [" + path + "]");
        Intent intent = new Intent();
        intent.putExtra("video_name",path);
        intent.setComponent(new ComponentName("com.huawei.ivi.hmi.netbus","com.huawei.ivi.hmi.netbus.ui.player.PlayerActivity"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);
//        getWindow().addFlags(Window.FEATURE_NO_TITLE);
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        mPlayer = findViewById(R.id.player);
        mPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NetBusMessageBean<VideoStateBean> data = new NetBusMessageBean<>();
//                data.setType(MSG_TYPE_VIDEO_INFO);
//                VideoStateBean videoStateBean = new VideoStateBean();
//                videoStateBean.setAction(2);
//                videoStateBean.setVideoName("muse");
//                data.setData(videoStateBean);
//                MessageManager.getInstance().sendMessage(DeviceConstant.DEVICE_NAME.REAR_RIGHT,"com.huawei.android.lancherback",gson.toJson(data));
//                finish();
            }
        });

        String path = getIntent().getStringExtra("video_name");
        mPlayer.setResource(path);
        mPlayer.play();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
        mPlayer.release();
        instance = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
