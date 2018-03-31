package com.alaryani.aamrny.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alaryani.aamrny.RequestService;

/**
 * Created by Administrator on 2/23/2017.
 */

public class RequestReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, RequestService.class);
        context.startService(serviceIntent);
    }
}
