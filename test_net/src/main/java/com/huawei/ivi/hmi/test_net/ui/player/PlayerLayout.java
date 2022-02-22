package com.huawei.ivi.hmi.test_net.ui.player;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.huawei.ivi.hmi.test_net.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class PlayerLayout extends FrameLayout implements SurfaceHolder.Callback {

    private Context mContext;
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
        LayoutInflater.from(mContext).inflate(R.layout.layout_player,this,true);
        mSurfaceView = findViewById(R.id.surface);
        mSurfaceView.getHolder().addCallback(this);
        mPlayer = new PlayerImpl();
        mPlayer.init(mContext);
    }

    public void setResource(String path){
//        String path = "/storage/emulated/10/Android/media/com.pateo.arhud/muse.mp4";
        Log.d("xLLL","path:"+path);
        mPlayer.setResource(path);
    }

    public void play(){
        mPlayer.play();
    }

    public void stop(){
        mPlayer.stop();
    }

    public void release(){
        mPlayer.release();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mPlayer.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
}
