package com.alaryani.aamrny.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.alaryani.aamrny.object.CarType;
import com.alaryani.aamrny.object.SettingObj;

public class PreferencesManager {
    private String TAG = getClass().getSimpleName();

    private final String KEY_PREFERENCES = "com.alaryani.aamrny.preference";
    private static final String LANGUAGE_PREFERENCES = "com.alaryani.aamrny.language.preference";

    private static PreferencesManager instance;

    private Context context;

    // ===================== KEY ======================
    private final String IS_LOGIN = "IS_LOGIN";
    private final String TOKEN = "TOKEN";
    private final String USER_ID = "USER_ID";
    private final String IS_DRIVER = "IS_DRIVER";
    private final String DRIVER_IS_ACTIVE = "DRIVER_IS_ACTIVE";
    private final String CHINASE = "CHINASE";

    private final String USER_NAME = "USER_NAME";
    private final String PASSWORD = "PASSWORD";
    private final String EMAIL = "EMAIL";
    private final String NAME = "NAME";
    private final String ADDRESS = "ADDRESS";
    private final String PHONE = "PHONE";
    private final String SKYPE = "SKYPE";
    private final String SEX = "SEX";
    private final String HOST = "HOST";
    private final String IMAGE = "IMAGE";
    private final String MIN_REDEEM = "MIN_REDEEM";

    // ===================== MAIN FOLLOW KEY ======================
    private final String HISTORY_HAVE_PUSH = "HISTORY_HAVE_PUSH";
    private final String DRIVER_HAVE_PUSH = "DRIVER_HAVE_PUSH";
    private final String DRIVER_IS_ONLINE = "DRIVER_IS_ONLINE";
    private final String DRIVER_IS_IN_TRIP = "DRIVER_IN_TRIP";
    private final String DRIVER_CURRENT_SCREEN = "DRIVER_CURREN_SCREEN";
    private final String USER_CHAT_SCREEN = "USER_CHAT_SCREEN";
    private final String CURRENT_ORDER_ID = "CURRENT_ORDER_ID";
    private final String DRIVER_START_TRIP = "DRIVER_START_TRIP";
    private final String START_APP_WITHOUT_MAIN = "START_APP_WITHOUT_MAIN";
    private final String APP_IS_SHOW = "APP_IS_SHOW";
    private final String PASSENGER_WAIT_CONFIRM = "PASSENGER_WAIT_CONFIRM";
    private final String PASSENGER_HAVE_PUSH = "PASSENGER_HAVE_PUSH";
    private final String PASSENGER_CURRENT_SCREEN = "PASSENGER_CURRENT_SCREEN";
    private final String PASSENGER_IS_IN_TRIP = "PASSENGER_IS_IN_TRIP";
    private final String PASSENGER_IS_PAYMENT = "PASSENGER_IS_PAYMENT";
    private final String DRIVER_ARRIVED = "DRIVER_ARRIVED";
    private final String DRIVER_ARRIVED_B = "DRIVER_ARRIVED_B";
    private final String TASKER_BEGIN_TASK = "TASKER_BEGIN_TASK";


    private final String FOR_DRIVER_BEGIN_TASK = "FOR_DRIVER_BEGIN_TASK";
    private final String FOR_DRIVER_ARRIVED_B = "FOR_DRIVER_ARRIVED_B";
    private final String FOR_DRIVER_ARRIVING_B = "FOR_DRIVER_ARRIVING_B";

    private final String TIME_TO_SEND_REQUEST_AGAIN = "TIME_TO_SEND_REQUEST_AGAIN";
    private final String MAX_TIME_SEND_REQUEST = "MAX_TIME_SEND_REQUEST";
    private final String ESTIMATE_FARE_SPEED = "ESTIMATE_FARE_SPEED";
    private final String PPM_OF_LINK_I = "PPM_OF_LINK_I";
    private final String PPM_OF_LINK_II = "PPM_OF_LINK_II";
    private final String PPM_OF_LINK_III = "PPM_OF_LINK_III";
    private final String PPK_OF_LINK_I = "PPK_OF_LINK_I";
    private final String PPK_OF_LINK_II = "PPK_OF_LINK_II";
    private final String PPK_OF_LINK_III = "PPK_OF_LINK_III";
    private final String SF_OF_LINK_I = "SF_OF_LINK_I";
    private final String SF_OF_LINK_II = "SF_OF_LINK_II";
    private final String SF_OF_LINK_III = "SF_OF_LINK_III";

    private final String FROM_BEFOR_SCREEN = "BEFORE_ONLINE";

    // ===================== FACEBOOK KEY ======================
    private final String FACEBOOK_TOKEN = "FACEBOOK_TOKEN";
    private final String FACEBOOK_NAME = "FACEBOOK_NAME";
    private final String FACEBOOK_EMAIL = "FACEBOOK_NAME";
    private final String FACEBOOK_ID = "FACEBOOK_ID";
    private final String FACEBOOK_FIRST_NAME = "FACEBOOK_FIRST_NAME";
    private final String FACEBOOK_LAST_NAME = "FACEBOOK_LAST_NAME";
    private final String FACEBOOK_IMAGE = "FACEBOOK_IMAGE";

    private final String CARTYPE_ID = "CARTYPE_ID";
    private final String CARTYPE_IMAGE = "CARTYPE_IMAGE";
    private final String CARTYPE_IMAGE_MARKER = "CARTYPE_IMAGE_MARKER";
    private final String CARTYPE_NAME = "CARTYPE_NAME";
    private final String CARTYPE_LINK = "CARTYPE_LINK";
    private final String CARTYPE_START_FARE = "CARTYPE_START_FARE";
    private final String CARTYPE_FEE_PER_MINUTE = "CARTYPE_FEE_PER_MINUTE";
    private final String CARTYPE_FEE_PER_KILOMETER = "CARTYPE_FEE_PER_KILOMETER";
    private final String CARTYPE_GOTO_A = "CARTYPE_GOTO_A";
    private final String CARTYPE_WORK_AT_B = "CARTYPE_WORK_AT_B";
    private final String TASKER_START_TASK = "TASKER_START_TASK";


    private PreferencesManager() {
    }

    /**
     * Constructor
     *
     * @param context
     * @return
     */
    public static PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager();
            instance.context = context;
        }
        return instance;
    }

    public void setCarTypeData(CarType setting) {
        putStringValue(CARTYPE_ID, setting.getLink());
        putStringValue(CARTYPE_IMAGE, setting.getImageType());
        putStringValue(CARTYPE_IMAGE_MARKER, setting.getImageMarker());
        putStringValue(CARTYPE_NAME, setting.getName());
        putStringValue(CARTYPE_LINK, setting.getLink());
        putStringValue(CARTYPE_START_FARE, setting.getStart_fare());
        putStringValue(CARTYPE_FEE_PER_MINUTE, setting.getFee_per_minute());
        putStringValue(CARTYPE_FEE_PER_KILOMETER, setting.getFee_per_kilometer());
        putStringValue(CARTYPE_GOTO_A, setting.getGotoA());
        putStringValue(CARTYPE_WORK_AT_B, setting.getWorkAtB());
    }

    public CarType getDataCarType() {
        CarType settingObj = new CarType();
        settingObj.setId(getStringValue(CARTYPE_ID));
        settingObj.setImageType(getStringValue(CARTYPE_IMAGE));
        settingObj.setImageMarker(getStringValue(CARTYPE_IMAGE_MARKER));
        settingObj.setName(getStringValue(CARTYPE_NAME));
        settingObj.setLink(getStringValue(CARTYPE_LINK));
        settingObj.setStart_fare(getStringValue(CARTYPE_START_FARE));
        settingObj.setFee_per_minute(getStringValue(CARTYPE_FEE_PER_MINUTE));
        settingObj.setFee_per_kilometer(getStringValue(CARTYPE_FEE_PER_KILOMETER));
        settingObj.setGotoA(getStringValue(CARTYPE_GOTO_A));
        settingObj.setWorkAtB(getStringValue(CARTYPE_WORK_AT_B));
        return settingObj;
    }

    public void setDataSetting(SettingObj setting) {
        putStringValue(TIME_TO_SEND_REQUEST_AGAIN, setting.getTime_to_send_request_again());
        putStringValue(MAX_TIME_SEND_REQUEST, setting.getMax_time_send_request());
        putStringValue(ESTIMATE_FARE_SPEED, setting.getEstimate_fare_speed());
        putStringValue(PPM_OF_LINK_I, setting.getPpm_of_link_i());
        putStringValue(PPM_OF_LINK_II, setting.getPpm_of_link_ii());
        putStringValue(PPM_OF_LINK_III, setting.getPpm_of_link_iii());
        putStringValue(PPK_OF_LINK_I, setting.getPpk_of_link_i());
        putStringValue(PPK_OF_LINK_II, setting.getPpk_of_link_ii());
        putStringValue(PPK_OF_LINK_III, setting.getPpk_of_link_iii());
        putStringValue(SF_OF_LINK_I, setting.getSf_of_link_i());
        putStringValue(SF_OF_LINK_II, setting.getSf_of_link_ii());
        putStringValue(SF_OF_LINK_III, setting.getSf_of_link_iii());
    }

    public SettingObj getDataSettings() {
        SettingObj settingObj = new SettingObj();
        settingObj.setTime_to_send_request_again(getStringValue(TIME_TO_SEND_REQUEST_AGAIN));
        settingObj.setMax_time_send_request(getStringValue(MAX_TIME_SEND_REQUEST));
        settingObj.setEstimate_fare_speed(getStringValue(ESTIMATE_FARE_SPEED));
        settingObj.setPpm_of_link_i(getStringValue(PPM_OF_LINK_I));
        settingObj.setPpm_of_link_ii(getStringValue(PPM_OF_LINK_II));
        settingObj.setPpm_of_link_iii(getStringValue(PPM_OF_LINK_III));
        settingObj.setPpk_of_link_i(getStringValue(PPK_OF_LINK_I));
        settingObj.setPpk_of_link_ii(getStringValue(PPK_OF_LINK_II));
        settingObj.setPpk_of_link_iii(getStringValue(PPK_OF_LINK_III));
        settingObj.setSf_of_link_i(getStringValue(SF_OF_LINK_I));
        settingObj.setSf_of_link_ii(getStringValue(SF_OF_LINK_II));
        settingObj.setSf_of_link_iii(getStringValue(SF_OF_LINK_III));
        return settingObj;
    }

    public PreferencesManager(Context context) {
        this.context = context;
    }

    // ======================== MANAGER FUNCTIONS ==========================

    // =========================== FOR MAIN FOLLOW =============================
    public void setDriverOnline() {
        putBooleanValue(DRIVER_IS_ONLINE, true);
    }

    public void setDriverOffline() {
        putBooleanValue(DRIVER_IS_ONLINE, false);
    }

    public boolean driverIsOnline() {
        return getBooleanValue(DRIVER_IS_ONLINE);
    }

    public void setAppIsShow() {
        putBooleanValue(APP_IS_SHOW, true);
    }

    public void setAppIsHide() {
        putBooleanValue(APP_IS_SHOW, false);
    }

    public boolean appIsShow() {
        return getBooleanValue(APP_IS_SHOW);
    }

    public void setDriverHavePush() {
        putBooleanValue(DRIVER_HAVE_PUSH, true);
    }


    public void setDriverHaveNoPush() {
        putBooleanValue(DRIVER_HAVE_PUSH, false);
    }

    public boolean driverIsHavePush() {
        return getBooleanValue(DRIVER_HAVE_PUSH);
    }

    public void setDriverIsInTrip() {
        putBooleanValue(DRIVER_IS_IN_TRIP, true);
    }

    public void setDriverIsNotInTrip() {
        putBooleanValue(DRIVER_IS_IN_TRIP, false);
    }

    public boolean driverIsInTrip() {
        return getBooleanValue(DRIVER_IS_IN_TRIP);
    }

    public boolean taskerIsInTask() {
        return getBooleanValue(TASKER_START_TASK);
    }

    public void setTaskerInTask(boolean check) {
        putBooleanValue(TASKER_START_TASK, check);
    }

    public void setDriverCurrentScreen(String screen) {
        putStringValue(DRIVER_CURRENT_SCREEN, screen);
    }

    public String getDriverCurrentScreen() {
        return getStringValue(DRIVER_CURRENT_SCREEN);
    }

    public void setUserOnChatScreen(String screen) {
        putStringValue(USER_CHAT_SCREEN, screen);
    }

    public String getUserOnChatScreen() {
        return getStringValue(USER_CHAT_SCREEN);
    }

    public void setCurrentOrderId(String id) {
        putStringValue(CURRENT_ORDER_ID, id);
    }

    public String getCurrentOrderId() {
        return getStringValue(CURRENT_ORDER_ID);
    }

    public void setDriverStartTrip(boolean value) {
        putBooleanValue(DRIVER_START_TRIP, value);
    }

    public boolean driverIsStartTrip() {
        return getBooleanValue(DRIVER_START_TRIP);
    }

    public void setStartWithOutMain(boolean value) {
        putBooleanValue(START_APP_WITHOUT_MAIN, value);
    }

    public boolean IsStartWithOutMain() {
        return getBooleanValue(START_APP_WITHOUT_MAIN);
    }

    public void setPassengerWaitConfirm(boolean value) {
        putBooleanValue(PASSENGER_WAIT_CONFIRM, value);
    }

    public boolean IsPassengerWaitConfirm() {
        return getBooleanValue(PASSENGER_WAIT_CONFIRM);
    }

    public void setPassengerHavePush(boolean value) {
        putBooleanValue(PASSENGER_HAVE_PUSH, value);
    }

    public boolean passengerIsHavePush() {
        return getBooleanValue(PASSENGER_HAVE_PUSH);
    }

    public void setPassengerCurrentScreen(String screen) {
        putStringValue(PASSENGER_CURRENT_SCREEN, screen);
    }

    public String getPassengerCurrentScreen() {
        return getStringValue(PASSENGER_CURRENT_SCREEN);
    }

    public void setPassengerIsInTrip(boolean value) {
        putBooleanValue(PASSENGER_IS_IN_TRIP, value);
    }

    public void isFromBeforeOnline(boolean value) {
        putBooleanValue(FROM_BEFOR_SCREEN, value);
    }

    public boolean getFromBeforeOnline() {
        return getBooleanValue(FROM_BEFOR_SCREEN);
    }

    public boolean passengerIsInTrip() {
        return getBooleanValue(PASSENGER_IS_IN_TRIP);
    }

    public void setPassengerHaveDonePayment(boolean value) {
        putBooleanValue(PASSENGER_IS_PAYMENT, value);
    }

    public boolean passengerIsHaveDonePayment() {
        return getBooleanValue(PASSENGER_IS_PAYMENT);
    }

    // =========================== FOR ACCOUNT =============================
    public boolean isChinase() {
        return getBooleanValue(CHINASE);
    }

    public void setIsEnglish() {
        putBooleanValue(CHINASE, false);
    }

    public void setIsChinese() {
        putBooleanValue(CHINASE, true);
    }

    public void setLogin() {
        putBooleanValue(IS_LOGIN, true);
    }

    public void setLogout() {
        putBooleanValue(IS_LOGIN, false);
    }

    public boolean isAlreadyLogin() {
        return getBooleanValue(IS_LOGIN);
    }

    public void setToken(String token) {
        putStringValue(TOKEN, token);
    }

    public String getToken() {
        return getStringValue(TOKEN);
    }

    public void setUserID(String token) {
        putStringValue(USER_ID, token);
    }

    public String getUserID() {
        return getStringValue(USER_ID);
    }

    public void setIsDriver() {
        putBooleanValue(IS_DRIVER, true);
    }

    public boolean isDriver() {
        return getBooleanValue(IS_DRIVER);
    }

    public void setIsUser() {
        putBooleanValue(IS_DRIVER, false);
    }

    public boolean isUser() {
        return !getBooleanValue(IS_DRIVER);
    }

    public void setDriverIsActive() {
        putBooleanValue(DRIVER_IS_ACTIVE, true);
    }

    public void setDriverIsUnActive() {
        putBooleanValue(DRIVER_IS_ACTIVE, false);
    }

    public boolean isActiveDriver() {
        return getBooleanValue(DRIVER_IS_ACTIVE);
    }

    public void setHistoryPush(boolean isPush) {
        putBooleanValue(HISTORY_HAVE_PUSH, isPush);
    }

    public boolean IsHistoryPush() {
        return getBooleanValue(HISTORY_HAVE_PUSH);
    }

    public void setRedeem(String redeem) {
        putStringValue(MIN_REDEEM, redeem);
    }

    public void setArrived(String value) {
        putStringValue(DRIVER_ARRIVED, value);
    }

    public void setArrivedB(String value) {
        putStringValue(DRIVER_ARRIVED_B, value);
    }

    public void setBeginTask(String value) {
        putStringValue(TASKER_BEGIN_TASK, value);
    }

    public void setDriverBeginTask(String value) {
        putStringValue(FOR_DRIVER_BEGIN_TASK, value);
    }

    public void setDriverArrivingB(String value) {
        putStringValue(FOR_DRIVER_ARRIVING_B, value);
    }

    public void setDriverArrivedB(String value) {
        putStringValue(FOR_DRIVER_ARRIVED_B, value);
    }

    public String getArrived() {
        return getStringValue(DRIVER_ARRIVED);
    }

    public String getBeginTask() {
        return getStringValue(TASKER_BEGIN_TASK);
    }

    public String getDriverBeginTask() {
        return getStringValue(FOR_DRIVER_BEGIN_TASK);
    }

    public String getDriverArrivingB() {
        return getStringValue(FOR_DRIVER_ARRIVING_B);
    }

    public String getDriverArrivedB() {
        return getStringValue(FOR_DRIVER_ARRIVED_B);
    }

    public String getArrivedB() {
        return getStringValue(DRIVER_ARRIVED_B);
    }

    public String getRedeem() {
        return getStringValue(MIN_REDEEM);
    }

    public void clearUser() {
        putStringValue(USER_ID, "");
        putStringValue(USER_NAME, "");
        putStringValue(PASSWORD, "");
        putStringValue(EMAIL, "");
        putStringValue(NAME, "");
        putStringValue(ADDRESS, "");
        putBooleanValue(SEX, false);
        putBooleanValue(HOST, false);
        putStringValue(PHONE, "");
        putStringValue(SKYPE, "");
        putStringValue(IMAGE, "");
    }

    public void setFacebookToken(String fbToken) {
        putStringValue(FACEBOOK_TOKEN, fbToken);
    }

    public void setFbAccount(String name, String firstName, String lastName,
                             String email, String id, String avatar) {
        putStringValue(FACEBOOK_ID, id);
        putStringValue(FACEBOOK_NAME, name);
        putStringValue(FACEBOOK_FIRST_NAME, firstName);
        putStringValue(FACEBOOK_LAST_NAME, lastName);
        putStringValue(FACEBOOK_EMAIL, email);
        putStringValue(FACEBOOK_IMAGE, avatar);
    }

    // ======================== UTILITY FUNCTIONS ========================

    /**
     * Save a long integer to SharedPreferences
     *
     * @param key
     * @param n
     */
    public void putLongValue(String key, long n) {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES,
                0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, n);
        editor.commit();
    }

    /**
     * Read a long integer to SharedPreferences
     *
     * @param key
     * @return
     */
    public long getLongValue(String key) {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES,
                0);
        return pref.getLong(key, 0);
    }

    /**
     * Save an integer to SharedPreferences
     *
     * @param key
     * @param n
     */
    public void putIntValue(String key, int n) {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES,
                0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, n);
        editor.commit();
    }

    /**
     * Read an integer to SharedPreferences
     *
     * @param key
     * @return
     */
    public int getIntValue(String key) {
        // SmartLog.log(TAG, "Get integer value");
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES,
                0);
        return pref.getInt(key, 0);
    }

    /**
     * Save an string to SharedPreferences
     *
     * @param key
     * @param s
     */
    public void putStringValue(String key, String s) {
        // SmartLog.log(TAG, "Set string value");
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES,
                0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, s);
        editor.commit();
    }

    /**
     * Read an string to SharedPreferences
     *
     * @param key
     * @return
     */
    public String getStringValue(String key) {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES,
                0);
        return pref.getString(key, "");
    }

    /**
     * Read an string to SharedPreferences
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getStringValue(String key, String defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES,
                0);
        return pref.getString(key, defaultValue);
    }

    /**
     * Save an boolean to SharedPreferences
     *
     * @param key
     * @param
     */
    public void putBooleanValue(String key, Boolean b) {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES,
                0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, b);
        editor.commit();
    }

    /**
     * Read an boolean to SharedPreferences
     *
     * @param key
     * @return
     */
    public boolean getBooleanValue(String key) {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES,
                0);
        return pref.getBoolean(key, false);
    }

    /**
     * Save an float to SharedPreferences
     *
     * @param key
     * @param
     */
    public void putFloatValue(String key, float f) {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES,
                0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, f);
        editor.commit();
    }

    /**
     * Read an float to SharedPreferences
     *
     * @param key
     * @return
     */
    public float getFloatValue(String key) {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES,
                0);
        return pref.getFloat(key, 0.0f);
    }

    public static void setString(Context mContext, String key, String value) {
        SharedPreferences sharedpreferences = mContext.getSharedPreferences(LANGUAGE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context mContext, String key,String def_value) {
        SharedPreferences preferences = mContext.getSharedPreferences(LANGUAGE_PREFERENCES, Context.MODE_PRIVATE);
        String stringvalue = preferences.getString(key, def_value);
        return stringvalue;
    }

}
