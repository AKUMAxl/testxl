package com.xl.testui.function;


import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.qinggan.dcs.DcsDataWrapper;
import com.qinggan.dcs.bean.DcsBean;
import com.qinggan.dcs.bean.NluBean;
import com.qinggan.dcs.bean.hotel.HotelDetailNluBean;
import com.qinggan.dcs.bean.hotel.HotelNumNluBean;
import com.qinggan.dcs.bean.hotel.HotelQueryNluBean;
import com.qinggan.dcs.bean.nav.NavConditionBean;
import com.qinggan.dcs.bean.nav.NavNluBean;
import com.qinggan.dcs.bean.nav.NavPOIBean;
import com.qinggan.dcs.bean.restaurant.RestaurantQueryNluBean;
import com.qinggan.dcs.bean.restaurant.detail.RestaurantDetailNluBean;
import com.qinggan.dcs.bean.tourist.list.TourQueryNluBean;
import com.qinggan.speech.VoiceAction;
import com.qinggan.speech.VoiceActionID;
import com.qinggan.speech.VoiceParam;
import com.qinggan.speech.VoiceParamValue;
import com.qinggan.speech.VuiActionHandler;
import com.qinggan.speech.VuiServiceMgr;
import com.qinggan.speech.VuiTtsObj;
import com.qinggan.speech.internal.IVuiTtsProcessHandler;

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
            VoiceActionID.ACTION_TOURIS_TATTRACTION_DETAIL,//景点详情
            VoiceActionID.ACTION_VOICE_IVOKA_NUMBER_TYPE,//第几个第几个
            VoiceActionID.ACTION_VOICE_CHARGING_STATION_QUERY,//充电桩相关
            VoiceActionID.ACTION_VOICE_CHARGING_STATION_DETAIL,//充电桩相关
            VoiceActionID.ACTION_NAVI_CANCEL_MIDWAY_POINT,// 删除途经点


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
        speakTTS("ActionProvider在单个组件中定义丰富的菜单交互协调动画，输入和绘图的时间。上下文包装器，允许您修改或替换包装上下文的主题。");
    }


    private void bindVoiceService() {
        mVuiServiceMgr.registerHandler(VuiServiceMgr.AppServiceType.mbNavi.ordinal(), mActions, new VuiActionHandler() {
            @Override
            public boolean onProcessResult(Message message) {
                Log.d(TAG, "onProcessResult() called with: message = [" + message.what + "]");
                Bundle data = message.getData();
                DcsDataWrapper wrapper = data.getParcelable(VoiceParam.VOICE_PARAM_DCS_DISPLAY);
                NluBean nluBean = null;
                if (wrapper!=null){
                    nluBean = wrapper.getNluBean();
                    String sortType = wrapper.getSortType();
                    if (!TextUtils.isEmpty(sortType)){
                        Log.d(TAG, "onProcessResult() called with: sortType = [" + sortType + "]");
                    }
                    String sort = wrapper.getSort();
                    if (!TextUtils.isEmpty(sortType)){
                        Log.d(TAG, "onProcessResult() called with: sort = [" + sort + "]");
                    }
                }
                boolean ret = true;
                switch (message.what) {
                    // online
                    case VoiceActionID.ACTION_DCS_RESTAURANT_NAV://美食相关
                        if (nluBean instanceof RestaurantQueryNluBean){
                            RestaurantQueryNluBean restaurantQueryNluBean = (RestaurantQueryNluBean) nluBean;
                            parseRestaurantQueryNluBean(restaurantQueryNluBean);
                        }

                        break;
                    case VoiceActionID.ACTION_DCS_POI_NAV: //普通poi
                        Log.d(TAG, "ACTION_DCS_ROUTELINE_NAV  DcsDataWrapper = [" + wrapper.toString() + "]");
                        if (nluBean instanceof NavNluBean){
                            NavNluBean navNluBean = (NavNluBean) nluBean;
                            parseNaviBean(navNluBean);
                        }
                        // 附近有没有中国银行
                        // 找找五道口附近的药店
                        // 两公里以内的加油站
                        break;
                    case VoiceActionID.ACTION_DCS_TOURISTATTRACTION_NAV://景点相关
                    case VoiceActionID.ACTION_TOURIS_TATTRACTION_DETAIL://景点详情
                        if (nluBean instanceof TourQueryNluBean){
                            TourQueryNluBean tourQueryNluBean = (TourQueryNluBean) wrapper.getNluBean();
                            parseTourQueryNluBean(tourQueryNluBean);
                        }
                        break;
                    case VoiceActionID.ACTION_DCS_HOTEL_NAV://酒店相关
                    case VoiceActionID.ACTION_DCS_COMMON_SHOW_HOTEL_DETAIL://酒店详情
                        if (nluBean instanceof HotelQueryNluBean){
                            HotelQueryNluBean hotelQueryNluBean = (HotelQueryNluBean) wrapper.getNluBean();
                            parseHotel(hotelQueryNluBean);
                        }
                        break;
                    case VoiceActionID.ACTION_DCS_PARKING_NAV://保留 停车场相关
                        break;
                    case VoiceActionID.ACTION_VOICE_IVOKA_HOTEL_CANCEL://保留
                        break;
                    case VoiceActionID.ACTION_VOICE_RESTAURANT_DETAIL://美食详情
                        if (nluBean instanceof RestaurantDetailNluBean){
                            RestaurantDetailNluBean restaurantQueryNluBean = (RestaurantDetailNluBean) wrapper.getNluBean();
                            parseRestaurantDetailNluBean(restaurantQueryNluBean);
                        }
                        break;
                    case VoiceActionID.ACTION_VOICE_IVOKA_NUMBER_TYPE://第几个第几个
                        if (nluBean instanceof HotelNumNluBean){
                            HotelNumNluBean hotelNumNluBean = (HotelNumNluBean) wrapper.getNluBean();
                            parseNumber(hotelNumNluBean);
                        }
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

                        Log.d(TAG, "ACTION_DCS_ROUTELINE_NAV  DcsDataWrapper = [" + wrapper.toString() + "]");
                        if (nluBean instanceof NavNluBean){
                            NavNluBean navNluBean = (NavNluBean) nluBean;
                            parseNaviBean(navNluBean);
//                            return true;
                        }
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
//                        Log.d(TAG, "ACTION_DCS_ROUTELINE_NAV  DcsDataWrapper = [" + wrapper.toString() + "]");
                        if (nluBean instanceof NavNluBean){
                            NavNluBean navNluBean = (NavNluBean) nluBean;
                            parseNaviBean(navNluBean);
                        }
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
                        }else if (queryType.equals("locate_road")){
                            // 我现在在哪条路上
                        }
                        break;
                    case VoiceActionID.ACTION_NAVI_CONTROL:
                        // 还有多远、多久到
//                        Log.d(TAG, "ACTION_DCS_ROUTELINE_NAV  DcsDataWrapper = [" + wrapper.toString() + "]");
//                        if (nluBean instanceof NavNluBean){
//                            NavNluBean navNluBean = (NavNluBean) nluBean;
//                            parseNaviBean(navNluBean);
//                        }
                        String naviCommand = data.getString(VoiceParam.VOICE_PARAM_NAVI_COMMAND);
                        Log.d(TAG, "ACTION_DCS_ROUTELINE_NAV  naviCommand = [" + naviCommand + "]");

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
                    case VoiceActionID.ACTION_NAVI_CANCEL_MIDWAY_POINT:
                        String poiName = data.getString(VoiceParam.VOICE_PARAM_NAVI_POI);
                        String number = data.getString(VoiceParam.VOICE_PARAM_NAVI_MIDWAY_NUMBER);
                        Log.d(TAG, "ACTION_NAVI_CANCEL_MIDWAY_POINT called poiName:"+poiName+" -- number:"+number);
                        break;
                    default:
                        break;
                }
                return ret;
            }
        });
    }


    private void parseNaviBean(NavNluBean navNluBean){
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
    }

    private void parseRestaurantQueryNluBean(RestaurantQueryNluBean restaurantQueryNluBean){
        Log.d(TAG, "parseRestaurantQueryNluBean() called with: restaurantQueryNluBean = [" + restaurantQueryNluBean + "]");
        RestaurantQueryNluBean.PayloadBean payloadBean = restaurantQueryNluBean.getPayload();
        Log.d(TAG, "parseRestaurantQueryNluBean() called with: payloadBean = [" + payloadBean.toString() + "]");
        String distance = payloadBean.getDistance();
        if (!TextUtils.isEmpty(distance)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  距离 distance = [" + distance + "]");
        }
        String distanceDesc = payloadBean.getDistanceDesc();
        if (!TextUtils.isEmpty(distanceDesc)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  距离描述 distanceDesc = [" + distanceDesc + "]");
        }
        String price = payloadBean.getPrice();
        if (!TextUtils.isEmpty(price)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  价格 price = [" + price + "]");
        }
        String foodType = payloadBean.getFoodType();
        if (!TextUtils.isEmpty(foodType)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  美食类型 foodType = [" + foodType + "]");
        }
        String address = payloadBean.getAddress();
        if (!TextUtils.isEmpty(address)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  地址 address = [" + address + "]");
        }
        String appraisal = payloadBean.getAppraisal();
        if (!TextUtils.isEmpty(appraisal)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  评价 appraisal = [" + appraisal + "]");
        }
        String foodName = payloadBean.getFoodName();
        if (!TextUtils.isEmpty(foodName)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  菜名 foodName = [" + foodName + "]");
        }
        String costTime = payloadBean.getCostTime();
        if (!TextUtils.isEmpty(costTime)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  交通耗时 costTime = [" + costTime + "]");
        }
        String serialNumber = payloadBean.getSerialNumber();
        if (!TextUtils.isEmpty(serialNumber)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  序号 serialNumber = [" + serialNumber + "]");
        }
        String transport = payloadBean.getTransport();
        if (!TextUtils.isEmpty(transport)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  交通工具 transport = [" + transport + "]");
        }
        String costTimeDesc = payloadBean.getCostTimeDesc();
        if (!TextUtils.isEmpty(costTimeDesc)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  耗时描述 costTimeDesc = [" + costTimeDesc + "]");
        }
        String city = payloadBean.getCity();
        if (!TextUtils.isEmpty(city)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  城市 city = [" + city + "]");
        }
        String location = payloadBean.getLocation();
        if (!TextUtils.isEmpty(location)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  位置 location = [" + location + "]");
        }
        String maxPrice = payloadBean.getMaxPrice();
        if (!TextUtils.isEmpty(maxPrice)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  最大价格 maxPrice = [" + maxPrice + "]");
        }
        String minPrice = payloadBean.getMinPrice();
        if (!TextUtils.isEmpty(minPrice)) {
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  最小价格  minPrice = [" + minPrice + "]");
        }
    }

    private void parseRestaurantDetailNluBean(RestaurantDetailNluBean restaurantDetailNluBean){
        Log.d(TAG, "parseRestaurantDetailNluBean() called with: restaurantDetailNluBean = [" + restaurantDetailNluBean + "]");
        RestaurantDetailNluBean.PayloadBean payloadBean = restaurantDetailNluBean.getPayload();
        Log.d(TAG, "parseRestaurantQueryNluBean() called with: payloadBean = [" + payloadBean.toString() + "]");
        String distance = payloadBean.getDistance();
        if (!TextUtils.isEmpty(distance)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  距离 distance = [" + distance + "]");
        }
        String distanceDesc = payloadBean.getDistanceDesc();
        if (!TextUtils.isEmpty(distanceDesc)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  距离描述 distanceDesc = [" + distanceDesc + "]");
        }
        String price = payloadBean.getPrice();
        if (!TextUtils.isEmpty(price)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  价格 price = [" + price + "]");
        }
        String foodType = payloadBean.getFoodType();
        if (!TextUtils.isEmpty(foodType)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  美食类型 foodType = [" + foodType + "]");
        }
        String address = payloadBean.getAddress();
        if (!TextUtils.isEmpty(address)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  地址 address = [" + address + "]");
        }
        String appraisal = payloadBean.getAppraisal();
        if (!TextUtils.isEmpty(appraisal)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  评价 appraisal = [" + appraisal + "]");
        }
        String foodName = payloadBean.getFoodName();
        if (!TextUtils.isEmpty(foodName)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  菜名 foodName = [" + foodName + "]");
        }
        String costTime = payloadBean.getCostTime();
        if (!TextUtils.isEmpty(costTime)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  交通耗时 costTime = [" + costTime + "]");
        }
        String serialNumber = payloadBean.getSerialNumber();
        if (!TextUtils.isEmpty(serialNumber)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  序号 serialNumber = [" + serialNumber + "]");
        }
        String transport = payloadBean.getTransport();
        if (!TextUtils.isEmpty(transport)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  交通工具 transport = [" + transport + "]");
        }
        String costTimeDesc = payloadBean.getCostTimeDesc();
        if (!TextUtils.isEmpty(costTimeDesc)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  耗时描述 costTimeDesc = [" + costTimeDesc + "]");
        }
        String city = payloadBean.getCity();
        if (!TextUtils.isEmpty(city)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  城市 city = [" + city + "]");
        }
        String location = payloadBean.getLocation();
        if (!TextUtils.isEmpty(location)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  位置 location = [" + location + "]");
        }
        String maxPrice = payloadBean.getMaxPrice();
        if (!TextUtils.isEmpty(maxPrice)){
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  最大价格 maxPrice = [" + maxPrice + "]");
        }
        String minPrice = payloadBean.getMinPrice();
        if (!TextUtils.isEmpty(minPrice)) {
            Log.d(TAG, "ACTION_DCS_RESTAURANT_NAV  最小价格  minPrice = [" + minPrice + "]");
        }
    }

    private void parseNumber(HotelNumNluBean hotelNumNluBean){
        HotelNumNluBean.PayloadBean payloadBean = hotelNumNluBean.getPayload();
        String type = payloadBean.getType();
        if (!TextUtils.isEmpty(type)) {
            Log.d(TAG, "ACTION_VOICE_IVOKA_NUMBER_TYPE  类型  type = [" + type + "]");
        }
        String serialNumber = payloadBean.getSerialNumber();
        if (!TextUtils.isEmpty(serialNumber)) {
            Log.d(TAG, "ACTION_VOICE_IVOKA_NUMBER_TYPE  第n个  type = [" + serialNumber + "]");
        }
    }

    private void parseTourQueryNluBean(TourQueryNluBean tourQueryNluBean){
        TourQueryNluBean.PayloadBean payloadBean = tourQueryNluBean.getPayload();
        if (payloadBean==null){
            Log.e(TAG, "parseTourQueryNluBean: payload is null" );
            return;
        }
        Log.d(TAG, "parseTourQueryNluBean() called with: payloadBean = [" + payloadBean.toString() + "]");
        String searchName = payloadBean.getSearchName();
        if (!TextUtils.isEmpty(searchName)){
            Log.d(TAG, "ACTION_DCS_TOURISTATTRACTION_NAV  检索关键字 searchName = [" + searchName + "]");
        }
        String city = payloadBean.getCity();
        if (!TextUtils.isEmpty(city)){
            Log.d(TAG, "ACTION_DCS_TOURISTATTRACTION_NAV  城市 city = [" + city + "]");
        }
        String distance = payloadBean.getDistance();
        if (!TextUtils.isEmpty(distance)){
            Log.d(TAG, "ACTION_DCS_TOURISTATTRACTION_NAV  距离 distance = [" + distance + "]");
        }
        String location = payloadBean.getLocation();
        if (!TextUtils.isEmpty(location)){
            Log.d(TAG, "ACTION_DCS_TOURISTATTRACTION_NAV  位置 location = [" + location + "]");
        }
        String keyWord = payloadBean.getKeyWord();
        if (!TextUtils.isEmpty(keyWord)){
            Log.d(TAG, "ACTION_DCS_TOURISTATTRACTION_NAV  关键字 keyWord = [" + keyWord + "]");
        }
        String minPrice = payloadBean.getMinPrice();
        if (!TextUtils.isEmpty(minPrice)){
            Log.d(TAG, "ACTION_DCS_TOURISTATTRACTION_NAV  最低价 minPrice = [" + minPrice + "]");
        }
        String maxPrice = payloadBean.getMaxPrice();
        if (!TextUtils.isEmpty(maxPrice)){
            Log.d(TAG, "ACTION_DCS_TOURISTATTRACTION_NAV  最高价 maxPrice = [" + maxPrice + "]");
        }
        String serialNumber = payloadBean.getSerialNumber();
        if (!TextUtils.isEmpty(serialNumber)){
            Log.d(TAG, "ACTION_DCS_TOURISTATTRACTION_NAV  序号 serialNumber = [" + serialNumber + "]");
        }
        String poiAddress = payloadBean.getPoiAddress();
        if (!TextUtils.isEmpty(poiAddress)){
            Log.d(TAG, "ACTION_DCS_TOURISTATTRACTION_NAV  检索POI poiAddress = [" + poiAddress + "]");
        }
        String distanceDescr = payloadBean.getDistanceDescr();
        if (!TextUtils.isEmpty(distanceDescr)){
            Log.d(TAG, "ACTION_DCS_TOURISTATTRACTION_NAV  根据距离选择 distanceDescr = [" + distanceDescr + "]");
        }
        String priceDescr = payloadBean.getPriceDescr();
        if (!TextUtils.isEmpty(priceDescr)){
            Log.d(TAG, "ACTION_DCS_TOURISTATTRACTION_NAV  根据价格选择 priceDescr = [" + priceDescr + "]");
        }
        speakTTS("OK");
    }

    private void parseHotel(HotelQueryNluBean hotelQueryNluBean){

        HotelQueryNluBean.PayloadBean payloadBean = hotelQueryNluBean.getPayload();
        if (payloadBean==null){
            Log.e(TAG, "parseHotel: payload is null" );
            return;
        }
        Log.d(TAG, "parseHotel() called with: payloadBean = [" + payloadBean.toString() + "]");
        /**
         * city : 南京,* distance : 5000
         * "ratingMin": "4.5","location": "夫子庙","searchScope": "2","brand":"诺富特","seg":"民宿","minPrice":"300", "maxPrice":"400",
         * "star":"5","startDate":"20190706","facility":"游泳池","sorting":"price,desc","theme":"商务出行"
         */
        String city = payloadBean.getCity();
        if (!TextUtils.isEmpty(city)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  城市 city = [" + city + "]");
        }
        String distance = payloadBean.getDistance();
        if (!TextUtils.isEmpty(distance)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  距离 distance = [" + distance + "]");
        }
        String ratingMin = payloadBean.getRatingMin();
        if (!TextUtils.isEmpty(ratingMin)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  评分 ratingMin = [" + ratingMin + "]");
        }
        String location = payloadBean.getLocation();
        if (!TextUtils.isEmpty(location)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  位置 location = [" + location + "]");
        }
        String searchScope = payloadBean.getSearchScope();
        if (!TextUtils.isEmpty(searchScope)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  检索范围 searchScope = [" + searchScope + "]");
        }
        String brand = payloadBean.getBrand();
        if (!TextUtils.isEmpty(brand)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  品牌 brand = [" + brand + "]");
        }
        String seg = payloadBean.getSeg();
        if (!TextUtils.isEmpty(seg)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  类型 seg = [" + seg + "]");
        }
        String minPrice = payloadBean.getMinPrice();
        if (!TextUtils.isEmpty(minPrice)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  最低价 minPrice = [" + minPrice + "]");
        }
        String maxPrice = payloadBean.getMaxPrice();
        if (!TextUtils.isEmpty(maxPrice)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  最高价 maxPrice = [" + maxPrice + "]");
        }
        String star = payloadBean.getStar();
        if (!TextUtils.isEmpty(star)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  满星 star = [" + star + "]");
        }
        String startDate = payloadBean.getStartDate();
        if (!TextUtils.isEmpty(startDate)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  入住日期 startDate = [" + startDate + "]");
        }
        String facility = payloadBean.getFacility();
        if (!TextUtils.isEmpty(facility)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  设施 facility = [" + facility + "]");
        }
        String sorting = payloadBean.getSorting();
        if (!TextUtils.isEmpty(sorting)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  排序 = [" + sorting + "]");
        }
        String theme = payloadBean.getTheme();
        if (!TextUtils.isEmpty(theme)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  主题 theme = [" + theme + "]");
        }
        String defaultSearchName = payloadBean.getDefaultSearchName();
        if (!TextUtils.isEmpty(defaultSearchName)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  默认检索名 defaultSearchName = [" + defaultSearchName + "]");
        }
        String address = payloadBean.getAddress();
        if (!TextUtils.isEmpty(address)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  地址 address = [" + address + "]");
        }
        String HotelName = payloadBean.getHotelName();
        if (!TextUtils.isEmpty(HotelName)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  酒店名称 HotelName = [" + HotelName + "]");
        }
        String getHotelLvl = payloadBean.getHotelLvl();
        if (!TextUtils.isEmpty(getHotelLvl)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  酒店星级 getHotelLvl = [" + getHotelLvl + "]");
        }
        String price = payloadBean.getPrice();
        if (!TextUtils.isEmpty(price)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  价格 price = [" + price + "]");
        }
        String appraisal = payloadBean.getAppraisal();
        if (!TextUtils.isEmpty(appraisal)){
            Log.d(TAG, "ACTION_DCS_HOTEL_NAV  评分 appraisal = [" + appraisal + "]");
        }
        speakTTS("OK");
    }


    /**
     * TTS 需要调用 {@link com.qinggan.speech.VuiServiceMgr#getInstance(Context, VuiServiceMgr.VuiConnectionCallback)}
     *     在回调 {@link VuiServiceMgr.VuiConnectionCallback#onServiceConnected()} 后调用此方法
     * @param content
     */
    public void speakTTS(String content) {
        VuiTtsObj ttsObj = new VuiTtsObj("com.xl.testui", VuiTtsObj.TtsContentType.TEXT);// "com.packageName" 需要替换为应用自己的包名
        ttsObj.setSpeakContent(content);
        ttsObj.setDisplayText(content);
        mVuiServiceMgr.sendTtsNotify(ttsObj.toIntent(), new IVuiTtsProcessHandler() {
            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDone() {

            }

            @Override
            public void onError() {

            }
        });
    }

}