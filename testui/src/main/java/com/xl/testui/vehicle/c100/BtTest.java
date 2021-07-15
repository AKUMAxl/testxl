package com.xl.testui.vehicle.c100;

import android.car.Car;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.faw.hqzl3.hqdatastatussdk.HQDataStatusManager;
import com.faw.hqzl3.hqvehicleproxy.ActivityEntryManager;
import com.faw.hqzl3.hqvehicleproxy.BluetoothManager;
import com.faw.hqzl3.hqvehicleproxy.HQVehicleProxy;
import com.faw.hqzl3.hqvehicleproxy.Interfaces.BluetoothListener;
import com.faw.hqzl3.hqvehicleproxy.Interfaces.IVehicleServiceListener;
import com.xl.testui.util.CarForegroundAppUtils;


public class BtTest {

    private final String TAG = BtTest.class.getSimpleName();

    private Context mContext;

    private boolean btServiceConnected;

    private BtTest() {
    }

    private static class SingletonInstance {
        private static final BtTest INSTANCE = new BtTest();
    }

    public static BtTest getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void init(Context context){
        Log.d(TAG, "init() called with: context = [" + context + "]");
        this.mContext = context;
        HQDataStatusManager.getInstance().init(mContext);
        HQVehicleProxy.getInstance().init(mContext);

    }

    public void initBt(){
        if (btServiceConnected) {
            return;
        }
        HQVehicleProxy.getInstance().registerVehicleService(mContext, new IVehicleServiceListener() {
            @Override
            public void onServiceConnected() {
                btServiceConnected = true;
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

            }

            @Override
            public void onServiceDisconnected() {
                btServiceConnected = false;
            }
        });
    }

    public void getBtInfo(){
        String address = BluetoothManager.getInstance(mContext).getBtAddress();
        Log.d("xLLL","address:"+address);
    }


    public void saveHomeLocation(double lon,double lat){
//        HQDataStatusManager.getInstance().setSystemData("homeLon",String.valueOf(lon));
//        HQDataStatusManager.getInstance().setSystemData("homeLat",String.valueOf(lat));
    }

    public void saveCompanyLocation(double lon,double lat){
//        HQDataStatusManager.getInstance().setSystemData("companyLon",String.valueOf(lon));
//        HQDataStatusManager.getInstance().setSystemData("companyLat",String.valueOf(lat));
    }

    public void getForegroundAppInfo(){
        String foregroundActivityName = CarForegroundAppUtils.getForegroundActivityName(mContext);
        String foregroundPackageName = CarForegroundAppUtils.getForegroundPackageName(mContext);
        Log.d(TAG,"foregroundActivityName:"+foregroundActivityName+" -- foregroundActivityName"+foregroundPackageName);
    }

}