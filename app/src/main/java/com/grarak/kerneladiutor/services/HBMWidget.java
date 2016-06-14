package com.grarak.kerneladiutor.services;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.utils.Constants;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.kernel.Screen;

/**
 * Created by joe on 5/10/16.
 */
public class HBMWidget extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        if (Screen.hasScreenHBM()) {
            setWidgetActive(true, context.getApplicationContext());
        } else {
            Utils.toast("Your device does not have HBM/SRE, this widget will not work. Please remove it.", context);
        }
    }

    @Override
    public void onDisabled(Context context) {
        setWidgetActive(false, context.getApplicationContext());
        super.onDisabled(context);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            doupdate(context, Screen.isScreenHBMActive());

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.hbm_widget_layout);
            Intent intent = new Intent(context, HBMWidget.class);
            intent.setAction("com.kerneladiutor.mod.action.TOGGLE_HBM");
            int flag = PendingIntent.FLAG_UPDATE_CURRENT;
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, flag);
            views.setOnClickPendingIntent(R.id.imageView, pi);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("com.kerneladiutor.mod.action.TOGGLE_HBM")) {
            if (Screen.hasScreenHBM()) {
                Log.i(Constants.TAG + ": " + getClass().getSimpleName(), "Toggling High Brightness Mode");
                if (AutoHighBrightnessModeService.HBM_Widget_Toggled) {
                    AutoHighBrightnessModeService.HBM_Widget_Toggled = false;
                }
                else {
                    AutoHighBrightnessModeService.HBM_Widget_Toggled = true;
                }
                if (Screen.isScreenHBMActive()) {
                    Screen.activateScreenHBM(false, context);
                    doupdate(context, false);
                } else {
                    Screen.activateScreenHBM(true, context);
                    doupdate(context, true);
                }
            }
        }
        // Make sure that the widghet is in the correct state when the phone is unlocked.
        if (intent.getAction().equals("android.intent.action.USER_PRESENT")) {
            doupdate(context, Screen.isScreenHBMActive());
        }
    }

    private void setWidgetActive(boolean active, Context context){
        Utils.saveBoolean("Widget_Active", active, context);
    }

    public static void doupdate (Context context, boolean active) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.hbm_widget_layout);
        ComponentName thisWidget = new ComponentName(context, HBMWidget.class);
        if (active) {
            remoteViews.setImageViewResource(R.id.imageView, R.drawable.hbm_enable_ic);
        } else {
            remoteViews.setImageViewResource(R.id.imageView, R.drawable.hbm_disable_ic);
        }
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

}
