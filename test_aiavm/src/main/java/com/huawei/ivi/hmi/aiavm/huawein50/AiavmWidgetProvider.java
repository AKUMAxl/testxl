package com.huawei.ivi.hmi.aiavm.huawein50;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class AiavmWidgetProvider extends AppWidgetProvider {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate() called with: context = [" + context + "], appWidgetManager = [" + appWidgetManager + "], appWidgetIds = [" + appWidgetIds + "]");
//        final int count = appWidgetIds.length;
//        for (int i=0;i<count;i++){
//            int appWidgetId = appWidgetIds[i];
            // Create an Intent to launch ExampleActivity
//            Intent intent = new Intent(context, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
//            views.setOnClickPendingIntent(R.id.tv, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
//            appWidgetManager.updateAppWidget(appWidgetId, views);
//        }

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.d(TAG, "onAppWidgetOptionsChanged() called with: context = [" + context + "], appWidgetManager = [" + appWidgetManager + "], appWidgetId = [" + appWidgetId + "], newOptions = [" + newOptions + "]");
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled() called with: context = [" + context + "]");
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "onDisabled() called with: context = [" + context + "]");
        super.onDisabled(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        Log.d(TAG, "onRestored() called with: context = [" + context + "], oldWidgetIds = [" + oldWidgetIds + "], newWidgetIds = [" + newWidgetIds + "]");
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted() called with: context = [" + context + "], appWidgetIds = [" + appWidgetIds + "]");
        super.onDeleted(context, appWidgetIds);
    }
}
