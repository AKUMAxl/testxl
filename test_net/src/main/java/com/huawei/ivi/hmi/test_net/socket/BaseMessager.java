package com.huawei.ivi.hmi.test_net.socket;

import static com.huawei.ivi.hmi.test_net.socket.ClientMessager.SEND_MSG;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.gson.stream.MalformedJsonException;
import com.huawei.ivi.hmi.test_net.util.DeviceConfigUtil;
import com.huawei.ivi.hmi.netbuslib.DeviceConstant;
import com.huawei.ivi.hmi.netbuslib.bean.BaseDevice;
import com.huawei.ivi.hmi.netbuslib.bean.Device;
import com.huawei.ivi.hmi.test_net.socket.MessageCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class BaseMessager {

    private final String TAG = BaseMessager.class.getSimpleName();
//    public static final String HOST = "192.168.49.1";
    public static final String HOST = "192.168.1.102";
//    public static final String HOST = "10.10.84.56";
    public static final int PORT = 37200;
    public static final String SEND_THREAD = "SEND_MSG_THREAD";
    public static final String RECEIVE_THREAD = "RECEIVE_MSG_THREAD";
    public static final String MSG_HEADER = "message_header";
    public static final String MSG_BEAT = "beat<-";
    public static final String MSG_BEAT_RECEIVE = "beat_receive";
    public static final int BEAT_PERIOD = 1;
    /**
     * 心跳最大粘包数
     */
    final static int MAX_PACKAGE_TOGETHER = 30;

    public static final int SEND_MSG = 1;
//    public static final int SEND_BEAT = 2;
//    public static final int SEND_CHECK_BEAT = 3;

    // 数据长度占用字节数 LONG型为8
    public static final int DATA_LENGTH_LENGTH = 8;
    CopyOnWriteArrayList<MessageCallback> mMessageCallbackList = new CopyOnWriteArrayList<>();

    private HandlerThread mSendThread;
    public Handler mSendHandler;

    private byte[] mHeaderBytes = MSG_HEADER.getBytes(StandardCharsets.UTF_8);
    private byte[] mSendDataBytes;
    private byte[] mSendDataLengthBytes;
    private byte[] mSendAllData;


    public BaseMessager() {

        mSendThread = new HandlerThread(SEND_THREAD);
        mSendThread.start();
        mSendHandler = new Handler(mSendThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case SEND_MSG:
//                        Pair<String, byte[]> pair = (Pair<String, byte[]>) msg.obj;
//                        String destDevice = pair.first;
//                        byte[] data = pair.second;
                        Bundle bundle = msg.getData();
                        boolean isBeatMsg = bundle.getBoolean("isBeat");
                        String destDevice = bundle.getString("destName");
                        byte[] data = bundle.getByteArray("data");
                        sendMsg(isBeatMsg,data, destDevice);
                        break;
//                    case SEND_BEAT:
//                        beat();
//                        break;
//                    case SEND_CHECK_BEAT:
//                        checkBeat();
//                        break;
                }

            }
        };
    }

    public void setMessageCallback(List<MessageCallback> messageCallbacks) {
        mMessageCallbackList.addAll(messageCallbacks);
    }

    public void clearMessageCallback() {
        mMessageCallbackList.clear();
    }

    public void sendMsg(boolean isBeatMsg,String jsonStr, String destName) {
        mSendDataBytes = jsonStr.getBytes(StandardCharsets.UTF_8);
        long length = mSendDataBytes.length;
        mSendDataLengthBytes = long2bytes(length);
        mSendAllData = new byte[mHeaderBytes.length + mSendDataLengthBytes.length + mSendDataBytes.length];
        System.arraycopy(mHeaderBytes, 0, mSendAllData, 0, mHeaderBytes.length);
        System.arraycopy(mSendDataLengthBytes, 0, mSendAllData, mHeaderBytes.length, mSendDataLengthBytes.length);
        System.arraycopy(mSendDataBytes, 0, mSendAllData, mHeaderBytes.length + mSendDataLengthBytes.length, mSendDataBytes.length);
        Message msg = Message.obtain();
        msg.what = SEND_MSG;
//        msg.obj = new Pair<>(destName, mSendAllData);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isBeat",isBeatMsg);
        bundle.putString("destName",destName);
        bundle.putByteArray("data",mSendAllData);
        msg.setData(bundle);
        mSendHandler.sendMessage(msg);
    }


    public void callbackMessage(String content) {
        for (MessageCallback callback : mMessageCallbackList) {
            callback.onMsgReceive(content);
        }
    }

    /**
     * service notify client
     *
     * @param deviceList
     */
    public void callbackClientChange(List<Device> deviceList) {
        for (MessageCallback callback : mMessageCallbackList) {
            callback.onServiceClientChange(deviceList);
        }
    }

    /**
     * client receive service notify
     */
    public void onClientChange(BaseDevice deviceList) {
        for (MessageCallback callback : mMessageCallbackList) {
            callback.onClientChange(deviceList);
        }
    }

    public String macBytes2String(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(":").append(Integer.toHexString(0xFF & bytes[i]));
        }
        return sb.substring(1);
    }

    public abstract void sendMsg(boolean isBeatMsg,byte[] data, String destDeviceNam);

    public abstract void receiveMsg(String receiveMsg,Socket socket);

    public abstract void handleException(Socket socket,LinkedBlockingQueue<byte[]> receiveDataCacheQueue);

    public abstract void beat();

    public abstract void checkBeat();

    long bytes2long(byte[] bs) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bs, 0, bs.length);
        buffer.flip();
        return buffer.getLong();
    }

    private byte[] long2bytes(long data) {
        return ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(data).array();
    }

    class ReceiveMsgProviderThread extends Thread {

        private LinkedBlockingQueue<byte[]> receiveDataCacheQueue;
        private Socket socket;

        private boolean mReceiveRunning  =true;

        public ReceiveMsgProviderThread(Socket socket,LinkedBlockingQueue<byte[]> receiveDataCacheQueue) {
            this.socket = socket;
            this.receiveDataCacheQueue = receiveDataCacheQueue;
        }

        public void stopReceive(){
            mReceiveRunning = false;
        }

        @Override
        public void run() {
            while (mReceiveRunning) {
                receiveMsg();
            }
        }


        private void receiveMsg() {
            try {

                if (socket==null){
                    Log.e(TAG, "receiveMsg socket==null");
                    mReceiveRunning = false;
                    handleException(socket,receiveDataCacheQueue);
                    return;
                }
                if (socket.isClosed()||!socket.isConnected()){
                    Log.e(TAG, "receiveMsg: socket state isClosed:"+socket.isConnected()+" -- isConnected:"+socket.isConnected());
                    mReceiveRunning = false;
                    handleException(socket,receiveDataCacheQueue);
                    return;
                }
                InputStream inputStream = socket.getInputStream();
                int l = inputStream.available();
                byte[] data = new byte[l];
                inputStream.read(data, 0, l);
                if (data.length > 0) {
                    Log.d(TAG, "handleReceiveMsg() called with: data = [" + data.length + "]");
                    receiveDataCacheQueue.offer(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mReceiveRunning = false;
                handleException(socket,receiveDataCacheQueue);
            }
        }
    }


    class ReceiveMsgThread extends Thread {

        private LinkedBlockingQueue<byte[]> receiveDataCacheQueue;
        private Socket socket;
        private byte[] mReceiveDataLengthBytes;
        private byte[] mReceiveAllData;
        private byte[] mLastData;
        private byte[] mCurData;
        private byte[] mTotalData;

        public ReceiveMsgThread(String threadName,Socket socket,LinkedBlockingQueue<byte[]> receiveDataCacheQueue) {
            super(threadName);
            this.socket = socket;
            this.receiveDataCacheQueue = receiveDataCacheQueue;
        }

        @Override
        public void
        run() {
            super.run();
            while (true) {
                handleMsg();
            }
        }

        private void handleMsg() {
            try {
                mCurData = this.receiveDataCacheQueue.poll();
                if (mCurData == null || mCurData.length == 0) {
//                    Log.e(TAG, "handleMsg() mCurData null or 0");
                    return;
                }
                if (mLastData == null) {
                    mTotalData = mCurData;
                } else {
                    mTotalData = new byte[mLastData.length + mCurData.length];
                    System.arraycopy(mLastData, 0, mTotalData, 0, mLastData.length);
                    System.arraycopy(mCurData, 0, mTotalData, mLastData.length, mCurData.length);
                }
                int headLength = MSG_HEADER.length();
                mReceiveDataLengthBytes = new byte[DATA_LENGTH_LENGTH];
                System.arraycopy(mTotalData, headLength, mReceiveDataLengthBytes, 0, DATA_LENGTH_LENGTH);
                int dataLength = (int) bytes2long(mReceiveDataLengthBytes);
                int messageLength = headLength + DATA_LENGTH_LENGTH + dataLength;
                Log.d(TAG, "messageLength:"+messageLength+" -- mTotalData.length:"+mTotalData.length);
                if (messageLength < mTotalData.length) {
                    // message length < byte length
                    Log.d(TAG, "handleMsg() called 粘包 start");
                    int nextMsgIndex = 0;
                    int surplusDataLength = mTotalData.length;
                    while (surplusDataLength > headLength + DATA_LENGTH_LENGTH) {
                        mReceiveDataLengthBytes = new byte[DATA_LENGTH_LENGTH];
                        System.arraycopy(mTotalData, nextMsgIndex + headLength, mReceiveDataLengthBytes, 0, DATA_LENGTH_LENGTH);
                        dataLength = (int) bytes2long(mReceiveDataLengthBytes);
                        Log.d(TAG, "dataLength:"+dataLength);
                        mReceiveAllData = new byte[dataLength];
                        System.arraycopy(mTotalData, nextMsgIndex + headLength + DATA_LENGTH_LENGTH, mReceiveAllData, 0, dataLength);
                        String result = new String(mReceiveAllData, StandardCharsets.UTF_8);
                        receiveMsg(result,socket);
                        mReceiveAllData = null;
                        mReceiveDataLengthBytes = null;
                        result = null;
                        messageLength = headLength + DATA_LENGTH_LENGTH + dataLength;
                        nextMsgIndex += messageLength;
                        surplusDataLength -= messageLength;
                        Log.d(TAG, "messageLength:"+messageLength+" -- surplusDataLength:"+surplusDataLength);
                    } ;
                    if (surplusDataLength != 0) {
                        mLastData = new byte[surplusDataLength];
                        System.arraycopy(mTotalData, mTotalData.length - surplusDataLength, mLastData, 0, surplusDataLength);
                    } else {
                        mLastData = null;
                    }
                    Log.d(TAG, "handleMsg() called 粘包 end");
                } else if (messageLength > mTotalData.length) {
                    // message length > byte length
                    Log.d(TAG, "handleMsg() called 拆包");
                    if (mLastData == null) {
                        mLastData = mTotalData;
                    } else {
                        byte[] tempTotalData = new byte[mTotalData.length + mLastData.length];
                        System.arraycopy(mLastData, 0, tempTotalData, 0, mLastData.length);
                        System.arraycopy(mTotalData, 0, tempTotalData, mLastData.length, mTotalData.length);
                        mLastData = tempTotalData;
                        tempTotalData = null;
                    }
                } else {
                    // message length = byte length
                    Log.d(TAG, "handleMsg() called 正常包");
                    mReceiveAllData = new byte[dataLength];
                    System.arraycopy(mTotalData, headLength + DATA_LENGTH_LENGTH, mReceiveAllData, 0, dataLength);
                    String result = new String(mReceiveAllData, StandardCharsets.UTF_8);
                    receiveMsg(result,socket);
                    mReceiveAllData = null;
                    result = null;
                    mLastData = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
