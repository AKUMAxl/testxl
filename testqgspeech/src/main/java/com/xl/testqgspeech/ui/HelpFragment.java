package com.xl.testqgspeech.ui;

import android.view.View;

import androidx.navigation.Navigation;

import com.xl.testqgspeech.R;
import com.xl.testqgspeech.databinding.FragmentHelpBinding;

import java.util.Objects;

public class HelpFragment extends BaseFragment<FragmentHelpBinding>{
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_help;
    }

    @Override
    void initViewModel() {

    }

    @Override
    void initView() {
        mBinding.fragmentHelpTvVoiceSkillExplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_help_fragment_to_main_fragment);
            }
        });
        mBinding.fragmentHelpBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
            }
        });
    }


}
