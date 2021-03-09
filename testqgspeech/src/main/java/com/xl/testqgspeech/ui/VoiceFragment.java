package com.xl.testqgspeech.ui;

import com.xl.testqgspeech.R;
import com.xl.testqgspeech.databinding.FragmentVoiceBinding;

public class VoiceFragment extends BaseFragment<FragmentVoiceBinding>{

    public static VoiceFragment newInstance(){
        VoiceFragment voiceFragment = new VoiceFragment();
        return voiceFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_voice;
    }

    @Override
    void initViewModel() {

    }

    @Override
    void initView() {

    }
}
