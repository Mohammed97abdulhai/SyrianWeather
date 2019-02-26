package com.example.weatherapp.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.example.weatherapp.R;


public class RatingHelper {
    private Context context;
    private static RatingHelper instance;

    public RatingHelper(Context context) {
        this.context = context;
    }

    public static RatingHelper getInstance(Context context) {
        if (instance == null) {
            instance = new RatingHelper(context);
        }
        return instance;
    }

    public void rate() {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
        }
    }
}