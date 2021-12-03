package com.xl.testplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.IOException;

public class PlayerImpl implements PlayerInterface{

    private MediaPlayer mMediaPlayer;
    private Context mContext;

    @Override
    public void init(Context context) {
        this.mContext = context;
        mMediaPlayer = new MediaPlayer();

    }

    @Override
    public void setResouce(String path) {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mContext, Uri.parse(path));
//            mMediaPlayer.setDataSource("https://vd4.bdstatic.com/mda-mm203w9zy1s06sr8/sc/cae_h264/1638490314545158409/mda-mm203w9zy1s06sr8.mp4?v_from_s=hkapp-haokan-hnb&auth_key=1638524282-0-0-289d553299120d710d59833bd07b8995&bcevod_channel=searchbox_feed&pd=1&pt=3&abtest=3000201_4&klogid=0482143289");
            mMediaPlayer.setOnPreparedListener(onPreparedListener);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {
        mMediaPlayer.setDisplay(holder);
    }

    @Override
    public void play() {
//        mMediaPlayer.start();
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
    }

    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mMediaPlayer.start();
        }
    };
}
