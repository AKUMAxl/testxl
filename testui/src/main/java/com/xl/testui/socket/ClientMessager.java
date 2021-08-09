package com.xl.testui.socket;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientMessager extends Thread {

    private static final String HOST = "10.10.76.160";
//    private static final String HOST = "10.10.76.116";
//    private static final String HOST = "172.20.4.3";
    private static final int PORT = 10086;

    private final String TAG = ClientMessager.class.getSimpleName();

    private Socket socket;

    private InputStream inputStream;
    private OutputStream outputStream;

    public ClientMessager() {

    }


    public void closeClient() {
//        try {
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void sendMsg() {

        new Thread(() -> {
            try {
                if (outputStream == null) {
                    return;
                }

                outputStream.write("123".getBytes(StandardCharsets.UTF_8));
//                outputStream.close();
//                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }

    @Override
    public void run() {
        super.run();
        try {
            if (socket!=null){
                return;
            }
            socket = new Socket(HOST, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            while (true) {
                byte[] headerBuffer = new byte[]{};
                int ret = inputStream.read(headerBuffer);
                if (ret == -1) {
                    return;
                }
                StringBuffer sb = new StringBuffer();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                    sb.append(buffer);
                }
                Log.d(TAG, "client receiver:" + sb.toString());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
