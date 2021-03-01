package com.xl.testqgspeech.data.voice;

import com.qinggan.speech.VoiceActionID;

public interface VoiceAction {
    int[] MainRegisterAction = new int[]{
            VoiceActionID.ACTION_DCS_COMMON_CONTENT,
            VoiceActionID.ACTION_DCS_RESTAURANT_NAV,
            VoiceActionID.ACTION_DCS_POI_NAV, //普通poi
            VoiceActionID.ACTION_DCS_ROUTELINE_NAV,
            VoiceActionID.ACTION_OTW_QUERY,
            VoiceActionID.ACTION_DCS_TOURISTATTRACTION_NAV,
            VoiceActionID.ACTION_DCS_HOTEL_NAV,
            VoiceActionID.ACTION_DCS_SOCIETY_PHONE_CONTACTS_QUERY,
            VoiceActionID.ACTION_DCS_PARKING_NAV,
            VoiceActionID.ACTION_DCS_COMMON_SHOW_HOTEL_DETAIL,
            VoiceActionID.ACTION_DCS_COMMON_SHOW_HINTS,
            VoiceActionID.ACTION_NAVI_ROUTE, //离线状态下的导航搜索->沿途的加油站
            VoiceActionID.ACTION_LBS_QUERY, //离线状态下的搜索->附近的充电桩
            VoiceActionID.ACTION_HELP_LAUNCH,
            VoiceActionID.ACTION_HELP_QUERY,
            VoiceActionID.ACTION_VOICE_IVOKA_CLOSE_NAVI,
            VoiceActionID.ACTION_VOICE_TEXT_SHOW,
            VoiceActionID.ACTION_VOICE_IVOKA_CANCEL,
            VoiceActionID.ACTION_VOICE_IVOKA_HOTEL_CANCEL,
            VoiceActionID.ACTION_VOICE_RESTAURANT_DETAIL,
            VoiceActionID.ACTION_TOURIS_TATTRACTION_DETAIL,
            VoiceActionID.ACTION_ID_VIOLATION,//添加违章查询
            VoiceActionID.ACTION_VOICE_ELECTRONIC_MANUAL_TABLE,
            VoiceActionID.ACTION_VOICE_IVOKA_NUMBER_TYPE,
            VoiceActionID.ACTION_VOICE_ELECTRONIC_MANUAL_PIC_CONTENT,
            VoiceActionID.ACTION_VOICE_ELECTRONIC_MANUAL_VIDEO,
            VoiceActionID.ACTION_VOICE_IMAGE_MODE_CHANGED,
            VoiceActionID.ACTION_VOICE_MEDIA_SHARE,
            VoiceActionID.ACTION_NAVI_ADD_MIDWAY_POINT,
            VoiceActionID.ACTION_VOICE_CHARGING_STATION_QUERY,
            VoiceActionID.ACTION_VOICE_CHARGING_STATION_DETAIL,
            VoiceActionID.ACTION_ID_IDIO
    };
}
