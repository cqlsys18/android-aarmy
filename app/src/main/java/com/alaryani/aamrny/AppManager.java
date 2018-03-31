package com.alaryani.aamrny;

import com.splunk.mint.Mint;

import android.app.Application;

public class AppManager extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		Mint.initAndStartSession(this, "65988c8b");
	}

}
