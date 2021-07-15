package com.xl.testui.function;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.qinggan.dcs.DcsDataWrapper;
import com.qinggan.dcs.bean.DcsBean;
import com.qinggan.dcs.bean.nav.NavConditionBean;
import com.qinggan.dcs.bean.nav.NavNluBean;
import com.qinggan.dcs.bean.nav.NavPOIBean;
import com.qinggan.speech.VoiceActionID;
import com.qinggan.speech.VoiceParam;
import com.qinggan.speech.VoiceParamValue;
import com.qinggan.speech.VuiActionHandler;
import com.qinggan.speech.VuiServiceMgr;

import java.util.ArrayList;

public class NaviDemo {

    public static final String TAG = "NAVI_DEMO";

    private VuiServiceMgr mVuiServiceMgr;

    public static final int[] mActions = {

            /******************* 导航 **********************/
            // online
            VoiceActionID.ACTION_DCS_RESTAURANT_NAV,//美食相关
            VoiceActionID.ACTION_DCS_POI_NAV, //普通poi
            VoiceActionID.ACTION_DCS_TOURISTATTRACTION_NAV,//景点相关
            VoiceActionID.ACTION_DCS_HOTEL_NAV,//酒店相关
            VoiceActionID.ACTION_DCS_PARKING_NAV,//保留 停车场相关
            VoiceActionID.ACTION_DCS_COMMON_SHOW_HOTEL_DETAIL,//酒店详情
            VoiceActionID.ACTION_VOICE_IVOKA_HOTEL_CANCEL,//保留
            VoiceActionID.ACTION_VOICE_RESTAURANT_DETAIL,//美食详情
            VoiceActionID.ACTION_TOURIS_TATTRACTION_DETAIL,//景点相关
            VoiceActionID.ACTION_VOICE_IVOKA_NUMBER_TYPE,//第几个第几个
            VoiceActionID.ACTION_VOICE_CHARGING_STATION_QUERY,//充电桩相关
            VoiceActionID.ACTION_VOICE_CHARGING_STATION_DETAIL,//充电桩相关

            // online & offline
            VoiceActionID.ACTION_DCS_ROUTELINE_NAV,//导航到where
            VoiceActionID.ACTION_OTW_QUERY,

            // offline
            VoiceActionID.ACTION_NAVI_ROUTE, //离线状态下的导航搜索->沿途的加油站
            VoiceActionID.ACTION_LBS_QUERY, //离线状态下的搜索->附近的充电桩
            VoiceActionID.ACTION_VOICE_IVOKA_CLOSE_NAVI,// 取消导航 关闭导航 结束导航等。。。
            VoiceActionID.ACTION_NAVI_ADD_MIDWAY_POINT,//添加途径点

            VoiceActionID.ACTION_VOICE_IMAGE_MODE_CHANGED,//语音形象切换 与导航无关 可以取消注册

            // naviService 注册的技能
            VoiceActionID.ACTION_TRAFFIC_QUERY,
            VoiceActionID.ACTION_NAVI_QUERY,
            VoiceActionID.ACTION_NAVI_CONTROL,
//        VoiceActionID.ACTION_NAVI_GO_HOME_COMPANY,
            VoiceActionID.ACTION_WHEREI_POS,
            VoiceActionID.ACTION_DCS_TRAFFIC_NAV,
            VoiceActionID.ACTION_NAVI_LOCATE,


    };

    private NaviDemo() {
    }

    private static class SingletonInstance {
        private static final NaviDemo INSTANCE = new NaviDemo();
    }

    public static NaviDemo getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void init(Context context){
        mVuiServiceMgr = VuiServiceMgr.getInstance(context, new VuiServiceMgr.VuiConnectionCallback() {
            @Override
            public void onServiceConnected() {
                bindVoiceService();
                Log.d(TAG, "onServiceConnected() called");
            }

            @Override
            public void onServiceDisconnect() {
                Log.d(TAG, "onServiceDisconnect() called");
            }
        });
    }


    private void bindVoiceService() {
        mVuiServiceMgr.registerHandler(VuiServiceMgr.AppServiceType.mbNavi.ordinal(), mActions, new VuiActionHandler() {
            @Override
            public boolean onProcessResult(Message message) {
                Log.d(TAG, "onProcessResult() called with: message = [" + message.what + "]");
                Bundle data = message.getData();
                DcsDataWrapper wrapper = data.getParcelable(VoiceParam.VOICE_PARAM_DCS_DISPLAY);
                Log.d(TAG, "ACTION_DCS_ROUTELINE_NAV  DcsDataWrapper = [" + wrapper.toString() + "]");
                NavNluBean navNluBean = (NavNluBean) wrapper.getNluBean();
                Log.d(TAG, "ACTION_DCS_ROUTELINE_NAV  NavNluBean = [" + navNluBean.toString() + "]");
                NavNluBean.PayloadBean payloadBean = navNluBean.getPayload();
                Log.d(TAG, "ACTION_DCS_ROUTELINE_NAV  PayloadBean = [" + payloadBean.toString() + "]");
                String destination = payloadBean.getDestination();
                String queryAction = payloadBean.getAction();
                String pass = payloadBean.getPass();
                String location = payloadBean.getLocation();
                String radius = payloadBean.getRadius();
                String brand = payloadBean.getBrand();
                Log.d(TAG, "ACTION_DCS_ROUTELINE_NAV  目的地 destination = [" + destination + "]");
                Log.d(TAG, "ACTION_DCS_ROUTELINE_NAV  查询类型 queryAction = [" + queryAction + "]");
                Log.d(TAG, "ACTION_DCS_ROUTELINE_NAV  途经点 pass = [" + pass + "]");
                Log.d(TAG, "ACTION_DCS_ROUTELINE_NAV  位置 location = [" + location + "]");
                Log.d(TAG, "ACTION_DCS_ROUTELINE_NAV  范围（米） radius = [" + radius + "]");
                Log.d(TAG, "ACTION_DCS_ROUTELINE_NAV  品牌 brand = [" + brand + "]");
                boolean ret = false;
                switch (message.what) {
                    // online
                    case VoiceActionID.ACTION_DCS_RESTAURANT_NAV://美食相关
                        break;
                    case VoiceActionID.ACTION_DCS_POI_NAV: //普通poi
                        // 附近有没有中国银行
                        // 找找五道口附近的药店
                        // 两公里以内的加油站
                        break;
                    case VoiceActionID.ACTION_DCS_TOURISTATTRACTION_NAV://景点相关
                        break;
                    case VoiceActionID.ACTION_DCS_HOTEL_NAV://酒店相关
                        break;
                    case VoiceActionID.ACTION_DCS_PARKING_NAV://保留 停车场相关
                        break;
                    case VoiceActionID.ACTION_DCS_COMMON_SHOW_HOTEL_DETAIL://酒店详情
                        break;
                    case VoiceActionID.ACTION_VOICE_IVOKA_HOTEL_CANCEL://保留
                        break;
                    case VoiceActionID.ACTION_VOICE_RESTAURANT_DETAIL://美食详情
                        break;
                    case VoiceActionID.ACTION_TOURIS_TATTRACTION_DETAIL://景点相关
                        break;
                    case VoiceActionID.ACTION_VOICE_IVOKA_NUMBER_TYPE://第几个第几个
                        break;
                    case VoiceActionID.ACTION_VOICE_CHARGING_STATION_QUERY://充电桩相关
                        break;
                    case VoiceActionID.ACTION_VOICE_CHARGING_STATION_DETAIL://充电桩相关
                        break;
                    // online & offline
                    case VoiceActionID.ACTION_DCS_ROUTELINE_NAV://导航到where
                        // 去德基广场，最快的路
                        // 导航到新城市广场，中途要经过苏果超市
                        // 带我中途去一下龙华路地铁站
                        // 去石榴财智中心
                        // 帮我导航漓江路和湘江路交叉口
                        // 我要去北京东路63号
                        // 导航回家/公司
                        // 导航去德恒事务所
                        DcsBean dcsBean = wrapper.getDcsBean();
                        Log.d(TAG, "ACTION_DCS_ROUTELINE_NAV  DcsBean = [" + dcsBean.toString() + "]");
                        NavConditionBean navConditionBean = (NavConditionBean) dcsBean;
                        ArrayList<DcsBean> roadList = navConditionBean.getNavPOIBeenList();
                        if (roadList != null && roadList.size() > 0) {
                            NavPOIBean navPOIBean = (NavPOIBean) roadList.get(0);
                            String name = navPOIBean.getName();
                            if (TextUtils.equals(name, "家") || TextUtils.equals(name, "公司")) {
                                // 导航回家/公司

                            }
                        }
                        break;
                    case VoiceActionID.ACTION_OTW_QUERY:

                        break;
                    // offline
                    case VoiceActionID.ACTION_NAVI_ROUTE: //离线状态下的导航搜索->沿途的加油站
                        break;
                    case VoiceActionID.ACTION_LBS_QUERY: //离线状态下的搜索->附近的充电桩
                        break;
                    case VoiceActionID.ACTION_VOICE_IVOKA_CLOSE_NAVI:// 取消导航 关闭导航 结束导航等。。。
                        break;
                    case VoiceActionID.ACTION_NAVI_ADD_MIDWAY_POINT://添加途径点
                        break;
                    case VoiceActionID.ACTION_VOICE_IMAGE_MODE_CHANGED://语音形象切换 与导航无关 可以取消注册
                        break;
                    // naviService 注册的技能
                    case VoiceActionID.ACTION_TRAFFIC_QUERY:
                        break;
                    case VoiceActionID.ACTION_NAVI_QUERY:
                        // 到xx堵不堵
                        //终点
                        String trafficDirection = data.getString(VoiceParam.VOICE_PARAM_NAVI_ADDRESS);
                        //查询类型（remain_time/remain_distance）
                        String queryType = data.getString(VoiceParam.VOICE_PARAM_NAVI_QUERY_TYPE);

                        Log.d(TAG, "ACTION_NAVI_QUERY  trafficDirection = [" + trafficDirection + "]");
                        Log.d(TAG, "ACTION_NAVI_QUERY  queryType = [" + queryType + "]");
                        if (queryType.equals("remain_time")) {
//                            showRouteTrafficVr(trafficDirection, 3);
                        } else if (queryType.equals("remain_duration")) {
//                            showRouteTrafficVr(trafficDirection, 2);
                        } else if (queryType.equals("remain_traffic")) {
//                            showPoiTrafficVr(trafficDirection);
                        }
                        break;
                    case VoiceActionID.ACTION_NAVI_CONTROL:
                        // 还有多远、多久到
                        String naviCommand = data.getString(VoiceParam.VOICE_PARAM_NAVI_COMMAND);
                        switch (naviCommand) {
                            case VoiceParamValue.NAVI_COMMAND_REMAIN_DISTANCE:
                                // 多远
                                break;
                            case VoiceParamValue.NAVI_COMMAND_REAMAIN_TIME:
                                //多久
                                break;
                            case VoiceParamValue.NAVI_COMMAND_ARRIVE_TIME:
                                // 几点到
                                break;
                            default:
                                break;
                        }
                        Log.d(TAG, "ACTION_NAVI_CONTROL  naviCommand = [" + naviCommand + "]");
                        break;
//        VoiceActionID.ACTION_NAVI_GO_HOME_COMPANY,
                    case VoiceActionID.ACTION_WHEREI_POS:
                        break;
                    case VoiceActionID.ACTION_DCS_TRAFFIC_NAV:
                        break;
                    case VoiceActionID.ACTION_NAVI_LOCATE:
                        break;
                    default:
                        break;
                }
                return ret;
            }
        });
    }

}