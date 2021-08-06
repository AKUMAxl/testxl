package com.xl.testui.socket;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.xl.testui.R;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SocketTestManager {


    private Context mContext;
    private WindowManager mWindowManager;
    private View mRootView;

    private ServerSocket serverSocket;


    private SocketTestManager() {
    }

    private static class SingletonInstance {
        private static final SocketTestManager INSTANCE = new SocketTestManager();
    }

    public static SocketTestManager getInstance() {
        return SingletonInstance.INSTANCE;
    }


    public void init(Context context, WindowManager windowManager) {
        this.mContext = context;
        this.mWindowManager = windowManager;
        initView();
    }

    private void initView() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2038,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.END;
        params.y = 260;
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.test_socket_window, null);
        mRootView.findViewById(R.id.test_socket_back).setOnClickListener(v -> {
            mWindowManager.removeView(mRootView);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.xl.testui","com.xl.testui.MainActivity"));
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
        mRootView.findViewById(R.id.test_socket_start_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });
        mRootView.findViewById(R.id.test_socket_start_client).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startClient();
            }
        });
        mRootView.findViewById(R.id.test_socket_service_send_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceSendMsg();
            }
        });
        mRootView.findViewById(R.id.test_socket_client_send_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientSendMsg();
            }
        });
    }

    /**
     * Socket是TCP客户端API，通常用于connect到远程主机。
     * ServerSocket是TCP服务器API，通常来自客户端套接字的accept连接。
     * DatagramSocket是UDP端点API，用于send和receive datagram packets 。
     * MulticastSocket是处理多播组时使用的DatagramSocket的子类。
     *
     */

    private void startService(){

        try {
            serverSocket = new ServerSocket();
            ServiceReceiver serviceReceiver = new ServiceReceiver(serverSocket);
            serviceReceiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startClient(){
        Socket socket = null;
        try {
            socket = new Socket("10.10.76.40",7200);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(123);
            outputStream.close();
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void serviceSendMsg(){

    }

    private void clientSendMsg(){

    }

}