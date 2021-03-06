package com.xl.testqgspeech.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainFragmentViewModel extends ViewModel {


    public LiveData<String> getData(){
        return new MutableLiveData<>();
    }

}
