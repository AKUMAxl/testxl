package com.xl.testqgspeech.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xl.testqgspeech.R;
import com.xl.testqgspeech.bean.voiceBean.HelpDataNewBean;
import com.xl.testqgspeech.databinding.ItemHelpBinding;

import java.util.List;

public class HelpDataAdapter extends BaseAdapter<HelpDataNewBean, HelpDataAdapter.HelpDataHolder>{


    public HelpDataAdapter(Context context, List<HelpDataNewBean> helpDataNewBeans) {
        super(context, helpDataNewBeans);
    }

    @NonNull
    @Override
    public HelpDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_help,parent,false);
        return new HelpDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpDataHolder holder, int position) {
        HelpDataNewBean helpDataNewBean = mData.get(position);
        holder.bind(helpDataNewBean);
        holder.setOnClickListener(v -> mOnItemClickListener.onItemClick(position,mData.get(position)));
    }

    static class HelpDataHolder extends RecyclerView.ViewHolder {

        private final ItemHelpBinding mBinding;

        public HelpDataHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = ItemHelpBinding.bind(itemView);
        }

        public void bind(@NonNull HelpDataNewBean helpDataNewBean){
            mBinding.setHelpDataBean(helpDataNewBean);
        }

        public void setOnClickListener(@NonNull View.OnClickListener onClickListener){
            mBinding.setClickListener(onClickListener);
        }

    }
}
