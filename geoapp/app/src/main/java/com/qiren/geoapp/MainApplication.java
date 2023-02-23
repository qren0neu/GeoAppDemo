package com.qiren.geoapp;

import android.app.Application;
import android.content.Context;

public class MainApplication extends Application {

    private static MainApplication applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
    }

    public static MainApplication getApplication() {
        return applicationContext;
    }

    public static Context getContext() {
        return applicationContext.getApplicationContext();
    }
}
