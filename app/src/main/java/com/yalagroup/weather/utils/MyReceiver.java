package com.yalagroup.weather.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.yalagroup.weather.R;
import com.yalagroup.weather.activites.MainActivity;
import com.yalagroup.weather.models.WeatherInfo;
import com.yalagroup.weather.models.WeatherResponse;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yalagroup.weather.activites.MainActivity.Channel_ID;


public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }


    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";


    @Override
    public void onReceive(Context context, Intent intent) {


        Log.i("info", "hello");
        Calendar calendar = Calendar.getInstance();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Call<WeatherResponse> call = Util.getWeatherApiInstance().getWeatherByCityID(170654,
                "f0ca18a0a7c414bde9cd9d37a59890cd");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {

                if (!response.isSuccessful()) {
                    Log.i("failed1", String.valueOf(response.code()));
                }
                List<WeatherInfo> info = response.body().getWeather();
                String weatherStatus = info.get(0).getMain();
                double i = response.body().getDetailedWeather().getTemp();


                String notficationContent = "temparature: " + Util.kelvintoCelisuis((response.body().getDetailedWeather().getTemp())) + ", condition: " + response.body().getWeather().get(0).getDescription();
                Notification notification = getNotification(context, notficationContent);
                int id = intent.getIntExtra(NOTIFICATION_ID, 0);
                notificationManager.notify(id, notification);
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

                Log.d("MainActivity", t.getMessage());
            }
        });


        /*Intent intent1 = new Intent(context, MyNewIntentService.class);
        context.startService(intent1);*/
    }


    private Notification getNotification(Context context, String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            Intent activityIntent = new Intent(context, MainActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntentWithParentStack(activityIntent);

            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


            int importance = NotificationManager.IMPORTANCE_LOW;
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);

            NotificationChannel channel = new NotificationChannel(Channel_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);


            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("حالة الطقس اليومية");
            builder.setContentText(content);
            builder.setContentIntent(resultPendingIntent);
            builder.setSmallIcon(R.mipmap.app_icon);
            builder.setChannelId(Channel_ID);

            return builder.build();

        }

        Intent activityIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(activityIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("حالة الطقس اليومية");
        builder.setContentText(content);
        builder.setContentIntent(resultPendingIntent);
        builder.setSmallIcon(R.mipmap.app_icon);

        return builder.build();
    }


}
