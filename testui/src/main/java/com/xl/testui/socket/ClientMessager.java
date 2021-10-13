package com.xl.testui.socket;

import android.text.TextUtils;
import android.util.Log;

import com.xl.testui.util.ThreadPoolUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ClientMessager extends BaseMessager {

    private static final String HOST = "10.10.76.160";
    //    private static final String HOST = "10.10.76.116";
//    private static final String HOST = "172.20.4.3";

    private final String TAG = ClientMessager.class.getSimpleName();
    private String mHost;
    private Socket socket;

    private InputStream inputStream;
    private OutputStream outputStream;

    private boolean mRunning = true;

    public ClientMessager(String hostIp) {
        this.mHost = hostIp;
    }


    public void closeClient() {
        mRunning = false;
        try {
            if (socket != null) {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            }
            if (inputStream!=null){
                inputStream.close();
            }
            if (outputStream!=null){
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    void sendMsg(byte[] data, String destDeviceNam) {
        ThreadPoolUtil.getInstance().execute(() -> {
            try {
                if (outputStream == null) {
                    Log.e(TAG, "sendMsg: outputStream is null");
                    if (socket!=null){
                        outputStream = socket.getOutputStream();
                    }else {
                        Log.e(TAG, "sendMsg: socket is null");
                        return;
                    }
                }
                outputStream.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    public void run() {
        super.run();
        try {
            if (TextUtils.isEmpty(mHost)) {
                mHost = HOST;
            }
            socket = new Socket(mHost, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            while (mRunning) {
//                byte[] headerBuffer = new byte[]{};
//                int ret = inputStream.read(headerBuffer);
//                if (ret == -1) {
//                    return;
//                }
//                StringBuffer sb = new StringBuffer();
//                byte[] buffer = new byte[1024];
//                int len = 0;
//                while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
//                    sb.append(buffer);
//                }
                byte[] receiveBytes = new byte[inputStream.available()];
                int length = inputStream.read(receiveBytes);
                String data;
                data = new String(receiveBytes, receiveBytes.length>22?22:0, receiveBytes.length>22?length-22:length, Charset.defaultCharset());
                if (TextUtils.isEmpty(data)){
                    continue;
                }
                Log.d(TAG, "receive data:"+data);
                callbackMessage(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
