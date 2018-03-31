package com.alaryani.aamrny.service;


import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class FusedLocationService extends Service implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private final String TAG = "FusedLocationService";
    private final int INTERVAL_MILISECOND = 7000;
    private final int FASTINTERVAL_MILISECOND = 5000;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;
    private String mLastUpdateTime;

    private Location mOldLocation;
    private float curentDistance = 0;

    private final IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        public FusedLocationService getService() {
            return FusedLocationService.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        createLocationRequest();
        buildGoogleApiClient();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onStop fired ..............");
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
        Log.d(TAG,
                "isConnected ...............: "
                        + mGoogleApiClient.isConnected());
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mOldLocation != null) {
            if (getDistance(location, mOldLocation) >= 0.01) {
                curentDistance += getDistance(location, mOldLocation);
                mOldLocation = location;
            }
            //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            Log.d(TAG, "onLocationChanged ..............: " + curentDistance + " updated: ");
        } else {
            mOldLocation = location;
        }

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind ..............: onUnbind");
        return super.onUnbind(intent);

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Bundle arg0) {
        Log.d(TAG, "onConnected ..............");
        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub

    }

    public float getCurentDistance() {
        return curentDistance;
    }

    public void connectionApi() {
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
        curentDistance = 0;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL_MILISECOND);
        mLocationRequest.setFastestInterval(FASTINTERVAL_MILISECOND);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }


    private float getDistance(Location toLocation, Location fromLocation) {
        float distance = toLocation.distanceTo(fromLocation);
        distance = distance / 1000.0f;
        return distance;
    }

}
