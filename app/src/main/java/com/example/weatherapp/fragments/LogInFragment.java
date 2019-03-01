package com.example.weatherapp.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.R;
import com.example.weatherapp.models.ParentModel;
import com.example.weatherapp.utils.RatingHelper;
import com.example.weatherapp.utils.ShareHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Calendar;

import javax.security.auth.login.LoginException;

public class LogInFragment extends DialogFragment {

    ImageView supportUs;
    ImageButton rateUS;
    ImageButton share;
    LoginButton loginButton;
    CallbackManager callbackManager;


    public static LogInFragment newInstance(ParentModel model)
    {
        LogInFragment fragment = new LogInFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("",model);
        fragment.setArguments(bundle);

        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {

        super.onResume();
        if(Build.VERSION.SDK_INT <= 20)
        {
            int width = 300;
            int height = 500;
            getDialog().getWindow().setLayout(width, height);
        }
        else
        {
            int width = 700;
            int height = 650;
            getDialog().getWindow().setLayout(width, height);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.log_in_dialog, container, false);


        supportUs = v.findViewById(R.id.support_label);
        //The original Facebook button
        loginButton = v.findViewById(R.id.login_button);


        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.setFragment(this);

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {
//                Log.d(TAG, "onCancel: " + "Cancelled");
            }

            @Override
            public void onError(FacebookException error) {

            }

        });


        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        //Our custom Facebook button
        /*loginFacebook = v.findViewById(R.id.fb);

        loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });*/

        rateUS = v.findViewById(R.id.rating_button);
        share = v.findViewById(R.id.sharing_button);



        rateUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingHelper helper = new RatingHelper(getContext());
                helper.rate();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareHelper helper = ShareHelper.getInstance(getContext());
                helper.share();
            }
        });
        return v;
    }

}
