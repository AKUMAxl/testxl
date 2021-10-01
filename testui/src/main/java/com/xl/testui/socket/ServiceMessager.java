package com.xl.testui.socket;

import static com.xl.testui.socket.DeviceConstant.HW_HOST;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xl.testui.bean.Device;
import com.xl.testui.bean.MessageBean;
import com.xl.testui.util.DeviceConfigUtil;
import com.xl.testui.util.ThreadPoolUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceMessager extends BaseMessager {

    private final String TAG = ServiceMessager.class.getSimpleName();

    private ServerSocket mServiceSocket;
    private ConcurrentHashMap<String, Socket> mClientSocketMap = new ConcurrentHashMap<>();
    private boolean mRunning = true;


    public ServiceMessager() {
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
            for (String key : mClientSocketMap.keySet()) {
                Socket socket = mClientSocketMap.get(key);
                if (socket != null) {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    socket.close();
                }
            }
            mClientSocketMap.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClient(String clientName) {
        try {
            if (mClientSocketMap.containsKey(clientName)) {
                Socket socket = mClientSocketMap.get(clientName);
                if (socket != null) {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    socket.close();
                }
                mClientSocketMap.remove(clientName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String jsonStr, String clientName) {
        ThreadPoolUtil.getInstance().execute(() -> {
            try {
                if (!TextUtils.isEmpty(clientName)) {
                    if (mClientSocketMap.containsKey(clientName)) {
                        Socket socket = mClientSocketMap.get(clientName);
                        if (socket == null) {
                            return;
                        }
                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write(jsonStr.getBytes(StandardCharsets.UTF_8));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    public void run() {
        Log.d(TAG, "init service socket and accept client socket start");
        super.run();
        try {
            mServiceSocket = new ServerSocket();
            mServiceSocket.setReuseAddress(true);
            mServiceSocket.bind(new InetSocketAddress(PORT));
//            设置accept超时时间
            mServiceSocket.setSoTimeout(ACCEPT_TIMEOUT);
            while (mRunning) {
                Socket clientSocket = mServiceSocket.accept();
                String clientIpAddress = clientSocket.getInetAddress().getHostAddress();
                InetAddress inetAddress = clientSocket.getLocalAddress();
                byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
                Log.d(TAG, "current accept client ip mac:" + mac);
                Log.d(TAG, "current accept client ip mac:" + macBytes2String(mac));
                byte[] macAddress = clientSocket.getInetAddress().getAddress();
                byte[] localMacAddress = clientSocket.getLocalAddress().getAddress();
                Log.d(TAG, "current accept client ip address:" + clientIpAddress);
                String clientMacAddress = macBytes2String(macAddress);
                Log.d(TAG, "current accept client mac address:" + clientMacAddress);
                Log.d(TAG, "current accept client mac address:" + macAddress);
                String clientLocalMacAddress = macBytes2String(localMacAddress);
                Log.d(TAG, "current accept client local mac address:" + clientLocalMacAddress);
                Log.d(TAG, "current accept client local mac address:" + localMacAddress);
                Log.d(TAG, "current accept client getLocalSocketAddress:" + clientSocket.getLocalSocketAddress().toString());
                Log.d(TAG, "current accept client getRemoteSocketAddress:" + clientSocket.getRemoteSocketAddress().toString());
                if (mClientSocketMap.containsKey(clientMacAddress)) {
                    removeClient(clientMacAddress);
                }
                mClientSocketMap.put(clientMacAddress, clientSocket);
                ThreadPoolUtil.getInstance().execute(new ReceiveMsgRunnable(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class ReceiveMsgRunnable implements Runnable {

        private Socket socket;
        private InputStream inputStream;

        public ReceiveMsgRunnable(Socket socket) {
            try {
                this.socket = socket;
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (mRunning) {
                    sleep(500);

                    byte[] receiveBytes = new byte[inputStream.available()];
                    int length = inputStream.read(receiveBytes);
                    String data;
//                    if (length<=BUFFER_SIZE){
//                        data = new String(receiveBytes, 0, length, StandardCharsets.UTF_8);
//                    }else {
//                        StringBuffer sb = new StringBuffer();
//                        int len = 0;
//                        while ((len = inputStream.read(receiveBytes, 0, receiveBytes.length)) != -1) {
//                            sb.append(receiveBytes);
//                        }
//                        data = sb.toString();
//                    }
                    data = new String(receiveBytes, 0, length, Charset.defaultCharset());
                    if (TextUtils.isEmpty(data)){
                        continue;
                    }
                    Log.d(TAG, "receive data:"+data);
                    Gson gson = new Gson();
                    Type type = new TypeToken<MessageBean<Object>>() {
                    }.getType();
                    MessageBean<Object> messageBean = gson.fromJson(data, type);
                    if (messageBean.getType()==MessageBean.TYPE_DEVICE_INFO){
                        Type t = new TypeToken<MessageBean<Device>>(){}.getType();
                        MessageBean<Device> devicesMessage = gson.fromJson(data,t);
                        String clientName = devicesMessage.getSenderName();
                        mClientSocketMap.put(clientName,socket);
                    }else {
                        if (!DeviceConfigUtil.isSocketService(messageBean.getReceiverName())){
                            sendMsg(data,messageBean.getReceiverName());
                        }else {
                            if (!TextUtils.isEmpty(data)) {
                                callbackMessage(data);
                            }
                        }
                    }

                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
