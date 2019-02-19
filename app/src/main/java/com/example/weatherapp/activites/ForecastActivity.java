package com.example.weatherapp.activites;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.weatherapp.APIs.WeatherApi;
import com.example.weatherapp.R;
import com.example.weatherapp.adapters.MainAdapter;
import com.example.weatherapp.models.ForecastResponse;
import com.example.weatherapp.models.ParentModel;
import com.example.weatherapp.utils.Util;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForecastActivity extends AppCompatActivity {

    XRecyclerView recyclerView;
    ArrayList<ParentModel> items;
    ArrayList<String> tempartures;
    ArrayList<String> images;
    ArrayList<String> hours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText((CharSequence) getIntent().getExtras().get("name"));


        intiRecycler();

        items = new ArrayList<>();

        tempartures = new ArrayList<>();
        images = new ArrayList<>();
        hours = new ArrayList<>();

        long cityID = (long) getIntent().getExtras().get("city");


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final WeatherApi weatherApi = retrofit.create(WeatherApi.class);



        // Set up progress before call
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ForecastActivity.this);
        // show it
        progressDoalog.show();

        Call<ForecastResponse> call = weatherApi.getForecastByCityId(cityID,
                "f0ca18a0a7c414bde9cd9d37a59890cd");

        call.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {


                progressDoalog.dismiss();
                if(!response.isSuccessful())
                {
                    Log.i("hello", String.valueOf(response.code()));
                }
                for(int i=0;i<response.body().getResponse().size();i++)
                {
                    ParentModel parentModel = new ParentModel();
                    parentModel.temparature = Util.kelvintoCelisuis(response.body().getResponse().get(i).getDetailedWeather().getTemp());
                    parentModel.hummidity = response.body().getResponse().get(i).getDetailedWeather().getHumidity();
                    parentModel.windspeed = response.body().getResponse().get(i).getWind().getSpeed();
                    parentModel.image = response.body().getResponse().get(i).getWeather().get(0).getIcon();

                    tempartures.add(Util.kelvintoCelisuis(response.body().getResponse().get(i).getDetailedWeather().getTemp()));
                    images.add(response.body().getResponse().get(i).getWeather().get(0).getIcon());

                    java.util.Date time=new java.util.Date((long)response.body().getResponse().get(i).getDt()*1000);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(time);
                    int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                    if(currentHour > 12)
                    {
                        currentHour = currentHour - 12;
                        parentModel.hour = String.valueOf(currentHour)+":00 pm";
                        hours.add(String.valueOf(currentHour)+":00 pm");
                    }
                    else
                    {
                        parentModel.hour = String.valueOf(currentHour)+":00 am";
                        hours.add(String.valueOf(currentHour)+ ":00 am");

                    }

                    items.add(parentModel);

                }

                MainAdapter adapter = new MainAdapter(ForecastActivity.this,Util.intiDays(),items);
                recyclerView.setAdapter(adapter);
                Log.i("hello",response.body().getResponse().get(0).getWeather().get(0).getMain());
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void intiRecycler()
    {
        recyclerView = findViewById(R.id.days_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

    }

}
