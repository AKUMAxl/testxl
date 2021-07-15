package com.xl.testlocaltion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.xl.testlocaltion.location.LocationHelper;
import com.xl.testlocaltion.test.InvokeTest;
import com.xl.testlocaltion.test.TestInterface;

import java.lang.reflect.Proxy;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
//        LocationHelper.getInstance().init(getApplicationContext());
        testInvoke();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult() called with: requestCode = [" + requestCode + "], permissions = [" + permissions + "], grantResults = [" + grantResults + "]");
        LocationHelper.getInstance().init(getApplicationContext());
    }

    private void testInvoke(){
        try {
            Class<?> c = Class.forName("com.xl.testlocaltion.test.RealTest");
            Object o = c.newInstance();
            TestInterface t = (TestInterface) Proxy.newProxyInstance(c.getClassLoader(),c.getInterfaces(),new InvokeTest(o));
            t.doThins("aaa");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}