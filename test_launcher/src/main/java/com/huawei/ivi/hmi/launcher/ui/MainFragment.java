package com.huawei.ivi.hmi.launcher.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.huawei.ivi.hmi.launcher.R;
import com.huawei.ivi.hmi.launcher.databinding.FragmentMainBinding;
import com.huawei.ivi.hmi.launcher.viewmodel.MainViewModel;

import androidx.navigation.Navigation;

public class MainFragment extends BaseFragment<FragmentMainBinding, MainViewModel> implements GestureDetector.OnGestureListener {

    private final String TAG = MainFragment.class.getSimpleName();

    @Override
    int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    void initViewModel() {

    }

    @Override
    void initView() {
        mBinding.setClickFreeForm(view -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ActivityOptions activityOptions = ActivityOptions.makeBasic();
                activityOptions.setLaunchBounds(new Rect(0,0,350,350));
                Bundle bundle = activityOptions.toBundle();
                bundle.putInt("android.activity.windowingMode",102);
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClassName("com.huawei.ivi.hmi.meter","com.huawei.ivi.hmi.meter.MainActivity");
                startActivity(intent);
            }

        });
        mBinding.setClickServer(view -> {
            Navigation.findNavController(view).navigate(R.id.action_main_to_server);

        });
        mBinding.setClickControl(view -> {
            Navigation.findNavController(view).navigate(R.id.action_main_to_control);
        });
        mBinding.setClickTask(view -> {
            Navigation.findNavController(view).navigate(R.id.action_main_to_task);
        });

    }

    @Override
    GestureDetector.OnGestureListener getGestureListener() {
        return this;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        float dest = motionEvent.getX()-motionEvent1.getX();

        Log.d(TAG, "onFling: dest:"+dest);
        if (motionEvent.getX()-motionEvent1.getX()<-50){
            if (isShowing()){
                Log.d(TAG, "onFling: left");
                float tvx = mBinding.tv.getX();
                float tvy = mBinding.tv.getY();
                Log.d(TAG, "onFling: tvx"+tvx);
                Log.d(TAG, "onFling: tvy"+tvy);


                Rect rect = new Rect();
                mBinding.tv.getFocusedRect(rect);
                rect.left += tvx;
                rect.right += tvx;
                rect.top += tvy;
                rect.bottom += tvy;
                boolean focusOnBar = rect.contains((int) motionEvent.getX(),(int)motionEvent.getY());
                Log.d(TAG, "onFling: left"+rect.left);
                Log.d(TAG, "onFling: top"+rect.top);
                Log.d(TAG, "onFling: right"+rect.right);
                Log.d(TAG, "onFling: bottom"+rect.bottom);
                Log.d(TAG, "onFling: contains"+focusOnBar);
                if (focusOnBar){
                    Navigation.findNavController(getActivity(),R.id.tv).navigate(R.id.action_main_to_server);
                }
                return true;
            }
        }else if (motionEvent.getX()-motionEvent1.getX()>50){
            if (isShowing()){
                Log.d(TAG, "onFling: right");
                return true;
            }
        }
        return false;
    }
}
