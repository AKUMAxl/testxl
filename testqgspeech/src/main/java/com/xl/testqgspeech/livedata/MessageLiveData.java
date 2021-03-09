package com.xl.testqgspeech.livedata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xl.testqgspeech.bean.messageBean.BaseMessageBean;
import com.xl.testqgspeech.ui.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class MessageLiveData extends LiveData<List<BaseMessageBean>> {

    MutableLiveData<List<BaseMessageBean>> mBaseMessageBeans = new MutableLiveData<>();

    @Inject
    public MessageLiveData(){

    }

    public void setMessage(List<BaseMessageBean> baseMessageBeans){
        mBaseMessageBeans.setValue(baseMessageBeans);
    }

    public void postMessage(List<BaseMessageBean> baseMessageBeans){
        mBaseMessageBeans.postValue(baseMessageBeans);
    }

    public MutableLiveData<List<BaseMessageBean>> getMessageData(){
        List<BaseMessageBean> data = new ArrayList<BaseMessageBean>();
        data.add(new BaseMessageBean(0,"11111111","2021"));
        data.add(new BaseMessageBean(0,"22222222","2021"));
        setMessage(data);
        return mBaseMessageBeans;
    }
}
