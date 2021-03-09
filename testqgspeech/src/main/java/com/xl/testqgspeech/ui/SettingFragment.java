package com.xl.testqgspeech.ui;

import com.xl.testqgspeech.R;
import com.xl.testqgspeech.databinding.FragmentSettingBinding;

public class SettingFragment extends BaseFragment<FragmentSettingBinding>{

    public static SettingFragment newInstance(){
        SettingFragment settingFragment = new SettingFragment();
        return settingFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    void initViewModel() {

    }

    @Override
    void initView() {

    }
}
