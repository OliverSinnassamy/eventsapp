package com.example.penrice9.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Penrice9 on 21/01/2019.
 */

public class NotificationReceive extends BroadcastReceiver {
    Notifications n;
    @Override
    //called from alarm manager in NotificationTimer
    public void onReceive(Context context, Intent intent) {
        n = new Notifications(context);
        //checks for event name and date from intent
        if (intent.hasExtra("eventName")){
            if(intent.hasExtra("eventDate")){
                //sets the Title and Content of the notification
                n.setDetails(intent.getStringExtra("eventName"), intent.getStringExtra("eventDate"));
            }
        }
    }
}
