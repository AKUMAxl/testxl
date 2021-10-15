package com.xl.testui.socket;

import android.util.Log;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public abstract class BaseMessager extends Thread {

    private final String TAG = BaseMessager.class.getSimpleName();

    public static final String MSG_HEADER = "message_header";

    public static final int BUFFER_SIZE = 1024;
    public static final int PORT = 37000;
    public static final int ACCEPT_TIMEOUT = 1000 * 60 * 10;
    // 数据长度占用字节数 LONG型为8
    public static final int DATA_LENGTH_LENGTH = 8;
    public MessageCallback mMessageCallback;

    private String mData;
    private Long mDataLength;
    private StringBuilder mStringBuilder;

    public BaseMessager() {
        mStringBuilder = new StringBuilder();
    }

    public void registerMessageCallback(MessageCallback messageCallback) {
        this.mMessageCallback = messageCallback;
    }

    public void unregisterMessageCallback() {
        this.mMessageCallback = null;
    }

    public void sendMsg(String jsonStr, String destName) {
        Log.d(TAG, "sendMsg() called with: jsonStr = [" + jsonStr + "], destName = [" + destName + "]");
        byte[] header = MSG_HEADER.getBytes(StandardCharsets.UTF_8);
        byte[] data = jsonStr.getBytes(StandardCharsets.UTF_8);
        long length = data.length;
        byte[] dataLength = long2bytes(length);
        byte[] allData = new byte[header.length + dataLength.length + data.length];
        Log.d(TAG, "header length: " + header.length);
        Log.d(TAG, "dataLength length: " + dataLength.length);
        System.arraycopy(header, 0, allData, 0, header.length);
        System.arraycopy(dataLength, 0, allData, header.length, dataLength.length);
        System.arraycopy(data, 0, allData, header.length + dataLength.length, data.length);
        sendMsg(allData, destName);
    }

    abstract void sendMsg(byte[] data, String destDeviceNam);

    public void handleReceiveMsg(byte[] receiveBytes) {

        if (isHeaderOfData(receiveBytes)) {
            // 数据包以常量MSG_HEADER开头
            handleMessageBeginOfHeader(receiveBytes);
        } else {
            // 数据包不是以常量MSG_HEADER开头,读取拼接数据
            handleMessageNotBeginOfHeader(receiveBytes);
        }
    }

    /**
     * 处理以MSG_HEADER开头的数据包
     *
     * @return 一个数据包内剩余的不完整数据 如果没有剩余有效数据  return null
     */
    private byte[] handleMessageBeginOfHeader(byte[] receiveBytes){
        byte[] surplusBytes = receiveBytes;
        while (isHeaderOfData(surplusBytes)){

        }
        byte[] lengthBytes = new byte[DATA_LENGTH_LENGTH];
        System.arraycopy(receiveBytes, MSG_HEADER.length(),lengthBytes,0,lengthBytes.length);
        try {
            mDataLength = bytes2long(lengthBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int dataLength = receiveBytes.length-MSG_HEADER.length()-DATA_LENGTH_LENGTH;
        if (dataLength<BUFFER_SIZE-MSG_HEADER.length()-DATA_LENGTH_LENGTH){
            // 数据未填满BUFFER 输出本次数据 无需合并数据包,如果数据包内存在下一条数据，需要处理
            byte[] dataBytes = new byte[dataLength];
            System.arraycopy(receiveBytes,MSG_HEADER.length()+DATA_LENGTH_LENGTH,dataBytes,0,dataLength);

        }else {
            // 数据超过BUFFER大小，需要从下一个BUFFER继续读数据
            byte[] dataBytes = new byte[BUFFER_SIZE - MSG_HEADER.length() - DATA_LENGTH_LENGTH];
            mStringBuilder.append(dataBytes);
        }
        return null;
    }

    /**
     * 处理不是以MSG_HEADER开头的数据包
     *
     * @return 一个数据包内剩余的不完整数据 如果没有剩余有效数据  return null
     */
    private byte[] handleMessageNotBeginOfHeader(byte[] receiveBytes){
        Long surplusDataLength = mDataLength-mStringBuilder.length();
        if (surplusDataLength<BUFFER_SIZE-MSG_HEADER.length()-DATA_LENGTH_LENGTH){
            byte[] dataBytes = new byte[Math.toIntExact(surplusDataLength)];
            mStringBuilder.append(receiveBytes);
        }
        return null;
    }

    private boolean isHeaderOfData(byte[] data){
        String header = new String(data, 0, MSG_HEADER.length(), Charset.defaultCharset());
        Log.d(TAG, "isHeaderOfData: header:"+header);
        boolean isHeaderOfData = Objects.equals(header, MSG_HEADER);
        Log.d(TAG, "isHeaderOfData: isHeaderOfData:"+header);
        return isHeaderOfData;
    }

    private void callbackMessage(){
        String data = mStringBuilder.toString();
        mStringBuilder = null;
        mStringBuilder = new StringBuilder();
        mDataLength = 0L;
        if (mMessageCallback!=null){
            mMessageCallback.receiveMsg(data);
        }
    }


    public void callbackMessage(String content) {
        if (mMessageCallback != null) {
            mMessageCallback.receiveMsg(content);
        }
    }

    public String macBytes2String(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(":").append(Integer.toHexString(0xFF & bytes[i]));
        }
        return sb.substring(1);
    }

    static long bytes2long(byte[] bs) throws Exception {
        int bytes = bs.length;
        if (bytes > 1) {
            if ((bytes % 2) != 0 || bytes > 8) {
                throw new Exception("not support");
            }
        }
        switch (bytes) {
            case 0:
                return 0;
            case 1:
                return (long) ((bs[0] & 0xff));
            case 2:
                return (long) ((bs[0] & 0xff) << 8 | (bs[1] & 0xff));
            case 4:
                return (long) ((bs[0] & 0xffL) << 24 | (bs[1] & 0xffL) << 16 | (bs[2] & 0xffL) << 8 | (bs[3] & 0xffL));
            case 8:
                return (long) ((bs[0] & 0xffL) << 56 | (bs[1] & 0xffL) << 48 | (bs[2] & 0xffL) << 40 | (bs[3] & 0xffL) << 32 |
                        (bs[4] & 0xffL) << 24 | (bs[5] & 0xffL) << 16 | (bs[6] & 0xffL) << 8 | (bs[7] & 0xffL));
            default:
                throw new Exception("not support");
        }
    }

    public byte[] long2bytes(long data) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) (data >> 8 & 0xff);
        bytes[2] = (byte) (data >> 16 & 0xff);
        bytes[3] = (byte) (data >> 24 & 0xff);
        bytes[4] = (byte) (data >> 32 & 0xff);
        bytes[5] = (byte) (data >> 40 & 0xff);
        bytes[6] = (byte) (data >> 48 & 0xff);
        bytes[7] = (byte) (data >> 56 & 0xff);
        return bytes;
    }
}
