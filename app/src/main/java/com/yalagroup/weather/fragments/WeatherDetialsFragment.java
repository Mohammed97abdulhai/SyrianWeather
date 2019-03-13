package com.yalagroup.weather.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yalagroup.weather.R;
import com.yalagroup.weather.models.ParentModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class WeatherDetialsFragment extends DialogFragment {

    private static final String Key = "key";
    private ParentModel model;
    public static WeatherDetialsFragment newInstance(ParentModel model)
    {
        WeatherDetialsFragment fragment = new WeatherDetialsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Key,model);
        fragment.setArguments(bundle);

        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.weather_details_dialog, container, false);

        TextView textView = v.findViewById(R.id.main_temp);
        ImageView imageView = v.findViewById(R.id.weather_stats);
        TextView humidity = v.findViewById(R.id.humidity_in_dialog);
        TextView windSpeed = v.findViewById(R.id.wind_speed_in_dialog);

        model = (ParentModel) getArguments().getSerializable(Key);
        textView.setText(model.getTemparature());
        Picasso.with(getContext()).load(getString(R.string.imageLink) + model.getImage() + getString(R.string.pngSuffex)).into(imageView);
        humidity.setText(String.valueOf(new DecimalFormat("#").format(model.getHummidity())) + " %");
        windSpeed.setText(String.valueOf(new DecimalFormat("#.##").format(model.getWindspeed())) + " m/s");


        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return v;
    }
}