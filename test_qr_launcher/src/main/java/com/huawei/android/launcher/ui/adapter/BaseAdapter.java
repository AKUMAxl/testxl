package com.huawei.android.launcher.ui.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseAdapter<Data,T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    Context mContext;
    List<Data> mData;
    OnItemClickListener mOnItemClickListener;


    public BaseAdapter(Context context, List<Data> data){
        mData = new ArrayList<>();
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getItemCount() {
        if (mData==null){
            return 0;
        }
        return mData.size();
    }

    public void updateData(List<Data> data){
        mData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<Data>{

        void onItemClick(int position,Data data);

    }
}
