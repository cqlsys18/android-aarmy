package com.alaryani.aamrny.volleynetwork;

import com.android.volley.VolleyError;

/**
 * Created by phamtuan on 20/10/2015.
 */
public interface HttpError {
    void onHttpError(VolleyError volleyError);
}
