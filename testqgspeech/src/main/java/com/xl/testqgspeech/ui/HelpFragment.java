package com.xl.testqgspeech.ui;

import android.util.Log;
import android.view.View;


import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.xl.testqgspeech.R;
import com.xl.testqgspeech.bean.voiceBean.HelpDataNewBean;
import com.xl.testqgspeech.databinding.FragmentHelpBinding;
import com.xl.testqgspeech.ui.adapter.BaseAdapter;
import com.xl.testqgspeech.ui.adapter.HelpDataAdapter;
import com.xl.testqgspeech.viewmodel.HelpViewModel;


public class HelpFragment extends BaseFragment<FragmentHelpBinding>{


    public static final int SPAN_COUNT = 2;

    private HelpDataAdapter mHelpDataAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_help;
    }

    @Override
    void initViewModel() {
        HelpViewModel mHelpViewModel = new ViewModelProvider(requireActivity()).get(HelpViewModel.class);
        mHelpViewModel.getHelpData().observe(this, helpDataNewBeans -> mHelpDataAdapter.updateData(helpDataNewBeans));
    }

    @Override
    void initView() {
        mHelpDataAdapter = new HelpDataAdapter(getContext(),null);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),SPAN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mHelpDataAdapter.getItemViewType(position) == HelpDataAdapter.HELP_DATA_TYPE_TITLE){
                    return SPAN_COUNT;
                }
                return 1;
            }
        });
        mBinding.fragmentHelpRc.setLayoutManager(gridLayoutManager);
        mBinding.fragmentHelpRc.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mHelpDataAdapter.setOnItemClickListener((BaseAdapter.OnItemClickListener) (position, o) -> Log.d("xLLL","help data:"+((HelpDataNewBean)o).toString()));
        mBinding.fragmentHelpRc.setAdapter(mHelpDataAdapter);
        mBinding.fragmentHelpTvVoiceSkillExplain.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_help_fragment_to_main_fragment));
        mBinding.fragmentHelpBtnBack.setOnClickListener(v -> requireActivity().finish());
    }


}
