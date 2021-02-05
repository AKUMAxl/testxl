package com.xl.testui;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.ViewHolder>  {

    private Context mContext;
    private List<ImageItemBean> list;
    private OnClickListener mOnClickListener;

    public SelectImageAdapter(Context context, List<ImageItemBean> list){
        this.mContext = context;
        this.list = list;
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_select_image,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageItemBean imageItemBean = list.get(position);
        Log.d("xLLL","adapter:"+imageItemBean.toString());
        holder.mTextView.setText(imageItemBean.getPath());
//        holder.mImageView.setImageURI(Uri.fromFile(new File(imageItemBean.getPath())));
        Glide.with(mContext).load(imageItemBean.getPath()).into(holder.mImageView);
        holder.itemView.setOnClickListener(v -> {
            if (mOnClickListener!=null){
                mOnClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView mImageView;
        private final TextView mTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image);
            mTextView = itemView.findViewById(R.id.item_tv);
        }
    }

    public interface OnClickListener{
        void onItemClick(int position);
    }

}
