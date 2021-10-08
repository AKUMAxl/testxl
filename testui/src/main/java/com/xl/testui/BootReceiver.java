package com.xl.testui;

import static com.xl.testui.socket.DeviceConstant.HW_HOST;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xl.testui.bean.MessageBean;
import com.xl.testui.socket.ClientMessager;
import com.xl.testui.socket.IPMapping;
import com.xl.testui.socket.P2pInfoListener;
import com.xl.testui.socket.P2pManager;
import com.xl.testui.socket.ServiceMessager;
import com.xl.testui.socket.SocketTestManager;
import com.xl.testui.util.DeviceConfigUtil;

import java.lang.reflect.Type;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    public static final String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("xLLL", "action:"+action);
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)|| "android.intent.action.FIRST_BOOT_COMPLETED".equals(action)) {
            Log.d("xLLL", "Android操作系统开机了，运行中.......");
//            Toast.makeText(context, "start p2p", Toast.LENGTH_SHORT).show();
            startP2p(context);
        } else if (Intent.ACTION_SHUTDOWN.equals(action)) {
            Log.d("xLLL", "Android操作系统关机了.......");
        } else if ("com.test.test".equals(action)){
            startP2p(context);
        }
    }

    public void startP2p(Context context){
        P2pManager.getInstance().init(context);
        if (DeviceConfigUtil.isSocketService()){
            P2pManager.getInstance().createGroup();
        }else {
            P2pManager.getInstance().discoverPeer();
        }
    }



}
