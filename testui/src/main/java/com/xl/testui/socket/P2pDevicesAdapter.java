package com.xl.testui.socket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xl.testui.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class P2pDevicesAdapter extends RecyclerView.Adapter<P2pDevicesAdapter.ViewHolder>{

    private Context mContext;
    private List<WifiP2pDevice> list;
    private P2pDevicesAdapter.OnClickListener mOnClickListener;


    public P2pDevicesAdapter(Context context, List<WifiP2pDevice> list){
        this.mContext = context;
        this.list = list;
    }

    public void setOnClickListener(P2pDevicesAdapter.OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<WifiP2pDevice> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public P2pDevicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_speech_text,parent,false);
        return new P2pDevicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull P2pDevicesAdapter.ViewHolder holder, int position) {
        WifiP2pDevice  device = list.get(position);
        Log.d("xLLL","adapter:"+device);
        holder.mTextView.setText(device.deviceName);
//        holder.mImageView.setImageURI(Uri.fromFile(new File(imageItemBean.getPath())));
        holder.itemView.setOnClickListener(v -> {
            if (mOnClickListener!=null){
                mOnClickListener.onItemClick(device);
            }
        });
    }

    @Override
    public int getItemCount() {
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
