package com.noahliu.appwidgetdemo;

import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import java.sql.Time;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class MyTimeWidget extends AppWidgetProvider {
    public static final String TAG = "BalanceServiceMy";

    /**接收廣播資訊*/
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive: "+intent.getAction());
        switch (intent.getAction()){
            case "android.appwidget.action.APPWIDGET_UPDATE":
                Boolean isRun = isServiceRun(context);
                Log.d(TAG, "onReceive: 有Service再跑？: "+isRun);
                if (!isRun)startRunService(context);
                break;
        }
    }
    /**當小工具被建立時*/
    @Override
    public void onEnabled(Context context) {
        startRunService(context);
    }
    /**當小工具被刪除時*/
    @Override
    public void onDisabled(Context context) {
        context.stopService(new Intent(context,TimeService.class));
    }
    /**啟動Service*/
    private void startRunService(Context context) {
        Intent intent = new Intent(context,TimeService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        }
        context.startService(intent);
    }
    /**判斷此是否已有我的Service再跑*/
    private Boolean isServiceRun(Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list =  manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : list){
            if (TimeService.class.getName().equals(info.service.getClassName()))return true;
        }
        return false;
    }
}