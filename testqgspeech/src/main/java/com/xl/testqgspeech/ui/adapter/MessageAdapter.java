package com.xl.testqgspeech.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xl.testqgspeech.bean.messageBean.BaseMessageBean;
import com.xl.testqgspeech.databinding.ItemMessageBinding;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter<BaseMessageBean, MessageAdapter.MessageHolder> {

    public MessageAdapter(Context context, ArrayList<BaseMessageBean> baseMessageBeans) {
        super(context, baseMessageBeans);
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageHolder(ItemMessageBinding.inflate(LayoutInflater.from(mContext)));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.mBinding.itemMessageLayout.setOnClickListener(v -> mOnItemClickListener.onItemClick(position));
    }


    static class MessageHolder extends RecyclerView.ViewHolder {

        ItemMessageBinding mBinding;

        public MessageHolder(@NonNull ItemMessageBinding binding) {
            super(binding.getRoot());
            mBinding = binding;

        }

    }

}
