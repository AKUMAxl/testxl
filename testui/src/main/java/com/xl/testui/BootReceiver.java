package com.xl.testui;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.xl.testui.socket.P2pManager;
import com.xl.testui.socket.SocketTestManager;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("xLLL", "action:"+action);
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            startP2pService(context);
            Log.d("xLLL", "Android操作系统开机了，运行中.......");
        } else if (Intent.ACTION_SHUTDOWN.equals(action)) {
            Log.d("xLLL", "Android操作系统关机了.......");
        } else if ("com.test.test".equals(action)){
            startP2pService(context);
        }
    }

    private void startP2pService(Context context){
        P2pManager.getInstance().init(context);
//        P2pManager.getInstance().startP2pService();
        P2pManager.getInstance().discoverPeer();
    }

}
