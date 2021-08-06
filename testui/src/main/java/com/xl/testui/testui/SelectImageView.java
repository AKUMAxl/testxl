package com.xl.testui.testui;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xl.testui.R;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectImageView extends ConstraintLayout {

    private RecyclerView rv;
    private TextView tv;
    private Button btn_ok;
    private SelectImageAdapter adapter;
    private List<ImageItemBean> list = new ArrayList<>();
    private SelectResultCallback callback;

    public SelectImageView(Context context) {
        super(context);
    }

    public SelectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.layout_select,this,true);
        rv = view.findViewById(R.id.rv);
        tv = view.findViewById(R.id.tv_path);
        btn_ok = view.findViewById(R.id.ok);
        btn_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("xLLL","onClick:"+tv.getText());
                callback.selectResult(tv.getText().toString());
            }
        });
//        /sdcadrd/pateo/testui/preview/
        checkImage();
        adapter = new SelectImageAdapter(context,list);
        adapter.setOnClickListener(new SelectImageAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                tv.setText(list.get(position).getPath());
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);


    }


    private void checkImage(){
//        new Thread(() -> {
//
//        }).start();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d("xLLL","path:"+path);
        File file = new File(path+"/pateo/testui/preview/");
        boolean isDir = file.isDirectory();
        if (!file.exists()){
            boolean result = file.mkdirs();
            Log.d("xLLL","mkdirs result:"+result);
        }
        Log.d("xLLL","isDir:"+isDir+" -- path:"+file.getAbsolutePath()+" -- can read:"+file.canRead());

        File[] files = file.listFiles();
        Log.d("xLLL","files:"+files);
        Log.d("xLLL","file size:"+files.length);
        for (File f:files){
            Log.d("xLLL",f.getAbsolutePath()+" -- "+f.getName());
            list.add(new ImageItemBean(f.getName(),f.getAbsolutePath()));
        }
    }

    public void setSelectResultListener(SelectResultCallback callback){
        this.callback = callback;
    }
}
