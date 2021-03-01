package com.iflytek.speech;

import android.util.Log;

import com.iflytek.speech.mvw.IMVWListener;

public class libissmvw {
	private static final String tag = "libissmvw";
	private static libissmvw instance = null;

	static {
		System.loadLibrary("websockets");
        System.loadLibrary("w_ivw");
		System.loadLibrary("SpWord");
		System.loadLibrary("issmvw");
		System.loadLibrary("issauth");
	}
	
	// Log level.According to the requirement to combine mask
	final public static int ISS_SE_PARAM_LOG_LEVEL                           = (0X00000506);
	final public static String ISS_SE_VOLUME_LOG_LEVEL_ALL                   = "-1";				///< all info
	final public static String ISS_SE_VOLUME_LOG_LEVEL_NONE			         = "0";					///< none
	final public static String ISS_SE_VOLUME_LOG_LEVEL_CRIT			         = "1";					///< critical info
	final public static String ISS_SE_VOLUME_LOG_LEVEL_ERROR			     = "2";					///< error info
	final public static String ISS_SE_VOLUME_LOG_LEVEL_WARNING		         = "4";					///< warnint info
	
	// Log output.According to the requirement to combine mask.
	final public static int ISS_SE_PARAM_LOG_OUTPUT    				         = (0X00000507);
	final public static String ISS_SE_VOLUME_LOG_OUTPUT_NONE				 = "0";	///< none
	final public static String ISS_SE_VOLUME_LOG_OUTPUT_FILE				 = "1";	///< file
	final public static String ISS_SE_VOLUME_LOG_OUTPUT_CONSOLE				 = "2";	///< console（except for android）
	final public static String ISS_SE_VOLUME_LOG_OUTPUT_DEBUGGER			 = "4";	///< debugger
	final public static String ISS_SE_VOLUME_LOG_OUTPUT_MSGBOX		    	 = "8";	///< message box
	
	// Log FileName
    final public static int ISS_SE_PARAM_LOG_FILE_NAME	                     = (0X00000508);
	
	//message
	final public static int	ISS_MVW_MSG_InitStatus							=  40000;  // init status
	final public static int	ISS_MVW_MSG_VolumeLevel							=  40001;	// volume output
	final public static int	ISS_MVW_MSG_Error								=  40002;  // error msg
	final public static int	ISS_MVW_MSG_Result								=  40003;	// wakeup result with direction
	final public static int	ISS_MVW_MSG_Timeout								=  40004;	// time out, disable default
	final public static int	ISS_MVW_MSG_VPR_Result							=  40008;	// voice print output
	final public static int	ISS_MVW_MSG_Only_Wakeup_Result					=  40011;	// wakeup result without direction
	
	public static native synchronized int create(NativeHandle nativeHandle,String resDir, IMVWListener iMvwListener);
    public static native synchronized int createEx(NativeHandle nativeHandle,String resDir, IMVWListener iMvwListener);

	public static native synchronized int destroy(NativeHandle nativeHandle);

	public static native synchronized int setThreshold(NativeHandle nativeHandle,int nMvwScene, int nMvwId,
			int threshold);
			
	public static native synchronized int setParam(NativeHandle nativeHandle,String szParam, String szParamValue);
	
	public static native synchronized int start(NativeHandle nativeHandle,int nMvwScene);
	public static native synchronized int addstartscene(NativeHandle nativeHandle,int nMvwScene);

	public static native synchronized int appendAudioData(NativeHandle nativeHandle,byte[] AudioBuffer,
			int nNumberOfByte);

	public static native synchronized int stop(NativeHandle nativeHandle);
	public static native synchronized int stopscene(NativeHandle nativeHandle,int nMvwScene);
	public static native synchronized int setMvwKeyWords(NativeHandle nativeHandle,int nMvwScene, String szWords);
	public static native synchronized int setMvwDefaultKeyWords(NativeHandle nativeHandle,int nMvwScene);
	public static native synchronized int setMvwLanguage(int nLangType);
	public static native synchronized boolean isCouldAppendAudioData();
    public static native synchronized int setIvwOptimizeUpload(NativeHandle nativeHandle, boolean bEnableFlag, int nMaxAudioCnt);
    public static native synchronized int setIvwOptimizeDownload(NativeHandle nativeHandle);
    public static native synchronized int vprStart(NativeHandle nativeHandle, int nMvwScene, int nVoiceId, int nMode);
    public static native synchronized int vprStop(NativeHandle nativeHandle, int nMvwScene, int nVoiceId, int nMode);
	public static native synchronized int vprSetThreshold(NativeHandle nativeHandle, int nMvwScene, double jvprthreshold);
	public static native synchronized int GetCacheAudioData(NativeHandle nativeHandle,int nDirection, int nStartBytes, 
    int nCopyNumber,  byte[] pOutBuffer,int[] pFrameGot);
	public static native synchronized int setLogCfgParam(int nParamID, String szParamValue);
}
