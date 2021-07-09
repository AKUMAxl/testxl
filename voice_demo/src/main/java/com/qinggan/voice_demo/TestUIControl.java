package com.qinggan.voice_demo;

import android.content.Context;
import android.util.Log;

import com.qinggan.speech.UIControl;
import com.qinggan.speech.UIControlCallback;
import com.qinggan.speech.UIControlElementItem;
import com.qinggan.speech.UIControlItem;
import com.qinggan.speech.UIControlListener;
import com.qinggan.speech.UIControlSceneID;
import com.qinggan.speech.VuiServiceMgr;
import com.qinggan.speech.VuiTtsObj;
import com.qinggan.speech.internal.IVuiTtsProcessHandler;

import java.util.ArrayList;

public class TestUIControl {

    public static final String TAG = TestUIControl.class.getSimpleName();
    
    public static final String IDENTIFY = "to_up";

    private VuiServiceMgr mVuiServiceMgr;

    /**
     * 初始化
     * @param context
     */
    public void init(Context context){
        UIControl.getInstance().init(context);
    }


    public void setVuiServiceMgr(VuiServiceMgr vuiServiceMgr){
        this.mVuiServiceMgr = vuiServiceMgr;
    }

    /**
     * 注册列表相关免唤醒  需要先调用初始化 {@link #init}
     */
    public void registerListUIControl(){
        // 保护机制 防止其他应用没有释放 导致本场景的UIControl失效
        UIControl.getInstance().clearElementUCWords();
        UIControl.getInstance().clearListUCWords();

        ArrayList<UIControlItem> listUIControl = new ArrayList<>();

        UIControlItem uiControlItem1 = new UIControlItem();
        uiControlItem1.setLabel("北陵公园");
        uiControlItem1.setIndex(0);
        uiControlItem1.setUrl("list_select:1"); // list_select:1 自己定义即可
        // 用于音乐 电台 等 UIControlSceneID.SCENE_MUSIC;
        // 用于导航 UIControlSceneID.SCENE_POI;
        /// 用于美食 景点 酒店 UIControlSceneID.SCENE_RESTAURANT:
        uiControlItem1.setScene(UIControlSceneID.SCENE_POI);

        UIControlItem uiControlItem2 = new UIControlItem();
        uiControlItem2.setLabel("东陵公园");
        uiControlItem2.setIndex(2);
        uiControlItem2.setUrl("list_select:2"); // list_select:1 自己定义即可
        // 用于音乐 电台 等 UIControlSceneID.SCENE_MUSIC;
        // 用于导航 UIControlSceneID.SCENE_POI;
        /// 用于美食 景点 酒店 UIControlSceneID.SCENE_RESTAURANT:
        uiControlItem2.setScene(UIControlSceneID.SCENE_POI);

        listUIControl.add(uiControlItem1);
        listUIControl.add(uiControlItem2);

        // 第二个参数为共有几页
        UIControl.getInstance().setListUCWords(listUIControl,2);
        UIControl.getInstance().registerUIControlCallback(new UIControlCallback() {

            @Override
            public void onItemSelected(String s) {
                Log.d(TAG, "onItemSelected() called with: s = [" + s + "]");
                // 选中第几个 返回参数为setUrl的值
                speakTTS("收到");
            }

            @Override
            public void onPageSelected(int i) {
                Log.d(TAG, "onPageSelected() called with: i = [" + i + "]");
                // 第几页
            }

            @Override
            public void onSwitchPage(int i) {
                Log.d(TAG, "onSwitchPage() called with: i = [" + i + "]");
                // 无需关注
            }


        });
    }

    /**
     * 注册普通免唤醒 需要先调用初始化 {@link #init}
     */
    public void registerUIControl(){
        // 保护机制 防止其他应用没有释放 导致本场景的UIControl失效
        UIControl.getInstance().clearElementUCWords();
        UIControl.getInstance().clearListUCWords();

        UIControlElementItem uiControlElementItem = new UIControlElementItem();
        uiControlElementItem.setIdentify(IDENTIFY);
        uiControlElementItem.setWord("车头朝上");
        ArrayList<UIControlElementItem> list = new ArrayList<>();
        list.add(uiControlElementItem);
        UIControl.getInstance().registerOnUCSelectedListener(new UIControlListener() {
            @Override
            public void onItemClick(String s, boolean b) {
                Log.d(TAG, "onItemClick() called with: s = [" + s + "], b = [" + b + "]");
                // 回调参数 s 为setIdentify的值
            }
        });


        UIControl.getInstance().setElementUCWords(list);

    }


    public void unRegisterUIControl(){
        UIControl.getInstance().clearElementUCWords();
        UIControl.getInstance().clearListUCWords();
        UIControl.getInstance().unRegisterOnUCSelectedListener();
    }


    /**
     * TTS 需要调用 {@link VuiServiceMgr#getInstance(Context, VuiServiceMgr.VuiConnectionCallback)}
     *     在回调 {@link VuiServiceMgr.VuiConnectionCallback#onServiceConnected()} 后调用此方法
     * @param content
     */
    public void speakTTS(String content) {
        VuiTtsObj ttsObj = new VuiTtsObj("com.packgeName", VuiTtsObj.TtsContentType.TEXT);// "com.packageName" 需要替换为应用自己的包名
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
