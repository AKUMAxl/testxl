package com.xl.testqgspeech;

import android.util.Log;

public class TestView {

    public final String TAG =this.getClass().getSimpleName();

    public void idleToLeft(){
        Log.d(TAG, "idleToLeft() called");
    }

    public void idleToRight(){
        Log.d(TAG, "idleToRight() called");
    }

    public void toListen(){
        Log.d(TAG, "toListen() called");
    }

    public void toIdle(){
        Log.d(TAG, "toIdle() called");
    }

    public void updateText(String text){
        Log.d(TAG, "updateText() called with: text = [" + text + "]");
    }

}
