package com.alaryani.aamrny.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Trang on 6/10/2016.
 */
public class MyGcmSharedPrefrences {

    private static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    private static final String TOKEN_ID = "TOKEN_ID";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public static void saveToken(Context context, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(TOKEN_ID, value).apply();
    }

    public static String getToken(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(TOKEN_ID,"");
    }

    public static void saveTokenSenToServer(Context context, boolean value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, value).apply();
    }

    public static boolean isSentToserver(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(SENT_TOKEN_TO_SERVER, false);
    }

}
