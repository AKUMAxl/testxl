package com.xl.testplayer;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PlayerLayout extends FrameLayout implements SurfaceHolder.Callback {

    private Context mContext;
    private View mRootView;
    private SurfaceView mSurfaceView;
    private PlayerInterface mPlayer;

    public PlayerLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PlayerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context){
        this.mContext = context;
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.layout_player,this,true);
        mSurfaceView = findViewById(R.id.surface);
        mSurfaceView.getHolder().addCallback(this);
        mPlayer = new PlayerImpl();
        mPlayer.init(mContext);


    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"test.mp4";
//        path = "file:/"+path;
        Log.d("xLLL","path:"+path);
        mPlayer.setResouce(path);
        mPlayer.play();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mPlayer.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
}
