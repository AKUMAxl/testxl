package com.xl.testui.socket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xl.testui.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class P2pClientDevicesAdapter extends RecyclerView.Adapter<P2pClientDevicesAdapter.ViewHolder>{

    private Context mContext;
    private List<String> devicesName;
    private P2pClientDevicesAdapter.OnClickListener mOnClickListener;


    public P2pClientDevicesAdapter(Context context, List<String> devicesName){
        this.mContext = context;
        this.devicesName = devicesName;
    }

    public void setOnClickListener(P2pClientDevicesAdapter.OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<String> devicesName){
        this.devicesName = devicesName;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public P2pClientDevicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_speech_text,parent,false);
        return new P2pClientDevicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull P2pClientDevicesAdapter.ViewHolder holder, int position) {
        String deviceName = devicesName.get(position);
        Log.d("xLLL","macAndDevicesName:"+deviceName+" -- "+IPMapping.getMacAddress(deviceName));
        holder.mTextView.setText(deviceName+" -- "+IPMapping.getMacAddress(deviceName));
//        holder.mImageView.setImageURI(Uri.fromFile(new File(imageItemBean.getPath())));
        holder.itemView.setOnClickListener(v -> {
            if (mOnClickListener!=null){
                mOnClickListener.onItemClick(deviceName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return devicesName.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.item_tv);
        }
    }

    public interface OnClickListener{
        void onItemClick(String deviceName);
    }

}
