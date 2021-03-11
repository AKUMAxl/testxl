package com.xl.testqgspeech.viewmodel;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.xl.testqgspeech.bean.voiceBean.HelpDataNewBean;
import com.xl.testqgspeech.livedata.HelpDataLiveData;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HelpViewModel extends ViewModel {

    @Inject
    HelpDataLiveData mHelpDataLiveData;

    @Inject
    @ViewModelInject
    public HelpViewModel(@Assisted SavedStateHandle savedStateHandle){

    }

    public MutableLiveData<ArrayList<HelpDataNewBean>> getHelpData(){
        if (mHelpDataLiveData == null){
            mHelpDataLiveData = new HelpDataLiveData();
        }
        return mHelpDataLiveData.getHelpData();
    }

}
