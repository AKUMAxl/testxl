package com.xl.testqgspeech.ui;

import android.view.View;

import androidx.navigation.Navigation;

import com.xl.testqgspeech.R;
import com.xl.testqgspeech.databinding.FragmentHelpBinding;

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
        mRootView.findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(mRootView).navigateUp();
            }
        });
    }


}
