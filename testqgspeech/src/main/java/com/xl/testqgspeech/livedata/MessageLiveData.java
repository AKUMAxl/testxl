package com.xl.testqgspeech.livedata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xl.testqgspeech.bean.messageBean.BaseMessageBean;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MessageLiveData extends LiveData<List<BaseMessageBean>> {

    MutableLiveData<List<BaseMessageBean>> mBaseMessageBeans = new MutableLiveData<>();

    @Inject
    public MessageLiveData() {
    }

    public void setMessage(List<BaseMessageBean> baseMessageBeans){
        mBaseMessageBeans.setValue(baseMessageBeans);
    }

    public void postMessage(List<BaseMessageBean> baseMessageBeans){
        mBaseMessageBeans.postValue(baseMessageBeans);
    }

    public MutableLiveData<List<BaseMessageBean>> getMessageData(){
        List<BaseMessageBean> data = new ArrayList<BaseMessageBean>();
        setMessage(data);
        return mBaseMessageBeans;
    }

}