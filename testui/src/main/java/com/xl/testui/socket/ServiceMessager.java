package com.xl.testui.socket;

import android.text.TextUtils;
import android.util.Log;

import com.xl.testui.util.ThreadPoolUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    //    private CopyOnWriteArrayList<Socket> mClientSockets = new CopyOnWriteArrayList<>();
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

    public void removeClient(String macAddress) {
        try {
            if (mClientSocketMap.containsKey(macAddress)) {
                Socket socket = mClientSocketMap.get(macAddress);
                if (socket != null) {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    socket.close();
                }
                mClientSocketMap.remove(macAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String content,String destMacAddress) {
        ThreadPoolUtil.getInstance().execute(() -> {
            try {
                if (TextUtils.isEmpty(destMacAddress)){
                    for (String key : mClientSocketMap.keySet()) {

                        Socket socket = mClientSocketMap.get(key);
                        if (socket == null) {
                            continue;
                        }
                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
                    }
                }else {
                    if (mClientSocketMap.containsKey(destMacAddress)){
                        Socket socket = mClientSocketMap.get(destMacAddress);
                        if (socket==null){
                            return;
                        }
                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
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
                Log.d(TAG, "current accept client getLocalSocketAddress:" +clientSocket.getLocalSocketAddress().toString());
                Log.d(TAG, "current accept client getRemoteSocketAddress:" +clientSocket.getRemoteSocketAddress().toString());
                if (mClientSocketMap.containsKey(clientMacAddress)){
                    removeClient(clientMacAddress);
                }
                mClientSocketMap.put(clientMacAddress,clientSocket);
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
                    if (!TextUtils.isEmpty(data)) {
                        callbackMessage(data);
                    }
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
