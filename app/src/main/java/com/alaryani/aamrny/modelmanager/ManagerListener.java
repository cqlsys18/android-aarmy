package com.alaryani.aamrny.modelmanager;

import com.android.volley.VolleyError;

/**
 * Created by Administrator on 3/13/2017.
 */

public interface ManagerListener {
    public void onError(VolleyError error);

    public void onSuccess(String json);
}
