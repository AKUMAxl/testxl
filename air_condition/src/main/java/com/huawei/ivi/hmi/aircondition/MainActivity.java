package com.huawei.ivi.hmi.aircondition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.huawei.hmsauto.intelligence.appmanager.AirConditionAppCallback;
import com.huawei.hmsauto.intelligence.appmanager.AirConditionAppManager;
import com.huawei.hmsauto.intelligence.appmanager.IntelligenceApiManager;
import com.huawei.hmsauto.intelligence.appmanager.IntelligenceRemoteException;
import com.huawei.hosauto.vehiclecontrol.HvacManager;

import java.sql.DriverManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        IntelligenceApiManager.init(getApplicationContext());
//        HvacManager hvacManager = HvacManager.getInstance();
//
//        AirConditionAppManager manager = IntelligenceApiManager.getAirConditionAppManager();
//        try {
//            manager.registerAirConditionCallback(new AirConditionAppCallback() {
//                @Override
//                public int setHvacSwitchStatus(boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setHvacSingleSwitchStatus(boolean b, int i) {
//                    return 0;
//                }
//
//                @Override
//                public int setHvacAutoSwitchStatus(int i, boolean b, boolean b1) {
//                    return 0;
//                }
//
//                @Override
//                public int setHvacEnergySavingSwitchStatus(int i, boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setHvacSyncSwitchStatus(int i, int i1, boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setFrontDefrosterSwitchStatus(boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setRearDefrosterSwitchStatus(boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setDefrosterSwitchStatus(boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setHvacAcSwitchStatus(int i, boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setAirClearSwitchStatus(int i, boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setHvacCirculationStatus(boolean b, int i) {
//                    return 0;
//                }
//
//                @Override
//                public int setAbsoluteHvacTemperature(int i, float v, int i1, boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setRelativeHvacTemperature(int i, float v, int i1, boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setHvacTemperatureMax(int i, boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setHvacTemperatureMin(int i, boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setAbsoluteHvacVolume(int i, int i1, boolean b, int i2) {
//                    return 0;
//                }
//
//                @Override
//                public int setRelativeHvacVolume(int i, int i1, boolean b, int i2) {
//                    return 0;
//                }
//
//                @Override
//                public int setHvacVolumeMax(int i, boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setHvacVolumeMin(int i, boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setBlowAllModeStatus(int i, boolean b, int i1) {
//                    return 0;
//                }
//
//                @Override
//                public int setColdWarmStatus(int i, boolean b, int i1) {
//                    return 0;
//                }
//
//                @Override
//                public int setSeatHeatingStatus(int i, boolean b, boolean b1) {
//                    return 0;
//                }
//
//                @Override
//                public int setAbsoluteSeatHeating(int i, int i1, boolean b, int i2) {
//                    return 0;
//                }
//
//                @Override
//                public int setRelativeSeatHeating(int i, int i1, boolean b, int i2) {
//                    return 0;
//                }
//
//                @Override
//                public int setSeatHeatingMax(int i, boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setSeatHeatingMin(int i, boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setSeatVentilatingStatus(int i, boolean b, boolean b1) {
//                    return 0;
//                }
//
//                @Override
//                public int setAbsoluteSeatVentilating(int i, int i1, boolean b, int i2) {
//                    return 0;
//                }
//
//                @Override
//                public int setRelativeSeatVentilating(int i, int i1, boolean b, int i2) {
//                    return 0;
//                }
//
//                @Override
//                public int setSeatVentilatingMax(int i, boolean b) {
//                    return 0;
//                }
//
//                @Override
//                public int setSeatVentilatingMin(int i, boolean b) {
//                    return 0;
//                }
//            });
//        } catch (IntelligenceRemoteException e) {
//            e.printStackTrace();
//        }

    }
}