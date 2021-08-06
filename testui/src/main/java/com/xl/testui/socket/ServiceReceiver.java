package com.xl.testui.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServiceReceiver extends Thread{

    private ServerSocket mServiceSocket;
    private CopyOnWriteArrayList<Socket> mClientSockets = new CopyOnWriteArrayList<>();

    public ServiceReceiver(ServerSocket serviceSocket){
        this.mServiceSocket = serviceSocket;
    };

    @Override
    public void run() {
        super.run();
        while (true){
            try {
                Thread.sleep(50);
                Socket socket = mServiceSocket.accept();

//                socket.getInetAddress().getAddress();

                InputStream inputStream = socket.getInputStream();
                byte[] headerBuffer = new byte[]{};
                int ret = inputStream.read(headerBuffer);
                if (ret==-1){
                    return;
                }
                StringBuffer sb = new StringBuffer();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer,0,buffer.length)) !=-1){
                    sb.append(buffer);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
