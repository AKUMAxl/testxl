package com.xl.testui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.MacAddress;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
import android.net.wifi.WifiNetworkSpecifier;
import android.net.wifi.aware.WifiAwareManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ComponentActivity;

@RequiresApi(api = Build.VERSION_CODES.Q)
@SuppressLint("RestrictedApi")
public class AwareActivity extends ComponentActivity {

    private final String TAG = AwareActivity.class.getSimpleName();

    private TextView tv_aware;

//    final NetworkSpecifier specifier =
//            new WifiNetworkSpecifier.Builder()
//                    .setSsidPattern(new PatternMatcher("test", PatternMatcher.PATTERN_PREFIX))
//                    .setBssidPattern(MacAddress.fromString("10:03:23:00:00:00"), MacAddress.fromString("ff:ff:ff:00:00:00"))
//                    .build();
    final NetworkSpecifier specifier =
            new WifiNetworkSpecifier.Builder()
                    .setSsidPattern(new PatternMatcher("XL", PatternMatcher.PATTERN_PREFIX))
                    .setWpa2Passphrase("1qaz@WSX")
                    .build();

    final NetworkRequest request =
            new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .setNetworkSpecifier(specifier)
                    .build();



    final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            Log.d(TAG, "onAvailable() called with: network = [" + network + "]");
        }

        @Override
        public void onUnavailable() {
            super.onUnavailable();
            Log.d(TAG, "onUnavailable() called");
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aware);

        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean enable = getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI_AWARE);
        tv_aware = findViewById(R.id.tv_state);
        tv_aware.setText(enable?"ENABLE":"UN ENABLE");
        WifiAwareManager wifiAwareManager = (WifiAwareManager)getSystemService(Context.WIFI_AWARE_SERVICE);
        IntentFilter filter = new IntentFilter(WifiAwareManager.ACTION_WIFI_AWARE_STATE_CHANGED);
        BroadcastReceiver myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (wifiAwareManager.isAvailable()){
                    Log.d(TAG, "onReceive() called with: isAvailable");
                }else {
                    Log.d(TAG, "onReceive() called with: is not Available");
                }
            }


        };
        registerReceiver(myReceiver, filter);

        connectivityManager.requestNetwork(request, networkCallback);
//        connectivityManager.unregisterNetworkCallback(networkCallback);
    }


}
