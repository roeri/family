package org.noip.roberteriksson.family.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.robert.family.R;

import java.util.ArrayList;

public class WidgetIntentReceiver extends BroadcastReceiver {
    public static int clickCount = 0;
    private String msg[] = null;
    final static String WIDGET_UPDATE_ACTION ="com.javatechig.intent.action.UPDATE_WIDGET";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WIDGET_UPDATE_ACTION)) {
            updateWidgetPictureAndButtonListener(context);
        }
    }

    private void updateWidgetPictureAndButtonListener(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget);

        // updating view
        remoteViews.setTextViewText(R.id.title, getTitle());
        remoteViews.setTextViewText(R.id.desc, getDesc(context));

        // re-registering for click listener
        remoteViews.setOnClickPendingIntent(R.id.sync_button,
                WidgetProvider.buildButtonPendingIntent(context));

        WidgetProvider.pushWidgetUpdate(context.getApplicationContext(),
                remoteViews);
    }

    private String getDesc(Context context) {
        // some static jokes from xml
        //msg = context.getResources().getStringArray(R.array.news_headlines);
        ArrayList<String> newMsg = new ArrayList<>();
        newMsg.add("SOMETHING?");
        msg = (String[]) newMsg.toArray();
        if (clickCount >= msg.length) {
            clickCount = 0;
        }
        return msg[clickCount];
    }

    private String getTitle() {
        return "Family widget";
    }
}