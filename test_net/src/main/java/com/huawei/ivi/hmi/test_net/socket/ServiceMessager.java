package com.huawei.ivi.hmi.test_net.socket;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.MalformedJsonException;
import com.huawei.ivi.hmi.netbuslib.bean.Device;
import com.huawei.ivi.hmi.test_net.bean.MessageBean;
import com.huawei.ivi.hmi.test_net.util.ThreadPoolUtil;
import com.huawei.ivi.hmi.netbuslib.DeviceConstant;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ServiceMessager extends BaseMessager {

    private final String TAG = ServiceMessager.class.getSimpleName();

    private ServerSocket mServiceSocket;
    private ConcurrentHashMap<String, Device> mClientDeviceMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ReceiveMsgProviderThread> mReceiveMsgProviderThreads = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ReceiveMsgThread> mReceiveMsgThreads = new ConcurrentHashMap<>();
    private boolean mRunning = true;

    public ServiceMessager() {
        super();
    }

    public void closeService() {
        try {
            mRunning = false;
            if (mServiceSocket != null) {
                mServiceSocket.close();
                mServiceSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cleanClient() {
        try {
            for (String key : mClientDeviceMap.keySet()) {
                Device device = mClientDeviceMap.get(key);
                if (device==null){
                    return;
                }
                Socket socket = device.getSocket();
                if (socket != null) {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    socket.close();
                    socket = null;
                }
            }
            mClientDeviceMap.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClient(String clientName) {
        try {
            if (mClientDeviceMap.containsKey(clientName)) {
                Socket socket = mClientDeviceMap.get(clientName).getSocket();
                if (socket != null) {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    socket.close();
                    socket = null;
                }
                mClientDeviceMap.remove(clientName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void startServiceSocket(){
        Log.d(TAG, "init service socket and accept client socket start");
        mRunning = true;
        try {
//            mServiceSocket = new ServerSocket(PORT);
            mServiceSocket = new ServerSocket();
            mServiceSocket.setReuseAddress(true);
            mServiceSocket.bind(new InetSocketAddress(HOST,PORT));
            while (mRunning) {
                if (mServiceSocket.isClosed()){
                    Thread.sleep(500);
                    continue;
                }
                Socket clientSocket = mServiceSocket.accept();
                if (clientSocket != null) {
                    String ip = clientSocket.getInetAddress().toString().replace("/","");
                    Log.d(TAG, "ServiceSocket.accept():"+ip+" -- "+clientSocket.hashCode());
                    LinkedBlockingQueue<byte[]> receiveDataCacheQueue = new LinkedBlockingQueue<>();
                    ReceiveMsgProviderThread  receiveMsgProviderThread = new ReceiveMsgProviderThread(clientSocket,receiveDataCacheQueue);
                    mReceiveMsgProviderThreads.put(ip,receiveMsgProviderThread);
                    receiveMsgProviderThread.start();
                    ReceiveMsgThread receiveMsgThread = new ReceiveMsgThread(RECEIVE_THREAD+ip,clientSocket,receiveDataCacheQueue);
                    mReceiveMsgThreads.put(ip,receiveMsgThread);
                    receiveMsgThread.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            startServiceSocket();
            if (!Thread.interrupted()) {
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }


    @Override
    public void sendMsg(boolean isBeatMsg,byte[] data, String destDeviceNam) {
        Log.d(TAG, "sendMsg() called with: data = [" + data + "], destDeviceNam = [" + destDeviceNam + "]");
        try {
            if (!TextUtils.isEmpty(destDeviceNam)) {
                mClientDeviceMap.keySet();
                if (mClientDeviceMap.containsKey(destDeviceNam)) {
                    Device device = mClientDeviceMap.get(destDeviceNam);
                    if (device== null){
                        Log.e(TAG, "sendMsg: device is null");
                        return;
                    }
                    Socket socket = device.getSocket();
                    if (socket == null) {
                        Log.e(TAG, "sendMsg: socket is null");
                        return;
                    }
                    if (socket.isClosed()||!socket.isConnected()){
                        return;
                    }
                    Log.d(TAG, "sendMsg socket:" + destDeviceNam + " -- " + device.getP2pip()+ " -- "+socket.hashCode());
                    OutputStream outputStream = socket.getOutputStream();
                    if (outputStream == null) {
                        Log.e(TAG, "sendMsg: socket is null");
                        return;
                    }
                    outputStream.write(data);
                    outputStream.flush();
                    Log.d(TAG, "sendMsg() success");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveMsg(String receiveMsg,Socket socket) {
        if (TextUtils.isEmpty(receiveMsg)) {
            return;
        }
        Log.d(TAG, "receive data:" + receiveMsg);
        if (receiveMsg.startsWith(MSG_BEAT)) {
            String senderName = receiveMsg.substring(MSG_BEAT.length()).split(":")[0];
            sendMsg(true,MSG_BEAT_RECEIVE, senderName);
            return;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<MessageBean<Object>>() {
        }.getType();
        MessageBean<Object> messageBean = gson.fromJson(receiveMsg, type);
        if (messageBean.getType() == MessageBean.TYPE_DEVICE_INFO) {
            Device device = new Device();
            String clientName = messageBean.getSenderName();
            device.setName(clientName);
            device.setSocket(socket);
            device.setP2pip(socket.getInetAddress().toString().replace("/",""));
            Log.d(TAG, "push socket client:" + clientName +" device:"+device.toString() );
            mClientDeviceMap.put(clientName, device);
            List<Device> deviceList = new ArrayList<>();
            Set<String> keys = mClientDeviceMap.keySet();
            for (String key:keys) {
                Device device1 = mClientDeviceMap.get(key);
                deviceList.add(device1);
            }
            callbackClientChange(deviceList);
        } else {
            if (!messageBean.getReceiverName().equals(DeviceConstant.DEVICE_NAME.LANTU)) {
                sendMsg(false,receiveMsg, messageBean.getReceiverName());
            } else {
                callbackMessage(receiveMsg);
            }
        }
    }

    @Override
    public void handleException(Socket socket, LinkedBlockingQueue<byte[]> receiveDataCacheQueue) {
        Log.d(TAG, "handleException() called");
        String ip = socket.getInetAddress().toString().replace("/","");
        Log.d(TAG, "handleException() socket ip:"+ip);
        if (mReceiveMsgProviderThreads.containsKey(ip)){
            ReceiveMsgProviderThread receiveMsgProviderThread = mReceiveMsgProviderThreads.get(ip);
            if (receiveMsgProviderThread!=null){
                receiveMsgProviderThread.interrupt();
                receiveMsgProviderThread = null;
            }
        }
        if (mReceiveMsgThreads.containsKey(ip)){
            ReceiveMsgThread receiveMsgThread = mReceiveMsgThreads.get(ip);
            if (receiveMsgThread!=null){
                receiveMsgThread.interrupt();
                receiveMsgThread = null;
            }
        }
    }

    @Override
    public void beat() {

    }

    @Override
    public void checkBeat() {

    }
}
