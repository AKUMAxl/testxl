package com.xl.testui.iflytek;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xl.testui.R;
import com.xl.testui.testui.ImageItemBean;
import com.xl.testui.testui.SelectImageAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpeechTextAdapter extends RecyclerView.Adapter<SpeechTextAdapter.ViewHolder>{

    private Context mContext;
    private List<String> list;
    private SpeechTextAdapter.OnClickListener mOnClickListener;


    public SpeechTextAdapter(Context context, List<String> list){
        this.mContext = context;
        this.list = list;
    }

    public void setOnClickListener(SpeechTextAdapter.OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public SpeechTextAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_speech_text,parent,false);
        return new SpeechTextAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpeechTextAdapter.ViewHolder holder, int position) {
        String  text = list.get(position);
        Log.d("xLLL","adapter:"+text);
        holder.mTextView.setText(text);
//        holder.mImageView.setImageURI(Uri.fromFile(new File(imageItemBean.getPath())));
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

        private final TextView mTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.item_tv);
        }
    }

    public interface OnClickListener{
        void onItemClick(int position);
    }

}
