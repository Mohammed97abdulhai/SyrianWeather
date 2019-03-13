package com.yalagroup.weather.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ShareHelper {
    private static  ShareHelper instance;
    private Context context;
    private ShareHelper(Context context) {
        this.context = context;
    }

    public static ShareHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ShareHelper(context);
        }
        return instance;
    }

    public void share() {
        String packageName = context.getPackageName();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=" + packageName);
        sendIntent.setType("text/plain");
        ((Activity)context).startActivity(sendIntent);
    }

}