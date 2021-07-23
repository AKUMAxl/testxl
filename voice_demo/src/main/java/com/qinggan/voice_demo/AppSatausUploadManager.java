package com.qinggan.voice_demo;

import com.qinggan.appstatus.AppStatusConstant;
import com.qinggan.appstatus.AppStatusData;
import com.qinggan.appstatus.navi.NaviStatusBean;
import com.qinggan.dcs.DcsClassify;
import com.qinggan.dcs.DcsOpenAPI;
import com.qinggan.dcs.IvokaVoiceDetailBean;
import com.qinggan.dcs.bean.DcsBean;
import com.qinggan.dcs.bean.nav.HotelBean;
import com.qinggan.dcs.bean.nav.RestaurantBean;
import com.qinggan.dcs.bean.nav.TouristAttractionBean;
import com.qinggan.speech.VoiceActionID;

public class AppSatausUploadManager {

    private AppSatausUploadManager() {
    }

    private static class SingletonInstance {
        private static final AppSatausUploadManager INSTANCE = new AppSatausUploadManager();
    }

    public static AppSatausUploadManager getInstance() {
        return SingletonInstance.INSTANCE;
    }


    /**
     * 检索结果状态上传
     * @param voiceActionId 技能id
     */
    public void uploadStatus(int voiceActionId){
        AppStatusData appStatusData = new AppStatusData();
        NaviStatusBean naviStatusBean = new NaviStatusBean();
        // 如果是一下技能需要  通知语音状态
        switch (voiceActionId) {
            case VoiceActionID.ACTION_DCS_ROUTELINE_NAV:
            case VoiceActionID.ACTION_DCS_POI_NAV:
            case VoiceActionID.ACTION_OTW_QUERY:
                naviStatusBean.setAppName(AppStatusConstant.NaviAppName.MAPU);
                break;
            case VoiceActionID.ACTION_DCS_RESTAURANT_NAV:
                naviStatusBean.setAppName(AppStatusConstant.NaviAppName.RESTAURANT);
                break;
            case VoiceActionID.ACTION_DCS_TOURISTATTRACTION_NAV:
                naviStatusBean.setAppName(AppStatusConstant.NaviAppName.SCENIC_SPOT);
                break;
            case VoiceActionID.ACTION_DCS_HOTEL_NAV:
                naviStatusBean.setAppName(AppStatusConstant.NaviAppName.HOTEL);
                break;
            case VoiceActionID.ACTION_DCS_PARKING_NAV:
                naviStatusBean.setAppName(AppStatusConstant.NaviAppName.PARKING);
                break;
            default:
                return;
        }
        // 设置当前场景
        // AppStatusConstant.SceneStatus.Navi.MORE_TARGET 检索到多个结果
        // AppStatusConstant.SceneStatus.Navi.ONE_TARGET 检索到一个结果
        // AppStatusConstant.SceneStatus.Navi.NO_TARGET 没有检索到结果
        naviStatusBean.setSceneStatus(AppStatusConstant.SceneStatus.Navi.MORE_TARGET);
        // 应用在前台或后台
        // AppStatusConstant.ActiveStatus.FOR_GROUNT
        // AppStatusConstant.ActiveStatus.BACK_GROUNT
        naviStatusBean.setActiveStatus(AppStatusConstant.ActiveStatus.FOR_GROUNT);
        // 当前执行的操作
        // AppStatusConstant.StatusName.NAVI_POISEARCH
        naviStatusBean.setService(AppStatusConstant.StatusName.NAVI_POISEARCH);

        // 当前执行的操作
        appStatusData.setStatusName(AppStatusConstant.StatusName.NAVI_POISEARCH);
        appStatusData.setAppStatusBean(naviStatusBean);
        DcsOpenAPI.getInstance().sendAppStatus(appStatusData);
    }



    /**
     * 上传路线规划和导航状态
     */
    public void uploadNaviStatus(){

        AppStatusData appStatusData = new AppStatusData();
        NaviStatusBean naviStatusBean = new NaviStatusBean();
        // 设置当前场景
        // AppStatusConstant.SceneStatus.Navi.ROUTING 路线规划
        // AppStatusConstant.SceneStatus.Navi.NAVIGATION 导航中
        // AppStatusConstant.SceneStatus.Navi.NO_NAVI 非导航和路线规划状态
        naviStatusBean.setSceneStatus(AppStatusConstant.SceneStatus.Navi.ROUTING);
        naviStatusBean.setActiveStatus(AppStatusConstant.ActiveStatus.FOR_GROUNT);
        naviStatusBean.setService(AppStatusConstant.StatusName.NAVI_ROUTLINE);
        naviStatusBean.setAppName(AppStatusConstant.NaviAppName.MAPU);
        appStatusData.setAppStatusBean(naviStatusBean);
        appStatusData.setStatusName(AppStatusConstant.StatusName.NAVI_ROUTLINE);
        DcsOpenAPI.getInstance().sendAppStatus(appStatusData);
    }

    /**
     * 上传前后台切换状态
     */
    public void uploadDetailStatus(){
        AppStatusData appStatusData = new AppStatusData();
        NaviStatusBean naviStatusBean = new NaviStatusBean();
        // AppStatusConstant.NaviAppName.HOTEL 酒店
        // AppStatusConstant.NaviAppName.RESTAURANT 美食
        // AppStatusConstant.NaviAppName.SCENIC_SPOT 景点
        // AppStatusConstant.NaviAppName.MAPU 导航
        // AppStatusConstant.NaviAppName.PARKING 停车场
        naviStatusBean.setAppName(AppStatusConstant.NaviAppName.PARKING);
        naviStatusBean.setSceneStatus(AppStatusConstant.SceneStatus.Navi.TARGET_INFO);
        // 前后台
        // AppStatusConstant.ActiveStatus.FOR_GROUNT
        // AppStatusConstant.ActiveStatus.BACK_GROUNT
        naviStatusBean.setActiveStatus(AppStatusConstant.ActiveStatus.BACK_GROUNT);
        naviStatusBean.setService(AppStatusConstant.StatusName.NAVI_POISEARCH);
        appStatusData.setStatusName(AppStatusConstant.StatusName.NAVI_POISEARCH);
        appStatusData.setAppStatusBean(naviStatusBean);
        DcsOpenAPI.getInstance().sendAppStatus(appStatusData);
    }

    /**
     * 进入美食详情页时上传
     * @param dcsBean 美食item内的信息
     */
    public void enterRestaurantDetail(DcsBean dcsBean){
        RestaurantBean info = (RestaurantBean) dcsBean;
        IvokaVoiceDetailBean dcsEvent = new IvokaVoiceDetailBean();
        dcsEvent.setClassify(DcsClassify.REQUEST_TABLE_DETAIL);
        dcsEvent.setCurrentId(info.getSid());
        dcsEvent.setDistance(info.getDistance());
        dcsEvent.setIntentName("FOODS_DETAILS_INTENT");
        dcsEvent.setSkillId("2019052800000485");
        DcsOpenAPI.getInstance().sendEvent(dcsEvent);
    }

    /**
     * 进入景点详情页时上传
     * @param dcsBean 景点item内的信息
     */
    public void enterTouristDetail(DcsBean dcsBean){
        TouristAttractionBean info = (TouristAttractionBean) dcsBean;
        IvokaVoiceDetailBean dcsEvent = new IvokaVoiceDetailBean();
        dcsEvent.setClassify(DcsClassify.REQUEST_TABLE_DETAIL);
        dcsEvent.setCurrentId(info.getSid());
        dcsEvent.setLng(info.getLng() + "");
        dcsEvent.setDistance(info.getDistance());
        dcsEvent.setIntentName("VIEW_SPOT_DETAIL_NUM");
        dcsEvent.setSkillId("2019061500000163");
        DcsOpenAPI.getInstance().sendEvent(dcsEvent);
    }

    /**
     * 进入酒店详情页时上传
     * @param dcsBean 酒店item内的信息
     */
    public void enterHotelDetail(DcsBean dcsBean){
        HotelBean hotelBean = (HotelBean) dcsBean;
        IvokaVoiceDetailBean dcsEvent = new IvokaVoiceDetailBean();
        dcsEvent.setClassify(DcsClassify.REQUEST_TABLE_DETAIL);
        dcsEvent.setCurrentId(hotelBean.getSid());
        dcsEvent.setDistance(hotelBean.getDistance());
        dcsEvent.setIntentName("HOTEL_DETAIL");
        dcsEvent.setSkillId("2019051300000052");
        DcsOpenAPI.getInstance().sendEvent(dcsEvent);
    }

}