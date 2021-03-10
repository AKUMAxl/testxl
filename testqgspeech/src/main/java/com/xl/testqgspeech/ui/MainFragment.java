package com.xl.testqgspeech.ui;


import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.xl.testqgspeech.Contants;
import com.xl.testqgspeech.R;
import com.xl.testqgspeech.bean.messageBean.BaseMessageBean;
import com.xl.testqgspeech.databinding.FragmentMainBinding;
import com.xl.testqgspeech.ui.adapter.MessageAdapter;
import com.xl.testqgspeech.viewmodel.MainFragmentViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends BaseFragment<FragmentMainBinding> {

    private MainFragmentViewModel mViewModel;

    private SkillCenterFragment mSkillCenterFragment = SkillCenterFragment.newInstance();
    private ImageFragment mImageFragment = ImageFragment.newInstance();
    private VoiceFragment mVoiceFragment = VoiceFragment.newInstance();
    private SettingFragment mSettingFragment = SettingFragment.newInstance();

    private MessageAdapter mMessageAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    void initViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(MainFragmentViewModel.class);
        mViewModel.getMessageData().observe(this, baseMessageBeans -> {
            for (BaseMessageBean baseMessageBean:baseMessageBeans){
                Log.d("xLLL","bean:"+baseMessageBean.toString());
            }
            mMessageAdapter.updateData(baseMessageBeans);
        });
    }

    @Override
    void initView() {
        mBinding.viewPager2.setUserInputEnabled(false);
        mBinding.viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(mSkillCenterFragment);
        fragments.add(mImageFragment);
        fragments.add(mVoiceFragment);
        fragments.add(mSettingFragment);
        mBinding.viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });
        mBinding.mainBtnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        mBinding.mainTvTitle.setOnClickListener(v -> {
            mBinding.viewPager2.setCurrentItem(fragments.indexOf(mSkillCenterFragment));
            updateLayoutParams(false);
        });
        mBinding.mainIvImage.setOnClickListener(v -> {
            mBinding.viewPager2.setCurrentItem(fragments.indexOf(mImageFragment));
            updateLayoutParams(false);
        });
        mBinding.mainIvVoice.setOnClickListener(v -> {
            mBinding.viewPager2.setCurrentItem(fragments.indexOf(mVoiceFragment));
            updateLayoutParams(false);
        });
        mBinding.mainIvSetting.setOnClickListener(v -> {
            mBinding.viewPager2.setCurrentItem(fragments.indexOf(mSettingFragment));
            updateLayoutParams(true);
        });
        int item = requireActivity().getIntent().getIntExtra(Contants.EXTRA_KEY.INDEX,0);
        mBinding.viewPager2.setCurrentItem(item);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mMessageAdapter = new MessageAdapter(getContext(),null);
        mBinding.mainRc.setLayoutManager(layoutManager);
        mBinding.mainRc.setAdapter(mMessageAdapter);
        mMessageAdapter.setOnItemClickListener(position -> {

            Log.d("xLLL","click:"+position);
        });
    }

    private void updateLayoutParams(boolean matchParent){
        ViewGroup.LayoutParams layoutParams = mBinding.viewPager2.getLayoutParams();
        if (matchParent){
            layoutParams.width = 1096;
        }else {
            layoutParams.width = 548;
        }
        mBinding.viewPager2.requestLayout();
    }

}
