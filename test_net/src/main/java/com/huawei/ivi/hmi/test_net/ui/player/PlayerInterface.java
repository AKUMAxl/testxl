package com.huawei.ivi.hmi.test_net.ui.player;

import android.content.Context;
import android.view.SurfaceHolder;

public interface PlayerInterface {

    public void init(Context context);

    public void setResource(String path);

    public void setDisplay(SurfaceHolder holder);

    public void play();

    public void pause();

    public void stop();

    public void release();

}
