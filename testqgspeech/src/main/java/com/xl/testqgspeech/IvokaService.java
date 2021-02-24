package com.xl.testqgspeech;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.xl.testqgspeech.data.IDataInterface;
import com.xl.testqgspeech.di.annotation.MessageData;
import com.xl.testqgspeech.di.annotation.SpeechData;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class IvokaService extends Service {

    @SpeechData
    @Inject
    IDataInterface mSpeechData;

    @MessageData
    @Inject
    IDataInterface mMessageData;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("xLLL", "onStartCommand()");
        // 在API11之后构建Notification的方式
          Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
    Intent nfIntent = new Intent(this, MainActivity.class);

    builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
            .setContentTitle("下拉列表中的Title") // 设置下拉列表里的标题
            .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
            .setContentText("要显示的内容") // 设置上下文内容
            .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        return START_STICKY;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSpeechData.sendRequest();
        mMessageData.sendRequest();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
