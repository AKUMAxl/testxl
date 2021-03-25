package com.xl.testui.record;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by yxw on 17-5-8.
 */

public class PcmDump {

    private static final String TAG = "pcmDump";
    private static boolean mIsInited = false;
    private static String mAudioPath = "/storage/emulated/0/record/";
    private OutputStream mDataOut = null;

    private File mFile;

    private String mFilePath;

    public PcmDump() {
        if (mIsInited == false) {
            synchronized (PcmDump.class) {
                File file = new File(mAudioPath);
                if (file.exists() || file.isDirectory()) {
                    Log.d(TAG, "audio save path is: " + mAudioPath);
                } else {
//                    mAudioPath = Config.AudioSaveCacheDir;
                    Log.d(TAG, "audio save path is11111111: " + mAudioPath);
                }
            }
            mIsInited = true;
        }
    }

    public void openFile(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        mFilePath = fileName;
        try {
            Log.d(TAG,"111");
            closeFile();
            if (mFilePath == null) {
                Log.d(TAG, "get file path error");
                return;
            }
            Log.d(TAG, "record file: " + mAudioPath+mFilePath);
//            mFile = new File(mAudioPath + mFilePath);
//            if (!mFile.exists()) {
//                mFile.createNewFile();
//            } else if (mFile.exists()) {
//
//            }
            mFile = new File(mAudioPath + mFilePath.substring(0, mFilePath
                    .lastIndexOf(".")) + "_" + (new TimeString().getTimeString()) + ".pcm");
            Log.d(TAG,"mData new :"+mFile.getAbsolutePath());
            mDataOut = new FileOutputStream(mFile);
            Log.d(TAG,"mData new 111:"+mDataOut);
        } catch (FileNotFoundException e) {
            Log.d(TAG,"222:"+e.getMessage());
            closeFile();
            e.printStackTrace();
        }

    }

    public void closeFile() {
        Log.d(TAG,"closeFilel");
        try {
            if (mDataOut != null) {
                mDataOut.close();
                Log.d(TAG,"mData = null");
                mDataOut = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordData(byte[] data) {
        if ((data != null) && (data.length > 0)) {
            recordData(data, data.length);
        }
    }

    public void recordData(byte[] data, int length) {
        if (mDataOut != null) {
            try {
                if ((data != null) && (data.length > 0)) {
                    mDataOut.write(data, 0, length);
                } else {
                    Log.d(TAG, "null data");
                }
            } catch (FileNotFoundException e) {
                Log.d(TAG,"444:"+e.getMessage());
                closeFile();
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(TAG,"555:"+e.getMessage());
                closeFile();
                e.printStackTrace();
            }
        }else {
            Log.d(TAG,"mDataOut == null:"+mAudioPath+" -- "+mFilePath);
            mFilePath = "record.pcm";
            try {
                if (mFile==null){
                    mFile = new File(mAudioPath + mFilePath.substring(0, mFilePath.lastIndexOf(".")) + "_" + (new TimeString().getTimeString()) + ".pcm");
                }
                mDataOut = new FileOutputStream(mFile);
            } catch (FileNotFoundException e) {
                Log.d(TAG,"666:"+e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
