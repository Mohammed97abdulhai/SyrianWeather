package com.example.weatherapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.weatherapp.R;
import com.example.weatherapp.services.MyNewIntentService;


public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }


    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";


    @Override
    public void onReceive(Context context, Intent intent) {


        Log.i("he","hem");



        Log.i("helo","same");





        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
        /*Intent intent1 = new Intent(context, MyNewIntentService.class);
        context.startService(intent1);*/
    }
}
