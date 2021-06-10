package com.qinggan.voice_demo;

import android.content.Context;

//import com.faw.hqzl3.hqdatastatussdk.HQDataStatusManager;

public class VehicleTest {

    public static final String TAG = "VOICE_TEST_VEHICLE";

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
        this.mContext = context;
//        HQDataStatusManager.getInstance().init(mContext);
    }

    public void testPDSN(){
//        String pdsn = HQDataStatusManager.getInstance().getVehiclePDSN();

    }
}