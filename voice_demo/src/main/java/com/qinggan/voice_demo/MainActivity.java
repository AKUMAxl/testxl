package com.qinggan.voice_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.qinggan.speech.VoiceActionID;
import com.qinggan.speech.VuiActionHandler;
import com.qinggan.speech.VuiServiceMgr;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "NAVI_DEMO";

    private VuiServiceMgr mVuiServiceMgr;

    public static final int[] mActions = {
        VoiceActionID.ACTION_DCS_COMMON_CONTENT,
        VoiceActionID.ACTION_DCS_RESTAURANT_NAV,
        VoiceActionID.ACTION_DCS_POI_NAV, //普通poi
        VoiceActionID.ACTION_DCS_ROUTELINE_NAV,
        VoiceActionID.ACTION_OTW_QUERY,
        VoiceActionID.ACTION_DCS_TOURISTATTRACTION_NAV,
        VoiceActionID.ACTION_DCS_HOTEL_NAV,
        VoiceActionID.ACTION_DCS_SOCIETY_PHONE_CONTACTS_QUERY,
        VoiceActionID.ACTION_DCS_PARKING_NAV,
        VoiceActionID.ACTION_DCS_COMMON_SHOW_HOTEL_DETAIL,
        VoiceActionID.ACTION_DCS_COMMON_SHOW_HINTS,
        VoiceActionID.ACTION_NAVI_ROUTE, //离线状态下的导航搜索->沿途的加油站
        VoiceActionID.ACTION_LBS_QUERY, //离线状态下的搜索->附近的充电桩
        VoiceActionID.ACTION_HELP_LAUNCH,
        VoiceActionID.ACTION_HELP_QUERY,
        VoiceActionID.ACTION_VOICE_IVOKA_CLOSE_NAVI,
        VoiceActionID.ACTION_VOICE_TEXT_SHOW,
        VoiceActionID.ACTION_VOICE_IVOKA_CANCEL,
        VoiceActionID.ACTION_VOICE_IVOKA_HOTEL_CANCEL,
        VoiceActionID.ACTION_VOICE_RESTAURANT_DETAIL,
        VoiceActionID.ACTION_TOURIS_TATTRACTION_DETAIL,
        VoiceActionID.ACTION_ID_VIOLATION,//添加违章查询
        VoiceActionID.ACTION_VOICE_ELECTRONIC_MANUAL_TABLE,
        VoiceActionID.ACTION_VOICE_IVOKA_NUMBER_TYPE,
        VoiceActionID.ACTION_VOICE_ELECTRONIC_MANUAL_PIC_CONTENT,
        VoiceActionID.ACTION_VOICE_ELECTRONIC_MANUAL_VIDEO,
        VoiceActionID.ACTION_VOICE_IMAGE_MODE_CHANGED,
        VoiceActionID.ACTION_VOICE_MEDIA_SHARE,
        VoiceActionID.ACTION_NAVI_ADD_MIDWAY_POINT,
        VoiceActionID.ACTION_VOICE_CHARGING_STATION_QUERY,
        VoiceActionID.ACTION_VOICE_CHARGING_STATION_DETAIL,
        VoiceActionID.ACTION_ID_IDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VehicleTest.getInstance().init(this);
        findViewById(R.id.init).setOnClickListener(v -> init());
        findViewById(R.id.pdsn).setOnClickListener(v -> VehicleTest.getInstance().testPDSN());
        findViewById(R.id.start_ivoka).setOnClickListener(v -> startVoiceService());
        findViewById(R.id.voice_hand_key).setOnClickListener(v -> sendVoiceHardKey());
        findViewById(R.id.ign_on).setOnClickListener(v -> sendIGN(1));
        findViewById(R.id.ign_off).setOnClickListener(v -> sendIGN(2));
    }

    private void startVoiceService(){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.qinggan.ivoka","com.qinggan.ivoka.service.WindowService"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }
    }

    private void bindVoiceService(){
        mVuiServiceMgr.registerHandler(VuiServiceMgr.AppServiceType.mbNavi.ordinal(), mActions, new VuiActionHandler() {
            @Override
            public boolean onProcessResult(Message message) {
                Log.d(TAG, "onProcessResult() called with: message = [" + message.what + "]");
                boolean ret = false;
                switch (message.what){
                    case VoiceActionID.ACTION_DCS_ROUTELINE_NAV:// 例：我要去沈阳北站
                        ret = true;
                        break;
                    default:
                        break;
                }
                return ret;
            }
        });
    }

    private void init(){
        mVuiServiceMgr = VuiServiceMgr.getInstance(getApplication(), new VuiServiceMgr.VuiConnectionCallback() {
            @Override
            public void onServiceConnected() {
                bindVoiceService();
                Log.d(TAG, "onServiceConnected() called");
            }

            @Override
            public void onServiceDisconnect() {
                Log.d(TAG, "onServiceDisconnect() called");
            }
        });
    }

    private void sendVoiceHardKey(){
        Intent intent = new Intent("com.qinggan.test.voice_hardkey");
        sendBroadcast(intent);
    }

    private void sendIGN(int data){
        Intent intent = new Intent("com.qinggan.test.ign");
        intent.putExtra("status",data);
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVuiServiceMgr!=null){
            mVuiServiceMgr.exit();
        }

    }
}