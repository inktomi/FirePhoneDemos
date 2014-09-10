package com.zappos.firephone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.amazon.device.home.HeroWidgetIntent;

/**
 * Broadcast receiver that listens for broadcasts sent by the launcher indicating
 * that an item in this application's app widget has been selected.
 */
public class WidgetBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = WidgetBroadcastReceiver.class.getSimpleName();

    // Called when the WidgetBroadcastReceiver receives an intent broadcast.
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "Relieved a click from our home widget!");

        // Take action on this broadcast by launching MainActivity.
        final Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.putExtra(MainActivity.EXTRA_KEY, intent.getStringExtra(HeroWidgetIntent.EXTRA_HERO_WIDGET_DATA));
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
    }
}
