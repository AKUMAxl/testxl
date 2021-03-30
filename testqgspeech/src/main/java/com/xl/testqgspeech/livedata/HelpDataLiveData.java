package com.xl.testqgspeech.livedata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xl.testqgspeech.bean.messageBean.BaseMessageBean;
import com.xl.testqgspeech.bean.voiceBean.HelpDataNewBean;
import com.xl.testqgspeech.util.HelpDataParser;
import com.xl.testqgspeech.util.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.HiltAndroidApp;


@Singleton
public class HelpDataLiveData extends LiveData<ArrayList<HelpDataNewBean>> {

    @Inject
    HelpDataParser mHelpDataParser;

    MutableLiveData<ArrayList<HelpDataNewBean>> mHelpDataNewBeans = new MutableLiveData<>();

    @Inject
    public HelpDataLiveData(){

    }

    public MutableLiveData<ArrayList<HelpDataNewBean>> getHelpData(){
        ThreadPoolUtil.getInstance().execute(() -> mHelpDataNewBeans.postValue(mHelpDataParser.getWakeUpHelpData()));
        return mHelpDataNewBeans;
    }

}
