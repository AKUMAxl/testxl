package com.xl.testqgspeech.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xl.testqgspeech.R;
import com.xl.testqgspeech.bean.messageBean.BaseMessageBean;
import com.xl.testqgspeech.databinding.ItemMessageBinding;

import java.util.List;

public class MessageAdapter extends BaseAdapter<BaseMessageBean, MessageAdapter.MessageHolder> {

    public MessageAdapter(Context context, List<BaseMessageBean> baseMessageBeans) {
        super(context, baseMessageBeans);
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message,parent,false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        BaseMessageBean baseMessageBean = mData.get(position);
        holder.bind(baseMessageBean);
        holder.setClickListener(v -> mOnItemClickListener.onItemClick(position,mData.get(position)));
    }


    static class MessageHolder extends RecyclerView.ViewHolder {

        private final ItemMessageBinding mBinding;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = ItemMessageBinding.bind(itemView);
        }

        public void bind(@NonNull BaseMessageBean baseMessageBean){
            mBinding.setMessageBean(baseMessageBean);
        }

        public void setClickListener(View.OnClickListener onClickListener){
            mBinding.setClickListener(onClickListener);
        }

    }

}
