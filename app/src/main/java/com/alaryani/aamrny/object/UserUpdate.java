package com.alaryani.aamrny.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 4/13/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUpdate {
    private String lat;
    private String lng;
    private String status;

    public UserUpdate(String lat, String lng, String status) {
        this.lat = lat;
        this.lng = lng;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
