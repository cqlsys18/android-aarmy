package com.alaryani.aamrny.config;

import android.content.Context;

import com.alaryani.aamrny.R;

public class LinkApi {

    public static final int REQUEST_TIME_OUT = 30000;
    public static int RESULT_OK = 1;
    public static int LOGIN_NORMAL = 0;
    public static int LOGIN_FACEBOOK = 1;
    private static Context mContext;

    public LinkApi() {


    }

    // ===================== DOMAIN =====================
    public static String APP_DOMAIN;
    // ===================== WEB SERVICE LINK =====================
    public final static String SERACH_DRIVER = APP_DOMAIN + "searchDriver";
    public final static String CREATE_REQUEST = "createRequest";
    public final static String UPDATE_STATUS = "online";
    public final static String LOGOUT = "logout";
    public final static String CANCEL_TRIP = "cancelTrip";
    public final static String SEND_MESSAGE = "SendMessage";
    public final static String GET_CHAT = "Chat";
    public final static String CANCEL_REQUEST = "cancelRequest";
    public final static String SHOW_MY_REQUEST = "showMyRequest";
    public final static String DRIVER_CONFIRM = "driverConfirm";
    public final static String UPDATE_COORDINATE = "updateCoordinate";
    public final static String START_TRIP = "startTrip";
    public final static String END_TRIP = "endTrip";
    public final static String RATE_DRIVER = "rateDriver";
    public final static String RATE_CUSTOMER = "ratePassenger";
    public final static String TRIP_PAYMENT = "tripPayment";
    public final static String SHOW_DISTANCE = "showDistance";
    public final static String DRIVER_ARRIVED = "driverArrived";
    public final static String SHOW_TRIP_DETAIL = "showTripDetail";
    public final static String DRIVER_CONFIRM_PAY_BY_CASH = "driverConfirmPaymentTrip";
    // ===================== KEY =====================
    public final static String KEY_JSON_STATUS = "status";
    public final static String KEY_JSON_DATA = "data";
    public final static String KEY_JSON_COUNT = "count";
    public final static String JSON_STATUS_SUCCESS = "SUCCESS";
    public final static String JSON_STATUS_ERROR = "ERROR";
    public final static String JSON_DATA_ERROR = "0";
    public final static String JSON_DATA_SUCCESS = "1";
    public static final String KEY_MESSAGE = "message";

    // - ===================== FOR ACCOUNT =====================
    // Login
    public static final String LOGIN = "login";
    public static final String SHOW_STATE_CITY = "showStateCity";
    public static final String GET_DRIVER_LOCATION = "getDriverLocation";

    public static final String SEND_TRIP_REQUEST = "https://aamrny.com/admin-ali/stripe/web/index.php";
    // PrepareLogin
    public static final String PREPARELOGIN = "prepareLogin";
    // authorize
    public static final String AUTHORRIZE = "authorize";
    // Register
    public static final String REGISTER = "register";
    // Token
    public static final String USERINFO = "showUserInfo";
    // Update Profile
    public static final String UPDAT_PROFILE = "updateProfile";
    // Update Profile
    public final static String REGISTER_DRIVER = "driverRegisterAndroid";
    // Update Profile driver
    public final static String UPDATE_PROFILE_DRIVER =
            "updateDriverDataAndroid";

    // Search Taxi Number
    public final static String SEARCH_DRIVER_NUMBER =
            "searchDriver";

    // ======================FOR SHARE======================
    public final static String SHARE = "shareApp";
    public final static String GENERAL_SETTINGS = "generalSettings";
    public final static String NEED_HELP = "needHelpTrip";

    // ======================FOR HISTORY======================
    // Trip History
    public static final String TRIP_HISTORY = "showMyTrip";
    // Transaction History
    public static final String TRANSACTION_HISTORY = "transactionHistory";
    public final static String REGISTERACCOUNT =
            "signupAndroid";
    public final static String activation =
            "activation";
    // ======================FOR PAYMENT======================
    // Pay out
    public static final String PAYOUT = "pointRedeem";
    // Transfer
    public static final String TRANSFER = "pointTransfer";
    // Payment
    public static final String PAYMENT = "pointExchange";
    public static final String TAB_PAYMENT = "TabPayment";
    // Search User
    public static final String SEARCH_USER = "searchUser";
    public final static String LOGINACCOUNT =
            "loginNormal";
    public final static String CHANGEPASSWORD =
            "changePassword";
    public final static String FORGOTPASSWORD =
            "forgotPassword";
    public final static String CHANGE_STATUS = "changeStatus";
    public final static String APPLY_PROMO_CODE = "ConfrimPromoCode";
    // ===================== GOOGLE API =====================
    public final static String GET_LATLNG_BY_ADDRESS = "http://maps.google.com/maps/api/geocode/json";
    public final static String GET_DISTANCE_AND_TIME_FROM_MAP = "http://maps.googleapis.com/maps/api/directions/json";
    public final static String GET_PARAMS_DISTANCE = "&sensor=false&units=metric&mode=driving";
    public final static String SHOW_CAR_TYPE = "showCarType";

    /***
     *
     *
     */

    public static String getDomain(Context context) {
        mContext = context;
        if (mContext != null) {
            APP_DOMAIN = mContext.getString(R.string.web_service_config);
        }
        return APP_DOMAIN;
    }

    public static String getLinkApi(Context context, String api) {

        return getDomain(context) + api;
    }

    public static final String FIREBASE_URL = "https://taxinear-c804e.firebaseio.com/";
}
