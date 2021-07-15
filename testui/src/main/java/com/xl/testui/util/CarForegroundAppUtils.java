package com.xl.testui.util;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class CarForegroundAppUtils {

    private static final String TAG = CarForegroundAppUtils.class.getSimpleName();

    private static final long END_TIME = System.currentTimeMillis();
    private static final long TIME_INTERVAL = 7 * 24 * 60 * 60 * 1000L;
    private static final long START_TIME = END_TIME - TIME_INTERVAL;
    /**
     * 获取栈顶的应用包名
     */
    public static String getForegroundActivityNameByReflect(Context context) {
        String currentClassName = "";
        Log.i("CarForegroundAppUtils ", "getForegroundActivityName");
        try {
            Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
            Log.d("CarForegroundAppUtils activityManagerNative ", activityManagerNative.toString());
            Object iActivityManager=activityManagerNative.getMethod("getDefault").invoke(activityManagerNative);
            Log.d("CarForegroundAppUtils iActivityManager ", iActivityManager.toString());
            Method getAllStackInfos =iActivityManager.getClass().getMethod("getAllStackInfos");
            Log.d("CarForegroundAppUtils getAllStackInfos ", getAllStackInfos.toString());
            List<Object> stackInfosList = (List<Object>) getAllStackInfos.invoke(iActivityManager);
            Log.d("CarForegroundAppUtils stackInfosList ", stackInfosList.toString());
            Object stackInfo = stackInfosList.get(0);
            Log.d("CarForegroundAppUtils stackInfo ", stackInfo.toString());
            Field topActivityComponentName = stackInfo.getClass().getField("topActivity");
            Log.d("CarForegroundAppUtils topActivityComponentName ", topActivityComponentName.getName()+"");
            ComponentName topActivity = (ComponentName) topActivityComponentName.get(stackInfo);
            Log.d("CarForegroundAppUtils topActivity ", topActivity.getPackageName()+"");
            currentClassName = topActivity.getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("CarForegroundAppUtils", "Exception: " + e.toString());
        }
        Log.d("CarForegroundAppUtils return ForegroundActivityName ", currentClassName);
        return currentClassName;
    }
    /**
     * 获取栈顶Activity ClassName
     */
    public static String getForegroundActivityName(Context context) {
        Log.i(TAG,"getOnForgroundActivity");
        boolean isInit = true;
        UsageStatsManager usageStatsManager =
                (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long ts = System.currentTimeMillis();
        List<UsageStats> queryUsageStats =
                usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);
        UsageEvents usageEvents = usageStatsManager.queryEvents(isInit ? 0 : ts-5000, ts);
        if (usageEvents == null) {
            Log.i(TAG,"getOnForgroundActivity null1");
            return null;
        }

        UsageEvents.Event event = new UsageEvents.Event();
        String lastEventClass = null;
        while (usageEvents.getNextEvent(event)) {
            // if from notification bar, class name will be null
            if (event.getPackageName() == null || event.getClassName() == null) {
                continue;
            }

            lastEventClass = event.getClassName();
            Log.i(TAG,"getOnForgroundActivity lastEvent class:" + lastEventClass);
        }

        if (lastEventClass == null) {
            Log.i(TAG,"getOnForgroundActivity null2");
            return null;
        }
        Log.i(TAG,"OnForgroundActivity ClassName:"+lastEventClass);
        return lastEventClass;
    }

    /**
     * 获取栈顶Activity PackageName
     */
    public static String getForegroundPackageName(Context context) {
        Log.i(TAG,"getOnForgroundActivity");
        boolean isInit = true;
        UsageStatsManager usageStatsManager =
                (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long ts = System.currentTimeMillis();
        List<UsageStats> queryUsageStats =
                usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);
        UsageEvents usageEvents = usageStatsManager.queryEvents(isInit ? 0 : ts-5000, ts);
        if (usageEvents == null) {
            Log.i(TAG,"getOnForgroundActivity null1");
            return null;
        }

        UsageEvents.Event event = new UsageEvents.Event();
        String lastEventPackage = null;
        while (usageEvents.getNextEvent(event)) {
            // if from notification bar, class name will be null
            if (event.getPackageName() == null || event.getClassName() == null) {
                continue;
            }

            lastEventPackage = event.getPackageName();
            Log.i(TAG,"getOnForgroundActivity lastEvent package:" + lastEventPackage);
        }

        if (lastEventPackage == null) {
            Log.i(TAG,"getOnForgroundActivity null2");
            return null;
        }
        Log.i(TAG,"OnForgroundActivity PackageName:"+lastEventPackage);
        return lastEventPackage;
    }


    /**
     * 判断当前应用是否在前台
     */
    public static boolean isForegroundApp(Context context) {
        return TextUtils.equals(getForegroundActivityNameByReflect(context), context.getPackageName());
    }
    /**
     * 获取时间段内，
     */
    public static long getTotleForegroundTime(Context context) {
        UsageStats usageStats = getCurrentUsageStats(context, START_TIME, END_TIME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return usageStats != null ? usageStats.getTotalTimeInForeground() : 0;
        }
        return 0;
    }
    /**
     * 获取记录前台应用的UsageStats对象
     */
    private static UsageStats getForegroundUsageStats(Context context, long startTime, long endTime) {
        UsageStats usageStatsResult = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            List<UsageStats> usageStatses = getUsageStatsList(context, startTime, endTime);
            if (usageStatses == null || usageStatses.isEmpty()) return null;
            for (UsageStats usageStats : usageStatses) {
                if (usageStatsResult == null || usageStatsResult.getLastTimeUsed() < usageStats.getLastTimeUsed()) {
                    usageStatsResult = usageStats;
                }
            }
        }
        return usageStatsResult;
    }
    /**
     * 获取记录当前应用的UsageStats对象
     */
    public static UsageStats getCurrentUsageStats(Context context, long startTime, long endTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            List<UsageStats> usageStatses = getUsageStatsList(context, startTime, endTime);
            if (usageStatses == null || usageStatses.isEmpty()) return null;
            for (UsageStats usageStats : usageStatses) {
                if (TextUtils.equals(usageStats.getPackageName(), context.getPackageName())) {
                    return usageStats;
                }
            }
        }
        return null;
    }
    /**
     * 通过UsageStatsManager获取List<UsageStats>集合
     */
    public static List<UsageStats> getUsageStatsList(Context context, long startTime, long endTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager manager = (UsageStatsManager) context.getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
            //UsageStatsManager.INTERVAL_WEEKLY，UsageStatsManager的参数定义了5个，具体查阅源码
            List<UsageStats> usageStatses = manager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startTime, endTime);
            if (usageStatses == null || usageStatses.size() == 0) {// 没有权限，获取不到数据
//                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.getApplicationContext().startActivity(intent);
                return null;
            }
            return usageStatses;
        }
        return null;
    }
}
