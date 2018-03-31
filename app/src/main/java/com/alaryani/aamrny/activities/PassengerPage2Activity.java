package com.alaryani.aamrny.activities;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.service.GPSTracker;
import com.alaryani.aamrny.widget.TextViewPixeden;

public class PassengerPage2Activity extends BaseActivity {
	RelativeLayout btnLink1, btnLink2, btnLink3;

	private GoogleMap map;
	private GPSTracker gps;
	Handler handler;
	TextViewPixeden btnBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_passenger_page2);
		handler = new Handler();
		gps = new GPSTracker(self);
		setUpMap();
		init();
		((Button) findViewById(R.id.btnBook))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						gotoActivity(WaitDriverConfirmActivity.class);
						finish();
					}
				});

		initUIInThis();
	}

	public void initUIInThis() {

		btnBack = (TextViewPixeden) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void init() {
		btnLink1 = (RelativeLayout) findViewById(R.id.btnlink1);
		btnLink1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				gotoActivity(WaitDriverConfirmActivity.class);
				finish();
			}
		});
		btnLink2 = (RelativeLayout) findViewById(R.id.btnlink2);
		btnLink2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				gotoActivity(WaitDriverConfirmActivity.class);
				finish();
			}
		});
		btnLink3 = (RelativeLayout) findViewById(R.id.btnlink3);
		btnLink3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				gotoActivity(WaitDriverConfirmActivity.class);
				finish();
			}
		});

	}

	// --------------------------------------------
	// --- Set up for map
	// --------------------------------------------
	private void setUpMap() {
		if (map == null) {
			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		}
		if (gps.canGetLocation()) {
			handler.post(runGoogleUpdateLocation);
		} else {
			gps.showSettingsAlert();
		}
		map.setMyLocationEnabled(true);
		map.getUiSettings().setMyLocationButtonEnabled(false);
	}

	Runnable runGoogleUpdateLocation = new Runnable() {

		@Override
		public void run() {
			refreshMyLocation();
		}
	};

	private void refreshMyLocation() {
		Location location = null;
		if (map != null) {
			location = map.getMyLocation();
		}
		if (location == null) {
			if (gps.canGetLocation()) {
				location = gps.getLocation();
			}
			handler.postDelayed(runGoogleUpdateLocation, 2 * 1000);
		}
		setLocationLatLong(location.getLongitude(), location.getLatitude());
	}

	private void setLocationLatLong(double longitude, double latitude) {
		LatLng latLng = new LatLng(latitude, longitude);
		map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
		// zoom : 2-21
	}
}
