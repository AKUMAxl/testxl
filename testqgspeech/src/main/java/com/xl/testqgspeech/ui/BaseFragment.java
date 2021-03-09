package com.xl.testqgspeech.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import dagger.hilt.android.AndroidEntryPoint;



public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {

    T mBinding;

    View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(),getLayoutId(),container,false);
        mRootView = mBinding.getRoot();
        mBinding.setLifecycleOwner(getActivity());
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        initView();
    }

    abstract int getLayoutId();

    abstract void initViewModel();

    abstract void initView();
}
