package com.alaryani.aamrny.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ListView;

import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.adapters.RequestPassengerAdapter;
import com.alaryani.aamrny.config.Constant;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.object.RequestObj;
import com.alaryani.aamrny.service.GPSTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RequestPassengerActivity extends BaseActivity implements
        LocationListener, RequestPassengerAdapter.IListennerAdapter {

    private ListView lvRequestPassenger;
    private RequestPassengerAdapter adapter;
    private ArrayList<RequestObj> array;
    public PreferencesManager preferencesManager;
    private LocationManager locationManager;
    private String provider;
    private GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_request_passenger);
        initWithoutHeader();
        preferencesManager = PreferencesManager.getInstance(context);
        gps = new GPSTracker(this);
        initView();
        initControl();

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver,
                new IntentFilter(Constant.ACTION_PASSENGER_CREATE_REQUEST));
    }

    @Override
    protected void onDestroy() {
        /* DESTROY RECEIVER MESSAGE */
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        super.onDestroy();
    }

    /* FOR RECEIVER MESSAGE */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(Constant.KEY_ACTION);
            if (action.equals(Constant.ACTION_PASSENGER_CREATE_REQUEST)) {
                showMyRequest();
            } else {
                if (action.equals(Constant.ACTION_PASSENGER_CANCEL_REQUEST)) {
                    showToastMessage(R.string.message_you_request_cancel_by_driver);
                    supdateRequest();
                }
            }
        }
    };

    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        showMyRequest();
        preferencesManager.setDriverCurrentScreen("RequestPassengerActivity");
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}*/
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the location listener updates when Activity is paused */
    @Override
    public void onPause() {
        super.onPause();
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}*/
        locationManager.removeUpdates(this);
    }

    private void initView() {
        lvRequestPassenger = (ListView) findViewById(R.id.lvRequestPassenger);
    }

    private void initControl() {
        array = new ArrayList<RequestObj>();
        adapter = new RequestPassengerAdapter(this, array, this);
        lvRequestPassenger.setAdapter(adapter);
    /*	lvRequestPassenger.setOnItemClickListener(new OnItemClickListener() {
            @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				driverConfirm(array.get(position).getId());
			}
		});*/
    }

    private void showMyRequest() {
        ModelManager.showMyRequest(preferencesManager.getToken(), self, true,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            array.clear();
                            array.addAll(ParseJsonUtil.parseMyRequest(json));
                            if (array.size() == 0) {
                                showToastMessage(R.string.message_you_request_cancel_by_driver);
                            }
                            adapter.setArrViews(array);
                            adapter.notifyDataSetChanged();
                            lvRequestPassenger.invalidateViews();
                        } else {
                            showToastMessage(ParseJsonUtil.getMessage(json));
                        }
                    }

                    @Override
                    public void onError() {
                        showToastMessage(getResources().getString(
                                R.string.message_have_some_error));
                    }
                });
    }

    private void refreshMyRequest() {
        ModelManager.showMyRequest(preferencesManager.getToken(), self, true,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            array.clear();
                            array.addAll(ParseJsonUtil.parseMyRequest(json));
                            if (array.size() == 0) {
                                finish();
                            }
                            adapter.setArrViews(array);
                            adapter.notifyDataSetChanged();
                            lvRequestPassenger.invalidateViews();
                        } else {
                            showToastMessage(ParseJsonUtil.getMessage(json));
                        }
                    }

                    @Override
                    public void onError() {
                        showToastMessage(getResources().getString(
                                R.string.message_have_some_error));
                    }
                });
    }

    private void supdateRequest() {
        ModelManager.showMyRequest(preferencesManager.getToken(), self, true,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            array.clear();
                            array.addAll(ParseJsonUtil.parseMyRequest(json));
                            if (array != null && array.size() > 0) {
                                adapter.setArrViews(array);
                                adapter.notifyDataSetChanged();
                                lvRequestPassenger.invalidateViews();
                            } else {
                                gotoActivity(OnlineActivity.class);
                                finish();
                            }

                        } else {
                            showToastMessage(ParseJsonUtil.getMessage(json));
                        }
                    }

                    @Override
                    public void onError() {
                        showToastMessage(getResources().getString(
                                R.string.message_have_some_error));
                    }
                });
    }

    @Override
    public void onBackPressed() {
        gotoActivity(OnlineActivity.class);
        finish();
    }

    public void driverConfirm(String requestId) {
        if (!gps.canGetLocation()) {
            gps.showSettingsAlert();
        } else {
            String address = getCompleteAddressString(gps.getLatitude(), gps.getLongitude());
            ModelManager.driverConfirm(preferencesManager.getToken(), requestId, gps.getLatitude() + "", gps.getLongitude() + "", address,
                    this, true, new ModelManagerListener() {
                        @Override
                        public void onSuccess(String json) {
                            if (ParseJsonUtil.isSuccess(json)) {
                                globalValue.setCurrentOrder(ParseJsonUtil.parseCurrentOrder(json));
                                if (globalValue.getCurrentOrder().getPickUpAtA() != null && globalValue.getCurrentOrder().getPickUpAtA().equals("0")) {
                                    gotoActivity(StartTripForDriverActivity.class);
                                    preferencesManager
                                            .setDriverCurrentScreen(StartTripForDriverActivity.class
                                                    .getSimpleName());
                                } else {
                                    gotoActivity(ShowPassengerActivity.class);
                                    preferencesManager
                                            .setDriverCurrentScreen(ShowPassengerActivity.class
                                                    .getSimpleName());
                                }
                                preferencesManager.setDriverIsInTrip();
                                preferencesManager.setCurrentOrderId(globalValue.getCurrentOrder().getId());
                                finish();
                            } else {
                                showToastMessage(ParseJsonUtil.getMessage(json));
                                refreshMyRequest();
                            }
                        }

                        @Override
                        public void onError() {
                            showToastMessage(R.string.message_have_some_error);
                        }
                    });
        }

    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    @Override
    public void onLocationChanged(Location location) {

        updateCoordinate(location.getLatitude() + "", location.getLongitude()
                + "");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }

    @Override
    public void onProviderEnabled(String provider) {


    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onClickItemAdapter(int position) {
        driverConfirm(array.get(position).getId());
    }
}
