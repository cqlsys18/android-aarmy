package com.alaryani.aamrny.object;

import java.io.Serializable;

/**
 * Created by Administrator on 11/18/2016.
 */

public class LocationDriverObj implements Serializable {
    private String driverId;
    private String latitude;
    private String longitude;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
