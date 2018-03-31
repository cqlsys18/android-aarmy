package com.alaryani.aamrny.config;

import android.content.Context;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.alaryani.aamrny.object.CarType;
import com.alaryani.aamrny.object.CurrentOrder;
import com.alaryani.aamrny.object.ItemTripHistory;
import com.alaryani.aamrny.object.RequestObj;
import com.alaryani.aamrny.object.Transfer;
import com.alaryani.aamrny.object.User;
import com.alaryani.aamrny.object.UserFacebook;

public final class GlobalValue {

    public static final boolean DEMO_STATUS = false;

    private static GlobalValue mGlobalValue;

    public static GlobalValue getInstance() {
        if (mGlobalValue == null) {
            synchronized (GlobalValue.class) {
                if (mGlobalValue == null) {
                    mGlobalValue = new GlobalValue();
                }
            }
        }
        return mGlobalValue;
    }

    // ============= START CUONGPM =============
    private RequestObj currentRequestObj;
    private ArrayList<CarType> listCarTypes;

    public ArrayList<CarType> getListCarTypes() {
        return listCarTypes;
    }

    public void setListCarTypes(ArrayList<CarType> listCarTypes) {
        this.listCarTypes = listCarTypes;
    }

    public RequestObj getCurrentRequestObj() {
        return currentRequestObj;
    }

    public void setCurrentRequestObj(RequestObj currentRequestObj) {
        this.currentRequestObj = currentRequestObj;
    }

    private CurrentOrder currentOrder;

    public CurrentOrder getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(CurrentOrder currentOrder) {
        this.currentOrder = currentOrder;
    }

    // ============= END CUONGPM =============

    public static boolean DEBUG_MODE = true;
    public static boolean DEBUG_DB = true;
    public static ArrayList<String> arrCity = new ArrayList<String>();
    public static int valueItemsPerPage = 3;
    public static String UTILS_PARAM_NOTIF = "0";
    public static String UTILS_NOTIF = "notif";
    public static LatLng addLatLng;
    public static boolean fromGetLocation = false;
    public String estimate_fare;

    public static boolean IS_LOGIN = false;
    public UserFacebook currentUser = new UserFacebook();

    public String getEstimate_fare() {
        return estimate_fare;
    }

    public void setEstimate_fare(String estimate_fare) {
        this.estimate_fare = estimate_fare;
    }

    //================START TIEN==========================
    public User user = new User();
    public Transfer transfer = new Transfer();
    public ItemTripHistory currentHistory = new ItemTripHistory();
    public double driver_earn = 0.0;

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static boolean isIS_LOGIN() {
        return IS_LOGIN;
    }

    public static void setIS_LOGIN(boolean iS_LOGIN) {
        IS_LOGIN = iS_LOGIN;
    }

    public UserFacebook getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserFacebook currentUser) {
        this.currentUser = currentUser;
    }

    public double getDriver_earn() {
        return driver_earn;
    }

    public void setDriver_earn(double driver_earn) {
        this.driver_earn = driver_earn;
    }

    public int convertToInt(String s) {
        switch (s) {
            case "I":
                return 1;
            case "II":
                return 2;
            case "III":
                return 3;
            case "IV":
                return 4;
            case "V":
                return 5;
        }
        return 0;
    }

    public static String convertLinkToString(Context context, String link) {
        for (int i = 0; i < GlobalValue.getInstance().getListCarTypes().size(); i++) {
            if (GlobalValue.getInstance().getListCarTypes().get(i).getId() != null && GlobalValue.getInstance().getListCarTypes().get(i).getId().equals(link)) {
                return GlobalValue.getInstance().getListCarTypes().get(i).getName();
            }
        }
        return link;
    }
}
