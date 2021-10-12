package com.huawei.ivi.hmi.netlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MessageManager {

    private final String TAG = MessageManager.class.getSimpleName();

    public static final String PACKAGE_NAME = "com.xl.testui";
    public static final String SERVICE_NAME = "com.xl.testui.P2pService";

    private static final int RETRY_COUNT = 5;

    private Context mContext;

    private NetConnectListener mNetConnectListener;

    private INetInterface mINetInterface;

    private INetCallbackImpl mINetCallbackImpl;

    private ScheduledExecutorService scheduledExecutorService;

    private int mRetryCount;

    private boolean mIsBinding;

    private MessageManager() {
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        });
    }

    private static class SingletonInstance {
        private static final MessageManager INSTANCE = new MessageManager();
    }

    public static MessageManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                mINetInterface = INetInterface.Stub.asInterface(iBinder);
                iBinder.linkToDeath(mDeathRecipient, 0);
                if (mNetConnectListener!=null){
                    mNetConnectListener.onNetConnected();
                }
                mINetInterface.registerNetCallback(mINetCallbackImpl,mContext.getPackageName());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (mNetConnectListener!=null){
                mNetConnectListener.onNetDisconnected();
            }
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mINetInterface != null) {
                mINetInterface.asBinder().unlinkToDeath(mDeathRecipient, 0);
                mINetInterface = null;
            }
            if (mNetConnectListener!=null){
                mNetConnectListener.onNetDisconnected();
            }
            retryBindService();
        }
    };

    public void init(Context context,NetConnectListener netConnectListener) {
        if (context == null) {
            Log.e(TAG, "init error context is null");
            return;
        }
        Log.d(TAG, "init() called with: context = [" + context + "]");
        this.mContext = context;
        this.mNetConnectListener = netConnectListener;
        this.mINetCallbackImpl = new INetCallbackImpl();
        mINetCallbackImpl.registerNetConnectListener(mNetConnectListener);
        bindNetService();
    }

    public void registerINetCallback(MessageCallback messageCallback) {
        if (mINetInterface == null) {
            Log.e(TAG, "mINetInterface is null");
            return;
        }
        Log.d(TAG, "registerINetCallback() called with: messageCallback = [" + messageCallback + "]");
        mINetCallbackImpl.addMessageCallback(messageCallback);
    }

    public void unregisterINetCallback(MessageCallback messageCallback) {
        if (mINetInterface == null) {
            Log.e(TAG, "mINetInterface is null");
            return;
        }
        Log.d(TAG, "unregisterINetCallback() called with: messageCallback = [" + messageCallback + "]");
        mINetCallbackImpl.removeMessageCallback(messageCallback);
    }

    /**
     * 发送消息
     *
     * @param destDevice 目标设备 {@link DeviceConstant}
     * @param destAppPackageName 目标包名
     * @param content 内容
     */
    public void sendMessage(String destDevice, String destAppPackageName, String content) {
        Log.d(TAG, "sendMessage() called with: destDevice = [" + destDevice + "], destAppPackageName = [" + destAppPackageName + "], content = [" + content + "]");
        if (mINetInterface == null) {
            Log.e(TAG, "mINetInterface is null");
            return;
        }
        try {
            mINetInterface.sendMessage(destDevice, destAppPackageName, content);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private void bindNetService() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(PACKAGE_NAME, SERVICE_NAME);
        intent.setComponent(componentName);
        boolean ret = mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "bindNetService: "+ret);
        if (!ret) {
            mIsBinding = true;
            retryBindService();
        }else {
            mIsBinding = false;
            mRetryCount = 0;
            stopTimer();
        }
    }

    private void retryBindService() {
        if (mIsBinding){
            return;
        }
        startTimer();
    }

    private void startTimer(){
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (mRetryCount<RETRY_COUNT){
                    mRetryCount++;
                    bindNetService();
                }
            }
        },0,1, TimeUnit.MINUTES);
    }

    private void stopTimer(){
        if (!scheduledExecutorService.isShutdown()){
            scheduledExecutorService.shutdown();
        }
    }


}