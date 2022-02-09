package com.example.penrice9.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Penrice9 on 17/01/2019.
 */

public class NotificationTimer {

    public AlarmManager manager;
    Calendar date;
    protected Context c;
    String name, dateOfEvent;
    Database db;


    public NotificationTimer (Calendar eventDate, Context c, String name, String date) {
        this.date = eventDate;
        this.c = c;
        this.manager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        this.name = name;
        this.dateOfEvent = date;
        db = new Database(c);
    }

    /*public void setTime(){
        date.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH-7));
        Intent intent = new Intent(c, Notifications.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, 1, intent, 0);
        manager.setExact(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
    }*/

    //Issue as month is month -1. january = 0, february = 1

    public void setTime(){
        //creates intent to go to NotificationReceive class
        Intent intent = new Intent(c, NotificationReceive.class);
        intent.putExtra("eventName", name);
        intent.putExtra("eventDate", dateOfEvent);
        date.set(Calendar.HOUR_OF_DAY, 8);
        date.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH)-7);
        long time = date.getTimeInMillis();
        //Pending intent holds the intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c,db.getId(name) , intent, 0);
        /*Multiple notifications didn't work without ID from database.*/
        //alarm manager deals with when the onReceive method is jumped to
        manager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }
}
