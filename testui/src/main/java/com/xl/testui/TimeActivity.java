package com.xl.testui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.xl.testui.databinding.ActivityMainBinding;
import com.xl.testui.databinding.ActivityTimeBinding;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class TimeActivity extends AppCompatActivity {

    private boolean mFlag = true;
    private ActivityTimeBinding activityTimeBinding;
    private int distance = 30;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_time);
        activityTimeBinding = ActivityTimeBinding.inflate(getLayoutInflater());
        setContentView(activityTimeBinding.getRoot());

        startAnim();
    }

    private void startAnim(){
        activityTimeBinding.time.animate().translationY(distance).setDuration(1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd \n \n HH:mm:ss");
                String time = sdf.format(date.getTime());
                activityTimeBinding.time.setText(time);
                distance = -distance;
                startAnim();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }
}
