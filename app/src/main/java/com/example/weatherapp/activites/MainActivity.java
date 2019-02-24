package com.example.weatherapp.activites;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.weatherapp.APIs.WeatherApi;
import com.example.weatherapp.R;
import com.example.weatherapp.adapters.HorizontalAdapter;
import com.example.weatherapp.adapters.MainAdapter;
import com.example.weatherapp.models.WeatherInfo;
import com.example.weatherapp.models.WeatherResponse;
import com.example.weatherapp.utils.MyReceiver;
import com.example.weatherapp.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner cityName;
    TextView temprature;
    ImageView weatherIcon;
    TextView weatherDescription;
    TextView humidity;
    TextView humidityPercentage;
    ImageView humidityIcon;
    TextView wind;
    TextView windDetails;
    ImageView windIcon;
    Button getForecasat;

    ProgressDialog progressDoalog;

    //a string variable to save the chosen city name
    String city;

    //a long variable to save the id of the chosen city
    long cityID;

    static String [] conditionsEnglish = {"light rain","moderate rain","heavy intensity rain","very heavy rain","extreme rain","freezing rain","shower rain","clear sky","few clouds","scattered clouds","broken clouds","overcast clouds"};
    static String [] conditionsArabic = {"مطر خفيف","ماطر معتدل","ماطر بكثافة","ماطر بكثافة عالية ","ماطر بغزارة عالية جدا","ماطر متجمد","مطر مع برق","سماء صافية","غيوم قليلة","غيوم مبعثرة","غائم جزئيا","غائم كليا"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*Intent notifyIntent = new Intent(this,MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 60, pendingIntent);*/


        init_views();


        ArrayAdapter<CharSequence> governeratesAdapter = ArrayAdapter.createFromResource(this, R.array.governorates,
                R.layout.spinner_item);
        governeratesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityName.setAdapter(governeratesAdapter);
        cityName.setOnItemSelectedListener(this);






        getForecasat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ForecastActivity.class);
                i.putExtra("city", cityID);
                i.putExtra("name", city);
                MainActivity.this.startActivity(i);

            }
        });


    }


    void init_views() {

        cityName = findViewById(R.id.city_name);
        temprature = findViewById(R.id.temprature);
        weatherIcon = findViewById(R.id.weather_icon);
        weatherDescription = findViewById(R.id.weather_description);
        humidity = findViewById(R.id.humidity);
        humidityPercentage = findViewById(R.id.humidity_percentage);
        wind = findViewById(R.id.wind);
        windDetails = findViewById(R.id.wind_details);
        humidityIcon = findViewById(R.id.humidity_icon);
        windIcon = findViewById(R.id.wind_icon);
        getForecasat = findViewById(R.id.get_forecast);

        // Set up progress before call
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();


    }


    public static  String Channel_ID = "id";

    private void scheduleNotification(Notification notification, int delay) {



        Intent notificationIntent = new Intent(this, MyReceiver.class);
        notificationIntent.putExtra(MyReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(MyReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * delay, pendingIntent);
    }

    private Notification getNotification(String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_LOW;
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);

            NotificationChannel channel = new NotificationChannel(Channel_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);


            Notification.Builder builder = new Notification.Builder(this);
            builder.setContentTitle("حالة الطقس اليومية");
            builder.setContentText(content);
            builder.setSmallIcon(R.mipmap.ic_launcher_round);
            builder.setChannelId(Channel_ID);

            return builder.build();

        }

        Intent activityIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(activityIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("حالة الطقس اليومية");
        builder.setContentText(content);
        builder.setContentIntent(resultPendingIntent);
        builder.setSmallIcon(R.mipmap.app_icon);

        return builder.build();
    }

    private WeatherApi getWeatherApiInstance()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final WeatherApi weatherApi = retrofit.create(WeatherApi.class);

        return weatherApi;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        city = (String) parent.getItemAtPosition(position);
        switch (position) {
            case 0: {
                Log.i("spinner", "clicked");
                cityID = 170654;

            }
            break;
            case 1: {

                Log.i("spinner", "clicked");
                cityID = 172059;
            }
            break;
            case 2: {
                cityID = 170063;
            }
            break;
            case 3: {
                cityID = 169389;
            }
            break;
            case 4: {
                cityID = 170017;
            }
            break;
            case 5: {
                cityID = 169577;
            }
            break;
            case 6: {
                cityID = 172408;
            }
            break;
            case 7: {
                cityID = 172955;
            }
            break;
            case 8: {
                cityID = 173334;
            }
            break;
            case 9: {
                cityID = 169304;
            }
            break;
            case 10: {
                cityID = 163345;
            }
            break;
            case 11: {
                cityID = 173811;
            }
            break;
            case 12: {
                cityID = 173480;
            }
            break;
            case 13: {
                cityID = 163806;
            }
            break;

        }



        Log.i("cityId", String.valueOf(cityID));

        Call<WeatherResponse> call = getWeatherApiInstance().getWeatherByCityID(cityID,
                "f0ca18a0a7c414bde9cd9d37a59890cd");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {


                progressDoalog.dismiss();
                if (!response.isSuccessful()) {
                    Log.i("failed1", String.valueOf(response.code()));
                }
                List<WeatherInfo> info = response.body().getWeather();
                String weatherStatus = info.get(0).getMain();
                double i = response.body().getDetailedWeather().getTemp();

                temprature.setVisibility(View.VISIBLE);
                temprature.setText(Util.kelvintoCelisuis(i));


                Picasso.with(getApplicationContext()).load("http://openweathermap.org/img/w/" + info.get(0).getIcon() + ".png").into(weatherIcon);
                weatherIcon.setVisibility(View.VISIBLE);


                for(int index=0;index<conditionsEnglish.length; index++)
                {
                    if(conditionsEnglish[index].equalsIgnoreCase(response.body().getWeather().get(0).getDescription()))
                    {

                        weatherDescription.setText(conditionsArabic[index]);
                    }
                }
                weatherDescription.setVisibility(View.VISIBLE);


                wind.setVisibility(View.VISIBLE);
                windDetails.setText(String.valueOf(response.body().getWind().getSpeed()) + " m/h");
                windDetails.setVisibility(View.VISIBLE);

                windIcon.setVisibility(View.VISIBLE);

                humidity.setVisibility(View.VISIBLE);
                humidityPercentage.setText(String.valueOf(response.body().getDetailedWeather().getHumidity()) + "%");
                humidityPercentage.setVisibility(View.VISIBLE);

                humidityIcon.setVisibility(View.VISIBLE);

                getForecasat.setVisibility(View.VISIBLE);

                String notficationContent = "temparature: " + Util.kelvintoCelisuis((response.body().getDetailedWeather().getTemp())) + ", condition: " + response.body().getWeather().get(0).getDescription();
                scheduleNotification(getNotification(notficationContent), 60*60);


            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

                Log.d("MainActivity", t.getMessage());
            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void createNotificationChannel() {
        String CHANNEL_ID = "id";
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
