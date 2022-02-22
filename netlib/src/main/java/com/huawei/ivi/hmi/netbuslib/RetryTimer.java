package com.huawei.ivi.hmi.netbuslib;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class RetryTimer {

    private ScheduledExecutorService scheduledExecutorService;

    public RetryTimer(){

    }

    public void startTimer(Runnable task,int initialDelay,int period){
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        });
//        scheduledExecutorService.scheduleAtFixedRate(task,0,10, TimeUnit.SECONDS);
        scheduledExecutorService.scheduleAtFixedRate(task,initialDelay,period, TimeUnit.SECONDS);

    }

    public void stopTimer(){
        if (scheduledExecutorService==null){
            return;
        }
        scheduledExecutorService.shutdownNow();
        scheduledExecutorService = null;
    }


}