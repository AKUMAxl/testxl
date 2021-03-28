package com.xl.testqgspeech.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xl.testqgspeech.R;
import com.xl.testqgspeech.bean.voiceBean.HelpDataNewBean;
import com.xl.testqgspeech.databinding.ItemHelpItemBinding;
import com.xl.testqgspeech.databinding.ItemHelpTitleBinding;


import java.util.List;
import java.util.Objects;

public class HelpDataAdapter extends BaseAdapter<HelpDataNewBean, HelpDataAdapter.HelpDataHolder>{

    public static final int HELP_DATA_TYPE_TITLE = 1;
    public static final int HELP_DATA_TYPE_ITEM = 2;


    public HelpDataAdapter(Context context, List<HelpDataNewBean> helpDataNewBeans) {
        super(context, helpDataNewBeans);
    }

    @NonNull
    @Override
    public HelpDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HelpDataHolder helpDataHolder;
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        if (viewType == HELP_DATA_TYPE_TITLE){
            view = layoutInflater.inflate(R.layout.item_help_title,parent,false);
            helpDataHolder = new HelpDataTitleHolder(view);
        }else {
            view = layoutInflater.inflate(R.layout.item_help_item,parent,false);
            helpDataHolder = new HelpDataItemHolder(view);
        }
        return helpDataHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HelpDataHolder holder, int position) {

        HelpDataNewBean helpDataNewBean = mData.get(position);
        if (getItemViewType(position)==HELP_DATA_TYPE_TITLE){
            HelpDataTitleHolder helpDataTitleHolder = (HelpDataTitleHolder) holder;
            helpDataTitleHolder.bind(helpDataNewBean);
        }else {
            HelpDataItemHolder helpDataItemHolder = (HelpDataItemHolder) holder;
            helpDataItemHolder.bind(helpDataNewBean);
            helpDataItemHolder.setOnClickListener(v -> mOnItemClickListener.onItemClick(position,mData.get(position)));
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (Objects.equals(mData.get(position).getType(),HelpDataNewBean.TYPE_TITLE)){
            return HELP_DATA_TYPE_TITLE;
        }else {
            return HELP_DATA_TYPE_ITEM;
        }
    }

    static class HelpDataHolder extends RecyclerView.ViewHolder{

        public HelpDataHolder(@NonNull View itemView){
            super(itemView);
        }

    }

    static class HelpDataTitleHolder extends HelpDataHolder {

        private final ItemHelpTitleBinding mBinding;

        public HelpDataTitleHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = ItemHelpTitleBinding.bind(itemView);
        }

        public void bind(@NonNull HelpDataNewBean helpDataNewBean){
            mBinding.setHelpDataBean(helpDataNewBean);
        }


    }

    static class HelpDataItemHolder extends HelpDataHolder {

        private final ItemHelpItemBinding mBinding;

        public HelpDataItemHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = ItemHelpItemBinding.bind(itemView);
        }

        public void bind(@NonNull HelpDataNewBean helpDataNewBean){
            mBinding.setHelpDataBean(helpDataNewBean);
        }

        public void setOnClickListener(@NonNull View.OnClickListener onClickListener){
            mBinding.setClickListener(onClickListener);
        }

    }
}
