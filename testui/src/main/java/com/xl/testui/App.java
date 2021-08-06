package com.xl.testui;

import android.app.Application;


import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StringBuffer param = new StringBuffer();
        param.append("appid="+getString(R.string.app_id));
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(App.this, param.toString());
    }
}
