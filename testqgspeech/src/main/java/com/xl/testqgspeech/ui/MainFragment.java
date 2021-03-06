package com.xl.testqgspeech.ui;


import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.xl.testqgspeech.R;
import com.xl.testqgspeech.viewmodel.MainFragmentViewModel;

public class MainFragment extends BaseFragment {

    MainFragmentViewModel mViewModel;

    @Override
    int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    void initViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(MainFragmentViewModel.class);
        mViewModel.getData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });
    }

    @Override
    void initView() {
//        mRootView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(mRootView).navigate(R.id.action_main_fragment_to_help_fragment);
//            }
//        });
    }


}
