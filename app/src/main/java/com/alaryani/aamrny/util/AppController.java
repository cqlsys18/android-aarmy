package com.alaryani.aamrny.util;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.alaryani.aamrny.config.PreferencesManager;


/**
 * Created by cqlsys on 1/15/2016.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        LocaleHelper.setLocale(getApplicationContext(), PreferencesManager.getString(getApplicationContext(), "app_language", "en"));
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleHelper.setLocale(getApplicationContext(), PreferencesManager.getString(getApplicationContext(), "app_language", "en"));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}