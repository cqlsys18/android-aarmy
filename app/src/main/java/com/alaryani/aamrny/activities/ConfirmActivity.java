package com.alaryani.aamrny.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alaryani.aamrny.adapters.ChattingAdapter;
import com.alaryani.aamrny.object.ChattingResponse;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.config.Constant;
import com.alaryani.aamrny.config.LinkApi;
import com.alaryani.aamrny.googledirections.Route;
import com.alaryani.aamrny.googledirections.Routing;
import com.alaryani.aamrny.googledirections.RoutingListener;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.object.UserUpdate;
import com.alaryani.aamrny.service.GPSTracker;
import com.alaryani.aamrny.widget.TextViewRaleway;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ConfirmActivity extends BaseActivity implements RoutingListener, GoogleMap.OnMyLocationChangeListener {
    private TextViewRaleway lblCarPlate;
    private TextViewRaleway lblName, tvSeat;
    private TextView lblPhone;
    private RatingBar ratingBar;
    private ImageView imgPassenger, imgDriver;
    private ImageView imgCar;
    private TextView btnCancel;
    private TextViewRaleway lblDistance;
    private TextViewRaleway lblStartLocation;
    private TextViewRaleway lblEndlocation, lblTimes, lblDistanceTime;
    TextView txtStar;
    AQuery aq;

    // For timer
    private int mInterval = 1; // 5 seconds by default, can be changed later
    private Timer mTimer;
    private GoogleMap mMap;
    private GPSTracker gps;
    private double lat;
    private double lnt;
    private ScrollView scrollView;
    LatLng startLocation, endLocation;
    Bitmap iconMarker;
    Runnable runnable;

    private LatLngBounds latlngBounds;
    private Polyline newPolyline;

    private CardView imgBack;
    // For timer
    private Routing routing;
    private Marker mMarkerStartLocation, mMarkerEndLocation;
    List<LatLng> polyz;
    private TextViewRaleway lblTitlePassenger, lblTitleDriver, txtStatus;
    private int width, height;
    private boolean checkZoom = true;
    private ImageView imgCall, imgSms;
    private boolean checkFake = true;
    private boolean checkDataDistance = true;
    private Firebase ref;
    private boolean checkFirst = true, checkPath = true;
    private String dataPath = "";
    private int dem = 0;
    Handler handler = new Handler();
    Runnable updateMarker;
    TextView imgChat;
    Dialog chat_dialog;
    ArrayList<ChattingResponse> arrayList;
    ChattingAdapter chattingAdapter;

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

//    Handler handler;

    @Override
    public void onRoutingFailure() {

    }

    @Override
    public void onRoutingStart() {
        lblDistanceTime.setText(dataPath);
    }

    @Override
    public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route) {
        if (startLocation != null || endLocation != null) {
            if (checkDataDistance) {
                mMap.clear();
                setStartMarkerAgain();
                setLocationLatLong(startLocation);
                PolylineOptions polyOptions = new PolylineOptions();
                polyOptions.color(R.color.second_primary);
                polyOptions.width(10);
                polyOptions.addAll(mPolyOptions.getPoints());
                mMap.addPolyline(polyOptions);
                String msg = getString(R.string.msgDrivingComingDistance);
                msg = msg.replace("[a]", route.getDistanceText());
                msg = msg.replace("[b]", route.getDurationText());
                lblDistanceTime.setText(msg);
                checkPath = false;
                dataPath = msg;
            } else {
                String msg = getString(R.string.msgDrivingComingDistance);
                msg = msg.replace("[a]", route.getDistanceText());
                msg = msg.replace("[b]", route.getDurationText());
                lblDistanceTime.setText(msg);
                dataPath = msg;
            }


        }
    }

    @Override
    public void onMyLocationChange(Location location) {

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

    protected void showDirection() {
        if (startLocation != null && endLocation != null) {
            checkDataDistance = true;
//            findDirections(startLocation.latitude, startLocation.longitude, endLocation.latitude, endLocation.longitude, GMapV2Direction.MODE_DRIVING);
            routing = new Routing(Routing.TravelMode.DRIVING);
            routing.registerListener(this);
            routing.execute(startLocation, endLocation);

        }
    }

    protected void showDistanceAndTime() {
        if (startLocation != null && endLocation != null) {
            checkDataDistance = false;
//            findDirections(startLocation.latitude, startLocation.longitude, endLocation.latitude, endLocation.longitude, GMapV2Direction.MODE_DRIVING);
            routing = new Routing(Routing.TravelMode.DRIVING);
            routing.registerListener(this);
            routing.execute(startLocation, endLocation);

        }

    }

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

    public void setStartMarkerAgain() {
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

    /* OVERRIDE */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isCurrentAppLangRtl())
            setContentView(R.layout.activity_confirm_rtl);
        else
            setContentView(R.layout.activity_confirm);
        Firebase.setAndroidContext(this);
        ref = new Firebase(LinkApi.FIREBASE_URL);
        aq = new AQuery(self);
//        getSreenDimanstions();
        initUI();
        initView();
        Maps();
        initData();
        initControl();
        initLocalBroadcastManager();
        autoRefreshEvents();
        preferencesManager.setPassengerWaitConfirm(false);
        preferencesManager.putStringValue("countDriver", "0");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }


    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        if (gps != null) {
            gps.stopUsingGPS();
        }
        handler.removeCallbacks(updateMarker);
        super.onDestroy();
    }

    private String getDistanceOnRoad(double latitude, double longitude,
                                     double prelatitute, double prelongitude) {
        String result_in_kms = "";
        String url = "http://maps.google.com/maps/api/directions/xml?origin="
                + latitude + "," + longitude + "&destination=" + prelatitute
                + "," + prelongitude + "&sensor=false&units=metric";
        String tag[] = {"text"};
        HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);
            InputStream is = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(is);
            if (doc != null) {
                NodeList nl;
                ArrayList args = new ArrayList();
                for (String s : tag) {
                    nl = doc.getElementsByTagName(s);
                    if (nl.getLength() > 0) {
                        Node node = nl.item(nl.getLength() - 1);
                        args.add(node.getTextContent());
                    } else {
                        args.add(" - ");
                    }
                }
                result_in_kms = String.format("%s", args.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result_in_kms.substring(0, result_in_kms.length() - 3);
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
    public void onResume() {
        if (preferencesManager.getPassengerCurrentScreen().equals(
                "StartTripForPassengerActivity")) {
            Log.e("arrived", "onResume");
            gotoActivity(StartTripForPassengerActivity.class);
            finish();
        } else if (preferencesManager.getPassengerCurrentScreen().equals("")) {
            gotoActivity(MainActivity.class);
            finish();
        } else {
            Log.e("arrived", "onResume 1");
            preferencesManager.setPassengerWaitConfirm(false);
            preferencesManager.setPassengerCurrentScreen(ConfirmActivity.class
                    .getSimpleName());
            preferencesManager.setPassengerIsInTrip(true);
            initData();
        }

        if (chat_dialog != null) {
            if (chat_dialog.isShowing()) {
                Log.e("onchat", "open");
                preferencesManager.setUserOnChatScreen("1");
                getChatApi();
            }
        }
        super.onResume();
    }

    ;

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
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));b
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
//        if (gps.canGetLocation()) {
//            startLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
//        } else {
//            gps.showSettingsAlert();
//        }

    }

    @Override
    public void onBackPressed() {
        cancelTrip();
    }

    /* FUNCTION */
    private void initView() {
        arrayList = new ArrayList<>();
        chattingAdapter = new ChattingAdapter(ConfirmActivity.this, arrayList);
        //setHeaderTitle(R.string.lbl_order_confirm);
        tvSeat = (TextViewRaleway) findViewById(R.id.tvSeat);
        lblName = (TextViewRaleway) findViewById(R.id.lblName);
        lblCarPlate = (TextViewRaleway) findViewById(R.id.lblCarPlate);
        lblPhone = (TextView) findViewById(R.id.lblPhone);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        imgPassenger = (ImageView) findViewById(R.id.imgPassenger);
        imgCar = (ImageView) findViewById(R.id.imgCar);
        btnCancel = (TextView) findViewById(R.id.btnCancel);
        lblDistance = (TextViewRaleway) findViewById(R.id.lblDistance);
        lblStartLocation = (TextViewRaleway) findViewById(R.id.lblStartLocation);
        lblEndlocation = (TextViewRaleway) findViewById(R.id.lblEndlocation);
        imgDriver = (ImageView) findViewById(R.id.imgDriver);
        lblTimes = (TextViewRaleway) findViewById(R.id.lblTimes);
        lblTitleDriver = (TextViewRaleway) findViewById(R.id.lblTitleDriver);
        txtStatus = (TextViewRaleway) findViewById(R.id.txtStatus);
        lblTitlePassenger = (TextViewRaleway) findViewById(R.id.lblTitlePassenger);
        txtStar = (TextView) findViewById(R.id.txtStar);
        imgBack = (CardView) findViewById(R.id.cv_back);
        lblDistanceTime = (TextViewRaleway) findViewById(R.id.lblDistanceTime);
        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgCall = (ImageView) findViewById(R.id.imgCall);
        imgSms = (ImageView) findViewById(R.id.imgSms);
        imgChat = (TextView) findViewById(R.id.imgChat);

        imgChat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatDialog();
            }
        });
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

    @Override
    public void onPause() {
        super.onPause();
        preferencesManager.setUserOnChatScreen("0");
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
        Log.e("idss", "user " + globalValue.getCurrentOrder().getDriverId() + " " + preferencesManager.getUserID());
        ModelManager.sendMessage(preferencesManager.getToken(), globalValue.getCurrentOrder().getId(),
                globalValue.getCurrentOrder().getDriverId(), message, context, true,
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


    private void initData() {
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

                                if (globalValue.getCurrentOrder().getPickUpAtA() != null && globalValue.getCurrentOrder().getPickUpAtA().equals("0")) {
                                    endLocation = new LatLng(Double.parseDouble(ParseJsonUtil
                                            .parseCurrentOrder(json).getEndLat()), Double.parseDouble(ParseJsonUtil
                                            .parseCurrentOrder(json).getEndLong()));
                                } else {
                                    endLocation = new LatLng(Double.parseDouble(ParseJsonUtil
                                            .parseCurrentOrder(json).getStartLat()), Double.parseDouble(ParseJsonUtil
                                            .parseCurrentOrder(json).getStartLong()));
                                }

                                lblName.setText(globalValue.getCurrentOrder()
                                        .getDriverName());
                                lblCarPlate.setText(globalValue
                                        .getCurrentOrder().getCarPlate());
                                lblPhone.setText(globalValue.getCurrentOrder()
                                        .getDriver_phone(true));
                                lblStartLocation.setText(globalValue
                                        .getCurrentOrder().getStartLocation());
                                lblEndlocation.setText(globalValue
                                        .getCurrentOrder().getEndLocation());
                                lblTitlePassenger.setText(globalValue
                                        .getCurrentOrder().getDriverName());
                                lblTitleDriver.setText(globalValue
                                        .getCurrentOrder().getPassengerName());
                                if (globalValue.getCurrentOrder()
                                        .getDriverRate().isEmpty()) {
                                    txtStar.setText("0");
//                                    ratingBar.setRating(0);
                                } else {
                                    txtStar.setText("" + Float
                                            .parseFloat(globalValue
                                                    .getCurrentOrder()
                                                    .getDriverRate()) / 2);
//                                    ratingBar.setRating(Float
//                                            .parseFloat(globalValue
//                                                    .getCurrentOrder()
//                                                    .getPassenger_rate()) / 2);
                                }
                                tvSeat.setText(convertLinkToString(globalValue.convertToInt(globalValue.getCurrentOrder().getLink()) + ""));
                                aq.id(imgCar).image(
                                        globalValue.getCurrentOrder()
                                                .getCarImage());
                                Log.e("eeeeeeeeee", "image: " + globalValue.getCurrentOrder()
                                        .getActualFare());
                                aq.id(imgPassenger).image(
                                        globalValue.getCurrentOrder()
                                                .getImageDriver());
                                aq.id(imgDriver).image(
                                        globalValue.getCurrentOrder()
                                                .getCarImage());
                                if (globalValue.getCurrentOrder().getStatus().equals(Constant.STATUS_ARRIVED_A)) {
                                    txtStatus.setText(getString(R.string.lbl_driver_arrived));
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

            tvSeat.setText(convertLinkToString(globalValue.convertToInt(globalValue.getCurrentOrder().getLink()) + ""));
            lblName.setText(globalValue.getCurrentOrder().getDriverName());
            lblCarPlate.setText(globalValue.getCurrentOrder().getCarPlate());
            lblPhone.setText(globalValue.getCurrentOrder().getDriver_phone(true));
            lblStartLocation.setText(globalValue
                    .getCurrentOrder().getStartLocation());
            lblEndlocation.setText(globalValue
                    .getCurrentOrder().getEndLocation());
            if (globalValue.getCurrentOrder()
                    .getDriverRate().isEmpty()) {
                txtStar.setText("0");
//                                    ratingBar.setRating(0);
            } else {
                txtStar.setText("" + Float
                        .parseFloat(globalValue
                                .getCurrentOrder()
                                .getDriver_rate()) / 2);
//                                    ratingBar.setRating(Float
//                                            .parseFloat(globalValue
//                                                    .getCurrentOrder()
//                                                    .getPassenger_rate()) / 2);
            }
            aq.id(imgCar).image(globalValue.getCurrentOrder().getCarImage());
            // TODO: 12/12/2015 need to update image for car. currently url image die
            Log.e("eeeeeeeeee", "image: " + globalValue.getCurrentOrder()
                    .getCarImage());
            aq.id(imgPassenger).image(
                    globalValue.getCurrentOrder().getImageDriver());
            if (globalValue.getCurrentOrder().getStatus().equals(Constant.STATUS_ARRIVED_A)) {
                txtStatus.setText(getString(R.string.lbl_driver_arrived));
            }
            getDistance();
            if (globalValue.getCurrentOrder().getPickUpAtA() != null && globalValue.getCurrentOrder().getPickUpAtA().equals("0")) {
                endLocation = new LatLng(Double.parseDouble(globalValue.getCurrentOrder().getEndLat()), Double.parseDouble(globalValue.getCurrentOrder().getEndLong()));
            } else {
                endLocation = new LatLng(Double.parseDouble(globalValue.getCurrentOrder().getStartLat()), Double.parseDouble(globalValue.getCurrentOrder().getStartLong()));
            }
            updatePositionForPassenger(globalValue.getCurrentOrder().getDriverId());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endLocation, 14.0f));
        }
    }

    public String convertLinkToString(String link) {
        switch (link) {
            case "I":
                return getString(R.string.sedan4);

            case "II":
                return getString(R.string.suv6);

            case "III":
                return getString(R.string.lux);
        }
        return link;
    }

    private void initControl() {
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTrip();
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
                smsIntent.putExtra("address", globalValue.getCurrentOrder().getDriver_phone(false));
                smsIntent.putExtra("sms_body", "message");
                startActivity(smsIntent);
            }
        });
    }

    private void cancelTrip() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.msg_do_you_cancel)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                cancelTripAPI();
                            }
                        }).setNegativeButton(android.R.string.cancel, null)
                .create().show();
    }

    private void cancelTripAPI() {
        ModelManager.cancelTrip(preferencesManager.getToken(), globalValue
                        .getCurrentOrder().getId(), context, true,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            preferencesManager
                                    .setPassengerCurrentScreen("MainActivity");
                            if (preferencesManager.IsStartWithOutMain()) {
                                gotoActivity(MainActivity.class);
                                finish();
                                overridePendingTransition(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_right);
                            } else {
                                finish();
                                overridePendingTransition(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_right);
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

    private void initLocalBroadcastManager() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_DRIVER_START_TRIP);
        intentFilter.addAction(Constant.ACTION_CANCEL_TRIP);
        intentFilter.addAction(Constant.ACTION_DRIVER_ARRIVED);
        intentFilter.addAction(Constant.SEND_MESSAGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver,
                intentFilter);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(Constant.KEY_ACTION);
            if (action.equals(Constant.ACTION_CANCEL_TRIP)) {
                showToastMessage(R.string.message_you_trip_cancel_by_driver);
                preferencesManager.setPassengerCurrentScreen("");
                preferencesManager.setPassengerIsInTrip(false);
                preferencesManager.setPassengerHavePush(false);
                if (preferencesManager.IsStartWithOutMain()) {
                    gotoActivity(MainActivity.class);
                    finish();
                } else {
                    finish();
                }
            } else {
                if (action.equals(Constant.ACTION_DRIVER_START_TRIP)) {
                    gotoActivity(StartTripForPassengerActivity.class);
                    Log.e("arrived", "mMessageReceiver");
                    finish();
                } else if (action.equals(Constant.ACTION_DRIVER_ARRIVED)) {
                    imgChat.setVisibility(View.GONE);
                    if (chat_dialog != null)
                        chat_dialog.dismiss();
                    txtStatus.setText(getString(R.string.lbl_driver_arrived));
                    preferencesManager.getPassengerCurrentScreen().equals(
                            "ConfirmActivity");
//                    checkFake = false;
//                    setLocationLatLong(endLocation);
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
        }
    };


    private int convertToInt(String s) {
        switch (s) {
            case "I":
                return 1;

            case "II":
                return 2;

            case "III":
                return 3;
            case "IV":
                return 4;
            case "V":
                return 5;


        }
        return 0;
    }


    public void updatePositionForPassenger(final String driverId) {
        updateMarker = new Runnable() {
            @Override
            public void run() {
                ref.child("user").child(driverId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.e("chay vao day", "chay vao day");
                        try {
                            JSONObject object = new JSONObject(snapshot.getValue().toString());
                            String latitude = object.getString("lat");
                            String longitude = object.getString("lng");
                            startLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                            setLocationLatLong(startLocation);
                            if (checkFirst) {
                                setStartMarker();
                                checkFirst = false;
                            } else {
                                if (!checkPath) {
                                    showDistanceAndTime();
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

    public void updatePositionForPassengeNoUpdate(String driverId) {
        ref.child("user").child(driverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                //Getting the data from snapshot
                UserUpdate person = snapshot.getValue(UserUpdate.class);
                Log.e("data", "data lat:" + person.getLat() + "-" + person.getLng());
                String latitude = person.getLat();
                String longitude = person.getLng();
                startLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                showDistanceAndTime();
                setLocationLatLong(startLocation);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
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
////                    if (!checkFake) {
////                        setLocationLatLong(endLocation);
////                    } else {
//                    setLocationLatLong(startLocation);
//
////                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
    }
}
