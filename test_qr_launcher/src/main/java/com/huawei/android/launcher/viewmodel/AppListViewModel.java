package com.huawei.android.launcher.viewmodel;

import android.content.pm.PackageInfo;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.huawei.android.launcher.bean.AppInfoBean;
import com.huawei.android.launcher.manager.PackagesManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AppListViewModel extends ViewModel {

    @Inject
    @ViewModelInject
    public AppListViewModel(@Assisted SavedStateHandle savedStateHandle){

    }

    public List<AppInfoBean> getAppNameList(){
        return PackagesManager.getInstance().getPackageList();
    }

    public void startApp(String packageName){
        PackagesManager.getInstance().startApp(packageName);
    }


}
