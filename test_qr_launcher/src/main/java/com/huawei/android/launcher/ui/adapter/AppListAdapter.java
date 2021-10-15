package com.huawei.android.launcher.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.android.launcher.R;
import com.huawei.android.launcher.bean.AppInfoBean;
import com.huawei.android.launcher.databinding.ItemAppInfoBinding;

import java.util.List;

public class AppListAdapter extends BaseAdapter<AppInfoBean, AppListAdapter.AppListHolder>{


    public AppListAdapter(Context context, List<AppInfoBean> appNameList) {
        super(context, appNameList);
    }

    @NonNull
    @Override
    public AppListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AppListHolder appListHolder;
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.item_app_info,parent,false);
        appListHolder = new AppListHolder(view);
        return appListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppListHolder holder, @SuppressLint("RecyclerView") int position) {
        AppInfoBean appInfoBean = mData.get(position);
        holder.bind(appInfoBean);
        holder.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(position,mData.get(position));
            }
        });
    }

    static class AppListHolder extends RecyclerView.ViewHolder{

        private final ItemAppInfoBinding mBinding;

        public AppListHolder(@NonNull View itemView){
            super(itemView);
            mBinding = ItemAppInfoBinding.bind(itemView);
        }

        public void bind(@NonNull AppInfoBean appInfoBean){
            mBinding.setAppInfo(appInfoBean);

        }

        public void setOnItemClickListener(View.OnClickListener clickListener){
            mBinding.setClickListener(clickListener);
        }
    }

}
