package com.example.weatherapp.activites;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.weatherapp.R;
import com.example.weatherapp.fragments.LogInFragment;
import com.example.weatherapp.models.ParentModel;
import com.example.weatherapp.models.WeatherInfo;
import com.example.weatherapp.models.WeatherResponse;
import com.example.weatherapp.utils.MyReceiver;
import com.example.weatherapp.utils.Util;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    LinearLayout detailsLayout, valuesLayout;


    ProgressBar progressBar;

    //a string variable to save the chosen city name
    String city;

    //a long variable to save the id of the chosen city
    long cityID;

    private static final String key = "f0ca18a0a7c414bde9cd9d37a59890cd";
    public static  String Channel_ID = "id";
    static long [] listOfCitiesID = {170654,172059,170063,169389,170017,169577,172408,172955,173334,169304,163345,173811,173480,163806};
    static String [] conditionsEnglish = {"light rain","moderate rain","heavy intensity rain","very heavy rain","extreme rain","freezing rain","shower rain","clear sky","few clouds","scattered clouds","broken clouds","overcast clouds"};
    static String [] conditionsArabic = {"مطر خفيف","ماطر معتدل","ماطر بكثافة","ماطر بكثافة عالية ","ماطر بغزارة عالية جدا","ماطر متجمد","مطر مع برق","سماء صافية","غيوم قليلة","غيوم مبعثرة","غائم جزئيا","غائم كليا"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.i("info",getKeyHash());


        init_views();

        showfragmentDialog();


        ArrayAdapter<CharSequence> governeratesAdapter = ArrayAdapter.createFromResource(this, R.array.governorates,
                R.layout.spinner_simple_item_my);
        governeratesAdapter.setDropDownViewResource(R.layout.spinner_item);
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
        progressBar = findViewById(R.id.progressbar);

        detailsLayout = findViewById(R.id.details_layout);
        valuesLayout = findViewById(R.id.details_values_layout);
    }



    private void startBroadCastReciever() {

        Intent notificationIntent = new Intent(this, MyReceiver.class);
        notificationIntent.putExtra(MyReceiver.NOTIFICATION_ID, 1);
        //notificationIntent.putExtra(MyReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, 0);


        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Calendar firingCal = Calendar.getInstance() ;
        Calendar calendar = Calendar.getInstance();

        firingCal.set(Calendar.HOUR_OF_DAY, 7);
        firingCal.set(Calendar.MINUTE, 1);
        firingCal.set(Calendar.SECOND, 1);
        long intendedTime= firingCal.getTimeInMillis();
        long currentTime= calendar.getTimeInMillis();

        if (intendedTime >= currentTime) {
            alarmManager.setRepeating(AlarmManager.RTC,
                    intendedTime,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent);
        }
        else
        {
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = firingCal.getTimeInMillis();
            alarmManager.setRepeating(AlarmManager.RTC,
                    intendedTime,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent);

        }


    }


    public void showfragmentDialog()
    {
        FragmentManager manager = MainActivity.this.getSupportFragmentManager();

        FragmentTransaction ft = manager.beginTransaction();
        Fragment prev = MainActivity.this.getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        LogInFragment logInFragment = LogInFragment.newInstance();
        logInFragment.show(ft, "dialog");
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        city = (String) parent.getItemAtPosition(position);

        cityID = listOfCitiesID[position];

        progressBar.setVisibility(View.VISIBLE);

        callWeatherData();
        Log.i("cityId", String.valueOf(cityID));

    }

    private void callWeatherData()
    {
        Call<WeatherResponse> call = Util.getWeatherApiInstance().getWeatherByCityID(cityID,
                key);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (!response.isSuccessful()) {
                    Log.i("failed1", String.valueOf(response.code()));
                    return ;
                }
                progressBar.setVisibility(View.GONE);
                detailsLayout.setVisibility(View.VISIBLE);
                valuesLayout.setVisibility(View.VISIBLE);

                List<WeatherInfo> info = response.body().getWeather();
                double i = response.body().getDetailedWeather().getTemp();
                temprature.setVisibility(View.VISIBLE);
                temprature.setText(Util.kelvintoCelisuis(i));

                Picasso.with(getApplicationContext()).load(getString(R.string.imageLink) + info.get(0).getIcon() + getString(R.string.pngSuffex)).into(weatherIcon);
                weatherIcon.setVisibility(View.VISIBLE);

                for(int index=0;index<conditionsEnglish.length; index++)
                {
                    if(conditionsEnglish[index].equalsIgnoreCase(response.body().getWeather().get(0).getDescription()))
                    {

                        weatherDescription.setText(conditionsArabic[index]);
                    }
                }
                weatherDescription.setVisibility(View.VISIBLE);

                windDetails.setText(String.valueOf(new DecimalFormat("#.##").format(response.body().getWind().getSpeed())) + " m/h");

                humidityPercentage.setText(String.valueOf(new DecimalFormat("#").format(response.body().getDetailedWeather().getHumidity())) + "%");

                getForecasat.setVisibility(View.VISIBLE);

                startBroadCastReciever();

            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

                Log.d("MainActivity", t.getMessage());
            }
        });

    }
    private String getKeyHash() {
        PackageInfo info;
        String keyHash = "";
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = new String(Base64.encode(md.digest(), 0));
            }
        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
//            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
//            Log.e("exception", e.toString());
        }

        return keyHash;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
