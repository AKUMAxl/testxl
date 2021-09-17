package com.huawei.ivi.hmi.launcher.viewmodel;

import javax.inject.Inject;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    @Inject
    @ViewModelInject
    public MainViewModel(@Assisted SavedStateHandle savedStateHandle){

    }

}
