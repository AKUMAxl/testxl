package com.xl.testqgspeech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.qinggan.speech.VuiServiceMgr;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VuiServiceMgr vuiServiceMgr = VuiServiceMgr.getInstance(getApplication(), new VuiServiceMgr.VuiConnectionCallback() {
            @Override
            public void onServiceConnected() {
                Log.d("xLLL","on service connected");
            }

            @Override
            public void onServiceDisconnect() {
                Log.d("xLLL","on service disconnected");
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("xLLL","on click");
                vuiServiceMgr.cancelRequest();
            }
        });

    }
}