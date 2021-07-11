package com.xl.testui;

import android.content.Context;
import android.util.Log;

import com.faw.hqzl3.hqvehicleproxy.BluetoothManager;
import com.faw.hqzl3.hqvehicleproxy.HQVehicleProxy;
import com.faw.hqzl3.hqvehicleproxy.Interfaces.BluetoothListener;
import com.faw.hqzl3.hqvehicleproxy.Interfaces.IVehicleServiceListener;

public class VehicleTest {

    private final String TAG = VehicleTest.class.getSimpleName();

    private Context mContext;

    private VehicleTest() {
    }

    private static class SingletonInstance {
        private static final VehicleTest INSTANCE = new VehicleTest();
    }

    public static VehicleTest getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void init(Context context){
        Log.d(TAG, "init() called with: context = [" + context + "]");
        this.mContext = context;
        HQVehicleProxy.getInstance().registerVehicleService(context, new IVehicleServiceListener() {
            @Override
            public void onServiceConnected() {
                BluetoothManager.getInstance(mContext).setBluetoothListener(new BluetoothListener() {
                    @Override
                    public void onBluetoothStateChanged() {
                        Log.d(TAG, "onBluetoothStateChanged() called");
                    }

                    @Override
                    public void onCallActive() {
                        Log.d(TAG, "onCallActive() called");
                    }

                    @Override
                    public void onCallHeld() {
                        Log.d(TAG, "onCallHeld() called");
                    }

                    @Override
                    public void onCallDialing() {
                        Log.d(TAG, "onCallDialing() called");
                    }

                    @Override
                    public void onTerminated() {
                        Log.d(TAG, "onTerminated() called");
                    }

                    @Override
                    public void onInComing() {
                        Log.d(TAG, "onInComing() called");
                    }

                    @Override
                    public void onHeadsetClientDeviceConnectStateChange(int i) {
                        Log.d(TAG, "onHeadsetClientDeviceConnectStateChange() called with: i = [" + i + "]");
                    }

                    @Override
                    public void onPhoneBookDownloadStatusChange(int i) {
                        Log.d(TAG, "onPhoneBookDownloadStatusChange() called with: i = [" + i + "]");
                    }
                });
                String address = BluetoothManager.getInstance(mContext).getBtAddress();
                Log.d("xLLL","address:"+address);
            }

            @Override
            public void onServiceDisconnected() {

            }
        });

    }


}