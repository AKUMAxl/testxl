package com.xl.testplayer;

import android.content.Context;
import android.view.SurfaceHolder;

public interface PlayerInterface {

    public void init(Context context);

    public void setResouce(String path);

    public void setDisplay(SurfaceHolder holder);

    public void play();

    public void pause();

}
