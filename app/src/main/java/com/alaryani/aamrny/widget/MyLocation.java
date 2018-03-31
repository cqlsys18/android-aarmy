package com.alaryani.aamrny.widget;

/**
 * Created by Administrator on 4/27/2017.
 */

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.alaryani.aamrny.config.Constant;

public class MyLocation {
    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled = false;
    //    boolean network_enabled = false;
//    boolean checkNetwork = false;
    private Context mContext;
    private boolean isCheckGpsEnable = false;

    public boolean getLocation(Context context, LocationResult result) {
        mContext = context;
        locationResult = result;
        try {
            if (lm == null)
                lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }
            if (!gps_enabled)
                return false;

            if (gps_enabled)
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
            timer1 = new Timer();
            timer1.schedule(new GetLastLocation(), Constant.TIME_GPS_SIGNAL);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
        return true;
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            isCheckGpsEnable = true;
            lm.removeUpdates(locationListenerGps);
            Log.e("online", "online: by gps");
            Location gps_loc = null;
            if (gps_enabled)
                gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (gps_loc != null) {
                locationResult.gotLocation(gps_loc);
                return;
            }

            if (gps_loc != null) {
                locationResult.gotLocation(gps_loc);
                return;
            }
            locationResult.gotLocation(null);
            timer1.cancel();
        }
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }
}