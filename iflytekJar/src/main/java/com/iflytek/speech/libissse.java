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
	final public static String ISS_SE_PARAM_BEAM_INDEX_VALUE_FRONT_RIGHT	 = "1";					///< output the front right channel audio
	final public static String ISS_SE_PARAM_BEAM_INDEX_VALUE_REAR_LEFT		 = "2";					///< output the rear left channel audio
	final public static String ISS_SE_PARAM_BEAM_INDEX_VALUE_REAR_RIGHT		 = "3";					///< output the rear right channel audio
	final public static String ISS_SE_PARAM_BEAM_INDEX_VALUE_BEAM_REAR_MID	 = "4";					///< output the rear mid channel audio

	final public static String ISS_SE_PARAM_AEC_WORK_MODE					 = "aec_work_mode";		///< param AEC work mode param_id
	final public static String ISS_SE_PARAM_VALUE_AEC_OFF_MODE				 = "1";					///< AEC mode off
	final public static String ISS_SE_PARAM_VALUE_AEC_MUS_MODE				 = "2";					///< AEC mode on for music
	final public static String ISS_SE_PARAM_VALUE_AEC_TEL_MODE				 = "3";					///< AEC mode on for telephone
	
	final public static String ISS_SE_PARAM_INPUT_CHANNEL_NUM				 =	"input_channel_num";			///< input_channel_num
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_1		 =	"1";					///< input_channel_num:1
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_2		 =	"2";				///< input_channel_num:2
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_3		 =	"3";				///< input_channel_num:3
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_4		 =	"4";					///< input_channel_num:4
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_5		 =	"5";				///< input_channel_num:5
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_6		 =	"6";				///< input_channel_num:6
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_7		 =	"7";					///< input_channel_num:7
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_8		 =	"8";					///< input_channel_num:8
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_9		 =	"9";					///< input_channel_num:9
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_10		 =	"10";				///< input_channel_num:10
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_11		 =	"11";				///< input_channel_num:11
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_12		 =	"12";				///< input_channel_num:12
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_13		 =	"13";				///< input_channel_num:13
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_14		 =	"14";				///< input_channel_num:14
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_15		 =	"15";				///< input_channel_num:15
	final public static String ISS_SE_PARAM_VALUE_INPUT_CHANNEL_NUM_16		 =	"16";				///< input_channel_num:16


	final public static String ISS_SE_PARAM_CHANNEL_TRANSFORM_RULES		     =	"channel_transform_rules";		///< param channel_transform_rules
	final public static String ISS_SE_PARAM_CHANNEL_TRANSFORM_RULES_E111	 =	"0=0*1, 1=1*1, 2=0*0, 3=0*0, 4=2*0.5+4*0.5, 5=3*0.5+5*0.5";	///< channel_transform_rules:E111
	final public static String ISS_SE_PARAM_CHANNEL_TRANSFORM_RULES_E111_2	 =	"0=0*5, 1=1*5, 2=0*0, 3=0*0, 4=2*0.5+4*0.5, 5=3*0.5+5*0.5";	///< channel_transform_rules:E111_2
	final public static String ISS_SE_PARAM_CHANNEL_TRANSFORM_RULES_H5		 =	"0=0*1, 1=1*1, 2=0*0, 3=0*0, 4=2*1, 5=3*1";					///< channel_transform_rules:H5
	final public static String ISS_SE_PARAM_CHANNEL_TRANSFORM_RULES_HS5		 =	"0=2*1, 1=3*1, 2=0*0, 3=0*0, 4=0*1, 5=1*1";					///< channel_transform_rules:HS5


	final public static String ISS_SE_PARAM_AEC_REF_NUMBER					 = "aec_ref_number";	///< param AEC ref number param_id
	final public static String ISS_SE_PARAM_VALUE_AEC_REF_ZERO				 = "0";					///< no ref
	final public static String ISS_SE_PARAM_VALUE_AEC_REF_SGL				 = "1";					///< single ref
	final public static String ISS_SE_PARAM_VALUE_AEC_REF_DBL				 = "2";					///< double ref
	final public static String ISS_SE_PARAM_VALUE_AEC_REF_QUAD				 = "4";					///< four ref
	
	final public static String ISS_SE_PARAM_MIC_NUMBER						 = "mic_number";		///< param mic number param_id
	final public static String ISS_SE_PARAM_VALUE_MIC_SGL					 = "1";					///< single mic
	final public static String ISS_SE_PARAM_VALUE_MIC_DBL					 = "2";					///< double mic
	final public static String ISS_SE_PARAM_VALUE_MIC_QUAD					 = "4";					///< four mic

	final public static String ISS_SE_PARAM_WORK_MODE						 = "work_mode";			///< param work_mode param_id
	final public static String ISS_SE_PARAM_VALUE_VAD_ONLY_MODE				 = "0";					///< VAD only mode for XF6010SYE
	final public static String ISS_SE_PARAM_VALUE_FAKE_MAE_MODE				 = "1";					///< fake MAE mode for XF6010SYE
	final public static String ISS_SE_PARAM_VALUE_LSA_MODE					 = "2";					///< LSA mode
	final public static String ISS_SE_PARAM_VALUE_MAE_MODE					 = "3";					///< MAE mode
	final public static String ISS_SE_PARAM_VALUE_MAB_MODE					 = "4";					///< MAB mode
	final public static String ISS_SE_PARAM_VALUE_MAB_AND_MAE_MODE			 = "5";					///< MAB_AND_MAE mode
	final public static String ISS_SE_PARAM_VALUE_FOURVOICE_MAB_MODE		 = "6";					///< MAB mode for Four Voice
	final public static String ISS_SE_PARAM_VALUE_FOURVOICE_MAE_MODE		 = "7";					///< MAE mode for Four Voice
	final public static String ISS_SE_PARAM_VALUE_FOURVOICE_MAB_AND_MAE_MODE = "8";					///< MAB_AND_MAE for Four Voice
	final public static String ISS_SE_PARAM_VALUE_FOURVOICE_FAKE_LSA_MODE	 = "9";					///< Fake LSA for Four Voice  No used, only can open in cfg file

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

	final public static String ISS_SE_PARAM_NEED_REF_OUT					 = "need_ref_audio";	///< param need_ref_audio param_id
	final public static String ISS_SE_PARAM_VALUE_OUTPUT_WITHOUT_REF		 = "0";					///< output audio without ref
	final public static String ISS_SE_PARAM_VALUE_OUTPUT_WITH_REF			 = "1";					///< output audio with ref

	final public static String ISS_SE_PARAM_MIC_IN_GAIN						 = "mic_gain";			///< gain of mic
	final public static String ISS_SE_PARAM_REF_IN_GAIN						 = "ref_gain";			///< gain of ref
	final public static String ISS_SE_PARAM_OUT_GAIN						 = "out_gain";			///< gain of audio out
	final public static String ISS_SE_PARAM_OUT_GAIN_FOR_VW					 = "out_vw_gain";		///< gain of audio out for vw
	final public static String ISS_SE_PARAM_OUT_GAIN_FOR_SR					 = "out_sr_gain";		///< gain of audio out for sr


	
	public static native int setMachineCode(String code);
	
	public static native int create(NativeHandle nativeHandle, String resPath);
	
	public static native int destroy(NativeHandle nativeHandle);
	
	public static native int process(NativeHandle nativeHandle, byte[] bufIn, int samplesIn, byte[] bufOut, int[] paramOut);
	
	public static native int setParam(NativeHandle nativeHandle, String param, String szParamValue);

	public static native int getPowerAndIndex(NativeHandle nativeHandle, int index, int[] wkIndex, float[] paramOut, int numCal);
	public static native int reset(NativeHandle nativeHandle);
	public static native int setLogCfgParam(int nParamID, String szParamValue);
	
}
