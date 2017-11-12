package com.sebduczmal.songapp;

import android.app.Application;

import com.sebduczmal.songapp.di.ApplicationComponent;
import com.sebduczmal.songapp.di.DaggerApplicationComponent;

import timber.log.Timber;


public class SongAppApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setupApplicationComponent();
        setupTimber();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    private void setupApplicationComponent() {
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new
                ApplicationComponent.ApplicationModule(this)).build();
    }

    private void setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
