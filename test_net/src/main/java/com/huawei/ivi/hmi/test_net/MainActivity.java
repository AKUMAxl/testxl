package com.huawei.ivi.hmi.test_net;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.ivi.hmi.netlib.DeviceConstant;
import com.huawei.ivi.hmi.netlib.INetCallbackImpl;
import com.huawei.ivi.hmi.netlib.MessageCallback;
import com.huawei.ivi.hmi.netlib.MessageManager;
import com.huawei.ivi.hmi.netlib.NetConnectListener;

public class MainActivity extends AppCompatActivity {

    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = findViewById(R.id.state);
        Log.d("xLLL","onCreate");
        MessageManager.getInstance().init(getApplicationContext(), new NetConnectListener() {
            @Override
            public void onNetConnected() {
                Log.d("xLLL", "onNetConnected() called");
                mTv.setText("service connected");
                MessageManager.getInstance().registerINetCallback(new MessageCallback() {
                    @Override
                    public void onMessageReceived(String s) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onNetDisconnected() {
                Log.d("xLLL", "onNetDisconnected() called");
            }

            @Override
            public void onNetAvailableChange(boolean b) {
                Log.d("xLLL", "onNetAvailableChange() called with: b = [" + b + "]");
                if (b){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mTv.setText("service p2p connected");
//                            Toast.makeText(getApplicationContext(),"service p2p connected",Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });

        findViewById(R.id.send_smg_to_lantu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MessageManager.getInstance().sendMessage(DeviceConstant.LANTU,"com.test.test","msg -> lantu");
            }
        });
        findViewById(R.id.send_smg_to_hw_host).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MessageManager.getInstance().sendMessage(DeviceConstant.HW_HOST,"com.test.test","msg -> hw host");
            }
        });
        findViewById(R.id.send_smg_to_hw_rear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MessageManager.getInstance().sendMessage(DeviceConstant.HW_REAR,"com.test.test","msg -> hw rear");
            }
        });


    }
}