package com.xl.testlocaltion.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GnssMeasurementsEvent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class LocationHelper {

    public static final String TAG = LocationHelper.class.getSimpleName();

    private Context mContext;

    private LocationManager locationManager;

    private static volatile LocationHelper singleton;

    private LocationHelper() {
    }

    public static LocationHelper getInstance() {
        if (singleton == null) {
            synchronized (LocationHelper.class) {
                if (singleton == null) {
                    singleton = new LocationHelper();
                }
            }
        }
        return singleton;
    }

    public void init(Context context){
        Log.d(TAG, "init() called with: context = [" + context + "]");
        this.mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> pr = locationManager.getAllProviders();
        for (int i = 0; i < pr.size(); i++) {
            Log.d("xLLL", "---" + pr.get(i));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.registerGnssMeasurementsCallback(new GnssMeasurementsEvent.Callback() {
                @Override
                public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {
                    super.onGnssMeasurementsReceived(eventArgs);
//                    Log.d(TAG, "onGnssMeasurementsReceived() called with: eventArgs = [" + eventArgs + "]");
                }

                @Override
                public void onStatusChanged(int status) {
                    super.onStatusChanged(status);
//                    Log.d(TAG, "onStatusChanged() called with: status = [" + status + "]");
                }
            });


            try {
                Class<?> clz = Class.forName("android.location.LocationManager");
                Log.d(TAG,clz.getName());
                Message message = Message.obtain();
                message.what = 1;
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLongitude(0d);
                location.setLatitude(0d);
                message.obj = location;
//                clz.getEnclosingClass()
                Method[] methods = clz.getDeclaredMethods();
                for (Method m:methods) {
                    Log.d("xLLL","m:"+m.getName());
                }
//                method.setAccessible(true);
//                method.invoke(location);
                Proxy.newProxyInstance(clz.getClassLoader(), clz.getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getName().equals("_handleMessage")){
                            Log.d("xLLL","_handleMessage");
                        }
                        return null;
                    }
                });

//                Class<?> clz = Class.forName("com.android.server.location.LocationManagerService");
//                Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{}, new InvocationHandler() {
//                    @Override
//                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                        if ("callLocationChangedLocked".equals(method.getName())){
//                            Log.d(TAG, "invoke() called with: proxy = [" + proxy + "], method = [" + method + "], args = [" + args + "]");
//                        }
//                        return null;
//                    }
//                });
//
//
//                Method methodLocationChange = clz.getMethod("callLocationChangedLocked");
//                Location location = new Location(LocationManager.GPS_PROVIDER);
//                location.setLatitude(0d);
//                location.setLongitude(0d);
//                methodLocationChange.invoke(location);
//                methodLocationChange.setAccessible(true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
//            catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            }
//            catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
//            catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    Log.d(TAG, "onLocationChanged() called with: location = [" + location + "]");
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Log.d(TAG, "onLocationChanged() called with: latitude = [" + latitude + "]");
                    Log.d(TAG, "onLocationChanged() called with: longitude = [" + longitude + "]");
                }
            });



        }
    }
}