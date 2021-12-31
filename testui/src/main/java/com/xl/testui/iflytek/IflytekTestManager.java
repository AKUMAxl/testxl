package com.xl.testui.iflytek;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.util.ResourceUtil;
import com.qinggan.appstatus.AppStatusConstant;
import com.qinggan.appstatus.AppStatusData;
import com.qinggan.appstatus.navi.NaviStatusBean;
import com.qinggan.dcs.DcsClassify;
import com.qinggan.dcs.DcsOpenAPI;
import com.qinggan.dcs.IDcsServiceListener;
import com.qinggan.dcs.IvokaVoiceDetailBean;
import com.qinggan.dcs.bean.nav.HotelBean;
import com.qinggan.dcs.bean.nav.RestaurantBean;
import com.qinggan.dcs.bean.nav.TouristAttractionBean;
import com.xl.testui.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class IflytekTestManager {

    private final String TAG = IflytekTestManager.class.getSimpleName();

    private Context mContext;
    private WindowManager mWindowManager;
    private View mRootView;
    private RecyclerView mContentRecyclerView;
    private RecyclerView mAppRecyclerView;
    private String mPath;
    private String mFileName;
    private SpeechTextAdapter mContentSpeechTextAdapter;
    private SpeechTextAdapter mAppAdapter;
    private TextView mCurApp;
    ArrayList<String> arrayList = new ArrayList();

    private String mCurForegroundApp;

    private IflytekTestManager() {
    }

    private static class SingletonInstance {
        private static final IflytekTestManager INSTANCE = new IflytekTestManager();
    }

    public static IflytekTestManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void init(Context context, WindowManager windowManager) {
        this.mContext = context;
        this.mWindowManager = windowManager;
        mPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName = "today.raw";
        initView();
        initTts();
        DcsOpenAPI.getInstance().init(mContext, true, new IDcsServiceListener() {
            @Override
            public void onPrepared() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onExit() {

            }
        });
    }

    private void initView() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2038,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        params.y = 260;
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.test_iflytek_window, null);
        mRootView.findViewById(R.id.test_iflytek_back).setOnClickListener(v -> {
            Log.d(TAG, "initView() called back");
            uploadBackground();
            mWindowManager.removeView(mRootView);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.xl.testui", "com.xl.testui.MainActivity"));
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
        mRootView.findViewById(R.id.test_iflytek_wakeup).setOnClickListener(v -> {
            Log.d(TAG, "initView() called init");
            SpeechSynthesizer.getSynthesizer().startSpeaking("hi红旗", null);
//            SpeechSynthesizer.getSynthesizer().startSpeaking("hello world", null);

        });
        mRootView.findViewById(R.id.test_iflytek_speech).setOnClickListener(v -> {
            Log.d(TAG, "initView() called speak");
//            SpeechSynthesizer.getSynthesizer().synthesizeToUri("打开音乐", mPath + File.separator + mFileName, null);
            String text = "附近的"+mCurApp.getText().toString();
            SpeechSynthesizer.getSynthesizer().startSpeaking(text, null);

        });
        mRootView.findViewById(R.id.test_iflytek_click_item).setOnClickListener(v -> {
            enterDetail();
        });
        mRootView.findViewById(R.id.test_iflytek_detail_fore).setOnClickListener(v -> {
            detailFore();
        });
        mRootView.findViewById(R.id.test_iflytek_detail_back).setOnClickListener(v -> {
            detailBack();
        });
        mCurApp = mRootView.findViewById(R.id.test_iflytek_cur);
        mCurApp.setOnClickListener(v -> {

        });
        mAppRecyclerView = mRootView.findViewById(R.id.test_iflytek_app);
        mAppRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ArrayList<String> appList = new ArrayList<String>();
        appList.add("美食");
        appList.add("景点");
        appList.add("酒店");
        appList.add("美食二次操作");
        appList.add("景点二次操作");
        appList.add("酒店二次操作");
        appList.add("美食详情");
        appList.add("景点详情");
        appList.add("酒店详情");
        appList.add("全局免唤醒");
        appList.add("音乐内免唤醒");
        appList.add("导航内免唤醒");
        appList.add("有声内免唤醒");
        appList.add("视频内免唤醒");
        appList.add("新闻内免唤醒");
        appList.add("音乐");
        appList.add("电台");
//        appList.add("新闻");
        appList.add("视频");
        appList.add("电话");
        appList.add("车机系统控制");
        appList.add("天气");
        appList.add("美食");
        appList.add("景点");
        appList.add("酒店");
        appList.add("导航");
        appList.add("电子手册");

        mAppAdapter = new SpeechTextAdapter(mContext,appList);
        mAppAdapter.setOnClickListener(new SpeechTextAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                String data = appList.get(position);
                updateData(data);
                uploadBackground();
                if (appList.get(position).contains("美食")){
                    mCurForegroundApp = AppStatusConstant.NaviAppName.RESTAURANT;
                }else if (appList.get(position).contains("景点")){
                    mCurForegroundApp = AppStatusConstant.NaviAppName.SCENIC_SPOT;
                }else if (appList.get(position).contains("酒店")){
                    mCurForegroundApp = AppStatusConstant.NaviAppName.HOTEL;
                }

                uploadForeground();
                mCurApp.setText(data.substring(0,2));
            }
        });
        mAppRecyclerView.setAdapter(mAppAdapter);


        mContentRecyclerView = mRootView.findViewById(R.id.test_iflytek_content);
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mContentSpeechTextAdapter = new SpeechTextAdapter(mContext,arrayList);
        mContentSpeechTextAdapter.setOnClickListener(new SpeechTextAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                if (SpeechSynthesizer.getSynthesizer().isSpeaking()){
                    SpeechSynthesizer.getSynthesizer().stopSpeaking();
                }
                SpeechSynthesizer.getSynthesizer().startSpeaking(arrayList.get(position), null);
            }
        });
        mContentRecyclerView.setAdapter(mContentSpeechTextAdapter);
        mWindowManager.addView(mRootView, params);


    }

    private void initTts() {
        SpeechSynthesizer.createSynthesizer(mContext, initListener);
    }

    private InitListener initListener = new InitListener() {
        @Override
        public void onInit(int i) {
            Log.d(TAG, "onInit() called with: i = [" + i + "]");
//            ENGINE_TYPE	引擎类型	通过此参数设置在线模式，在线语音合成设置为TYPE_CLOUD
//            voice_name	发音人	通过此参数设置不同的发音人，达到不同的语言和方言、性别等效果，默认发音人：xiaoyan
//            speed	合成语速	通过此参数，设置合成返回音频的语速，值范围：[0，100]，默认：50
//            volume	合成音量	通过此参数，设置合成返回音频的音量，值范围：[0，100]，默认：50
//            pitch	合成语调	通过此参数，设置合成返回音频的语调，值范围：[0，100]，默认：50
//            sample_rate	采样率	通过此参数设置音频的采样率，可选值：8000，16000，默认：16000
//            tts_audio_path	合成录音保存路径	通过此参数，可以在合成完成后在本地保存一个音频文件，值范围：有效的文件相对或绝对路径（含文件名），默认值：null
//            audio_format	音频格式	通过此参数设置合成音频文件格式，可选：pcm、wav，默认值：pcm

            SpeechSynthesizer.getSynthesizer().setParameter(SpeechConstant.VOLUME, "100");
            SpeechSynthesizer.getSynthesizer().setParameter(SpeechConstant.AUDIO_FORMAT_AUE, "raw");
            SpeechSynthesizer.getSynthesizer().setParameter(SpeechConstant.TTS_FADING, "true");
            SpeechSynthesizer.getSynthesizer().setParameter(SpeechConstant.TTS_AUDIO_PATH, mPath);
            //设置使用本地引擎
            SpeechSynthesizer.getSynthesizer().setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            //设置发音人资源路径
            SpeechSynthesizer.getSynthesizer().setParameter(ResourceUtil.TTS_RES_PATH,getResourcePath());
            //设置发音人
            SpeechSynthesizer.getSynthesizer().setParameter(SpeechConstant.VOICE_NAME, "xiaofeng");

            String voice_name = SpeechSynthesizer.getSynthesizer().getParameter(SpeechConstant.VOICE_NAME);
            String voice_volume = SpeechSynthesizer.getSynthesizer().getParameter(SpeechConstant.VOLUME);
            String voice_format = SpeechSynthesizer.getSynthesizer().getParameter(SpeechConstant.AUDIO_FORMAT_AUE);
            String voice_fading = SpeechSynthesizer.getSynthesizer().getParameter(SpeechConstant.TTS_FADING);
            String voice_path = SpeechSynthesizer.getSynthesizer().getParameter(SpeechConstant.TTS_AUDIO_PATH);
            Log.d(TAG, "onInit() called with:"
                    + "\n voice_name " + voice_name
                    + "\n voice_volume " + voice_volume
                    + "\n voice_format " + voice_format
                    + "\n voice_fading " + voice_fading
                    + "\n voice_path " + voice_path
            );
            // 16byte 1channle 16000hz
        }
    };


    private void updateData(String app){
        arrayList.clear();
        switch (app){
            case "美食":
                arrayList.addAll(SpeechContentProvider.msContent);
                break;
            case "美食二次操作":
                arrayList.addAll(SpeechContentProvider.msContentSelect);
                break;
            case "美食详情":
                arrayList.addAll(SpeechContentProvider.msContentDetail);
                break;
            case "酒店":
                arrayList.addAll(SpeechContentProvider.jddContent);
                break;
            case "酒店二次操作":
                arrayList.addAll(SpeechContentProvider.jddContentSelect);
                break;
            case "酒店详情":
                arrayList.addAll(SpeechContentProvider.jddContentDetail);
                break;
            case "景点":
                arrayList.addAll(SpeechContentProvider.jdContent);
                break;
            case "景点二次操作":
                arrayList.addAll(SpeechContentProvider.jdContentSelect);
                break;
            case "景点详情":
                arrayList.addAll(SpeechContentProvider.jdContentDetail);
                break;
            case "全局免唤醒":
                arrayList.addAll(SpeechContentProvider.inGlobleSpeechContent);
                break;
            case "音乐内免唤醒":
                arrayList.addAll(SpeechContentProvider.inMusicSpeechContent);
                break;
            case "导航内免唤醒":
                arrayList.addAll(SpeechContentProvider.inNaviSpeechContent);
                break;
            case "有声内免唤醒":
                arrayList.addAll(SpeechContentProvider.inRadioSpeechContent);
                break;
            case "视频内免唤醒":
                arrayList.addAll(SpeechContentProvider.inVideoSpeechContent);
                break;
            case "新闻内免唤醒":
                arrayList.addAll(SpeechContentProvider.inNewsSpeechContent);
                break;
            case "音乐":
                arrayList.addAll(SpeechContentProvider.musicContent);
                break;
            case "电台":
                arrayList.addAll(SpeechContentProvider.radioContent);
                break;
            case "新闻":
                arrayList.addAll(SpeechContentProvider.newsContent);
                break;
            case "视频":
                arrayList.addAll(SpeechContentProvider.videoContent);
                break;
            case "电话":
                arrayList.addAll(SpeechContentProvider.telContent);
                break;
            case "车机系统控制":
                arrayList.addAll(SpeechContentProvider.systemContent);
                break;
            case "天气":
                arrayList.addAll(SpeechContentProvider.weatherContent);
                break;
            case "美食冒烟测试":
                arrayList.addAll(SpeechContentProvider.foodContent);
                break;
            case "景点冒烟测试":
                arrayList.addAll(SpeechContentProvider.spotContent);
                break;
            case "酒店冒烟测试":
                arrayList.addAll(SpeechContentProvider.hotelContent);
                break;
            case "导航":
                arrayList.addAll(SpeechContentProvider.naviContent);
                break;
            case "电子手册":
                arrayList.addAll(SpeechContentProvider.eleContent);
                break;
            default:
                break;
        }

        mContentSpeechTextAdapter.notifyDataSetChanged();
    }


    private void uploadForeground(){
        AppStatusData appStatusData = new AppStatusData();
        NaviStatusBean naviStatusBean = new NaviStatusBean();
        // AppStatusConstant.NaviAppName.HOTEL 酒店
        // AppStatusConstant.NaviAppName.RESTAURANT 美食
        // AppStatusConstant.NaviAppName.SCENIC_SPOT 景点
        // AppStatusConstant.NaviAppName.MAPU 导航
        // AppStatusConstant.NaviAppName.PARKING 停车场
        naviStatusBean.setAppName(mCurForegroundApp);
        naviStatusBean.setSceneStatus(AppStatusConstant.SceneStatus.Navi.MORE_TARGET);
        // 前后台
        // AppStatusConstant.ActiveStatus.FOR_GROUNT
        // AppStatusConstant.ActiveStatus.BACK_GROUNT
        naviStatusBean.setActiveStatus(AppStatusConstant.ActiveStatus.FOR_GROUNT);
        naviStatusBean.setService(AppStatusConstant.StatusName.NAVI_POISEARCH);
        appStatusData.setStatusName(AppStatusConstant.StatusName.NAVI_POISEARCH);
        appStatusData.setAppStatusBean(naviStatusBean);
        DcsOpenAPI.getInstance().sendAppStatus(appStatusData);
    }

    private void uploadBackground(){
        AppStatusData appStatusData = new AppStatusData();
        NaviStatusBean naviStatusBean = new NaviStatusBean();
        // AppStatusConstant.NaviAppName.HOTEL 酒店
        // AppStatusConstant.NaviAppName.RESTAURANT 美食
        // AppStatusConstant.NaviAppName.SCENIC_SPOT 景点
        // AppStatusConstant.NaviAppName.MAPU 导航
        // AppStatusConstant.NaviAppName.PARKING 停车场
        naviStatusBean.setAppName(mCurForegroundApp);
        naviStatusBean.setSceneStatus(AppStatusConstant.SceneStatus.Navi.MORE_TARGET);
        // 前后台
        // AppStatusConstant.ActiveStatus.FOR_GROUNT
        // AppStatusConstant.ActiveStatus.BACK_GROUNT
        naviStatusBean.setActiveStatus(AppStatusConstant.ActiveStatus.BACK_GROUNT);
        naviStatusBean.setService(AppStatusConstant.StatusName.NAVI_POISEARCH);
        appStatusData.setStatusName(AppStatusConstant.StatusName.NAVI_POISEARCH);
        appStatusData.setAppStatusBean(naviStatusBean);
        DcsOpenAPI.getInstance().sendAppStatus(appStatusData);
    }

    //获取发音人资源路径
    private String getResourcePath(){
        StringBuffer tempBuffer = new StringBuffer();
        String tts = "tts";
        String speaker = "xiaofeng";
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, tts+"/common.jet"));
        tempBuffer.append(";");
        //发音人资源
//        if(mEngineType.equals(SpeechConstant.TYPE_XTTS)){
//            tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, type+"/"+TtsDemo.voicerXtts+".jet"));
//        }else {
//            tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, type + "/" + TtsDemo.voicerLocal + ".jet"));
//        }

        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, tts + "/" + speaker + ".jet"));


        return tempBuffer.toString();
    }

    private void enterDetail(){
        if (mCurForegroundApp.equals(AppStatusConstant.NaviAppName.RESTAURANT)){
            RestaurantBean dcsBean = new RestaurantBean();
            dcsBean.setSid("Sb2yV5B4ip5Vkmwq5s6ZPw");
            dcsBean.setDistance("2.1746345");
            RestaurantBean info = (RestaurantBean) dcsBean;
            IvokaVoiceDetailBean dcsEvent = new IvokaVoiceDetailBean();
            dcsEvent.setClassify(DcsClassify.REQUEST_TABLE_DETAIL);
            dcsEvent.setCurrentId(info.getSid());
            dcsEvent.setDistance(info.getDistance());
            dcsEvent.setIntentName("FOODS_DETAILS_INTENT");
            dcsEvent.setSkillId("2019052800000485");
            DcsOpenAPI.getInstance().sendEvent(dcsEvent);
        }else if (mCurForegroundApp.equals(AppStatusConstant.NaviAppName.SCENIC_SPOT)){
            TouristAttractionBean touristAttractionBean = new TouristAttractionBean();
            touristAttractionBean.setSid("em_hMtfKgM4pV2_PSLWwXQ");
            touristAttractionBean.setDistance("3.6929102");
            touristAttractionBean.setLng(123.437345);
            touristAttractionBean.setLat(41.749655);

            IvokaVoiceDetailBean dcsEvent = new IvokaVoiceDetailBean();
            dcsEvent.setClassify(DcsClassify.REQUEST_TABLE_DETAIL);
            dcsEvent.setCurrentId(touristAttractionBean.getSid());
            dcsEvent.setLng(touristAttractionBean.getLng() + "");
            dcsEvent.setLatitude(touristAttractionBean.getLat()+"");
            dcsEvent.setLongitude(touristAttractionBean.getLng()+"");
            dcsEvent.setDistance(touristAttractionBean.getDistance());
            dcsEvent.setIntentName("VIEW_SPOT_DETAIL_NUM");
            dcsEvent.setSkillId("2019061500000163");
            DcsOpenAPI.getInstance().sendEvent(dcsEvent);
        }else if (mCurForegroundApp.equals(AppStatusConstant.NaviAppName.HOTEL)){

            HotelBean hotelBean = new HotelBean();
            hotelBean.setSid("c15Ts9PTe6VGf_txadKAxw");
            hotelBean.setDistance("2.9km");

            IvokaVoiceDetailBean dcsEvent = new IvokaVoiceDetailBean();
            dcsEvent.setClassify(DcsClassify.REQUEST_TABLE_DETAIL);
            dcsEvent.setCurrentId(hotelBean.getSid());
            dcsEvent.setDistance(hotelBean.getDistance());
            dcsEvent.setIntentName("HOTEL_DETAIL");
            dcsEvent.setSkillId("2019051300000052");
            DcsOpenAPI.getInstance().sendEvent(dcsEvent);
        }else {
            Log.d(TAG, "enterDetail() called mCurForegroundApp:"+mCurForegroundApp);
        }


    }

    private void detailFore(){
        AppStatusData appStatusData = new AppStatusData();
        NaviStatusBean naviStatusBean = new NaviStatusBean();
        if (mCurForegroundApp.equals(AppStatusConstant.NaviAppName.RESTAURANT)){
            naviStatusBean.setAppName(AppStatusConstant.NaviAppName.RESTAURANT);
        }else if (mCurForegroundApp.equals(AppStatusConstant.NaviAppName.SCENIC_SPOT)){
            naviStatusBean.setAppName(AppStatusConstant.NaviAppName.SCENIC_SPOT);
        }else if (mCurForegroundApp.equals(AppStatusConstant.NaviAppName.HOTEL)){
            naviStatusBean.setAppName(AppStatusConstant.NaviAppName.HOTEL);
        }else {
            Log.d(TAG, "FoodDetailBack() called mCurForegroundApp:"+mCurForegroundApp);
            return;
        }
        naviStatusBean.setSceneStatus(AppStatusConstant.SceneStatus.Navi.TARGET_INFO);
        naviStatusBean.setActiveStatus(AppStatusConstant.ActiveStatus.FOR_GROUNT);
        naviStatusBean.setService(AppStatusConstant.StatusName.NAVI_POISEARCH);
        appStatusData.setStatusName(AppStatusConstant.StatusName.NAVI_POISEARCH);
        appStatusData.setAppStatusBean(naviStatusBean);
        DcsOpenAPI.getInstance().sendAppStatus(appStatusData);
    }

    private void detailBack(){
        AppStatusData appStatusData = new AppStatusData();
        NaviStatusBean naviStatusBean = new NaviStatusBean();
        if (mCurForegroundApp.equals(AppStatusConstant.NaviAppName.RESTAURANT)){
            naviStatusBean.setAppName(AppStatusConstant.NaviAppName.RESTAURANT);
        }else if (mCurForegroundApp.equals(AppStatusConstant.NaviAppName.SCENIC_SPOT)){
            naviStatusBean.setAppName(AppStatusConstant.NaviAppName.SCENIC_SPOT);
        }else if (mCurForegroundApp.equals(AppStatusConstant.NaviAppName.HOTEL)){
            naviStatusBean.setAppName(AppStatusConstant.NaviAppName.HOTEL);
        }else {
            Log.d(TAG, "detailBack() called mCurForegroundApp:"+mCurForegroundApp);
            return;
        }
        naviStatusBean.setSceneStatus(AppStatusConstant.SceneStatus.Navi.TARGET_INFO);
        naviStatusBean.setActiveStatus(AppStatusConstant.ActiveStatus.BACK_GROUNT);
        naviStatusBean.setService(AppStatusConstant.StatusName.NAVI_POISEARCH);
        appStatusData.setStatusName(AppStatusConstant.StatusName.NAVI_POISEARCH);
        appStatusData.setAppStatusBean(naviStatusBean);
        DcsOpenAPI.getInstance().sendAppStatus(appStatusData);
    }

}