package com.alaryani.aamrny.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alaryani.aamrny.adapters.ChattingAdapter;
import com.alaryani.aamrny.object.ChattingResponse;
import com.androidquery.AQuery;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
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
import com.alaryani.aamrny.googledirections.Route;
import com.alaryani.aamrny.googledirections.Routing;
import com.alaryani.aamrny.googledirections.RoutingListener;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.service.GPSTracker;
import com.alaryani.aamrny.widget.TextViewFontAwesome;
import com.alaryani.aamrny.widget.TextViewRaleway;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/***
 * todo //
 */
public class ShowPassengerActivity extends BaseActivity implements
        OnClickListener, LocationListener, RoutingListener, LocationSource.OnLocationChangedListener {
    RelativeLayout btnLoginFacebook, btnLoginGoogle;
    private LocationManager locationManager;
    private String provider;
    private LinearLayout btnProfile;
    private TextViewFontAwesome icProfile;
    private TextViewRaleway tvSeat;
    private TextView lblName;
    private TextView lblPhone;
    private RatingBar ratingBar;
    private TextViewRaleway lblStart;
    private TextViewRaleway lblEnd;
    private TextViewRaleway lblDistance, lblTimes;
    TextView txtStar;
    private ImageView imgPassenger;
    private TextView btnArrived;
    private TextView btnCancelTrip;
    private TextView imgChat;
    static AQuery aq;

    private GoogleMap mMap;
    private GPSTracker gps;
    private double lat;
    private double lnt;
    Handler handler;
    private ScrollView scrollView;
    LatLng startLocation, endLocation;
    Bitmap iconMarker;

    // For timer
    private int mInterval = 1; // 5 seconds by default, can be changed later
    private Timer mTimer;
    private Routing routing;
    private Marker mMarkerStartLocation, mMarkerDriverLocation;
    List<LatLng> polyz;
    Runnable runnable;
    private CardView imgBack;
    private ImageView imgCall, imgSms;
    ArrayList<ChattingResponse> arrayList;
    ChattingAdapter chattingAdapter;
    Dialog chat_dialog;

    public ShowPassengerActivity() {
    }

    private void autoRefreshEvents() {
        if (mTimer == null) {
            mTimer = new Timer();
            RefreshEvents refresh = new RefreshEvents();
            try {
                mTimer.scheduleAtFixedRate(refresh, mInterval * 10 * 1000,
                        mInterval * 10 * 1000);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onRoutingFailure() {
        showDirection();
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
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(R.color.second_primary);
            polyOptions.width(10);
            polyOptions.addAll(mPolyOptions.getPoints());
            mMap.addPolyline(polyOptions);
            lblTimes.setText(route.getDurationText());
        }
    }


    protected void showDirection() {
        if (startLocation != null && endLocation != null) {
            routing = new Routing(Routing.TravelMode.DRIVING);
            routing.registerListener(this);
            routing.execute(startLocation, endLocation);
        }
    }

//    public void setDriverMarker() {
//        if (startLocation != null) {
//            if (mMarkerDriverLocation != null) {
//                mMarkerDriverLocation.remove();
//            }
//            iconMarker = BitmapFactory.decodeResource(
//                    getResources(), R.drawable.ic_driver);
//            iconMarker = Bitmap.createScaledBitmap(iconMarker,
//                    iconMarker.getWidth(), iconMarker.getHeight(),
//                    false);
//            mMarkerDriverLocation = mMap.addMarker(new MarkerOptions().position(
//                    startLocation).icon(
//                    BitmapDescriptorFactory.fromBitmap(iconMarker)));
//            showDirection();
//        }
//    }

//    public void setDriverMarkerNoUpdateDirection() {
//        if (startLocation != null) {
//            if (mMarkerDriverLocation != null) {
//                mMarkerDriverLocation.remove();
//            }
//            iconMarker = BitmapFactory.decodeResource(
//                    getResources(), R.drawable.ic_driver);
//            iconMarker = Bitmap.createScaledBitmap(iconMarker,
//                    iconMarker.getWidth(), iconMarker.getHeight(),
//                    false);
//            mMarkerDriverLocation = mMap.addMarker(new MarkerOptions().position(
//                    startLocation).icon(
//                    BitmapDescriptorFactory.fromBitmap(iconMarker)));
//        }
//    }

    public void setStartMarker() {
        if (endLocation != null) {
            if (mMarkerStartLocation != null) {
                mMarkerStartLocation.remove();
            }
            iconMarker = BitmapFactory.decodeResource(
                    getResources(), R.drawable.ic_position_a);
            iconMarker = Bitmap.createScaledBitmap(iconMarker,
                    iconMarker.getWidth(), iconMarker.getHeight(),
                    false);
            mMarkerStartLocation = mMap.addMarker(new MarkerOptions().position(
                    endLocation).icon(
                    BitmapDescriptorFactory.fromBitmap(iconMarker)));
            showDirection();
        }
    }

    private class RefreshEvents extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Do some thing here
                    getDistance();
                }
            });
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isCurrentAppLangRtl())
            setContentView(R.layout.activity_show_passenger_rtl);
        else
            setContentView(R.layout.activity_show_passenger);
        aq = new AQuery(this);
        gps = new GPSTracker(this);
        initUI();
        initView();
        Maps();
        initData();
        initControl();
        initLocationService();
        initLocalBroadcastManager();
        autoRefreshEvents();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        updateData();


    }

    private void Maps() {
        //initData();
        setUpMap();
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
        if (gps.canGetLocation()) {
            startLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
        } else {
            gps.showSettingsAlert();
        }

    }

    @Override
    public void onBackPressed() {
        showCancelTripDialog();
    }

    ;

    private void initData() {
        if (getPreviousActivityName().equals(
                SplashActivity.class.getSimpleName())) {

            ModelManager.showTripDetail(preferencesManager.getToken(),
                    preferencesManager.getCurrentOrderId(), context, true,
                    new ModelManagerListener() {
                        @Override
                        public void onSuccess(String json) {
                            Log.e("tripId", "tripId:" + json);
                            if (ParseJsonUtil.isSuccess(json)) {
                                globalValue.setCurrentOrder(ParseJsonUtil
                                        .parseCurrentOrder(json));
                                Log.e("phone", "phone:" + globalValue.getCurrentOrder()
                                        .getPassenger_phone(true));
                                endLocation = new LatLng(Double.parseDouble(ParseJsonUtil
                                        .parseCurrentOrder(json).getStartLat()), Double.parseDouble(ParseJsonUtil
                                        .parseCurrentOrder(json).getStartLong()));
                                lblName.setText(globalValue.getCurrentOrder()
                                        .getPassengerName());
                                tvSeat.setText(GlobalValue.convertLinkToString(ShowPassengerActivity.this, globalValue.getCurrentOrder().getLink() + ""));
                                lblPhone.setText(globalValue.getCurrentOrder()
                                        .getPassenger_phone(true));
                                lblStart.setText(globalValue.getCurrentOrder()
                                        .getStartLocation());
                                lblEnd.setText(globalValue.getCurrentOrder()
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
                                Log.e("image", "image:" + globalValue.getCurrentOrder()
                                        .getImagePassenger());
                                aq.id(R.id.imgPassenger).image(
                                        globalValue.getCurrentOrder()
                                                .getImagePassenger());
                                getDistance();
                                setLocationLatLong(startLocation);
                                setStartMarker();
//                                setDriverMarker();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 14.0f));
//                                mMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
//                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start;Location, 16));

                            } else {
                                showToastMessage(ParseJsonUtil.getMessage(json));
                            }

//                            getChatApi();
                        }

                        @Override
                        public void onError() {

                            showToastMessage(R.string.message_have_some_error);
                        }
                    });

        } else {
            lblName.setText(globalValue.getCurrentOrder().getPassengerName());
            tvSeat.setText(GlobalValue.convertLinkToString(ShowPassengerActivity.this, globalValue.convertToInt(globalValue.getCurrentOrder().getLink()) + ""));
            lblPhone.setText(globalValue.getCurrentOrder().getPassenger_phone(true));
            lblStart.setText(globalValue.getCurrentOrder().getStartLocation());
            lblEnd.setText(globalValue.getCurrentOrder().getEndLocation());
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
            Log.e("image", "image:" + globalValue.getCurrentOrder()
                    .getImagePassenger());
            aq.id(R.id.imgPassenger).image(
                    globalValue.getCurrentOrder().getImagePassenger());
            getDistance();
            endLocation = new LatLng(Double.parseDouble(globalValue.getCurrentOrder().getStartLat()), Double.parseDouble(globalValue.getCurrentOrder().getStartLong()));
            setLocationLatLong(startLocation);
            setStartMarker();
//            setDriverMarker();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 14.0f));
//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }

    private void showCancelTripDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.msg_do_you_cancel)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                cancelTrip();
                            }
                        }).setNegativeButton(android.R.string.cancel, null)
                .create().show();
    }

    private void initView() {
        arrayList = new ArrayList<>();
        chattingAdapter = new ChattingAdapter(ShowPassengerActivity.this, arrayList);
//		setHeaderTitle(R.string.lbl_order_confirm);
        btnProfile = (LinearLayout) findViewById(R.id.btn_profile);
        icProfile = (TextViewFontAwesome) findViewById(R.id.ic_profile);
        lblName = (TextView) findViewById(R.id.lblName);
        tvSeat = (TextViewRaleway) findViewById(R.id.tvSeat);
        lblPhone = (TextView) findViewById(R.id.lblPhone);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        imgChat = (TextView) findViewById(R.id.imgChat);

        lblStart = (TextViewRaleway) findViewById(R.id.lblStart);
        lblStart.setSelected(true);
        lblEnd = (TextViewRaleway) findViewById(R.id.lblEnd);
        lblDistance = (TextViewRaleway) findViewById(R.id.lblDistance);
        lblTimes = (TextViewRaleway) findViewById(R.id.lblTimes);
        imgPassenger = (ImageView) findViewById(R.id.imgPassenger);
        btnArrived = (TextView) findViewById(R.id.btnArrived);
        btnCancelTrip = (TextView) findViewById(R.id.btnCancelTrip);
        txtStar = (TextView) findViewById(R.id.txtStar);
        imgBack = (CardView) findViewById(R.id.cv_back);
        imgCall = (ImageView) findViewById(R.id.imgCall);
        imgSms = (ImageView) findViewById(R.id.imgSms);
        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initControl() {
        btnArrived.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("param", "param:" + preferencesManager.getToken() + "-" + globalValue.getCurrentOrder().getId());
                ModelManager.changeStatus(preferencesManager.getToken(), globalValue
                        .getCurrentOrder().getId(), Constant.STATUS_ARRIVED_A, context, true, new ModelManagerListener() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onSuccess(String json) {
                        gotoActivity(StartTripForDriverActivity.class);
                        preferencesManager.setDriverCurrentScreen(StartTripForDriverActivity.class
                                .getSimpleName());
                        finish();
                    }
                });

            }
        });

        btnCancelTrip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelTripDialog();
            }
        });

        lblPhone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalValue.getCurrentOrder().getPassenger_phone(false).length() > 0) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"
                            + globalValue.getCurrentOrder()
                            .getPassenger_phone(false)));
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
                        .getPassenger_phone(false)));
                startActivity(intent);
            }
        });
        imgSms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", globalValue.getCurrentOrder().getPassenger_phone(false));
                smsIntent.putExtra("sms_body", "message");
                startActivity(smsIntent);
            }
        });

        imgChat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatDialog();
            }
        });
    }

    private void initLocationService() {
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
    }

    private void setLocationLatLong(LatLng location) {
        // set filter
        lat = location.latitude;
        lnt = location.longitude;
        LatLng latLng = new LatLng(location.latitude, location.longitude);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        // 2-21
    }

    private void openChatDialog() {
        preferencesManager.setUserOnChatScreen("1");

        chat_dialog = new Dialog(this, android.R.style.Theme_Holo_NoActionBar_Fullscreen);
        chat_dialog.setContentView(R.layout.chat_dialog);
        chat_dialog.setCanceledOnTouchOutside(false);

        ImageView cross = (ImageView) chat_dialog.findViewById(R.id.cross);
        ListView listView = (ListView) chat_dialog.findViewById(R.id.listView);
        final EditText edit_chat_text = (EditText) chat_dialog.findViewById(R.id.edit_chat_text);
        TextView send = (TextView) chat_dialog.findViewById(R.id.send);
        listView.setAdapter(chattingAdapter);
        cross.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesManager.setUserOnChatScreen("0");
                chat_dialog.dismiss();
            }
        });
        send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_chat_text.getText().toString().equals("")) {
                    showToastMessage(getResources().getString(R.string.enter_message));
                } else {
                    sendMessageApi(edit_chat_text.getText().toString());
                    edit_chat_text.setText("");
                }
            }
        });

        chat_dialog.show();

        getChatApi();
    }


    private void getChatApi() {
        if (arrayList.size() > 0)
            arrayList.clear();

        ModelManager.getChat(preferencesManager.getToken(), globalValue.getCurrentOrder().getId(), context, false,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            try {
                                JSONObject jsonObject1 = new JSONObject(json);
                                JSONArray data = jsonObject1.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsonObject11 = data.getJSONObject(i);
                                    ChattingResponse chattingResponse = new ChattingResponse();
                                    chattingResponse.setId(jsonObject11.getString("id"));
                                    chattingResponse.setMessage(jsonObject11.getString("message"));
                                    chattingResponse.setCreated_time(jsonObject11.getString("created_time"));

                                    chattingResponse.setSender_id(jsonObject11.getString("sender_id"));
                                    chattingResponse.setReceiver_id(jsonObject11.getString("receiver_id"));
                                    arrayList.add(chattingResponse);
                                }
                                chattingAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
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


    private void sendMessageApi(String message) {
        Log.e("idss", "driver " + globalValue.getCurrentOrder().getPassengerId() + " " + preferencesManager.getUserID());

        ModelManager.sendMessage(preferencesManager.getToken(), globalValue.getCurrentOrder().getId(),
                globalValue.getCurrentOrder().getPassengerId(), message, context, true,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            Log.e("result", "send " + json);
                            try {
                                JSONObject jsonObject1 = new JSONObject(json);
                                JSONObject data = jsonObject1.getJSONObject("data");

                                ChattingResponse chattingResponse = new ChattingResponse();
                                chattingResponse.setId(data.getString("id"));
                                chattingResponse.setMessage(data.getString("message"));
                                chattingResponse.setCreated_time(data.getString("created_time"));

                                chattingResponse.setSender_id(data.getString("sender_id"));
                                chattingResponse.setReceiver_id(data.getString("receiver_id"));

                                arrayList.add(chattingResponse);
                                chattingAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
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
    public void onPause() {
        super.onPause();
        preferencesManager.setUserOnChatScreen("0");
    }

    private void cancelTrip() {
        ModelManager.cancelTrip(preferencesManager.getToken(), globalValue
                        .getCurrentOrder().getId(), context, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
//                            gotoActivity(OnlineActivity.class);
                            finish();
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

    private void getDistance() {
        if (globalValue
                .getCurrentOrder() != null && globalValue
                .getCurrentOrder().getId() != null) {
            ModelManager.showDistance(preferencesManager.getToken(), globalValue
                            .getCurrentOrder().getId(), context, false,
                    new ModelManagerListener() {

                        @Override
                        public void onSuccess(String json) {
                            if (ParseJsonUtil.isSuccess(json)) {
                                try {
                                    Float temp = Float.parseFloat(ParseJsonUtil
                                            .getDistance(json));
                                    if (temp.toString().length() > 6) {
                                        lblDistance.setText(temp.toString().substring(
                                                0, 6)
                                                + " " + getString(R.string.unit_measure));
                                    } else {
                                        lblDistance.setText(temp.toString()
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

    private void initLocalBroadcastManager() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CANCEL_TRIP);
        intentFilter.addAction(Constant.SEND_MESSAGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver,
                intentFilter);

//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(Constant.ACTION_CANCEL_TRIP));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(Constant.KEY_ACTION);
            if (action.equals(Constant.ACTION_CANCEL_TRIP)) {
                showToastMessage(R.string.message_you_trip_cancel_by_passenger);
//                gotoActivity(OnlineActivity.class);
                finish();
            } else if (action.equals(Constant.SEND_MESSAGE)) {
                String data = intent.getStringExtra(Constant.KEY_DATA);
                try {
                    JSONObject jsonObject = new JSONObject(data);

                    ChattingResponse chattingResponse = new ChattingResponse();
                    chattingResponse.setId(jsonObject.getString("id"));
                    chattingResponse.setMessage(jsonObject.getString("message"));
                    chattingResponse.setCreated_time(jsonObject.getString("created_time"));

                    chattingResponse.setSender_id(jsonObject.getString("sender_id"));
                    chattingResponse.setReceiver_id(jsonObject.getString("receiver_id"));

                    if (arrayList.size() > 0) {
                        arrayList.add(arrayList.size(), chattingResponse);
                    } else {
                        arrayList.add(0, chattingResponse);
                    }

                    chattingAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        if (preferencesManager.getDriverCurrentScreen().equals("")) {
            showToastMessage(R.string.message_you_trip_cancel_by_passenger);
            gotoActivity(OnlineActivity.class);
            finish();
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);

        if (chat_dialog != null) {
            if (chat_dialog.isShowing()) {
                Log.e("onchat", "open");
                preferencesManager.setUserOnChatScreen("1");
                getChatApi();
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        if (gps != null) {
            gps.stopUsingGPS();
        }
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        updateCoordinate(location.getLatitude() + "", location.getLongitude()
                + "");
        getDistance();
        startLocation = new LatLng(location.getLatitude(), location.getLongitude());
//        setStartMarker();
//        setDriverMarker();
    }

    public void updateData() {
        handler = new Handler();

        runnable = new Runnable() {

            @Override
            public void run() {
                Log.e("update location", "update location:");
                if (globalValue.getCurrentOrder() != null) {
                    updatePositionForPassenger(globalValue.getCurrentUser().getId());
                }
                handler.postDelayed(runnable, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    public void updatePositionForPassenger(String driverId) {
        ModelManager.getLocationDriver(this, driverId, false, new ModelManagerListener() {
            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(String json) {
                try {
                    if (ParseJsonUtil.isSuccess(json)) {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String latitude = jsonObject1.getString("driverLat");
                        String longitude = jsonObject1.getString("driverLon");
                        startLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    }

//                    setDriverMarkerNoUpdateDirection();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void updatePositionForPassengerNoUpdate(String driverId) {
        ModelManager.getLocationDriver(this, driverId, false, new ModelManagerListener() {
            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    String latitude = jsonObject1.getString("driverLat");
                    String longitude = jsonObject1.getString("driverLon");
                    startLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))
                    ;
//                    setDriverMarkerNoUpdateDirection();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
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
}
