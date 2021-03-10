package com.xl.testqgspeech.viewmodel;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.xl.testqgspeech.bean.messageBean.BaseMessageBean;
import com.xl.testqgspeech.livedata.MessageLiveData;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainFragmentViewModel extends ViewModel {


//    @Inject
//    MainFragmentViewModel(){
//
//    }

    @Inject
    @ViewModelInject
    public MainFragmentViewModel(@Assisted SavedStateHandle savedStateHandle){

    }


    @Inject
    MessageLiveData mMessageLiveData;

    public MutableLiveData<List<BaseMessageBean>> getMessageData(){
        return mMessageLiveData.getMessageData();
    }

}
