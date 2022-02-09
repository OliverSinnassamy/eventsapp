package com.example.penrice9.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;



/**
 * Created by Penrice9 on 15/01/2019.
 */

public class Notifications{
    private Notification.Builder builder;
    public NotificationManager manager;
    Database db;

    public Notifications(Context context) {
        builder = new Notification.Builder(context);
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        db = new Database(context);
    }

    public void setDetails(String name, String date) {
        builder.setSmallIcon(R.drawable.calendar);
        builder.setContentTitle(name);
        builder.setContentText(date);
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        manager.notify(db.getId(name),builder.build());
    }

}

