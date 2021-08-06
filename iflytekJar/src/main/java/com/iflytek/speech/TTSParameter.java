package com.iflytek.speech;

/**
 * Created by PeiXie on 2016/9/27.
 */

public class TTSParameter {
    ///< The code page of the input TTS text
    final public static int ISS_TTS_CODEPAGE_GBK            = (936);
    final public static int ISS_TTS_CODEPAGE_UTF16LE        = (1200);
    final public static int ISS_TTS_CODEPAGE_UTF8           = (65001);

    final public static int ISS_TTS_RES_MODE_MEMORY         = (1);              /*!< TTS Res load type. load to memory */

    final public static int ISS_TTS_PARAM_SPEAKER           = (0x00000500);
    final public static int IvTTS_ROLE_JIAJIA               = (65180);          /**< JIAJIA	(female, Chinese) */
    final public static int IvTTS_ROLE_XIAOFENG             = (60030);          /**< XIAOFENG	(male, Chinese) */
    final public static int ivTTS_ROLE_SHIMENGHAN           = (67920);          /**< SHIMENGHAN	(female, Chinese) */
    final public static int ivTTS_ROLE_ZIXUAN               = (65510);          /**< ZIXUAN	(female, Chinese) */

/** \brief ParamID for ISSTTSSetParamEx.\n
 *  \When ParamID is ISS_TTS_PARAM_PERSONAL_SPEAKER, ParamValue must be a Json like: \n
 *  {"pvc_res_path": "/home/pvc/18818818888/df21526742251578537963d676ab.pvc", \n
 *   "user_id": "302001091041563114", \n
 *   "device_id": "123456qweasd"} \n
 */
    final public static int ISS_TTS_PARAM_PERSONAL_SPEAKER  = (0x00000501);
	
    final public static int ISS_TTS_PARAM_VOICE_SPEED       = (0x00000502);     /*!< Used to set the TTS voice speed */
    final public static int ISS_TTS_SPEED_MIN               = (-32768);         /*!< The slowest speed */
    final public static int ISS_TTS_SPEED_NORMAL_DEFAULT    = (0);              /*!< The normal speed by default */
    final public static int ISS_TTS_SPEED_MAX               = (+32767);         /*!< The fastest speed */

    final public static int ISS_TTS_PARAM_VOICE_PITCH       = (0x00000503);     /*!< Used to set the TTS voice pitch */
    final public static int ISS_TTS_PITCH_MIN               = (-32768);         /*!< The lowest pitch */
    final public static int ISS_TTS_PITCH_NORMAL_DEFAULT    = (0);              /*!< The normal pitch by default */
    final public static int ISS_TTS_PITCH_MAX               = (+32767);         /*!< The highest pitch */

    final public static int ISS_TTS_PARAM_VOLUME            = (0x00000504);     /*!< Used to set the TTS voice volume */
    final public static int ISS_TTS_VOLUME_MIN              = (-32768);         /*!< The minimum volume */
    final public static int ISS_TTS_VOLUME_NORMAL           = (0);              /*!< The normal volume */
    final public static int ISS_TTS_VOLUME_MAX_DEFAULT      = (+32767);         /*!< The maximum volume by default */

    final public static int ISS_TTS_PARAM_USERMODE          = (0x00000701);     /*!< user's mode */
    final public static int ISS_TTS_VOLUME_USE_NORMAL       = (0);              /*!< synthesize in the Mode of Normal */
    final public static int ISS_TTS_VOLUME_USE_NAVIGATION   = (1);              /*!< synthesize in the Mode of Navigation */
    final public static int ISS_TTS_VOLUME_USE_MOBILE       = (2);              /*!< synthesize in the Mode of Mobile */
    final public static int ISS_TTS_VOLUME_USE_EDUCATION    = (3);              /*!< synthesize in the Mode of Education */
    final public static int ISS_TTS_VOLUME_USE_TV           = (4);              /*!< synthesize in the Mode of TV */

    final public static int ISS_TTS_PARAM_VOLUME_INCREASE   = (0X00000505);     /*!< volume value increase */
    final public static int ISS_TTS_VOLUME_INCREASE_MIN     = (0);              /*!< minimized volume (default) */
    final public static int ISS_TTS_VOLUME_INCREASE_MAX     = (10);             /*!< maximized volume */

    final public static int ISS_TTS_PARAM_TMP_LOG_DIR       = (0X00000606);     /*!< Set tmp log directory for debugging */

    ///< Log level.According to the requirement to combine mask.
    final public static int     ISS_TTS_PARAM_LOG_LEVEL             = (0X00000506);
    final public static String  ISS_TTS_VOLUME_LOG_LEVEL_ALL        = "-1";     /*!< all info */
    final public static String  ISS_TTS_VOLUME_LOG_LEVEL_NONE       = "0";      /*!< none*/
    final public static String  ISS_TTS_VOLUME_LOG_LEVEL_CRIT       = "1";      /*!< critical info */
    final public static String  ISS_TTS_VOLUME_LOG_LEVEL_ERROR      = "2";      /*!< error info */
    final public static String  ISS_TTS_VOLUME_LOG_LEVEL_WARNING    = "4";      /*!< warnint info */

    ///< Log output.According to the requirement to combine mask.
    final public static int     ISS_TTS_PARAM_LOG_OUTPUT            = (0X00000507);
    final public static String  ISS_TTS_VOLUME_LOG_OUTPUT_NONE      = "0";      /*!< none */
    final public static String  ISS_TTS_VOLUME_LOG_OUTPUT_FILE      = "1";      /*!< file */
    final public static String  ISS_TTS_VOLUME_LOG_OUTPUT_CONSOLE   = "2";      /*!< console（except for android） */
    final public static String  ISS_TTS_VOLUME_LOG_OUTPUT_DEBUGGER  = "4";      /*!< debugger */
    final public static String  ISS_TTS_VOLUME_LOG_OUTPUT_MSGBOX    = "8";      /*!< message box */

    ///< Log FileName
    final public static int ISS_TTS_PARAM_LOG_FILE_NAME             = (0X00000508);

    ///< TTS Mode
    final public static int ISS_TTS_PARAM_MODE                      = (0X00000509);
    final public static int ISS_TTS_MODE_LOCAL                      = (0);      /*!< local synth (default) */
    final public static int ISS_TTS_MODE_CLOUD                      = (1);      /*!< cloud synth */
    final public static int ISS_TTS_MODE_MIX                        = (2);      /*!< local and cloud hybrid synth */

    ///< cloud synth response time out (default 800ms)
    final public static int ISS_TTS_PARAM_CLOUD_RESPONSE_TIMEOUT    = (0X00000510);
    
    ///< ISS_TTS_PARAM_NEEDTOSTOP INVALID!
    final public static int ISS_TTS_PARAM_NEEDTOSTOP                = (0X00000511);

    ///< tts cloud tts interrupted, local tts continue playing mode
    final public static int ISS_TTS_PARAM_CONTINUE_PLAYING_MODE         = (0X00000512);
    final public static int ISS_TTS_CLOUD_CP_MODE_NONE                  = (0);      /*!< tts cloud interrupted, tts end */
    final public static int ISS_TTS_CLOUD_CP_MODE_LC_SIMUL              = (1);      /*!< local and cloud synthing simultaneously */
    final public static int ISS_TTS_CLOUD_CP_MODE_PREVIOUS_PRO          = (2);      /*!< tts cloud interrupted, local tts continue playing from the previous progress */
    final public static int ISS_TTS_CLOUD_CP_MODE_NEXT_PRO              = (3);      /*!< tts cloud interrupted, local tts continue playing from the next progress */
    final public static int ISS_TTS_CLOUD_CP_MODE_CACHE_WHOLE_PROGRESS  = (4);      /*!< cache data of whole progress, otherwise abandon the data. tts cloud interrupted, local tts continue playing from the next progress */

    ///< message
    /** \brief  Local tts result;\n
    *          After the ISS_TTS_MSG_LocalResult message be sent, it means local tts data ready;\n
    *  \param  wParam     ISSErrID type:\n
    *                      ISS_SUCCESS: local tts daya ready.\n
    *  \param  lParam   "{\"rate\": 16000}"
    */
    final public static int ISS_TTS_MSG_LocalResult                 = (20000);
    /** \brief  Cloud tts result;\n
               After the ISS_TTS_MSG_CloudResult message be sent, if wParam type is ISS_SUCCESS, it means cloud tts data ready;\n
    *  \param  wParam     ISSErrID type:\n
    *                      ISS_SUCCESS: cloud tts daya ready.\n
    *                      ISS_ERROR_TIME_OUT: cloud tts timeout.\n
                           ISS_ERROR_GENERAL: cloud tts error. \n
    *  \param  lParam   "{\"rate\": 16000}" or "{\"rate\": 24000}" or NULL
    */
    final public static int ISS_TTS_MSG_CloudResult                 = (20001);
    /** \brief  ISS_TTS_MSG_ENDDATA INVALID!!!  */
    final public static int ISS_TTS_MSG_ENDDATA                     = (20002);
    /** \brief  LESL result;\n
    *  \param  wParam     int type: \n
    *                     0, if SwEnc check success. \n
    *                     error code of SwEnc, if SwEnc check error .\n
    *  \param  lParam     json type of SwEnc remind count and time, if SwEnc check success(wParam is 0) .\n
    *                     json string {"SoftwareEncryptionCount":-1,"SoftwareEncryptionTime":-1}, if GetAuthCount or GetAuthTime error .\n
    *                     not used, if SwEnc check error(wParam is not 0) .\n
    */
    final public static int ISS_TTS_MSG_CheckAuthorityResult        = (20003);
}