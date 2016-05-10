package com.grarak.kerneladiutor.services;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.utils.Constants;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.kernel.Screen;

import java.util.Arrays;

/**
 * Created by joe on 5/10/16.
 */
public class HBMWidget extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        setWidgetActive(true, context.getApplicationContext());
    }

    @Override
    public void onDisabled(Context context) {
        setWidgetActive(false, context.getApplicationContext());
        super.onDisabled(context);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];


            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.hbm_widget_layout);
            Intent intent = new Intent(context, HBMWidget.class);
            intent.setAction("com.kerneladiutor.mod.action.TOGGLE_HBM");
            int flag = PendingIntent.FLAG_UPDATE_CURRENT;
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, flag);
            views.setOnClickPendingIntent(R.id.imageView, pi);

            if (Screen.isScreenHBMActive()) {
                views.setImageViewResource(R.id.imageView, R.drawable.hbm_enable_ic);
            } else {
                views.setImageViewResource(R.id.imageView, R.drawable.hbm_disable_ic);
            }

           appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("com.kerneladiutor.mod.action.TOGGLE_HBM")) {
            Log.i(Constants.TAG + ": " + getClass().getSimpleName(), "Toggling High Brightness Mode via Widget");
            if (Screen.isScreenHBMActive()) {
                Screen.activateScreenHBM(false, context);
            } else {
                Screen.activateScreenHBM(true, context);
            }
        }
    }

    private void setWidgetActive(boolean active, Context context){
        Utils.saveBoolean("Widget_Active", active, context);
    }
}
