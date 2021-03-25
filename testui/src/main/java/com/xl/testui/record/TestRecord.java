package com.xl.testui.record;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import static android.media.AudioRecord.STATE_INITIALIZED;

public class TestRecord {

    private static final String TAG = "IflytekRecord";
    private RecordThread mRecordThread;

    private AudioRecord audioRecord;
    private int bufferSize;
    private int SAMPLE_RATE_HZ = 16000;
    private int E111_channel_count = 0x3fc;

    private int E111_audioSource = 1998;
    private int C100_audioSource = MediaRecorder.AudioSource.VOICE_RECOGNITION;
    String E111 = "g6sa";
    String HS5 = "t1c_acrn";

    PcmDump mPcmDump;
    private Context mContext;

    public TestRecord(Context context) {
        this.mContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initRecordThread();
        }

    }


    public void startRecord() {
        Log.i(TAG, "start record");
        mRecordThread = null;
        mPcmDump.openFile("record.pcm");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initRecordThread();
        }
        mRecordThread.start();
        Log.d(TAG, "start record thread id:" + mRecordThread.getId());
    }


    public void stopRecord() {
        Log.i(TAG, "stopRecord record");
        if (mRecordThread != null) {
            mRecordThread.interrupt();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initRecordThread() {
        mRecordThread = new RecordThread();
        mPcmDump = new PcmDump();

    }

    private class RecordThread extends Thread {

        private final static String TAG = "IflytekRecord-RecordThread";


        @RequiresApi(api = Build.VERSION_CODES.M)
        @SuppressLint("LongLogTag")
        public RecordThread() {
            super();
            //TODO 目前先由 读取车机属性 build.product 来区分车型，后续改为 HQVehicleProxy.getInstance().getVehicleModel();
            if (Build.PRODUCT.contains(E111)) {

            } else if (Build.PRODUCT.contains(HS5)) {
                Log.i(TAG, "thread is initializing  HS5");
                bufferSize = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT) * 2; // 原生接口限制,目前只能先取得2channel的大小后再乘2得出4channel的录音buffer大小
                Log.i(TAG, "thread is initializing HS5  bufferSize  " + bufferSize);
                audioRecord = new AudioRecord.Builder()
                        .setAudioSource(MediaRecorder.AudioSource.MIC)
                        .setAudioFormat(new AudioFormat.Builder()
                                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                .setSampleRate(16000)
                                .setChannelIndexMask(0xf)  // 0xf表示0x1111,即4个通道
                                .build())
                        .setBufferSizeInBytes(bufferSize)
                        .build();
            }
            Log.i(TAG, "thread is initializing  E111");
//                private int E111_channel_count = 0x3fc;
//
//                private int E111_audioSource = 1998;
            bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_HZ, E111_channel_count, AudioFormat.ENCODING_PCM_16BIT);
            Log.i(TAG, "thread is initializing E111  bufferSize  " + bufferSize);

            audioRecord = new AudioRecord(C100_audioSource, 16000,
                    E111_channel_count, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

//            AudioManager manager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
//            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANT).build();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                manager.requestAudioFocus(new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
//                        .setAudioAttributes(audioAttributes)
//                        .build());
//            }
        }


        @SuppressLint("LongLogTag")
        public void run() {
            super.run();
            Log.i(TAG, "audio recorder:" + audioRecord.getState());
            try {
                if (audioRecord.getState() == STATE_INITIALIZED) {
                    Log.i(TAG, "audio recorder buffer size:" + bufferSize);

                    byte[] buffer = new byte[bufferSize];
                    //byte[] buffer = new byte[bufferSize];
//                    byte[] output = new byte[bufferSize];

                    Log.d(TAG, "audio record thread:");
                    Log.i(TAG, "audio record thread: state " + audioRecord.getState());
                    Log.i(TAG, "currentThread().isInterrupted(): " + currentThread().isInterrupted());
                    audioRecord.startRecording();
                    Log.i(TAG, "audioRecord.getState(): " + audioRecord.getState());
                    while (!currentThread().isInterrupted()) {

                        int length = audioRecord.read(buffer, 0, bufferSize);
//                        if (length!=2048){
//                            Log.e(TAG,"length != 2048");
//                        }
                        mPcmDump.recordData(buffer);
                        if (length > 0) {
//                            byte[] output = new byte[length];
//                                Log.d(TAG, "record audio data length:" + length);
//                            System.arraycopy(buffer, 0, output, 0, length);
//                                Log.d(TAG, "record audio data length:" + output.length + ":output after");
//                                Log.d(TAG, "record audio data length:" + length + ":copy after");
//                            synchronized (getRecordListeners()) {
//                                for (IRecordListener recordListener : getRecordListeners()) {
//                                    recordListener.onData(buffer, length);
//                                }
//                            }
                        }
                    }
                    Log.i(TAG, "finish record");
                    boolean isInterrupted = currentThread().isInterrupted();
                    Log.e(TAG, "1 isInterrupted:" + isInterrupted);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof InterruptedException) {
                    Log.e(TAG, "InterruptedException");
                    interrupt();
                    boolean isInterrupted = currentThread().isInterrupted();
                    Log.e(TAG, "2 isInterrupted:" + isInterrupted);
                }
            } finally {
                if (audioRecord.getState()==STATE_INITIALIZED){
                    audioRecord.stop();
                    audioRecord.release();
                    audioRecord = null;
                }

            }

        }

    }

}
