package com.xl.testlocaltion;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GnssMeasurementsEvent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

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
                    Log.d(TAG, "onGnssMeasurementsReceived() called with: eventArgs = [" + eventArgs + "]");
                }

                @Override
                public void onStatusChanged(int status) {
                    super.onStatusChanged(status);
                    Log.d(TAG, "onStatusChanged() called with: status = [" + status + "]");
                }
            });
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {

                }
            });
        }
    }
}