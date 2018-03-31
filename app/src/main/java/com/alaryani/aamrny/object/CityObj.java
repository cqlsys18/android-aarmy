package com.alaryani.aamrny.object;

/**
 * Created by Administrator on 10/10/2016.
 */
public class CityObj {
    private String cityId;
    private String cityName;

    public CityObj() {
    }

    public CityObj(String cityId, String cityName) {
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
