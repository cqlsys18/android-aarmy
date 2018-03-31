package com.alaryani.aamrny.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.config.Constant;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.LinkApi;
import com.alaryani.aamrny.googledirections.Route;
import com.alaryani.aamrny.googledirections.Routing;
import com.alaryani.aamrny.googledirections.RoutingListener;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.service.GPSTracker;
import com.alaryani.aamrny.utility.DateUtil;
import com.alaryani.aamrny.utility.NetworkUtil;
import com.alaryani.aamrny.widget.TextViewFontAwesome;
import com.alaryani.aamrny.widget.TextViewRaleway;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;

public class StartTripForPassengerActivity extends BaseActivity implements RoutingListener {

    private LinearLayout btnProfile;
    private CardView llHelp;
    private TextViewFontAwesome icProfile;
    private TextViewRaleway lblProfile;
    private TextViewRaleway lblName, tvSeat, lblCarPlate;
    private TextView lblPhone;
    private RatingBar ratingBar;
    private TextViewRaleway lblStartLocation, lblDistance, lblTimes;
    private TextViewRaleway lblEndlocation, lblDistanceTime, txtStatus, lblDuration;
    TextView txtStar;
    private TextView btnStartTrip;
    private TextView btnEndTrip;
    private ImageView imgPassenger, imgCarDriver;
    private ImageView imgCar;
    AQuery aq;

    private int mInterval = 1; // 5 seconds by default, can be changed later
    private Timer mTimer;
    private GoogleMap mMap;
    private GPSTracker gps;
    private double lat;
    private double lnt;
    LatLng startLocation, endLocation, startLocationA;
    Bitmap iconMarker;
    Runnable runnable;

    // For timer
    private Routing routing;
    private Marker mMarkerStartLocation, mMarkerEndLocation, mMarkerA;
    List<LatLng> polyz;
    private boolean checkZoom = true;
    private String phoneAdmin = "";
    private ImageView imgCall, imgSms;
    private boolean checkDataDistance = true;
    private boolean checkFirst = true;
    private Firebase ref;
    private LinearLayout llDuration, llDistance;
    private long startTime = 0L;

    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    boolean checkPath = true;
    private String dataPath = "";
    private int dem = 0;
    Handler handler = new Handler();
    Runnable updateMarker;

    @Override
    public void onResume() {
        if (preferencesManager.getPassengerCurrentScreen().equals(
                "RateDriverActivity")) {
            gotoActivity(RateDriverActivity.class);
            finish();
        } else {
            preferencesManager
                    .setPassengerCurrentScreen(StartTripForPassengerActivity.class
                            .getSimpleName());
            initData();

        }
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        aq = new AQuery(self);
        if (isCurrentAppLangRtl())
            setContentView(R.layout.activity_start_trip_rtl);
        else
            setContentView(R.layout.activity_start_trip);
        Firebase.setAndroidContext(this);
        ref = new Firebase(LinkApi.FIREBASE_URL);
        initUI();
        initView();
        Maps();
        initData();
        initControl();
        initLocalBroadcastManager();
//        updateData();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

//    public void updateData() {
//        handler = new Handler();
//
//        runnable = new Runnable() {
//
//            @Override
//            public void run() {
//                Log.e("update location", "update location:");
//                if (globalValue.getCurrentOrder() != null) {
//                    updatePositionForPassengerNoUpdate(globalValue.getCurrentOrder().getDriverId());
//                }
//                handler.postDelayed(runnable, 5000);
//            }
//        };
//        handler.postDelayed(runnable, 5000);
//    }


    @Override
    public void onBackPressed() {
    }

    private void initData() {
        ModelManager.getGeneralSettings(preferencesManager.getToken(),
                self, true, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            phoneAdmin = ParseJsonUtil
                                    .getPhoneAdmin(json);
                        }
                    }

                    @Override
                    public void onError() {
                    }
                });
        preferencesManager.setArrived("0");
        if (!getPreviousActivityName().equals(
                WaitDriverConfirmActivity.class.getName())) {
            ModelManager.showTripDetail(preferencesManager.getToken(),
                    preferencesManager.getCurrentOrderId(), context, true,
                    new ModelManagerListener() {

                        @Override
                        public void onSuccess(String json) {
                            if (ParseJsonUtil.isSuccess(json)) {

                                globalValue.setCurrentOrder(ParseJsonUtil
                                        .parseCurrentOrder(json));
                                endLocation = new LatLng(Double.parseDouble(ParseJsonUtil
                                        .parseCurrentOrder(json).getEndLat()), Double.parseDouble(ParseJsonUtil
                                        .parseCurrentOrder(json).getEndLong()));
                                tvSeat.setText(GlobalValue.convertLinkToString(StartTripForPassengerActivity.this, globalValue.convertToInt(globalValue.getCurrentOrder().getLink()) + ""));
                                lblPhone.setText(globalValue.getCurrentOrder()
                                        .getDriver_phone(false));
                                lblName.setText(globalValue.getCurrentOrder()
                                        .getDriverName());
                                lblCarPlate.setText(globalValue.getCurrentOrder().getCarPlate());

                                lblStartLocation.setText(globalValue
                                        .getCurrentOrder().getEndLocation());
                                lblEndlocation.setText(globalValue
                                        .getCurrentOrder().getEndLocation());
                                if (globalValue.getCurrentOrder()
                                        .getPassenger_rate() == null || globalValue.getCurrentOrder()
                                        .getPassenger_rate().isEmpty()) {
                                    txtStar.setText("0");
//                                    ratingBar.setRating(0);
                                } else {
                                    txtStar.setText("" + Float
                                            .parseFloat(globalValue
                                                    .getCurrentOrder()
                                                    .getPassenger_rate()) / 2);
//                                    ratingBar.setRating(Float
//                                            .parseFloat(globalValue
//                                                    .getCurrentOrder()
//                                                    .getPassenger_rate()) / 2);
                                }

                                aq.id(imgCarDriver).image(
                                        globalValue.getCurrentOrder()
                                                .getCarImage());
                                aq.id(imgPassenger).image(
                                        globalValue.getCurrentOrder()
                                                .getImageDriver());
                                if (globalValue.getCurrentOrder().getStatus().equals(Constant.STATUS_ARRIVED_B)) {
                                    txtStatus.setText(getString(R.string.lbl_arrived_b));
                                    llDistance.setVisibility(View.VISIBLE);
                                    llDuration.setVisibility(View.GONE);
                                } else if (globalValue.getCurrentOrder().getStatus().equals(Constant.STATUS_START_TASK)) {
                                    txtStatus.setText(getString(R.string.lblBeginTask));
                                    llDistance.setVisibility(View.GONE);
                                    llDuration.setVisibility(View.VISIBLE);
                                    Log.e("data", "data:" + globalValue.getCurrentOrder().getStartTimeWorking());
                                    startTime = Long.parseLong(globalValue.getCurrentOrder().getStartTimeWorking());
                                    //
                                    lblDuration.setText(DateUtil.convertTimeStampToDate(startTime + "", "dd MMM h:m a"));
//                                    customHandler.postDelayed(updateTimerThread, 0);
                                }
                                getDistance();
                                updatePositionForPassenger(globalValue.getCurrentOrder().getDriverId());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endLocation, 14.0f));
                            } else {
                                showToastMessage(ParseJsonUtil.getMessage(json));
                            }
                        }

                        @Override
                        public void onError() {
                            showToastMessage(R.string.message_have_some_error);
                        }
                    });
        } else {
            tvSeat.setText(GlobalValue.convertLinkToString(StartTripForPassengerActivity.this, globalValue.getCurrentOrder().getLink()));
            lblPhone.setText(globalValue.getCurrentOrder().getDriver_phone(false));
            lblName.setText(globalValue.getCurrentOrder()
                    .getDriverName());
            lblCarPlate.setText(globalValue.getCurrentOrder().getCarPlate());
            lblStartLocation.setText(globalValue.getCurrentOrder()
                    .getEndLocation());
            lblEndlocation.setText(globalValue.getCurrentOrder()
                    .getEndLocation());
            if (globalValue.getCurrentOrder()
                    .getPassenger_rate() == null || globalValue.getCurrentOrder()
                    .getPassenger_rate().isEmpty()) {
                txtStar.setText("0");
//                                    ratingBar.setRating(0);
            } else {
                txtStar.setText("" + Float
                        .parseFloat(globalValue
                                .getCurrentOrder()
                                .getPassenger_rate()) / 2);
//                                    ratingBar.setRating(Float
//                                            .parseFloat(globalValue
//                                                    .getCurrentOrder()
//                                                    .getPassenger_rate()) / 2);
            }
            aq.id(imgCarDriver).image(
                    globalValue.getCurrentOrder()
                            .getCarImage());
            aq.id(imgPassenger).image(
                    globalValue.getCurrentOrder()
                            .getImageDriver());
            if (globalValue.getCurrentOrder().getStatus().equals(Constant.STATUS_ARRIVED_B)) {
                txtStatus.setText(getString(R.string.lbl_arrived_b));
                llDistance.setVisibility(View.VISIBLE);
                llDuration.setVisibility(View.GONE);
            } else if (globalValue.getCurrentOrder().getStatus().equals(Constant.STATUS_START_TASK)) {
                txtStatus.setText(getString(R.string.lblBeginTask));
                llDistance.setVisibility(View.GONE);
                llDuration.setVisibility(View.VISIBLE);
                Log.e("data", "data:" + globalValue.getCurrentOrder().getStartTimeWorking());
                startTime = Long.parseLong(globalValue.getCurrentOrder().getStartTimeWorking());
                lblDuration.setText(DateUtil.convertTimeStampToDate(startTime + "", "dd MMM h:m a"));
//                customHandler.postDelayed(updateTimerThread, 0);
            }
            getDistance();
            endLocation = new LatLng(Double.parseDouble(globalValue.getCurrentOrder().getStartLong()), Double.parseDouble(globalValue.getCurrentOrder().getEndLong()));
            updatePositionForPassenger(globalValue.getCurrentOrder().getDriverId());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endLocation, 14.0f));
        }
    }

    protected void showDistanceAndTime() {
        if (startLocation != null && endLocation != null) {
//            findDirections(startLocation.latitude, startLocation.longitude, endLocation.latitude, endLocation.longitude, GMapV2Direction.MODE_DRIVING);
            routing = new Routing(Routing.TravelMode.DRIVING);
            routing.registerListener(this);
            routing.execute(startLocation, endLocation);
            checkDataDistance = false;
        }

    }

    private void getDistance() {
        if (globalValue.getCurrentOrder() != null) {
            ModelManager.showDistance(preferencesManager.getToken(),
                    globalValue.getCurrentOrder().getId(), context, false,
                    new ModelManagerListener() {

                        @Override
                        public void onSuccess(String json) {
                            if (ParseJsonUtil.isSuccess(json)) {
                                try {
                                    Float temp = Float.parseFloat(ParseJsonUtil
                                            .getDistance(json));
                                    if (temp.toString().length() > 6) {
                                        lblDistance.setText(round(Double.parseDouble(temp.toString().substring(
                                                0, 6)), 1)
                                                + " " + getString(R.string.unit_measure));
                                    } else {
                                        lblDistance.setText(round(Double.parseDouble(temp.toString()), 1)
                                                + " " + getString(R.string.unit_measure));
                                    }
                                } catch (NumberFormatException e) {
                                    lblDistance.setText("0"
                                            + " " + getString(R.string.unit_measure));
                                }

                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    protected void showDirection() {
        if (startLocationA != null && endLocation != null) {
            routing = new Routing(Routing.TravelMode.DRIVING);
            routing.registerListener(this);
            routing.execute(startLocationA, endLocation);
            checkDataDistance = true;
        }
    }

    public void setStartMarker() {
        if (endLocation != null) {
            if (mMarkerStartLocation != null) {
                mMarkerStartLocation.remove();
            }
            iconMarker = BitmapFactory.decodeResource(
                    getResources(), R.drawable.ic_position_b);
            iconMarker = Bitmap.createScaledBitmap(iconMarker,
                    iconMarker.getWidth(), iconMarker.getHeight(),
                    false);
            mMarkerStartLocation = mMap.addMarker(new MarkerOptions().position(
                    endLocation).icon(
                    BitmapDescriptorFactory.fromBitmap(iconMarker)));
//            showDirection();
        }
    }

    public void setStartMarkerNoUpdateDirection() {
        if (endLocation != null) {
            if (mMarkerStartLocation != null) {
                mMarkerStartLocation.remove();
            }
            iconMarker = BitmapFactory.decodeResource(
                    getResources(), R.drawable.ic_position_b);
            iconMarker = Bitmap.createScaledBitmap(iconMarker,
                    iconMarker.getWidth(), iconMarker.getHeight(),
                    false);
            mMarkerStartLocation = mMap.addMarker(new MarkerOptions().position(
                    endLocation).icon(
                    BitmapDescriptorFactory.fromBitmap(iconMarker)));
        }
    }

    public void updatePositionForPassenger(final String driverId) {
        updateMarker = new Runnable() {
            @Override
            public void run() {
                ref.child("user").child(driverId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        try {
                            JSONObject object = new JSONObject(snapshot.getValue().toString());
                            String latitude = object.getString("lat");
                            String longitude = object.getString("lng");
                            startLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                            if (checkFirst) {
                                checkFirst = false;
                                if (globalValue.getCurrentOrder().getPickUpAtA().equals("1")) {
                                    setLocationLatLongA(startLocationA);
                                } else {
                                    showDirection();

                                }
                                setStartMarker();
                                setLocationLatLong(startLocation);
                            } else {
                                if (!checkPath) {
                                    showDistanceAndTime();
                                    setLocationLatLongNoCamera(startLocation);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Getting the data from snapshot
//                for (DataSnapshot child : snapshot.getChildren()) {
//                    UserUpdate person = (UserUpdate) child.getValue(UserUpdate.class);
//                    Log.e("data", "data lat:" + person.getLat());
//                }
                    }
//                String latitude = person.getLat();
//                String longitude = person.getLng();
//                startLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
//                showDistanceAndTime();
//                setLocationLatLong(startLocation);

//            }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });
            }
        };
        handler.postDelayed(updateMarker, 2000);
//        ModelManager.getLocationDriver(this, driverId, false, new ModelManagerListener() {
//            @Override
//            public void onError() {
//
//            }
//
//            @Override
//            public void onSuccess(String json) {
//                try {
//                    JSONObject jsonObject = new JSONObject(json);
//                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                    String latitude = jsonObject1.getString("driverLat");
//                    String longitude = jsonObject1.getString("driverLon");
//                    Log.e("startLocation", "startLocation:" + Double.parseDouble(latitude));
//                    startLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))
//                    ;
//                    setLocationLatLong(startLocation);
//                    setStartMarker();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
    }
//    public void updatePositionForPassenger(String driverId) {
//        ModelManager.getLocationDriver(this, driverId, false, new ModelManagerListener() {
//            @Override
//            public void onError() {
//
//            }
//
//            @Override
//            public void onSuccess(String json) {
//                try {
//                    JSONObject jsonObject = new JSONObject(json);
//                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                    String latitude = jsonObject1.getString("driverLat");
//                    String longitude = jsonObject1.getString("driverLon");
//                    startLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))
//                    ;
//                    setLocationLatLong(startLocation);
//                    setStartMarker();
//                    setLocationLatLongA(startLocationA);
////                    setLocationLatLong(endLocation);
////                    mMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
////                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 16));
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//    }

//    public void updatePositionForPassengerNoUpdate(String driverId) {
//        ModelManager.getLocationDriver(this, driverId, false, new ModelManagerListener() {
//            @Override
//            public void onError() {
//
//            }
//
//            @Override
//            public void onSuccess(String json) {
//                try {
//                    JSONObject jsonObject = new JSONObject(json);
//                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                    String latitude = jsonObject1.getString("driverLat");
//                    String longitude = jsonObject1.getString("driverLon");
//                    startLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))
//                    ;
//                    showDistanceAndTime();
//                    setLocationLatLongNoCamera(startLocation);
////                    setLocationLatLongNoCamera(endLocation);
//                    setStartMarkerNoUpdateDirection();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//    }

    private void initView() {
        //setHeaderTitle(R.string.lbl_title_start_trip);
        btnProfile = (LinearLayout) findViewById(R.id.btn_profile);
        llHelp = (CardView) findViewById(R.id.cv_help);
        icProfile = (TextViewFontAwesome) findViewById(R.id.ic_profile);
        lblProfile = (TextViewRaleway) findViewById(R.id.lbl_profile);
        tvSeat = (TextViewRaleway) findViewById(R.id.tvSeat);
        lblName = (TextViewRaleway) findViewById(R.id.lblName);
        lblCarPlate = (TextViewRaleway) findViewById(R.id.lblCarPlate);
        //lblDrvierId = (TextViewRaleway) findViewById(R.id.lblDrvierId);
        lblPhone = (TextView) findViewById(R.id.lblPhone);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        lblStartLocation = (TextViewRaleway) findViewById(R.id.lblStartLocation);
        lblEndlocation = (TextViewRaleway) findViewById(R.id.lblEndlocation);
        txtStatus = (TextViewRaleway) findViewById(R.id.txtStatus);
        lblDistance = (TextViewRaleway) findViewById(R.id.lblDistance);
        txtStar = (TextView) findViewById(R.id.txtStar);
        lblTimes = (TextViewRaleway) findViewById(R.id.lblTimes);
//        btnStartTrip = (TextView) findViewById(R.id.btnStartTrip);
        btnEndTrip = (TextView) findViewById(R.id.btnEndTrip);
        lblDistanceTime = (TextViewRaleway) findViewById(R.id.lblDistanceTime);
        lblDuration = (TextViewRaleway) findViewById(R.id.lblDuration);

        imgPassenger = (ImageView) findViewById(R.id.imgPassenger);
        imgCar = (ImageView) findViewById(R.id.imgCar);
        imgCarDriver = (ImageView) findViewById(R.id.imgCarDriver);
        imgCall = (ImageView) findViewById(R.id.imgCall);
        imgSms = (ImageView) findViewById(R.id.imgSms);
        llDuration = (LinearLayout) findViewById(R.id.llDuration);
        llDistance = (LinearLayout) findViewById(R.id.llDistance);
    /*	LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, Mode.SRC_ATOP);*/
    }

    private void initControl() {
//        btnStartTrip.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // btnEndTrip.setVisibility(v.VISIBLE);
//                // btnStartTrip.setVisibility(v.GONE);
//            }
//        });
        llHelp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.checkNetworkAvailable(StartTripForPassengerActivity.this)) {
                    ModelManager.sendNeedHelp(preferencesManager.getToken(), preferencesManager.getCurrentOrderId(), context, true, new ModelManagerListener() {
                        @Override
                        public void onError() {

                        }

                        @Override
                        public void onSuccess(String json) {
                            Log.e("Success", "Success");
                        }
                    });
                }
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"
                        + phoneAdmin));
                startActivity(callIntent);
            }
        });
        btnEndTrip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                gotoActivity(RateDriverActivity.class);
                finish();
            }
        });
        lblPhone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalValue.getCurrentOrder().getDriver_phone(false).length() > 0) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"
                            + globalValue.getCurrentOrder().getDriver_phone(false)));
                    startActivity(callIntent);
                } else {
                    showToastMessage(R.string.msg_call_phone);
                }
            }
        });
        imgCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + globalValue.getCurrentOrder()
                        .getDriver_phone(false)));
                startActivity(intent);
            }
        });
        imgSms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", globalValue.getCurrentOrder()
                        .getDriver_phone(false));
                smsIntent.putExtra("sms_body", "message");
                startActivity(smsIntent);
            }
        });
    }

    private void initLocalBroadcastManager() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_TASKER_ARRIVED_B);
        intentFilter.addAction(Constant.ACTION_TASKER_START_TASK);
        intentFilter.addAction(Constant.ACTION_DRIVER_END_TRIP);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver,
                intentFilter);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String json = intent.getStringExtra(Constant.KEY_DATA);
            String action = intent.getStringExtra(Constant.KEY_ACTION);
            if (action.equals(Constant.ACTION_DRIVER_END_TRIP)) {
                preferencesManager.setBeginTask("0");
//                customHandler.removeCallbacks(updateTimerThread);
                /*if(isCurrentAppLangRtl()){
                    globalValue.getCurrentOrder().setActualFare(arabicToDecimal(ParseJsonUtil.getActualFare(json)));
                }else{
                    globalValue.getCurrentOrder().setActualFare(ParseJsonUtil.getActualFare(json));
                }*/
                globalValue.getCurrentOrder().setActualFare(ParseJsonUtil.getActualFare(json));
                gotoActivity(RateDriverActivity.class);
                finish();
            } else if (action.equals(Constant.ACTION_TASKER_ARRIVED_B)) {
                txtStatus.setText(getString(R.string.lbl_arrived_b));
                llDistance.setVisibility(View.VISIBLE);
                llDuration.setVisibility(View.GONE);
            } else if (action.equals(Constant.ACTION_TASKER_START_TASK)) {
                preferencesManager.setArrivedB("0");
                ModelManager.showTripDetail(preferencesManager.getToken(),
                        preferencesManager.getCurrentOrderId(), context, false,
                        new ModelManagerListener() {

                            @Override
                            public void onSuccess(String json) {
                                if (ParseJsonUtil.isSuccess(json)) {
                                    globalValue.setCurrentOrder(ParseJsonUtil
                                            .parseCurrentOrder(json));
                                    Log.e("data", "data:" + globalValue.getCurrentOrder().getStartTimeWorking());
                                    startTime = Long.parseLong(globalValue.getCurrentOrder().getStartTimeWorking());
                                    //
                                    lblDuration.setText(DateUtil.convertTimeStampToDate(startTime + "", "dd MMM h:m a"));
//                                    customHandler.postDelayed(updateTimerThread, 0);
                                    txtStatus.setText(getString(R.string.lblBeginTask));
                                    llDistance.setVisibility(View.GONE);
                                    llDuration.setVisibility(View.VISIBLE);
                                } else {
                                    showToastMessage(ParseJsonUtil.getMessage(json));
                                }
                            }

                            @Override
                            public void onError() {
                                showToastMessage(R.string.message_have_some_error);
                            }
                        });

            }

        }
    };

    private void Maps() {
        //initData();
        setUpMap();
    }

    private void setLocationLatLong(LatLng location) {
        // set filter
        lat = location.latitude;
        lnt = location.longitude;
        LatLng latLng = new LatLng(location.latitude, location.longitude);
        if (mMarkerEndLocation != null) {
            mMarkerEndLocation.remove();
        }
        iconMarker = BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_driver);
        iconMarker = Bitmap.createScaledBitmap(iconMarker,
                iconMarker.getWidth(), iconMarker.getHeight(),
                false);
        mMarkerEndLocation = mMap.addMarker(new MarkerOptions().position(
                latLng).icon(
                BitmapDescriptorFactory.fromBitmap(iconMarker)));
        // 2-21
    }

    private void setLocationLatLongA(LatLng location) {
        // set filter
        lat = location.latitude;
        lnt = location.longitude;
        LatLng latLng = new LatLng(location.latitude, location.longitude);
        if (mMarkerA != null) {
            mMarkerA.remove();
        }
        iconMarker = BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_position_a);
        iconMarker = Bitmap.createScaledBitmap(iconMarker,
                iconMarker.getWidth(), iconMarker.getHeight(),
                false);
        mMarkerA = mMap.addMarker(new MarkerOptions().position(
                latLng).icon(
                BitmapDescriptorFactory.fromBitmap(iconMarker)));
        showDirection();
        // 2-21
    }

    private void setLocationLatLongNoCamera(LatLng location) {
        // set filter
        lat = location.latitude;
        lnt = location.longitude;
        LatLng latLng = new LatLng(location.latitude, location.longitude);
        if (mMarkerEndLocation != null) {
            mMarkerEndLocation.remove();
        }
        iconMarker = BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_driver);
        iconMarker = Bitmap.createScaledBitmap(iconMarker,
                iconMarker.getWidth(), iconMarker.getHeight(),
                false);
        mMarkerEndLocation = mMap.addMarker(new MarkerOptions().position(
                latLng).icon(
                BitmapDescriptorFactory.fromBitmap(iconMarker)));
        // 2-21
    }

    private void setUpMap() {
        // TODO Auto-generated method stub

        if (mMap == null) {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragMaps);
            mMap = fm.getMap();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        ModelManager.showTripDetail(preferencesManager.getToken(),
                preferencesManager.getCurrentOrderId(), context, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            globalValue.setCurrentOrder(ParseJsonUtil
                                    .parseCurrentOrder(json));
                            startLocationA = new LatLng(Double.parseDouble(globalValue.getCurrentOrder().getStartLat()), Double.parseDouble(globalValue.getCurrentOrder().getStartLong()));
                        } else {
                            showToastMessage(ParseJsonUtil.getMessage(json));
                        }
                    }

                    @Override
                    public void onError() {
                        showToastMessage(R.string.message_have_some_error);
                    }
                });


    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        if (gps != null) {
            gps.stopUsingGPS();
        }
//        customHandler.removeCallbacks(updateTimerThread);
        handler.removeCallbacks(updateMarker);
        super.onDestroy();
    }

    @Override
    public void onRoutingFailure() {
        lblDistanceTime.setText(dataPath);
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route) {
        if (startLocation != null || endLocation != null) {
//            mMap.clear();
//            setLocationLatLong(startLocation);
//            setStartMarker();
            if (checkDataDistance) {
                PolylineOptions polyOptions = new PolylineOptions();
                polyOptions.color(R.color.second_primary);
                polyOptions.width(10);
                polyOptions.addAll(mPolyOptions.getPoints());
                mMap.addPolyline(polyOptions);
                lblTimes.setText(route.getDurationText());
                String msg = getString(R.string.msgBeginTripDistance);
                msg = msg.replace("[a]", route.getDistanceText());
                msg = msg.replace("[b]", route.getDurationText());
                lblDistanceTime.setText(msg);
                checkPath = false;
                dataPath = msg;
            } else {
                String msg = getString(R.string.msgBeginTripDistance);
                msg = msg.replace("[a]", route.getDistanceText());
                msg = msg.replace("[b]", route.getDurationText());
                lblDistanceTime.setText(msg);
                dataPath = msg;
            }

        }
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            DateTime today = new DateTime();
            DateTime yesterday = new DateTime(startTime * 1000L);
            Duration duration = new Duration(yesterday, today);
            timeInMilliseconds = duration.getStandardSeconds() * 1000;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hour = mins / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
//            lblCountTime.setText("" + mins + ":"
//                    + String.format("%02d", secs) + ":"
//                    + String.format("%03d", milliseconds));
            lblDuration.setText(String.format("%02d", hour) + ":" + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }

    };
}
