package com.huawei.android.launcher.ui;

import android.content.pm.PackageInfo;
import android.view.View;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.huawei.android.launcher.R;
import com.huawei.android.launcher.bean.AppInfoBean;
import com.huawei.android.launcher.databinding.FragmentAppListBinding;
import com.huawei.android.launcher.ui.adapter.AppListAdapter;
import com.huawei.android.launcher.ui.adapter.BaseAdapter;
import com.huawei.android.launcher.viewmodel.AppListViewModel;

import java.util.ArrayList;
import java.util.List;

public class AppListFragment extends BaseFragment<FragmentAppListBinding, AppListViewModel>{

    private AppListAdapter mAdapter;
    private List<AppInfoBean> appInfoBeans = new ArrayList<>();

    @Override
    int getLayoutId() {
        return R.layout.fragment_app_list;
    }

    @Override
    void initViewModel() {
        appInfoBeans = mViewModel.getAppNameList();
    }

    @Override
    void initView() {
        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigateUp();
            }
        });
        mBinding.rvAppList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AppListAdapter(getContext(),appInfoBeans);
        mBinding.rvAppList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object o) {
                AppInfoBean appInfoBean = (AppInfoBean) o;
                mViewModel.startApp(appInfoBean.getPackageName());
            }
        });
    }
}
