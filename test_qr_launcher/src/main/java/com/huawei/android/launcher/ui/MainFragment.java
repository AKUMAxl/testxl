package com.huawei.android.launcher.ui;


import android.view.View;

import androidx.navigation.Navigation;

import com.huawei.android.launcher.R;
import com.huawei.android.launcher.databinding.FragmentMainQiruiBinding;
import com.huawei.android.launcher.viewmodel.MainViewModel;


public class MainFragment extends BaseFragment<FragmentMainQiruiBinding, MainViewModel> {

    private final String TAG = MainFragment.class.getSimpleName();

    @Override
    int getLayoutId() {
        return R.layout.fragment_main_qirui;
    }

    @Override
    void initViewModel() {

    }

    @Override
    void initView() {
        mBinding.btnAppList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_main_to_app_list);
            }
        });
    }

    
}
