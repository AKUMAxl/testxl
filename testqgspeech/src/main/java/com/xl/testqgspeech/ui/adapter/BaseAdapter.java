package com.xl.testqgspeech.ui.adapter;

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
        this.mData.addAll(data);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateData(List<Data> data){
        if (mData==null){
            mData = new ArrayList<>();
        }
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{

        void onItemClick(int position);

    }
}
