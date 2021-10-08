package com.huawei.ivi.hmi.test_net;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.huawei.ivi.hmi.netlib.DeviceConstant;
import com.huawei.ivi.hmi.netlib.INetCallbackImpl;
import com.huawei.ivi.hmi.netlib.MessageManager;
import com.huawei.ivi.hmi.netlib.NetConnectListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("xLLL","onCreate");
        MessageManager.getInstance().init(getApplicationContext(), new NetConnectListener() {
            @Override
            public void onNetConnected() {
                Log.d("xLLL", "onNetConnected() called");
                MessageManager.getInstance().registerINetCallback(new INetCallbackImpl());
            }

            @Override
            public void onNetDisconnected() {
                Log.d("xLLL", "onNetDisconnected() called");
            }
        });

        findViewById(R.id.send_smg_to_lantu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MessageManager.getInstance().sendMessage(DeviceConstant.LANTU,"com.test.test","test");
            }
        });
        findViewById(R.id.send_smg_to_hw_host).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MessageManager.getInstance().sendMessage(DeviceConstant.HW_HOST,"com.test.test","test");
            }
        });
        findViewById(R.id.send_smg_to_hw_rear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MessageManager.getInstance().sendMessage(DeviceConstant.HW_REAR,"com.test.test","test");
            }
        });
    }
}