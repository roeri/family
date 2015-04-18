package org.noip.roberteriksson.family.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.robert.family.R;

import org.noip.roberteriksson.family.session.Session;

public class WidgetProvider extends AppWidgetProvider {

    final static String WIDGET_UPDATE_ACTION ="com.javatechig.intent.action.UPDATE_WIDGET";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // initializing widget layout
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        int widgetShoppingListId = Session.getInstance().getWidgetShoppingListId();

        // register for button event
        //remoteViews.setOnClickPendingIntent(R.id.sync_button, buildButtonPendingIntent(context));

        // updating view with initial data
        //remoteViews.setTextViewText(R.id.title, getTitle());
        //remoteViews.setTextViewText(R.id.desc, getDesc());
        /*Intent intent = new Intent(context, GeotaskRemoteViewService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                remoteViews);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.widgetShoppingList, intent);*/

        // request for widget update
        pushWidgetUpdate(context, remoteViews);
    }

    public static PendingIntent buildButtonPendingIntent(Context context) {
        ++WidgetIntentReceiver.clickCount;

        // initiate widget update request
        Intent intent = new Intent();
        intent.setAction(WIDGET_UPDATE_ACTION);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static CharSequence getDesc() {
        return "Family widget yaaaay!";
    }

    private static CharSequence getTitle() {
        return "Family widget";
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, WidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }
}