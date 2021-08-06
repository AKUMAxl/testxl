package com.iflytek.speech;

import android.util.Log;

import com.iflytek.speech.mvw.IMVWListener;

public class libissmvw {
	private static final String tag = "libissmvw";
	private static libissmvw instance = null;

	static {
		System.loadLibrary("websockets");
        System.loadLibrary("w_ivw");
		System.loadLibrary("issmvw");
		System.loadLibrary("issauth");
		System.loadLibrary("SpWord");
	}
	
	/// Please refer to the configuration files to check the scene ID information and wakeup word ID information.
	final public static int ISS_MVW_SCENE_GLOBAL = 1;           ///< Wakeup scene: global wakeup
	final public static int ISS_MVW_SCENE_CONFIRM = 2;          ///< Wakeup scene: confirm scene
	final public static int ISS_MVW_SCENE_SELECT = 4;           ///< Wakeup scene: select scene
	final public static int ISS_MVW_SCENE_ANSWER_CALL = 8 ;     ///< Wakeup scene: answer call scene
	final public static int ISS_MVW_SCENE_COMMON = 16;          ///< Wakeup scene: common scene
	final public static int ISS_MVW_SCENE_ONESHOT = 32;         ///< Wakeup scene: oneshot
	
	//MVW_LANG
	final public static int ISS_MVW_LANG_CHN = 0;
	final public static int ISS_MVW_LANG_ENG = 1;
	final public static int ISS_MVW_LANG_BUTT = 2;
	
	// Log level.According to the requirement to combine mask.
	final public static int ISS_MVW_PARAM_LOG_LEVEL =(0X00000506);
	final public static String ISS_MVW_VOLUME_LOG_LEVEL_ALL	="-1";	// all info
	final public static String ISS_MVW_VOLUME_LOG_LEVEL_NONE = "0";	// none
	final public static String ISS_MVW_VOLUME_LOG_LEVEL_CRIT ="1";	// critical info
	final public static String ISS_MVW_VOLUME_LOG_LEVEL_ERROR =	"2"	;// error info
	final public static String ISS_MVW_VOLUME_LOG_LEVEL_WARNING	 = "4";// warnint info

	// Log output.According to the requirement to combine mask.
	final public static int ISS_MVW_PARAM_LOG_OUTPUT =(0X00000507);
	final public static String ISS_MVW_VOLUME_LOG_OUTPUT_NONE ="0";	// none
	final public static String ISS_MVW_VOLUME_LOG_OUTPUT_FILE =	"1";	// file
	final public static String ISS_MVW_VOLUME_LOG_OUTPUT_CONSOLE = "2";	// console（except for android）
	final public static String ISS_MVW_VOLUME_LOG_OUTPUT_DEBUGGER = "4";	// debugger
	final public static String ISS_MVW_VOLUME_LOG_OUTPUT_MSGBOX	="8";	// message box

	// Log FileName
	final public static int ISS_MVW_PARAM_LOG_FILE_NAME =(0X00000508);
	
	final public static String ISS_MVW_PARAM_AEC = "mvw_enable_aec";
	final public static String ISS_MVW_PARAM_LSA = "mvw_enable_lsa";
	final public static String ISS_MVW_PARAM_TMP_LOG_DIR = "TmpLogDir" ;               ///< Set tmp log directory for debugging
	final public static String ISS_MVW_PARAM_THRESHOLD_LEVEL = 	"mvw_threshold_level";
	final public static String ISS_MUL_IVW_INPUT_NUM = "mul_ivw_input_num";

	final public static String ISS_MVW_PARAM_VALUE_ON = "on";                    ///< On
	final public static String ISS_MVW_PARAM_VALUE_OFF ="off";                 ///< Off
	
	//message
	final public static int ISS_MVW_MSG_InitStatus              =   40000;
	final public static int ISS_MVW_MSG_VolumeLevel             =   40001;
	final public static int ISS_MVW_MSG_Error                   =   40002;
	final public static int ISS_MVW_MSG_Result                  =   40003;
	final public static int ISS_MVW_MSG_Timeout                 =   40004;
	final public static int ISS_MVW_MSG_VPR_Result              =   40008;
	final public static int ISS_MVW_MSG_VAD_Rlt                 =   40010; // 0:VA_VAD_STATUS_NONE 1:VA_VAD_STATUS_BEGIN

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
	public static native synchronized int ResRollBack(NativeHandle nativeHandle);
	public static native synchronized int ResUpdate(NativeHandle nativeHandle);
	public static native synchronized String ResVersionQuery(NativeHandle nativeHandle);
	
	public static native synchronized int setMachineCode(String code);
	
	public static native synchronized int vprStart(NativeHandle nativeHandle, int nMvwScene, int nVoiceId, int nMode);
	public static native synchronized int vprStop(NativeHandle nativeHandle, int nMvwScene, int nVoiceId, int nMode);
	public static native synchronized int vprSetThreshold(NativeHandle nativeHandle, int nMvwScene, double jvprthreshold);
	public static native synchronized int GetCacheAudioData(NativeHandle nativeHandle,int nDirection, long nStartBytes, 
    int nCopyNumber,  byte[] pOutBuffer,int[] pFrameGot);
	public static native synchronized int setLogCfgParam(int nParamID, String szParamValue);
}
