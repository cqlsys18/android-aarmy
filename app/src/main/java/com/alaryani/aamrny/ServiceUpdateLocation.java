package com.alaryani.aamrny;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.firebase.client.Firebase;
import com.alaryani.aamrny.config.LinkApi;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.object.UserUpdate;
import com.alaryani.aamrny.service.GPSTracker;

/**
 * Created by Administrator on 11/18/2016.
 */

public class ServiceUpdateLocation extends Service {
    public static String ACTION = "SEND_LOCATION_FROM_SERVICE";
    public static String LOCATION_LAT_LAST = "LOCATION_LAT_LAST";
    public static String LOCATION_LONG_LAST = "LOCATION_LONG_LAST";
    GPSTracker gpsTracker;
    Firebase ref;
    Handler handler = new Handler();

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            gpsTracker = new GPSTracker(getApplicationContext());
            if (gpsTracker.canGetLocation()) {
                if (PreferencesManager.getInstance(getApplicationContext()).isDriver()) {
                    if (PreferencesManager.getInstance(getApplicationContext()).driverIsOnline()) {
                        ref.child("user").child(PreferencesManager.getInstance(getApplicationContext()).getUserID()).setValue(new UserUpdate(gpsTracker.getLatitude() + "", gpsTracker.getLongitude() + "", "1"));
                    }
                }
            }
            handler.postDelayed(runnable, 1000);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Firebase.setAndroidContext(this);
        ref = new Firebase(LinkApi.FIREBASE_URL);


        handler.postDelayed(runnable, 1000);
        gpsTracker = new GPSTracker(getApplicationContext());
        if (gpsTracker.canGetLocation()) {
            updateCoornidate(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        }

        return START_NOT_STICKY;
    }

    public void updateCoornidate(final double latitude, final double longtitude) {
        ModelManager.updateCoordinate(PreferencesManager.getInstance(this).getToken(), latitude + "", longtitude + "", this, false, new ModelManagerListener() {
            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(String json) {
                if (PreferencesManager.getInstance(getApplicationContext()).driverIsOnline()) {
                    updateCoornidate(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                }
            }
        });

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        gpsTracker.stopUsingGPS();
        this.stopSelf();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onDestroy() {
        gpsTracker.stopUsingGPS();
        this.stopSelf();
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
