package com.huawei.ivi.hmi.launcher;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Objects;

public class MyAccessibilityService extends AccessibilityService {

//    private AudioManager audioManager;

    @Override
    public void onCreate() {
        super.onCreate();
//        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d("xLLL", "onAccessibilityEvent() called with: event = [" + event + "]");

        AccessibilityNodeInfo accessibilityNodeInfo = event.getSource();
        if (accessibilityNodeInfo==null){
            Log.e("xLLL","accessibilityNodeInfo==null");
            return;
        }
        if (TextUtils.isEmpty(accessibilityNodeInfo.getContentDescription())){
            Log.e("xLLL","accessibilityNodeInfo getContentDescription == null");
            return;
        }
        if (Objects.equals(accessibilityNodeInfo.getContentDescription(),"TEST_1")){
            Log.d("xLLL","accessibility test 1");
//            audioManager.adjustStreamVolume(AudioManager.STREAM_ACCESSIBILITY,AudioManager.ADJUST_RAISE,0);
            AccessibilityNodeInfo accessibilityNodeInfoParent = accessibilityNodeInfo.getParent();
            int count = accessibilityNodeInfoParent.getChildCount();
            for (int i = 0; i < count; i++) {
                AccessibilityNodeInfo info = accessibilityNodeInfoParent.getChild(i);
                if (Objects.equals(info.getContentDescription(),"TEST_2")){
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    info.recycle();
                }
            }
        }

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d("xLLL","onServiceConnected");
    }
}
