package com.huawei.ivi.hmi.test_net.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huawei.ivi.hmi.test_net.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class P2pDevicesAdapter extends RecyclerView.Adapter<P2pDevicesAdapter.ViewHolder>{

    private Context mContext;
    private List<WifiP2pDevice> list = new ArrayList<>();
    private OnClickListener mOnClickListener;


    public P2pDevicesAdapter(Context context, List<WifiP2pDevice> list){
        this.mContext = context;
        this.list = list;
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<WifiP2pDevice> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_text,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WifiP2pDevice  device = list.get(position);
        holder.mTextView.setText(device.deviceName);
        holder.itemView.setOnClickListener(v -> {
            if (mOnClickListener!=null){
                mOnClickListener.onItemClick(device);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list==null){
            return 0;
        }
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.item_tv);
        }
    }

    public interface OnClickListener{
        void onItemClick(WifiP2pDevice device);
    }

}
