package com.iflytek.speech;

import com.iflytek.speech.NativeHandle;
import com.iflytek.speech.tts.ITtsListener;

public class libisstts {
	static {
		System.loadLibrary("issxtts");
		
		System.loadLibrary("issauth");
		System.loadLibrary("hardinfo");
		System.loadLibrary("lesl");
		System.loadLibrary("websockets");
	}
	public static native synchronized int initRes(String resDir, int mode);

	public static native synchronized int unInitRes();

	public static native synchronized void create(NativeHandle nativeHandle, ITtsListener iTtsListener);
	
	public static native synchronized void destroy(NativeHandle nativeHandle);

	public static native synchronized void setParam(NativeHandle nativeHandle, int param, int value);
	
	public static native synchronized void setParamEx(NativeHandle nativeHandle, int param, String strValue);

	public static native synchronized void start(NativeHandle nativeHandle, String text);

	public static native synchronized void getAudioData(NativeHandle nativeHandle, byte[] audioBuffer, int nBytes, int[] outBytes);

	public static native synchronized void stop(NativeHandle nativeHandle);
	
	public static native synchronized int setMachineCode(String code);
}
