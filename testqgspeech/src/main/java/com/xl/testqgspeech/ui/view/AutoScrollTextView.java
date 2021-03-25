package com.xl.testqgspeech.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.xl.testqgspeech.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Description: 一个可以向上滚动展示文本的控件
 * Author: Seven
 * Mail: huangyawen.happy@gmail.com
 * Date: 17-9-7.
 */

public class AutoScrollTextView extends LinearLayout {
    private static final String TAG = "AutoScrollTextView";
    private static final int UPDATE_CONTENT = 0x1003;
    public static final int SCROLL_MODE_NORMAL = 0;
    public static final int SCROLL_MODE_REPEAT = 1;
    public static final int SCROLL_MODE_NONE = 2;
    public static final int SCROLL_MODE_VERTICAL = 3;
    private static final int UPDATE_VETICAL_CONTENT = 0x1005;
    private static final int LONG_TEXT_CANCEL_DELAY = 0x1006;
    private Context context;
    private List<String> contentList;
    private TextView contentTv;
    private TextView nextContentTv;
    private boolean isRunFlag;
    private int contentWidth;
    private int defaultContentWidth;
    private int index;
    private int scrollInterval;
    private int scrollNormalInterval;
    private int scrollRepeatInterval;
    private int maxLines;
    private int scrollMode;
    private Animation contentAnimation;
    private Animation nextContentAnimation;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_CONTENT:
                    updateContent();
                    break;
                case UPDATE_VETICAL_CONTENT:
                    updateVerticalContent();
                    break;
                case LONG_TEXT_CANCEL_DELAY:
                    cancel();
                    break;
                default:
                    break;
            }
        }
    };

    private void updateContent() {
//        Log.i(TAG, "into updateContent");
        if (isRunFlag) {
            contentTv.setText(contentList.get(index));
            contentTv.startAnimation(contentAnimation);
            nextContentTv.startAnimation(nextContentAnimation);
//            Log.i(TAG, "updateContent "+contentTv.getText().toString());
//            Log.i(TAG, "updateContent next "+nextContentTv.getText().toString());
        }
    }

    private void updateVerticalContent() {
//        Log.i(TAG, "into updateVerticalContent");
        if (isRunFlag) {
            contentTv.setText(nowContentText);
            //liyin 长消息连续播出时不被动画打断
            if(contentList.size()<2){
                contentTv.startAnimation(contentAnimation);
                nextContentTv.startAnimation(nextContentAnimation);
            }
//            Log.i(TAG, "updateVerticalContent "+contentTv.getText().toString());
//            Log.i(TAG, "updateVerticalContent next "+nextContentTv.getText().toString());
        }
    }

    public AutoScrollTextView(Context context) {
        this(context, null);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initView();
        initAnimation();
    }

    public void setScrollMode(int mode) {
        switch (mode) {
            case SCROLL_MODE_NORMAL:
                scrollMode = SCROLL_MODE_NORMAL;
                scrollInterval = scrollNormalInterval;
                break;
            case SCROLL_MODE_REPEAT:
                scrollMode = SCROLL_MODE_REPEAT;
                scrollInterval = scrollRepeatInterval;
                break;
            case SCROLL_MODE_NONE:
                scrollMode = SCROLL_MODE_NONE;
                break;
            case SCROLL_MODE_VERTICAL:
                scrollMode = SCROLL_MODE_VERTICAL;
                break;
            default:
                break;
        }
    }

    private void initAnimation() {
        contentAnimation = AnimationUtils.loadAnimation(context, R.anim.content_translate);
        contentAnimation.setFillAfter(true);
        nextContentAnimation = AnimationUtils.loadAnimation(context, R.anim.content_next_translate);
        nextContentAnimation.setFillAfter(true);
        nextContentAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                Log.i(TAG, "onAnimationStart");
                if (scrollMode == SCROLL_MODE_VERTICAL){
                    nextContentTv.setText(nextContentText);

                    nowContentText = nextContentText;
                }else {
                    setNextContent();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                Log.i(TAG, "onAnimationEnd");
                if (scrollMode != SCROLL_MODE_VERTICAL) {
                    handleNextAnimation();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void handleNextAnimation() {
        int i = index + maxLines;
        Log.i(TAG, "handleNext: " + i+ "contentsize "+contentList.size());
        switch (scrollMode) {
            case SCROLL_MODE_NORMAL:
                if (i <= contentList.size() - maxLines) {
                    mHandler.sendEmptyMessageDelayed(UPDATE_CONTENT, scrollInterval);
                } else {
//                    cancel();
                    mHandler.sendEmptyMessageDelayed(LONG_TEXT_CANCEL_DELAY, scrollInterval);
                }
                break;
            case SCROLL_MODE_REPEAT:
                mHandler.sendEmptyMessageDelayed(UPDATE_CONTENT, scrollInterval);
                break;
            default:
                break;
        }
    }

    private void setNextContent() {
        index += maxLines;
        Log.i(TAG, "setNextContent index: " + index);
        switch (scrollMode) {
            case SCROLL_MODE_NORMAL:
                if (index < contentList.size()) {
                    Log.i(TAG, "setNextContent SCROLL_MODE_NORMAL");
                    contentTv.setText(null);
                    nextContentTv.setText(getContent(index));
                }
                break;
            case SCROLL_MODE_REPEAT:
                if (index >= contentList.size()) {
                    index = 0;
                }
                nextContentTv.setText(getContent(index));
                break;
            default:
                break;
        }
    }

    private void init(Context context) {
        this.context = context;
        scrollMode = SCROLL_MODE_NORMAL;
        contentList = new ArrayList<>();
        scrollInterval = scrollNormalInterval = 2000;
        scrollRepeatInterval = 5000;
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_auto_scroll_item, this, true);
        contentTv = (TextView) view.findViewById(R.id.tv_content);
        nextContentTv = (TextView) view.findViewById(R.id.tv_next_content);
        if (contentTv != null && nextContentTv != null) {
            setMaxLine(1); // default
        }
    }

    /**
     * 如果在onMeasure()方法之前调用setText()方法，宽度为0，导致字符串一个字一行，
     * 所以设置一个默认宽度，以保证字符串分行能正常显示
     * @param defaultContentWidth 默认宽度
     */
    public void setDefaultContentWidth(int defaultContentWidth) {
        this.defaultContentWidth = defaultContentWidth;
    }

    private void resetAnimation() {
        Log.i(TAG, "resetAnimation");
        contentAnimation.reset();
        nextContentAnimation.reset();
        isRunFlag = false;
        mHandler.removeMessages(UPDATE_CONTENT);
        mHandler.removeMessages(UPDATE_VETICAL_CONTENT);
        mHandler.removeMessages(LONG_TEXT_CANCEL_DELAY);
        contentTv.clearAnimation();
        nextContentTv.clearAnimation();
    }

    private void spliteContent(String content) {
        if (contentWidth == 0)
            contentWidth = defaultContentWidth;

        contentList.clear();
        StaticLayout myStaticLayout = new StaticLayout(content, contentTv.getPaint(), contentWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        int count = myStaticLayout.getLineCount();
        for (int i = 0; i < count; i++) {
            int start = myStaticLayout.getLineStart(i);
            int end = myStaticLayout.getLineEnd(i);
            String line = content.substring(start, end);
            contentList.add(line);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth = contentTv.getMeasuredWidth();
    }

    /**
     * 停止滚动
     */
    public void cancel() {
        Log.i(TAG, "cancel");
        isRunFlag = false;
//        contentTv.setText(null);
        //liyin 暂停动画时防止两个textview一起重叠显示，设置next为null
        nextContentTv.setText(null);
        contentAnimation.cancel();
        nextContentAnimation.cancel();
        mHandler.removeMessages(UPDATE_CONTENT);
        contentTv.clearAnimation();
        nextContentTv.clearAnimation();
        //liyin 长内容播报之后清空list，以便插播提示语
        contentList.clear();
    }

    public void setTextSize(float size) {
        contentTv.setTextSize(size);
        nextContentTv.setTextSize(size);
    }

    public void setTextColor(int color) {
        contentTv.setTextColor(color);
        nextContentTv.setTextColor(color);
    }
//TODO  跟随系统字体改变文字大小
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        float defaultSp =24f;
        contentTv.setTextSize(defaultSp*newConfig.fontScale);
        nextContentTv.setTextSize(defaultSp*newConfig.fontScale);
    }



    /**
     * 设置文本
     * @param text 文本
     */

    private String nowContentText = "嗨，红旗";
    private String nextContentText = "嗨，红旗";
    public void setVeticalText(String text) {
        //Todo 输入滚动提示，进入滚动提示状态
//        if(TextUtils.isEmpty(text)){
//            //防止 嗨 红旗闪烁
//            if(!contentTv.getText().equals("嗨，红旗")){
//                text="嗨，红旗";
//            }else{
//
//            }
//        }
        Log.i(TAG, "setVeticalText: " + text);
        setScrollMode(SCROLL_MODE_VERTICAL);
        resetAnimation();
        nextContentTv.setText(null);
        isRunFlag = true;
        nextContentText = text;
        contentTv.setText(nowContentText);
        if (mHandler.hasMessages(UPDATE_VETICAL_CONTENT)){
            mHandler.removeMessages(UPDATE_VETICAL_CONTENT);
        }
        mHandler.sendEmptyMessageDelayed(UPDATE_VETICAL_CONTENT, 4000);
    }

    public void setNoScrollText(String text) {
        //Todo 输入语音识别文字
        Log.i(TAG, "setNoScrollText: " + text);
        if(TextUtils.isEmpty(text)){
            //防止 嗨 红旗闪烁
            if(!contentTv.getText().equals("嗨，红旗")){
                text="嗨，红旗";
            }else{

            }
        }

        setScrollMode(SCROLL_MODE_NONE);
        resetAnimation();
        nextContentTv.setText(null);
        if (!TextUtils.isEmpty(text)) {
            if (text.charAt(text.length() - 1) == '\n') {
                Log.i(TAG, "Get 0A");
                text = text.substring(0, text.length() - 1);
            }
            spliteContent(text);
            if (contentList.size() > 0) {
                String tmp = contentList.get(contentList.size() - 1);
                Log.e(TAG, "setNoScrollText: " + tmp);
                contentTv.setText(tmp);
            } else {
                contentTv.setText(null);
            }
        } else {
            contentTv.setText(null);
        }
    }
    public void setTextList(ArrayList<String> textList) {
        //Todo 输入滚动提示，长文字
        for(int i =0;i<textList.size();i++){
            Log.i(TAG, "setTextList "+i+" "+textList.get(i)  );
        }
        if(textList==null||textList.size()==0){
            return;
        }
//        if(textList!=null&&textList.size()==1&&textList.get(0).equals("嗨，红旗")){
        //防止 嗨 红旗闪烁
//            if(contentTv.getText().equals("嗨，红旗")){
//                return;
//            }else{
//
//            }
//        }
//        if(textList!=null&&textList.size()==1&&textList.get(0).equals(contentTv.getText())){
//            //防止 嗨 红旗闪烁
//            return;
//        }
//        if((!contentTv.getText().equals("嗨，红旗"))&&textList!=null&&textList.size()==1&&textList.get(0).equals("嗨，红旗")){
//            contentList.addAll(textList);
//            return;
//        }
        setScrollMode(SCROLL_MODE_REPEAT);
        resetAnimation();
        nextContentTv.setText(null);
        contentList.clear();
        if (textList != null && textList.size() > 0) {
            for (String text : textList) {
                if (text.charAt(text.length() - 1) == '\n') {
                    Log.i(TAG, "Get 0A");
                    text = text.substring(0, text.length() - 1);
                }
                contentList.add(text);
            }
            //长句的时候清理handler里的延迟msg
            if(contentList.size()>1){
                mHandler.removeMessages(UPDATE_VETICAL_CONTENT);
            }
            index = 0;
            isRunFlag = true;
            if (contentList.size() > maxLines) {
                contentTv.setText(getContent(index));
                mHandler.sendEmptyMessageDelayed(UPDATE_CONTENT, scrollInterval);
                Log.i(TAG, "setText Text size: " + contentList.size());
//                Log.i(TAG, "setText " + contentTv.getText().toString());
            } else {

                contentTv.setText(getContent(0));
//                Log.i(TAG, "setText " + contentTv.getText().toString());
            }
        } else {
            contentTv.setText(null);
        }
    }

    /**
     * 设置文本
     * @param text 文本
     */
    public void setText(String text) {
        //Todo 输入长文本，进入滚动提示状态
        Log.i(TAG, "setText: " + text +"time "+scrollInterval);
        if(TextUtils.isEmpty(text)){
            //防止 嗨 红旗闪烁
//            if(!contentTv.getText().equals("嗨，红旗")){
//                text="嗨，红旗";
//            }else{
//
//            }
        }
        setScrollMode(SCROLL_MODE_NORMAL);
        resetAnimation();
        nextContentTv.setText(null);
        if (!TextUtils.isEmpty(text)) {
            if (text.charAt(text.length() - 1) == '\n') {
                Log.i(TAG, "Get 0A");
                text = text.substring(0, text.length() - 1);
            }

            spliteContent(text);
            //长句的时候清理handler里的延迟msg
            if(contentList.size()>1){
                mHandler.removeMessages(UPDATE_VETICAL_CONTENT);
            }
            index = 0;
            isRunFlag = true;
            if (contentList.size() > maxLines) {
                contentTv.setText(getContent(index));
                mHandler.sendEmptyMessageDelayed(UPDATE_CONTENT, scrollInterval);
                Log.i(TAG, "Text size: " + contentList.size());
//                Log.i(TAG, "setText " + contentTv.getText().toString());
            } else {
                contentTv.setText(getContent(0));
//                Log.i(TAG, "setText " + contentTv.getText().toString());
            }
        } else {
            contentTv.setText(null);
        }
    }
    private String getContent(int index) {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < maxLines; i++) {
            if (index < contentList.size()) {
                content.append(contentList.get(index));
                index++;
            }
        }
        Log.e(TAG, "getContent: " + content.toString());
        return content.toString();
    }

    public void setMaxLine(int lines) {
        contentTv.setMaxLines(lines);
        nextContentTv.setMaxLines(lines);
        maxLines = lines;
    }

    /**
     * 设置滚动间隔，单位毫秒
     * @param interval 滚动间隔
     */
    public void setScrollInterval(int interval) {
        scrollNormalInterval = interval;
    }
}
