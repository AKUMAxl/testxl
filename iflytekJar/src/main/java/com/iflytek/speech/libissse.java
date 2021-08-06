package com.iflytek.speech;

import android.util.Log;

public class libissse {
	private static final String tag = "libissse";
	private static libissse instance = null;

	static {
		System.loadLibrary("issse");
		// if use chip encode, use follow line
		System.loadLibrary("issauth");
		System.loadLibrary("lesl");
		System.loadLibrary("hardinfo");
		// if use chip encode, use follow line
		// System.loadLibrary("isschipauth");
	}

	final public static int ISS_SE_FRAME_SHIFT 								 = 256;					///< samples of seopt processing once
	final public static int ISS_SE_FRAME_SHIFT_48K 							 = 768;					///< samples of seopt processing once,for 48k
	
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
	
	final public static String ISS_SE_PARAM_BEAM_INDEX						 = "beam_index";		///< param beamIndex id
	final public static String ISS_SE_PARAM_BEAM_INDEX_VALUE_FRONT_LEFT		 = "0";					///< output the front left channel audio
	final public static String ISS_SE_PARAM_BEAM_INDEX_VALUE_FRONT_RIGTH	 = "1";					///< output the front right channel audio
	final public static String ISS_SE_PARAM_BEAM_INDEX_VALUE_REAR_LEFT		 = "2";					///< output the rear left channel audio
	final public static String ISS_SE_PARAM_BEAM_INDEX_VALUE_REAR_RIGTH		 = "3";					///< output the rear right channel audio
	final public static String ISS_SE_PARAM_BEAM_INDEX_VALUE_BEAM_REAR_MID	 = "4";					///< output the rear mid channel audio

	final public static String ISS_SE_PARAM_AEC_WORK_MODE					 = "aec_work_mode";		///< param AEC work mode param_id
	final public static String ISS_SE_PARAM_VALUE_AEC_OFF_MODE				 = "1";					///< AEC mode off
	final public static String ISS_SE_PARAM_VALUE_AEC_MUS_MODE				 = "2";					///< AEC mode on for music
	final public static String ISS_SE_PARAM_VALUE_AEC_TEL_MODE				 = "3";					///< AEC mode on for telephone

	final public static String ISS_SE_PARAM_AEC_REF_NUMBER					 = "aec_ref_number";	///< param AEC ref number param_id
	final public static String ISS_SE_PARAM_VALUE_AEC_REF_ZERO				 = "0";					///< no ref
	final public static String ISS_SE_PARAM_VALUE_AEC_REF_SGL				 = "1";					///< single ref
	final public static String ISS_SE_PARAM_VALUE_AEC_REF_DBL				 = "2";					///< double ref
	final public static String ISS_SE_PARAM_VALUE_AEC_REF_QUAD				 = "4";					///< four ref
	
	final public static String ISS_SE_PARAM_MIC_NUMBER						 = "mic_number";		///< param mic number param_id
	final public static String ISS_SE_PARAM_VALUE_MIC_SGL					 = "1";					///< single mic
	final public static String ISS_SE_PARAM_VALUE_MIC_DBL					 = "2";					///< double mic
	final public static String ISS_SE_PARAM_VALUE_MIC_QUAD					 = "4";					///< four mic
	
	final public static String ISS_SE_PARAM_CHANNEL_TRANSFORM				 = "channel_transform";	///< param channels_transform param_id
	final public static String ISS_SE_PARAM_CHANNEL_TRANSFORM_DISABLE		 = "0";					///< disable
	final public static String ISS_SE_PARAM_CHANNEL_TRANSFORM_ENABLE		 = "1";					///< enable
	
	final public static String ISS_SE_PARAM_CHANNEL_TRANSFORM_RULES			 = "channel_transform_rules";	///< param channels_transform_rules param_id
	// channel_transform_rules e.g. "InputChannelMaxNum=8, 0=0*1, 1=1*1, 2=0*0, 3=0*0, 4=4*0.5+6*0.5, 5=5*0.5+7*0.5" 0~3 for mic; 4~5 for ref;

	final public static String ISS_SE_PARAM_WORK_MODE						 = "work_mode";			///< param work_mode param_id
	final public static String ISS_SE_PARAM_VALUE_MAB_MODE					 = "4";					///< MAB mode
	final public static String ISS_SE_PARAM_VALUE_MAB_AND_MAE_MODE			 = "5";					///< MAB_AND_MAE mode

	final public static String ISS_SE_PARAM_SAMPLING_DIGIT					 = "sampling_digit";	///< sampling digit of audio
	final public static String ISS_SE_PARAM_VALUE_SAMPLING_DIGIT_16BIT		 = "1";					///< 16 bit audio
	final public static String ISS_SE_PARAM_VALUE_SAMPLING_DIGIT_32FLOAT	 = "2";					///< 32 bit float audio
	final public static String ISS_SE_PARAM_VALUE_SAMPLING_DIGIT_32INT		 = "3";					///< 32 bit int audio

	final public static String ISS_SE_PARAM_OUTPUT_MODE					     = "output_mode";		///< param output_mode param_id
	final public static String ISS_SE_PARAM_VALUE_OUTPUT_MODE_MIX			 = "0";					///< mix output_mode
	final public static String ISS_SE_PARAM_VALUE_OUTPUT_MODE_MVW			 = "1";					///< mvw output_mode

	final public static String ISS_SE_PARAM_ARITHMETIC_TYPE					 = "arithmetic_type";	///< param arithmetic_type param_id
	final public static String ISS_SE_PARAM_VALUE_CLOSE_NN					 = "0";					///< close NN arithmetic
	final public static String ISS_SE_PARAM_VALUE_OPEN_NN					 = "1";					///< open NN arithmetic

	final public static String ISS_SE_PARAM_MIC_LAYOUT						 = "mic_layout";		///< param mic_layout param_id
	final public static String ISS_SE_PARAM_VALUE_LAYOUT_A0					 = "0";					///< A0 layout
	final public static String ISS_SE_PARAM_VALUE_LAYOUT_PIC				 = "1";					///< PIC layout

	final public static String ISS_SE_PARAM_NEED_REF_OUT					 = "need_ref_audio";		///< param need_ref_audio param_id
	final public static String ISS_SE_PARAM_VALUE_OUTPUT_WITHOUT_REF		 = "0";				///< output audio without ref
	final public static String ISS_SE_PARAM_VALUE_OUTPUT_WITH_REF			 = "1";				///< output audio with ref

	//set carplay frequency response 
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_100               = "140";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_200               = "141";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_300               = "142";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_400               = "143";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_500               = "144";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_600               = "145";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_700               = "146";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_800               = "147";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_900               = "148";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_1000              = "149";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_1200              = "150";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_1400              = "151";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_1600              = "152";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_1800              = "153";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_2000              = "154";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_2500              = "155";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_3000              = "156";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_3500              = "157";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_4000              = "158";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_5000              = "159";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_6000              = "160";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_7000              = "161";
	final public static String ISS_SE_PARAM_FRE_ADJUST_NEW_8000              = "162";
	
	final public static String ISS_SE_PARAM_M_CN_FLAG                        = "164";  
    final public static String ISS_SE_PARAM_NARROW_BAND_MODE                 = "165";	
	
	final public static String ISS_SE_PARAM_MIC_IN_GAIN						 = "mic_gain";			///< gain of mic
	final public static String ISS_SE_PARAM_REF_IN_GAIN						 = "ref_gain";			///< gain of ref
	final public static String ISS_SE_PARAM_OUT_GAIN						 = "out_gain";			///< gain of audio out
	final public static String ISS_SE_PARAM_OUT_GAIN_FOR_VW					 = "out_vw_gain";		///< gain of audio out for vw
	final public static String ISS_SE_PARAM_OUT_GAIN_FOR_SR					 = "out_sr_gain";		///< gain of audio out for sr
	
	final public static String ISS_SE_PARAM_LSA_EMASK						 = "lsa_emask";			///< lsa echo
	final public static String ISS_SE_PARAM_LSA_NMASK						 = "lsa_nmask";			///< lsa noise
	
	final public static String ISS_SE_PARAM_REF_DELAY						 = "ref_delay";			///< param ref delay
	final public static String ISS_SE_PARAM_REF_DELAY_VALUE_10ms			 = "10";				///< ref delay 10ms
	final public static String ISS_SE_PARAM_REF_DELAY_VALUE_20ms			 = "20";				///< ref delay 20ms
	final public static String ISS_SE_PARAM_REF_DELAY_VALUE_100ms			 = "100";				///< ref delay 100ms
	final public static String ISS_SE_PARAM_REF_DELAY_VALUE_180ms			 = "180";				///< ref delay 180ms
	
	final public static String ISS_SE_PARAM_AEC_TTS							 = "aec_tts";			///< param is open AEC_TTS
	final public static String ISS_SE_PARAM_AEC_TTS_OFF						 = "0";					///< AEC_TTS OFF
	final public static String ISS_SE_PARAM_AEC_TTS_ON						 = "1";					///< AEC_TTS ON
	
	final public static String ISS_SE_PARAM_AEC_TTSDATA						 = "aec_ttsdata";		///< param AEC_TTS data
	
	final public static String ISS_SE_PARAM_TMP_LOG_DIR						 = "tmp_log_dir";		///< pcm log dir
	
	final public static String ISS_SE_PARAM_NN_MICDIST						 = "nn_mic_dist";		///< mic dist(millimeter), range 20mm to 1000mm
																									///< for ISS_SE_PARAM_VALUE_MAB_NN_FOR_Nmm, ISS_SE_PARAM_VALUE_MAE_NN_FOR_Nmm, ISS_SE_PARAM_VALUE_MAE_MAB_NN_FOR_Nmm
	final public static String ISS_SE_PARAM_NN_MICDIST_80mm					 = "80";				///< mic dist 80mm

	public static native int setMachineCode(String code);
	
	public static native int create(NativeHandle nativeHandle, String resPath);
	
	public static native int destroy(NativeHandle nativeHandle);
	
	public static native int process(NativeHandle nativeHandle, byte[] bufIn, int samplesIn, byte[] bufOut, int[] paramOut);
	
	public static native int setParam(NativeHandle nativeHandle, String param, String szParamValue);

	public static native int getPowerAndIndex(NativeHandle nativeHandle, int index, int[] wkIndex, float[] paramOut, int numCal);
	public static native int reset(NativeHandle nativeHandle);
	public static native int setLogCfgParam(int nParamID, String szParamValue);
	
	public static native int setmicstate(NativeHandle nativeHandle);
	public static native int getmicstate(NativeHandle nativeHandle, int[] nstate);
	
}
