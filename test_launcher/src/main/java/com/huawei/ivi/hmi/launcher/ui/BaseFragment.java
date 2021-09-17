package com.huawei.ivi.hmi.launcher.ui;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public abstract class BaseFragment<T extends ViewDataBinding,M extends ViewModel> extends Fragment {

    T mBinding;
    M mViewModel;
    View mRootView;

    GestureDetector mGestureDetector;

    private boolean mIsShow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(getLayoutInflater(),getLayoutId(),container,false);
        if (getGestureListener()!=null){
            mGestureDetector = new GestureDetector(getActivity(),getGestureListener());
            HomeActivity.OnTouchListener onTouchListener = new HomeActivity.OnTouchListener() {
                @Override
                public boolean onTouch(MotionEvent motionEvent) {
                    return mGestureDetector.onTouchEvent(motionEvent);
                }
            };
            ((HomeActivity)getActivity()).registerTouchListener(onTouchListener);
        }
        mRootView = mBinding.getRoot();
        mBinding.setLifecycleOwner(getActivity());
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType =(ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        Class<M> viewModelClass = (Class<M>) types[1];
        mViewModel = new ViewModelProvider(requireActivity()).get(viewModelClass);
        initViewModel();
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsShow = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsShow = false;
    }

    GestureDetector.OnGestureListener getGestureListener(){
        return null;
    };

    boolean isShowing(){
        return mIsShow;
    }

    abstract int getLayoutId();

    abstract void initViewModel();

    abstract void initView();


}
