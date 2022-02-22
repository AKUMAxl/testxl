package com.huawei.ivi.hmi.test_net.socket;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.ivi.hmi.netbuslib.bean.BaseDevice;
import com.huawei.ivi.hmi.test_net.bean.MessageBean;
import com.huawei.ivi.hmi.test_net.util.DeviceConfigUtil;
import com.huawei.ivi.hmi.netbuslib.DeviceConstant;
import com.huawei.ivi.hmi.netbuslib.RetryTimer;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientMessager extends BaseMessager {


    private final String TAG = ClientMessager.class.getSimpleName();

    private String mHost;
    private Socket socket;

    private InputStream inputStream;
    private OutputStream outputStream;

    private RetryTimer mBeatTimer;
    private RetryTimer mCheckBeatTimer;
//    private Timer mBeatTimer;


    /**
     * 未发送数据缓冲队列
     */
    private LinkedBlockingQueue<byte[]> mDataCache = new LinkedBlockingQueue<>();
    /**
     * 接收数据队列
     */
    LinkedBlockingQueue<byte[]> mReceiveDataCacheQueue = new LinkedBlockingQueue<>();

    private ConnectServiceStateListener mConnectListener;

    private boolean mIsConnected = false;

    private ReceiveMsgProviderThread mReceiveMsgProviderThread;
    private ReceiveMsgThread mReceiveMsgThread;

    private int mSendBeatCount, mReceiveBeatCount;

    private Object mLock = new Object();

    public interface ConnectServiceStateListener {
        void onConnected();

        void onDisConnect();
    }

    public ClientMessager(String hostIp) {
        super();
        this.mHost = hostIp;
    }

    public void connect(ConnectServiceStateListener connectServiceStateListener) {
        Log.d(TAG, "connect() called with: connectServiceStateListener = [" + connectServiceStateListener + "]");
        while (true) {
            try {
                if (connectServiceStateListener != null) {
                    mConnectListener = connectServiceStateListener;
                }
                if (!Thread.interrupted()) {
                    Thread.sleep(5 * 1000);
                }
                if (mIsConnected) {
                    continue;
                }
                if (TextUtils.isEmpty(mHost)) {
                    mHost = HOST;
                }
                Log.d(TAG, "START SOCKET");
                mSendBeatCount = 0;
                mReceiveBeatCount = 0;
                socket = new Socket();

                socket.bind(new InetSocketAddress("192.168.1.103",37300));
//                socket.bind(new InetSocketAddress("10.10.84.65",37300));
//                socket.bind(new InetSocketAddress("127.0.0.1",37300));
                socket.connect(new InetSocketAddress(mHost, PORT), 2 * 1000);
//                socket.connect(new InetSocketAddress(mHost, PORT));
                Log.d(TAG, "START SOCKET OVER");
                socket.setTcpNoDelay(true);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Log.d(TAG, "SOCKET INIT SUCCESS");
            mIsConnected = true;
            startReceive();
            callbackConnectState(true);
            reSendMsg();
            startBeat();
            startCheckBeat();

        }

    }


    public void closeClient() {
        Log.d(TAG, "closeClient() called");
        try {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }
            if (socket != null) {
                if (socket.isConnected() && !socket.isClosed()) {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                }
                socket.close();
                socket = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void p2pDisconnect() {
        // todo noting socket auto reconnect
        stopReceive();
        stopBeat();
        stopCheckBeat();
        closeClient();
        mIsConnected = false;
    }

    private void callbackConnectState(boolean connect) {
        if (mConnectListener != null) {
            if (connect) {
                mConnectListener.onConnected();
            } else {
                mConnectListener.onDisConnect();
            }
        }
    }

    private void reconnect() {
        callbackConnectState(false);
        closeClient();
        stopReceive();
        stopBeat();
        stopCheckBeat();
        closeClient();
        mIsConnected = false;
    }

    private void cacheData(byte[] data) {
        if (data != null) {
            mDataCache.offer(data);
        }
    }

    private void reSendMsg() {
        Log.d(TAG, "reSendMsg() called mDataCache isEmpty:" + mDataCache.isEmpty());
        while (!mDataCache.isEmpty()) {
            sendMsg(false, mDataCache.poll(), null);
        }
    }


    private void startBeat() {
        Log.d(TAG, "startBeat() called");
//        mSendBeatCount++;
//        sendMsg(MSG_BEAT + DeviceConfigUtil.getDeviceName(), DeviceConstant.DEVICE_NAME.LANTU);
//        Message msg = Message.obtain();
//        msg.what = SEND_BEAT;
//        mSendHandler.sendMessageDelayed(msg, 5 * 1000);
        if (mBeatTimer == null) {
            Log.d(TAG, "beat timer start");
            mBeatTimer = new RetryTimer();
            mBeatTimer.startTimer(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "beat timer run");
                    mSendBeatCount++;
                    sendMsg(true,MSG_BEAT + DeviceConfigUtil.getDeviceName()+":"+mSendBeatCount, DeviceConstant.DEVICE_NAME.LANTU);
                }
            },BEAT_PERIOD,BEAT_PERIOD);
//            mBeatTimer = new Timer();
//            mBeatTimer.scheduleAtFixedRate(new TimerTask() {
//                @Override
//                public void run() {
//                    Log.d(TAG, "beat timer run");
//                    mSendBeatCount++;
//                    sendMsg(MSG_BEAT + DeviceConfigUtil.getDeviceName(), DeviceConstant.DEVICE_NAME.LANTU);
//                }
//            },0,5*1000);
        }
    }

    public void stopBeat() {
        Log.d(TAG, "stopBeat() called:" + mBeatTimer);
//        mSendHandler.removeMessages(SEND_BEAT);
        synchronized (mLock){
            if (mBeatTimer != null) {
                Log.d(TAG, "beat timer stop");
//            mBeatTimer.purge();
//            mBeatTimer.cancel();
                mBeatTimer.stopTimer();
                mBeatTimer = null;
            }
        }

    }

    private void startCheckBeat() {
        Log.d(TAG, "startCheckBeat() called");
//        Log.d(TAG, "check beat timer run mSendBeatCount:" + mSendBeatCount + " -- mReceiveBeatCount:" + mReceiveBeatCount);
//        if (mSendBeatCount == Integer.MAX_VALUE - MAX_PACKAGE_TOGETHER) {
//            mSendBeatCount = 0;
//            mReceiveBeatCount = 0;
//        }
//        // 允许 MAX_PACKAGE_TOGETHER 个beat package 粘包 超过 MAX_PACKAGE_TOGETHER 个执行重连socket
//        if (mSendBeatCount > mReceiveBeatCount + MAX_PACKAGE_TOGETHER) {
//            reconnect();
//        }
//        Message msg = Message.obtain();
//        msg.what = SEND_CHECK_BEAT;
//        mSendHandler.sendMessageDelayed(msg, 5 * 1000);
        if (mCheckBeatTimer == null) {
            Log.d(TAG, "check beat timer start");
            mCheckBeatTimer = new RetryTimer();
            mCheckBeatTimer.startTimer(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "check beat timer run mSendBeatCount:" + mSendBeatCount + " -- mReceiveBeatCount:" + mReceiveBeatCount);
                    if (mSendBeatCount == Integer.MAX_VALUE - MAX_PACKAGE_TOGETHER) {
                        mSendBeatCount = 0;
                        mReceiveBeatCount = 0;
                    }
                    // 允许 MAX_PACKAGE_TOGETHER 个beat package 粘包 超过 MAX_PACKAGE_TOGETHER 个执行重连socket
                    if (mSendBeatCount > mReceiveBeatCount + MAX_PACKAGE_TOGETHER) {
                        Log.e(TAG, "mort than MAX_PACKAGE_TOGETHER packages unreceived -> reconnect");
                        reconnect();
                    }
                }
            },BEAT_PERIOD,BEAT_PERIOD);
        }
    }

    private void stopCheckBeat() {
        Log.d(TAG, "stopCheckBeat() called:" + mCheckBeatTimer);
//        mSendHandler.removeMessages(SEND_CHECK_BEAT);
        if (mCheckBeatTimer != null) {
            Log.d(TAG, "check beat timer stop");
            mCheckBeatTimer.stopTimer();
            mCheckBeatTimer = null;
        }
    }


    @Override
    public void sendMsg(boolean isBeatMsg,byte[] data, String destDeviceNam) {
        Log.d(TAG, "sendMsg() called with: isBeatMsg = [" + isBeatMsg + "], data = [" + data + "], destDeviceNam = [" + destDeviceNam + "]");
        try {
            if (!mIsConnected) {
                Log.e(TAG, "sendMsg : connecting");
                if (!isBeatMsg){
                    cacheData(data);
                }
                return;
            }
            if (socket == null) {
                Log.e(TAG, "sendMsg: socket is null -> reconnect");
                if (!isBeatMsg){
                    cacheData(data);
                }
                reconnect();
                return;
            } else if (socket.isClosed() || !socket.isConnected()) {
                Log.e(TAG, "sendMsg: socket close or disconnect -> reconnect");
                if (!isBeatMsg){
                    cacheData(data);
                }
                reconnect();
                return;
            } else {
                outputStream = socket.getOutputStream();
            }

            outputStream.write(data);
            outputStream.flush();
            Log.d(TAG, "sendMsg() success");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception sendMsg: socket is null -> reconnect");
            if (!isBeatMsg){
                cacheData(data);
            }
            reconnect();
        }
    }

    @Override
    public void receiveMsg(String receiveMsg, Socket socket) {
        if (TextUtils.isEmpty(receiveMsg)) {
            return;
        }
        Log.d(TAG, "receive data:" + receiveMsg);
        if (receiveMsg.startsWith(MSG_BEAT_RECEIVE)) {
            mReceiveBeatCount++;
            return;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<MessageBean<Object>>() {
        }.getType();

        MessageBean<Object> messageBean = gson.fromJson(receiveMsg, type);
        if (messageBean.getType() == MessageBean.TYPE_DEVICES_CHANGE) {
            String json = messageBean.getData().toString();
            BaseDevice baseDevice = gson.fromJson(json, BaseDevice.class);
            onClientChange(baseDevice);
        } else {
            callbackMessage(receiveMsg);
        }
    }


    @Override
    public void handleException(Socket socket, LinkedBlockingQueue<byte[]> receiveDataCacheQueue) {
        Log.d(TAG, "handleException() called with: mIsConnected = [" + mIsConnected + "]");
        if (mIsConnected){
            stopReceive();
            startReceive();
        }

    }

    @Override
    public void beat() {
        startBeat();
    }

    @Override
    public void checkBeat() {
        startCheckBeat();
    }

    public void startReceive() {
        Log.d(TAG, "receiveMsg() called");
        if (socket == null) {
            Log.e(TAG, "receiveMsg() socket==null");
            reconnect();
            return;
        }
        if (socket.isClosed()) {
            Log.e(TAG, "receiveMsg() socket isClosed");
            reconnect();
            return;
        }
        mReceiveMsgProviderThread = new ReceiveMsgProviderThread(socket, mReceiveDataCacheQueue);
        mReceiveMsgProviderThread.start();
        mReceiveMsgThread = new ReceiveMsgThread(RECEIVE_THREAD, socket, mReceiveDataCacheQueue);
        mReceiveMsgThread.start();
    }

    public void stopReceive() {
        Log.d(TAG, "stopReceive() called");
        mReceiveDataCacheQueue.clear();
        if (mReceiveMsgProviderThread != null) {
            mReceiveMsgProviderThread.stopReceive();
            mReceiveMsgProviderThread.interrupt();
            mReceiveMsgProviderThread = null;
        }
        if (mReceiveMsgThread != null) {
            mReceiveMsgThread.interrupt();
            mReceiveMsgThread = null;
        }

    }


}
