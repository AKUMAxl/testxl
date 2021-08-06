package com.xl.testui.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;


import com.qinggan.system.LocationProvider;

import java.io.IOException;
import java.util.List;

public class LocationHelp extends LocationProvider {
    private static final String TAG = LocationHelp.class.getSimpleName();
    //南京
//    private static final double DEFAULT_LAT = 43.834345;
//    private static final double DEFAULT_LON = 125.164503;

    private static final double DEFAULT_LAT = 41.71756955;
    private static final double DEFAULT_LON = 123.44898532;
    //    41.71756955d    123.44898532d
    private double mLat = 0.0;
    private double mLong = 0.0;
    private String mCity = "";
    private LocationManager mlocationManager;
    private Criteria mCriteria;
    private String mProviderName = "";
    private LocationListener mLocationListener;
//    private boolean isNaviConnected = false;

    private Location mLocation;
    private Geocoder mGeocoder;
    private String mLocality;


    public LocationHelp() {
        mLocationListener = new LocationListener();
    }

    public void init(Context context) {
        Log.i(TAG, "init ");
        mlocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        initCriteria(context);
        mGeocoder =  new Geocoder(context);
//        naviControlInit(context);
    }

    private void initCriteria(Context context) {
        mCriteria = new Criteria();
        mCriteria.setAccuracy(Criteria.ACCURACY_FINE);//设置为最大精度
        mCriteria.setAltitudeRequired(false);//不要求海拔信息
        mCriteria.setBearingRequired(false);//不要求方位信息
        mCriteria.setCostAllowed(true);//是否允许付费
        mCriteria.setPowerRequirement(Criteria.POWER_LOW);//对电量的要求
        mProviderName = mlocationManager.getBestProvider(mCriteria, false);
        Log.d(TAG, "initCriteria() called with: mProviderName = [" + mProviderName + "]");
        List<String> providers = mlocationManager.getAllProviders();
        if (providers!=null&&providers.size()>0){
            for (String type:providers) {
                Log.d(TAG,"type -- "+type);
            }
            mProviderName = providers.get(0);
//            mProviderName = mlocationManager.getBestProvider(mCriteria, false);
        }
        android.util.Log.d(TAG, "location provider ::" + mProviderName);
        if (TextUtils.isEmpty(mProviderName)) {
            android.util.Log.d(TAG, "provider is null, using gps");
            mProviderName = "gps";
        }

        PackageManager pm = context.getPackageManager();
        if (Build.VERSION.SDK_INT >= 23 &&
                context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                context.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLocation = mlocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (mLocation != null) {
            mLong = mLocation.getLongitude();
            mLat = mLocation.getLatitude();
        }
        Log.d(TAG, "get init location:lat:" + mLat + ":long:" + mLong);
        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1,
                mLocationListener);
    }

    @Override
    public String getCity() {
        Log.d(TAG, "getCity() called mCity:"+mCity);
        return mCity;
    }

    @Override
    public double getLongitude() {
        if (mLocation != null) {
            mLat = mLocation.getLongitude();
        }else {
            Log.i(TAG,"getLongitude mLocation =null ");
        }
        Log.i(TAG, mLong + "");
        if (mLong != 0) {
            return mLong;
        }
        return DEFAULT_LON;
    }

    @Override
    public double getLatitude() {
        if (mLocation != null) {
            mLat = mLocation.getLatitude();
        }else {
            Log.i(TAG,"getLatitude mLocation =null ");
        }
        Log.i(TAG, mLat + "");
        if (mLat != 0) {
            return mLat;
        }
        return DEFAULT_LAT;
    }

    public String getProviderName() {
        String providerName = "";
        providerName = mLocality;
        Log.d(TAG, "getProviderName() == " + providerName);
        List<Address> addressList = null;
        try {
            addressList = mGeocoder.getFromLocation(DEFAULT_LAT,DEFAULT_LON,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Address address:addressList) {
            mLocality = address.getLocality();
            Log.d(TAG, "getProviderName() called address:"+address.toString());
        }
        return providerName;
    }



    public class LocationListener implements android.location.LocationListener {
        @Override
        public void onLocationChanged(android.location.Location location) {
            Log.d(TAG, "onLocationChanged() called with: location = [" + location + "]");
            if (location != null) {
                mLong = location.getLongitude();
                mLat = location.getLatitude();
                Log.d(TAG, "location on change:lat:" + mLat + ":long:" + mLong);
                try {
                    List<Address> addressList = mGeocoder.getFromLocation(mLat,mLong,1);
                    for (Address address:addressList) {
                        mLocality = address.getLocality();
                    }
                    Log.d(TAG, "get city call back name:" + mLocality);
//                    LocationMgr.getInstance().notifyUpdate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}
