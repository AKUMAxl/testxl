package com.xl.testui.socket;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServiceMessager extends Thread{

    private final String TAG = ServiceMessager.class.getSimpleName();

    private ServerSocket mServiceSocket;
    private CopyOnWriteArrayList<Socket> mClientSockets = new CopyOnWriteArrayList<>();

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ServiceMessager(){
        try {
            this.mServiceSocket = new ServerSocket();
            mServiceSocket.setReuseAddress(true);
            mServiceSocket.bind(new InetSocketAddress(7200));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeService() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg() {

        new Thread(() -> {
            try {
                if (socket == null) {
                    return;
                }
                outputStream = socket.getOutputStream();
                outputStream.write("123".getBytes(StandardCharsets.UTF_8));
                outputStream.close();
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }

    @Override
    public void run() {
        super.run();
        while (true){
            try {
                Thread.sleep(50);
                socket = mServiceSocket.accept();

                byte[] bytes = socket.getInetAddress().getAddress();
                String add = new String(bytes,"UTF-8");
                Log.d(TAG,"address:"+add);
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                byte[] headerBuffer = new byte[]{};
                int ret = inputStream.read(headerBuffer);
                if (ret==-1){
                    Log.d(TAG,"read -1");
                    return;
                }
                StringBuffer sb = new StringBuffer();
                byte[] buffer = new byte[1024];
                int len = 0;

                while ((len = inputStream.read(buffer,0,buffer.length)) !=-1){
//                    inputStream.available()
                    sb.append(buffer);
                }
                Log.d(TAG,"data:"+sb.toString());

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
