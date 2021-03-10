package com.xl.testqgspeech.ui;

import com.xl.testqgspeech.R;
import com.xl.testqgspeech.databinding.FragmentSkillCenterBinding;

public class SkillCenterFragment extends BaseFragment<FragmentSkillCenterBinding>{

    public static SkillCenterFragment newInstance(){
        SkillCenterFragment skillCenterFragment = new SkillCenterFragment();
        return skillCenterFragment;
    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_skill_center;
    }

    @Override
    void initViewModel() {

    }

    @Override
    void initView() {

    }
}
