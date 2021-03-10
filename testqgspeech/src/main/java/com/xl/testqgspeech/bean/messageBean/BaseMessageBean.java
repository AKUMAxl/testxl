package com.xl.testqgspeech.bean.messageBean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.xl.testqgspeech.BR;


public class BaseMessageBean extends BaseObservable {

    private int type;
    private String content;
    private String time;

    public BaseMessageBean() {
    }

    public BaseMessageBean(int type, String content, String time) {
        this.type = type;
        this.content = content;
        this.time = time;
    }

    @Bindable
    public int getType() {
        return type;

    }

    public void setType(int type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        notifyPropertyChanged(BR.content);
    }

    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    @Override
    public String toString() {
        return "BaseMessageBean{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
