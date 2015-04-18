package org.noip.roberteriksson.family.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.robert.family.R;

public class WidgetIntentReceiver extends BroadcastReceiver {
    public static int clickCount = 0;
    final static String WIDGET_UPDATE_ACTION ="com.javatechig.intent.action.UPDATE_WIDGET";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WIDGET_UPDATE_ACTION)) {
            updateWidgetPictureAndButtonListener(context);
        }
    }

    private void updateWidgetPictureAndButtonListener(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        // updating view
        //remoteViews.setTextViewText(R.id.title, getTitle());
        //remoteViews.setTextViewText(R.id.desc, getDesc());

        // re-registering for click listener
        //remoteViews.setOnClickPendingIntent(R.id.sync_button, WidgetProvider.buildButtonPendingIntent(context));

        WidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
    }

    private String getDesc() {
        return "YAY the family widget";
    }

    private String getTitle() {
        return "Family widget";
    }
}