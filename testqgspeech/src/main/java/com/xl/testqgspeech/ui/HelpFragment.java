package com.xl.testqgspeech.ui;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.xl.testqgspeech.R;
import com.xl.testqgspeech.bean.voiceBean.HelpDataNewBean;
import com.xl.testqgspeech.databinding.FragmentHelpBinding;
import com.xl.testqgspeech.ui.adapter.HelpDataAdapter;
import com.xl.testqgspeech.viewmodel.HelpViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class HelpFragment extends BaseFragment<FragmentHelpBinding>{

    private HelpViewModel mHelpViewModel;

    private HelpDataAdapter mHelpDataAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_help;
    }

    @Override
    void initViewModel() {
        mHelpViewModel = new ViewModelProvider(requireActivity()).get(HelpViewModel.class);
        mHelpViewModel.getHelpData().observe(this, new Observer<ArrayList<HelpDataNewBean>>() {
            @Override
            public void onChanged(ArrayList<HelpDataNewBean> helpDataNewBeans) {
                Log.d("xLLL","help data:"+helpDataNewBeans.toString());
                mHelpDataAdapter.updateData(helpDataNewBeans);
            }
        });
    }

    @Override
    void initView() {
        mBinding.fragmentHelpRc.setLayoutManager(new GridLayoutManager(getContext(),2));
        mHelpDataAdapter = new HelpDataAdapter(getContext(),null);
        mBinding.fragmentHelpRc.setAdapter(mHelpDataAdapter);
//        mBinding.fragmentHelpRc.addItemDecoration();

        mBinding.fragmentHelpTvVoiceSkillExplain.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_help_fragment_to_main_fragment));
        mBinding.fragmentHelpBtnBack.setOnClickListener(v -> requireActivity().finish());
    }


}
