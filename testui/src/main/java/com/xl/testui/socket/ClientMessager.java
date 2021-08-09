package com.xl.testui.socket;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientMessager extends Thread {

    private final String TAG = ClientMessager.class.getSimpleName();

    private Socket socket;

    private InputStream inputStream;
    private OutputStream outputStream;

    public ClientMessager() {

    }


    public void closeClient() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg() {
        if (socket == null) {
            return;
        }
        new Thread(() -> {
            try {
                outputStream = socket.getOutputStream();
                outputStream.write(123);
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
        try {
            socket = new Socket("10.10.76.160", 7200);
            inputStream = socket.getInputStream();
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
