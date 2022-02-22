package com.huawei.ivi.hmi.test_net.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huawei.ivi.hmi.test_net.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class P2pClientDevicesAdapter extends RecyclerView.Adapter<P2pClientDevicesAdapter.ViewHolder>{

    private Context mContext;
    private List<String> devicesName;
    private OnClickListener mOnClickListener;


    public P2pClientDevicesAdapter(Context context, List<String> devicesName){
        this.mContext = context;
        this.devicesName = devicesName;
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<String> devicesName){
        this.devicesName = devicesName;
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
        String deviceName = devicesName.get(position);
        holder.mTextView.setText(deviceName);
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
