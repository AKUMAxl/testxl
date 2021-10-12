package com.huawei.ivi.hmi.launcher.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.app.ActivityOptions;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.huawei.ivi.hmi.launcher.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private ArrayList<OnTouchListener> mOnTouchListeners = new ArrayList<>();

    private AppWidgetManager appWidgetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        setContentView(R.layout.activity_main);
        showFragment();
//        startFreeForm(getApplicationContext());
//        PackageManager packageManager =
//        appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
//        ComponentName componentName = new ComponentName("com.huawei.ivi.hmi.aiavm","com.huawei.ivi.hmi.aiavm.AiavmWidgetProvider");
//        boolean allow = appWidgetManager.bindAppWidgetIdIfAllowed(0,componentName);
//        Log.d("xLLL", "onCreate: allow"+allow);
//        if (!allow){
//            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_BIND);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 11);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, componentName);
//// This is the options bundle discussed above
////            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_OPTIONS, options);
//            startActivityForResult(intent, 100);
//        }
    }

    private void startFreeForm(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ActivityOptions activityOptions = ActivityOptions.makeBasic();
            activityOptions.setLaunchBounds(new Rect(0,0,350,350));
            Bundle bundle = activityOptions.toBundle();
            bundle.putInt("android.activity.windowingMode",102);
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName("com.huawei.ivi.hmi.aiavm","com.huawei.ivi.hmi.aiavm.MainActivity");
            context.startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            Log.d("xLLL", "onActivityResult: "+1111);
            addAppWidget(data);
        }
        if (requestCode==200){
            Log.d("xLLL", "onActivityResult: "+2222);
        }
    }

    void addAppWidget(Intent data) {
        if (data==null){
            Log.d("xLLL", "addAppWidget: ");
            return;
        }
        int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

//        String customWidget = data.getStringExtra(EXTRA_CUSTOM_WIDGET);
        AppWidgetProviderInfo appWidget =
                appWidgetManager.getAppWidgetInfo(appWidgetId);

        if (appWidget.configure != null) {
            // Launch over to configure widget, if needed.
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(appWidget.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            startActivityForResult(intent, 200);
        } else {
            // Otherwise, finish adding the widget.
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (OnTouchListener onTouchListener : mOnTouchListeners){
            onTouchListener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerTouchListener(OnTouchListener onTouchListener){
        mOnTouchListeners.add(onTouchListener);
    }

    public void unregisterTouchListener(OnTouchListener onTouchListener){
        mOnTouchListeners.remove(onTouchListener);
    }

    private void showFragment(){
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        NavController navController = navHostFragment.getNavController();
        navController.navigate(R.id.action_global_mainFragment);
    }


    public interface OnTouchListener{
        boolean onTouch(MotionEvent motionEvent);
    }

}