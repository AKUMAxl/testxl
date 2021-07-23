package com.xl.testui.record;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.qinggan.util.pcm.PcmDump;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import static android.media.AudioRecord.STATE_INITIALIZED;
import static android.media.AudioTrack.MODE_STATIC;
import static android.media.AudioTrack.MODE_STREAM;

public class TestRecord {

    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    private static final String TAG = "IflytekRecord";
    private Context mContext;
    private RecordThread mRecordThread;
    private PlayThread mPlayThread;
    private AudioRecord audioRecord;
    private AudioTrack mAudioTrack;
    private int bufferSize;
    private File mFile;
    private FileOutputStream mFos;
    private FileInputStream mFis;
    private boolean isRecording;

    private static final int mSampleRateInHz = 16000;
//    private static final int mChannelConfig = AudioFormat.CHANNEL_OUT_MONO;
    private static final int mChannelConfig = AudioFormat.CHANNEL_OUT_MONO;
    private static final int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;

    public TestRecord(Context context) {
        this.mContext = context;
        initRecordThread();
        initAudioTrackThread();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initRecordThread() {
        mRecordThread = new RecordThread();
    }

    private void initAudioTrackThread() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setLegacyStreamType(9).build();
        int audioTrackMinBuffSize = AudioTrack.getMinBufferSize(mSampleRateInHz, mChannelConfig, mAudioFormat);
        mAudioTrack = new AudioTrack.Builder()
                .setTransferMode(MODE_STREAM)
                .setAudioAttributes(audioAttributes)
                .setBufferSizeInBytes(audioTrackMinBuffSize)
//                .setAudioFormat(new AudioFormat.Builder()
//                        .setSampleRate(mSampleRateInHz)
//                        .setEncoding(mAudioFormat)
//                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
//                        .build())
                .build();

        mAudioTrack.setVolume(1.0f);
    }

    public void startRecord() {
        Log.i(TAG, "start record");
        if (isRecording) {
            return;
        }
        isRecording = true;
        createPcmFile();
        mRecordThread = null;
        initRecordThread();
        try {
            mFos = new FileOutputStream(mFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        audioRecord.startRecording();
        mRecordThread.start();
        Log.d(TAG, "start record thread id:" + mRecordThread.getId());
    }


    public void stopRecord() {
        Log.i(TAG, "stopRecord record");
        if (!isRecording) {
            return;
        }
        isRecording = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        if (mRecordThread != null) {
            mRecordThread.interrupt();
            mRecordThread = null;
        }

    }

    private void createPcmFile() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss");
        String time = simpleDateFormat.format(date);
        String fileName = "xltest_" + time + ".pcm";
        File dir = new File(PATH + File.separator + "xltext");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        mFile = new File(dir.getPath() + File.separator + fileName);
        if (mFile.exists()) {
            mFile.delete();
        }
        try {
            mFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void play() {
        if (!checkFile()) {
            return;
        }
        mPlayThread = new PlayThread();
        mPlayThread.start();
    }

    public void stop(){
        if (mAudioTrack==null){
            return;
        }
        mAudioTrack.stop();
        mAudioTrack.flush();
    }

    private boolean checkFile() {
        File dir = new File(PATH + File.separator + "xltext");
        if (!dir.exists()) {
            return false;
        }
        File[] files = dir.listFiles();
        if (files.length < 1) {
            return false;
        }
        for (File file : files) {
            Log.d(TAG, "file name :" + file.getAbsolutePath() + " -- " + file.getName());
        }
        File file = files[0];
        Log.d(TAG,"play file :"+file.getAbsolutePath());
        try {
            mFis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private class RecordThread extends Thread {

        private final static String TAG = "IflytekRecord-RecordThread";

        byte[] buffer;

        @RequiresApi(api = Build.VERSION_CODES.M)
        @SuppressLint("LongLogTag")
        public RecordThread() {
            super();
//            int channel = AudioFormat.CHANNEL_OUT_MONO;//0x3fc
            int channel = 0x3fc;//0x3fc
            int audioSource = MediaRecorder.AudioSource.VOICE_RECOGNITION;
//            int audioSource = 1998;
            bufferSize = AudioRecord.getMinBufferSize(16000, channel, AudioFormat.ENCODING_PCM_16BIT);
            Log.i(TAG, "bufferSize  " + bufferSize);
            audioRecord = new AudioRecord(audioSource, 16000,
                    channel, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
            buffer = new byte[bufferSize];
        }


        @Override
        @SuppressLint("LongLogTag")
        public void run() {
            super.run();
            try {
                while (audioRecord.getState() == STATE_INITIALIZED && isRecording && !currentThread().isInterrupted()) {
                    int ret = audioRecord.read(buffer, 0, bufferSize);
                    Log.d(TAG, "ret:" + ret + " -- " + buffer.length + " -- " + Arrays.toString(buffer));
                    if (AudioRecord.ERROR_INVALID_OPERATION != ret) {
                        mFos.write(buffer);
                    }
                }
                mFos.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    mFos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private class PlayThread extends Thread {

        private final static String TAG = "IflytekRecord-PlayThread";


        @Override
        public void run() {
            super.run();
            int audioTrackMinBuffSize = AudioTrack.getMinBufferSize(mSampleRateInHz, mChannelConfig, mAudioFormat);
            byte[] tempBuffer = new byte[audioTrackMinBuffSize];
            try {
                while (mFis.available() > 0) {
                    int readCount = mFis.read(tempBuffer);
                    if (readCount == AudioTrack.ERROR_INVALID_OPERATION ||
                            readCount == AudioTrack.ERROR_BAD_VALUE) {
                        continue;
                    }
                    if (readCount!=0&&readCount!=-1){
                        mAudioTrack.write(tempBuffer,0,readCount);
                    }
                }
                mAudioTrack.flush();
                mAudioTrack.play();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    mFis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
