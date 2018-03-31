package com.alaryani.aamrny.object;

import java.util.ArrayList;

/**
 * Created by Administrator on 12/24/2016.
 */

public class UserOnlineObj {
    private String count;
    private ArrayList<DriverOnlineObj> driverOnlineObj;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ArrayList<DriverOnlineObj> getDriverOnlineObj() {
        return driverOnlineObj;
    }

    public void setDriverOnlineObj(ArrayList<DriverOnlineObj> driverOnlineObj) {
        this.driverOnlineObj = driverOnlineObj;
    }
}
