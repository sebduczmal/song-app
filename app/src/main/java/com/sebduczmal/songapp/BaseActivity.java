package com.sebduczmal.songapp;


import android.support.v7.app.AppCompatActivity;

import com.sebduczmal.songapp.di.ApplicationComponent;

public class BaseActivity extends AppCompatActivity {

    protected ApplicationComponent getApplicationComponent() {
        final SongAppApplication application = (SongAppApplication) getApplication();
        return application.getApplicationComponent();
    }
}
