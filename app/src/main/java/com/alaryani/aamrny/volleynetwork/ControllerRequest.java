package com.alaryani.aamrny.volleynetwork;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.util.LocaleHelper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.splunk.mint.Mint;

import java.util.Objects;

/**
 * Created by pham on 20/10/2015.
 */
public class ControllerRequest extends Application {
    private RequestQueue requestQueue;
    public static final String TAG = ControllerRequest.class.getSimpleName();
    private static ControllerRequest controller;
    @Override
    public void onCreate() {
        super.onCreate();
        Mint.initAndStartSession(this, "65988c8b");
        controller = this;
        LocaleHelper.setLocale(getApplicationContext(), PreferencesManager.getString(getApplicationContext(), "app_language", "en"));
    }

    /**
     *
     * @return
     */

    public static ControllerRequest getInstance() {
        return controller;
    }

    /**
     *
     * @return trả về một đối tượng của RequestQueue sử dụng để gửi request
     */
    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    /**
     *
     * @param request một request bất kì
     * @param tag được sử dụng setTag cho request
     * @param <T> tham số extends từ Object
     */
   public  <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    /**
     *
     * @param request
     * @param <T> tham số extends từ Object
     */
    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);

    }

    /**
     *
     * @param tag
     */
    public void cancelRequest(Objects tag){
        if (requestQueue!=null){
            requestQueue.cancelAll(tag);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleHelper.setLocale(getApplicationContext(), PreferencesManager.getString(getApplicationContext(), "app_language", "en"));
    }

    /*@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }*/
}
