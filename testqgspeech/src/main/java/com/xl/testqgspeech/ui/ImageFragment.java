package com.xl.testqgspeech.ui;


import com.xl.testqgspeech.R;
import com.xl.testqgspeech.databinding.FragmentImageBinding;


public class ImageFragment extends BaseFragment<FragmentImageBinding> {


    public static ImageFragment newInstance(){
        ImageFragment imageFragment = new ImageFragment();
        return imageFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image;
    }

    @Override
    void initViewModel() {

    }

    @Override
    void initView() {

    }
}