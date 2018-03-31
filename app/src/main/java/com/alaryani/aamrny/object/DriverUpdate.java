package com.alaryani.aamrny.object;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Administrator on 4/14/2017.
 */

public class DriverUpdate {
    private String id;
    private Marker marker;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
