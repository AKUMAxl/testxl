package com.xl.testqgspeech.ui;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.xl.testqgspeech.Contants;
import com.xl.testqgspeech.R;
import com.xl.testqgspeech.Test;
import com.xl.testqgspeech.bean.messageBean.BaseMessageBean;
import com.xl.testqgspeech.ui.adapter.BaseAdapter;
import com.xl.testqgspeech.ui.adapter.MessageAdapter;
import com.xl.testqgspeech.viewmodel.MainFragmentViewModel;
import com.xl.testqgspeech.databinding.FragmentMainBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.EntryPoint;
import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.WithFragmentBindings;

@AndroidEntryPoint
public class MainFragment extends BaseFragment<FragmentMainBinding> {

    private MainFragmentViewModel mViewModel;

    private ImageFragment mImageFragment = ImageFragment.newInstance();
    private VoiceFragment mVoiceFragment = VoiceFragment.newInstance();
    private SettingFragment mSettingFragment = SettingFragment.newInstance();

    private MessageAdapter mMessageAdapter;

    @Inject Test test;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    void initViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(MainFragmentViewModel.class);
        mViewModel.getMessageData().observe(this, new Observer<List<BaseMessageBean>>() {
            @Override
            public void onChanged(List<BaseMessageBean> baseMessageBeans) {
                for (BaseMessageBean baseMessageBean:baseMessageBeans){

                    Log.d("xLLL","bean:"+baseMessageBean.toString());
                }
//                mMessageAdapter.updateData(baseMessageBeans);
            }
        });
    }

    @Override
    void initView() {
        mBinding.viewPager2.setUserInputEnabled(false);
        mBinding.viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        ArrayList<BaseFragment> fragments = new ArrayList<>();
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
        mBinding.mainBtnBack.setOnClickListener(v -> getActivity().finish());
        mBinding.mainIvImage.setOnClickListener(v -> mBinding.viewPager2.setCurrentItem(fragments.indexOf(mImageFragment)));
        mBinding.mainIvVoice.setOnClickListener(v -> mBinding.viewPager2.setCurrentItem(fragments.indexOf(mVoiceFragment)));
        mBinding.mainIvSetting.setOnClickListener(v -> mBinding.viewPager2.setCurrentItem(fragments.indexOf(mSettingFragment)));
        int item = getActivity().getIntent().getIntExtra(Contants.EXTRA_KEY.INDEX,0);
        mBinding.viewPager2.setCurrentItem(item);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        ArrayList<BaseMessageBean> beans = new ArrayList<>();
        beans.add(new BaseMessageBean(1,"3333","000"));
        beans.add(new BaseMessageBean(1,"4444","000"));
        beans.add(new BaseMessageBean(1,"4444","000"));
        beans.add(new BaseMessageBean(1,"4444","000"));
        beans.add(new BaseMessageBean(1,"4444","000"));
        beans.add(new BaseMessageBean(1,"4444","000"));

        mMessageAdapter = new MessageAdapter(getContext(),beans);
        mBinding.mainRc.setLayoutManager(layoutManager);
        mBinding.mainRc.setAdapter(mMessageAdapter);
        mMessageAdapter.setOnItemClickListener(position -> {
            Log.d("xLLL","click:"+position);
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        test.get();
    }
}
