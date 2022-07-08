package com.huawei.ivi.hmi.launcher.ui;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.huawei.ivi.hmi.launcher.R;
import com.huawei.ivi.hmi.launcher.databinding.FragmentControlBinding;
import com.huawei.ivi.hmi.launcher.databinding.FragmentTaskBinding;
import com.huawei.ivi.hmi.launcher.viewmodel.ControlViewModel;
import com.huawei.ivi.hmi.launcher.viewmodel.TaskViewModel;

public class TaskFragment extends BaseFragment<FragmentTaskBinding, TaskViewModel>{


    @Override
    int getLayoutId() {
        return R.layout.fragment_task;
    }

    @Override
    void initViewModel() {
    }

    @Override
    void initView() {
        mBinding.taskBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("xLLL","click test 1");
            }
        });
        mBinding.taskBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("xLLL","click btn");
                Toast.makeText(getContext(),"click!!!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
