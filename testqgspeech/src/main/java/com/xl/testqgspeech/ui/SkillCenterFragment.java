package com.xl.testqgspeech.ui;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.xl.testqgspeech.R;
import com.xl.testqgspeech.databinding.FragmentSkillCenterBinding;
import com.xl.testqgspeech.databinding.LayoutQuestionCommonBinding;

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
        View layoutQuestionCommon = mBinding.fragmentSkillCenterVsRequestCommon.getViewStub().inflate();
        LayoutQuestionCommonBinding layoutQuestionCommonBinding = DataBindingUtil.getBinding(layoutQuestionCommon);


    }
}
