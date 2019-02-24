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

    static List<String> temp = Arrays.asList("الأحد","الاثنين","الثلاثاء","الأربعاء","الخميس","الجمعة","السبت");
    static ArrayList<String> days = new ArrayList<>(temp);


    static public String kelvintoCelisuis(double temp)
    {
        double cel = temp-273;

        String temprature = new DecimalFormat("##.#").format(cel) + "\u00b0";
        return temprature;
    }

    static int dayofweek(int d, int m, int y)
    {
        int t[] = { 0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4 };
        y -= (m < 3) ? 1 : 0;
        return ( y + y/4 - y/100 + y/400 + t[m-1] + d) % 7;
    }
    static public ArrayList<String> intiDays()
    {

        ArrayList<String>  week = new ArrayList<>();
        week.add("اليوم");

        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        int day = c.get(Calendar.DAY_OF_WEEK);
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
