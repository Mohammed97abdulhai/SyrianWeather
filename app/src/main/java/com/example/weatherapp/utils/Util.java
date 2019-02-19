package com.example.weatherapp.utils;

import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Util {

    static List<String> temp = Arrays.asList("saturday","saunday","Monday","Tuesday","Wednesday","Thursday","Friday");
    static ArrayList<String> days = new ArrayList<>(temp);


    static public String kelvintoCelisuis(double temp)
    {
        double cel = temp-273;

        String temprature = new DecimalFormat("##.#").format(cel) + "\u00b0";
        return temprature;
    }

    static public ArrayList<String> intiDays()
    {

        ArrayList<String>  week = new ArrayList<>();
        week.add("today");
        Date now = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(now);
        int day = c.get(Calendar.DAY_OF_WEEK)+1;
        for(int i =0 ; i<4; i++)
        {
            if(day == 7)
            {
                day =0;
            }
            week.add(days.get(day));
            day++;
        }
        return week;
    }


}
