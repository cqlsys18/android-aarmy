package com.alaryani.aamrny.fragment;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alaryani.aamrny.utility.AppUtil;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.alaryani.aamrny.BaseFragment;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.RequestService;
import com.alaryani.aamrny.activities.PaymentPointActivity;
import com.alaryani.aamrny.activities.WaitDriverConfirmActivity;
import com.alaryani.aamrny.adapters.CarTypeAdapter;
import com.alaryani.aamrny.autocompleteaddress.PlacesAutoCompleteAdapter;
import com.alaryani.aamrny.config.Constant;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.LinkApi;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.googledirections.GMapV2Direction;
import com.alaryani.aamrny.googledirections.Route;
import com.alaryani.aamrny.googledirections.Routing;
import com.alaryani.aamrny.googledirections.RoutingListener;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.object.CarType;
import com.alaryani.aamrny.object.DriverOnlineObj;
import com.alaryani.aamrny.object.DriverUpdate;
import com.alaryani.aamrny.object.SettingObj;
import com.alaryani.aamrny.object.UserOnlineObj;
import com.alaryani.aamrny.service.GPSTracker;
import com.alaryani.aamrny.util.ImageUtil;
import com.alaryani.aamrny.utility.IMaps;
import com.alaryani.aamrny.utility.MapsUtil;
import com.alaryani.aamrny.widget.LinearLayoutPagerManager;
import com.alaryani.aamrny.widget.PicassoMarker;
import com.alaryani.aamrny.widget.TextViewPixeden;
import com.alaryani.aamrny.widget.TextViewRaleway;
import com.alaryani.aamrny.widget.animateMarker.LatLngInterpolator;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import me.drakeet.materialdialog.MaterialDialog;

public class PassengerPage1Fragment extends BaseFragment implements
        OnMyLocationChangeListener, RoutingListener {

    Handler handler;
    protected Double lat_start, lat_end, lng_start, lng_end;
    boolean processClick = true;

    // ===================== VARIABLE FOR LOG =====================
    private final String TAG = BaseFragment.class.getSimpleName();

    // ===================== VARIABLE FOR UI =====================
    private TextViewPixeden btnIcGPS, btnRefresh, btnMenu;
    private TextViewRaleway lblTitle1;
    private GoogleMap mMap;
    private TextViewRaleway lblAvailableVehicle, lbl_From, lbl_To;
    private ImageView btnBook;
    private Button btnBack;
    private AutoCompleteTextView txtFrom;
    private ImageView btnStart;
    private AutoCompleteTextView txtTo;
    private ImageView btnEnd;
    private TextViewRaleway btnLink1;
    private TextViewRaleway btnLink2;
    private TextViewRaleway btnLink3;


    // ======== VARIABLE FOR LOGIC START LOCATION AND END LOCATION ========
    private HandlerThread mHandlerThread;
    private Handler mThreadHandler;
    private PlacesAutoCompleteAdapter mAdapter;
    private boolean selectFromMap = false;
    LatLng startLocation, endLocation;
    Bitmap iconMarker;
    private boolean txtFromIsSelected = false, txtToIsSelected = false;
    private Marker mMarkerStartLocation, mMarkerEndLocation;

    // ======== VARIABLE FOR DRAW DIRECTION ========
    private GMapV2Direction md;
    private Routing routing;
    public PreferencesManager preferencesManager;
    protected GlobalValue globalValue;
    private IntentFilter mIntentFilter;
    Marker markerName;
    LatLngBounds.Builder builder;
    Circle circle;
    private HashMap<Integer, Marker> visibleMarkers = new HashMap<Integer, Marker>();
    ArrayList<DriverUpdate> listMarkers = new ArrayList<>();
    private boolean checkData = true;
    String estimateDistance = "";
    double price = 0;
    private View viewLink1, viewLink2, viewLink3;
    MaterialDialog dialog1, dialogPayment;
    private Firebase ref;
    private ArrayList<DriverOnlineObj> listDrivers;
    private ArrayList<CarType> labelerDates = new ArrayList<>();
    String carType = "";
    private RecyclerView rcvTypeCars;
    private CarTypeAdapter adapter;
    private PicassoMarker marker;
    private ArrayList<CarType> listCarTypes = new ArrayList<>();
    String link = "";
    BitmapDescriptor bitmapDescriptor = null;
    private BitmapDescriptor OldBitmapDescriptor = null;
    private String idMap = "";
    Dialog book_dialog;
    //    String promo_code_value = "";
    // ===================== @OVERRIDE =====================
    View view;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (isCurrentAppLangRtl())
            view = inflater.inflate(R.layout.fragment_passenger_page1_rtl, container, false);
        else
            view = inflater.inflate(R.layout.fragment_passenger_page1, container, false);

        preferencesManager = PreferencesManager.getInstance(getActivity());
        Firebase.setAndroidContext(getActivity());
        ref = new Firebase(LinkApi.FIREBASE_URL);
        if (GlobalValue.getInstance().getListCarTypes() != null) {
            Log.e("aaaaaa", "aaaaaa");
            listCarTypes.addAll(GlobalValue.getInstance().getListCarTypes());
            intiDataContent(view);
        } else {
            ModelManager.getGeneralSettings(preferencesManager.getToken(),
                    self, true, new ModelManagerListener() {
                        @Override
                        public void onSuccess(String json) {
                            Log.e("aaaaaa", "bbbbbbbbb");
                            if (ParseJsonUtil.isSuccess(json)) {
                                SettingObj settingObj = ParseJsonUtil.getGeneralSettings(json);
                                listCarTypes.addAll(ParseJsonUtil.parseListCarTypes(json));
                                GlobalValue.getInstance().setListCarTypes(listCarTypes);
                                preferencesManager.setDataSetting(settingObj);
                                intiDataContent(view);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }

//        mIntentFilter = new IntentFilter();
//        mIntentFilter.addAction(ServiceUpdateLocation.ACTION);
//        getActivity().registerReceiver(mReceiver, mIntentFilter);


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void intiDataContent(View view) {
        handler = new Handler();
        initUI(view);
        initView(view);
        initControl(view);
        initMenuButton(view);
        initCarTypes();
        setupAutoComplete(view);
        setUpMap();
        setUpMapOnClick();
        globalValue = GlobalValue.getInstance();
        mMap.setOnCameraChangeListener(getCameraChangeListener());
        link = listCarTypes.get(adapter.getPositionCheck()).getId();
        initDataMap(link);

        /*Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnMenu.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            btnMenu.setLayoutParams(params); //causes layout update

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) btnRefresh.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            btnRefresh.setLayoutParams(params1); //causes layout update

        } else {
            Log.e("isrtl", "no ");
        }*/
    }
//    private Bitmap getMarkerBitmapFromView(Bitmap bitmap) {
//
//        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
//        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
//        markerImageView.setImageBitmap(bitmap);
//        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
//        customMarkerView.buildDrawingCache();
//        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
//                Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(returnedBitmap);
//        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
//        Drawable drawable = customMarkerView.getBackground();
//        if (drawable != null)
//            drawable.draw(canvas);
//        customMarkerView.draw(canvas);
//        return returnedBitmap;
//    }

    public GoogleMap.OnCameraChangeListener getCameraChangeListener() {
        return new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                link = listCarTypes.get(adapter.getPositionCheck()).getId();
                initDataMap(link);
            }
        };
    }

    public void initDataMap(final String carType) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                VisibleRegion vr = mMap.getProjection().getVisibleRegion();
                final Location center = new Location("center");
                center.setLatitude(vr.latLngBounds.getCenter().latitude);
                center.setLongitude(vr.latLngBounds.getCenter().longitude);
                Location MiddleLeftCornerLocation = new Location("midleft");
                MiddleLeftCornerLocation.setLatitude(center.getLatitude());
                MiddleLeftCornerLocation.setLongitude(vr.latLngBounds.southwest.longitude);
                float dis = center.distanceTo(MiddleLeftCornerLocation);
                ModelManager.getTotalDriversAroundLocation(self, center.getLatitude(), center.getLongitude(), dis / 1000, carType, false, new ModelManagerListener() {
                            @Override
                            public void onError() {
                                lblAvailableVehicle.setText(0 + "");
                            }

                            @Override
                            public void onSuccess(String json) {
                                listDrivers = new ArrayList<>();
                                UserOnlineObj userOnlineObj = ParseJsonUtil.parseDriver(json);
                                listDrivers.addAll(userOnlineObj.getDriverOnlineObj());

                                ValueEventListener valueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                            if (listDrivers.size() > 0) {
                                                for (int i = 0; i < listDrivers.size(); i++) {
                                                    boolean checkMarker = true;
                                                    if (dsp.getKey().equals(listDrivers.get(i).getDriverId())) {
                                                        JSONObject object = null;
                                                        try {
                                                            object = new JSONObject(dsp.getValue().toString());
                                                            String latitude = object.getString("lat");
                                                            String longitude = object.getString("lng");
                                                            String status = null;
                                                            if (!object.isNull("status")) {
                                                                status = object.getString("status");
                                                            }
                                                            if (status != null && status.equals("1")) {
                                                                if (listMarkers.size() > 0) {
                                                                    for (int j = 0; j < listMarkers.size(); j++) {
                                                                        if (listMarkers.get(j).getId().equals(dsp.getKey())) {
                                                                            if (link.equals(preferencesManager.getDataCarType().getId())) {
                                                                                checkMarker = false;
                                                                                Location targetLocation = new Location("");
                                                                                targetLocation.setLatitude(Double.parseDouble(latitude));
                                                                                targetLocation.setLongitude(Double.parseDouble(longitude));
                                                                                animateMarker(targetLocation, listMarkers.get(j).getMarker());
                                                                            } else {
                                                                                listMarkers.get(j).getMarker().remove();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if (checkMarker) {
                                                                    Marker marker = mMap.addMarker(new MarkerOptions().icon(bitmapDescriptor)
                                                                            .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))));
                                                                    DriverUpdate driverUpdate = new DriverUpdate();
                                                                    driverUpdate.setId(dsp.getKey());
                                                                    driverUpdate.setMarker(marker);
                                                                    listMarkers.add(driverUpdate);
                                                                }
                                                            } else {
                                                                for (int j = 0; j < listMarkers.size(); j++) {
                                                                    if (listMarkers.get(j).getId().equals(listDrivers.get(i).getDriverId())) {
                                                                        listMarkers.get(j).getMarker().remove();
//                                                                        listMarkers.get(j).getMarker().setVisible(false);
                                                                    }
                                                                }
                                                                listDrivers.remove(listDrivers.get(i));
                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            } else {
                                                //sile list diriver = 0
                                                for (int i = 0; i < listMarkers.size(); i++) {
//                                                    listMarkers.get(i).getMarker().setVisible(false);
                                                    listMarkers.get(i).getMarker().remove();
                                                    listMarkers.remove(i);
                                                }
                                            }

                                        }

                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                };
                                if (valueEventListener != null)

                                {
                                    ref.removeEventListener(valueEventListener);
                                }

                                ref.child("user").

                                        addValueEventListener(valueEventListener);
                            }
                        }

                );
            }
        };

        handler.post(runnable);

    }

    public void animateMarker(final Location destination, final Marker marker) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());

            final float startRotation = marker.getRotation();

            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(1000); // duration 1 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        marker.setRotation(computeRotation(v, startRotation, destination.getBearing()));
                    } catch (Exception ex) {
                        // I don't care atm..
                    }
                }
            });

            valueAnimator.start();
        }
    }

    private static float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;

        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        float result = fraction * rotation + start;
        return (result + 360) % 360;
    }

//    public void animateMarker(final Marker m, final LatLng toPosition, final boolean hideMarke) {
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        Projection proj = mMap.getProjection();
//        Point startPoint = proj.toScreenLocation(m.getPosition());
//        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
//        final long duration = 2000;
//
//        final Interpolator interpolator = new LinearInterpolator();
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed
//                        / duration);
//                double lng = t * toPosition.longitude + (1 - t)
//                        * startLatLng.longitude;
//                double lat = t * toPosition.latitude + (1 - t)
//                        * startLatLng.latitude;
////                if (OldBitmapDescriptor == null || bitmapDescriptor != OldBitmapDescriptor) {
////                    if (bitmapDescriptor != null) {
////                        try {
////                            m.setIcon(bitmapDescriptor);
////                        } catch (Exception e) {
////                            Log.e("TAG", e.getMessage());
////                        }
////                        OldBitmapDescriptor = bitmapDescriptor;
////                    }
////                }
//                m.setPosition(new LatLng(lat, lng));
//
//                if (t < 1.0) {
//                    // Post again 16ms later.
//                    handler.postDelayed(this, 16);
//                } else {
//                    if (hideMarke) {
//                        m.setVisible(false);
//                    } else {
//                        m.setVisible(true);
//                    }
//                }
//            }
//        });
//    }

    public void refreshDataResume(String carType) {
        VisibleRegion vr = mMap.getProjection().getVisibleRegion();
        final Location center = new Location("center");
        center.setLatitude(vr.latLngBounds.getCenter().latitude);
        center.setLongitude(vr.latLngBounds.getCenter().longitude);
        Location MiddleLeftCornerLocation = new Location("midleft");
        MiddleLeftCornerLocation.setLatitude(center.getLatitude());
        MiddleLeftCornerLocation.setLongitude(vr.latLngBounds.southwest.longitude);
        float dis = center.distanceTo(MiddleLeftCornerLocation);
        ModelManager.getTotalDriversAroundLocation(self, center.getLatitude(), center.getLongitude(), dis / 1000, carType, false, new ModelManagerListener() {
                    @Override
                    public void onError() {
                        lblAvailableVehicle.setText(0 + "");
                    }

                    @Override
                    public void onSuccess(String json) {
                        for (int i = 0; i < listMarkers.size(); i++) {
                            listMarkers.get(i).getMarker().remove();
                            listMarkers.remove(i);
                        }
                        listDrivers = new ArrayList<>();
                        UserOnlineObj userOnlineObj = ParseJsonUtil.parseDriver(json);
                        listDrivers.addAll(userOnlineObj.getDriverOnlineObj());
                        ValueEventListener valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                    if (listDrivers.size() > 0) {
                                        for (int i = 0; i < listDrivers.size(); i++) {
                                            if (dsp.getKey().equals(listDrivers.get(i).getDriverId())) {
                                                JSONObject object = null;
                                                try {
                                                    object = new JSONObject(dsp.getValue().toString());
                                                    String latitude = object.getString("lat");
                                                    String longitude = object.getString("lng");
                                                    String status = null;
                                                    if (!object.isNull("status")) {
                                                        status = object.getString("status");
                                                    }
                                                    if (status != null && status.equals("1")) {
                                                        Marker marker = mMap.addMarker(new MarkerOptions().icon(bitmapDescriptor)
                                                                .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))));
                                                        DriverUpdate driverUpdate = new DriverUpdate();
                                                        driverUpdate.setId(dsp.getKey());
                                                        driverUpdate.setMarker(marker);
                                                        listMarkers.add(driverUpdate);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        };
                        if (valueEventListener != null)

                        {
                            ref.removeEventListener(valueEventListener);
                        }

                        ref.child("user").

                                addValueEventListener(valueEventListener);
                    }
                }

        );

    }

    @Override
    public void onResume() {
        super.onResume();
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bitmap = ImageUtil.createBitmapFromUrl(preferencesManager.getDataCarType().getImageMarker());
//                int size = ImageUtil.getSizeBaseOnDensity(getActivity(), 35);
//                bitmap = ImageUtil.getResizedBitmap(bitmap, size, size);
//                bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
//                link = preferencesManager.getDataCarType().getId();
//                refreshDataResume(link);
//            }
//        });


    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            double latitude = Double.parseDouble(intent.getStringExtra("latitude"));
            double longitude = Double.parseDouble(intent.getStringExtra("longitude"));
            if (markerName != null) {
                markerName.remove();
            }
            markerName = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Driver"))
            ;
        }
    };

    public void changeLanguage() {

//        btnBook.setText(R.string.btn_next);
        btnBack.setText(R.string.btn_back);
        lbl_From.setText(R.string.lbl_from);
        lbl_To.setText(R.string.lbl_to);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {

        }
    }


    @Override
    public void onMyLocationChange(Location lastKnownLocation) {
        mMap.clear();
        CameraUpdate myLoc = CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(new LatLng(lastKnownLocation.getLatitude(),
                                lastKnownLocation.getLongitude())).zoom(12)
                        .build());
        mMap.moveCamera(myLoc);
        mMap.setOnMyLocationChangeListener(null);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        getActivity().unregisterReceiver(mReceiver);
    }
// ===================== @OVERRIDE FOR DRAW ROUTING=====================

    @Override
    public void onRoutingFailure() {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route) {
        if (startLocation != null || endLocation != null) {
            checkData = true;
            mMap.clear();
            setMarker(listMarkers);

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(R.color.second_primary);
            polyOptions.width(10);
            polyOptions.addAll(mPolyOptions.getPoints());
            mMap.addPolyline(polyOptions);
            setStartMarkerAgain();
            setEndMarkerAgain();
            if (mPolyOptions.getPoints().size() > 0) {
                checkData = false;
            }

        }
    }

    protected void showDirection() {
        if (startLocation != null && endLocation != null) {
            routing = new Routing(Routing.TravelMode.DRIVING);
            routing.registerListener(this);
            routing.execute(startLocation, endLocation);
        }
    }

    // ===================== FUNCTION BASE FOR FRAGMENT =====================
    public void initView(View view) {
        btnIcGPS = (TextViewPixeden) view.findViewById(R.id.btnIcGPS);
        btnRefresh = (TextViewPixeden) view.findViewById(R.id.btnRefresh);
        lblTitle1 = (TextViewRaleway) view.findViewById(R.id.lblTitle);
        btnMenu = (TextViewPixeden) view.findViewById(R.id.btnMenu);
        mMap = ((MapFragment) mainActivity.getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        lblAvailableVehicle = (TextViewRaleway) view.findViewById(R.id.lblAvailableVehicle);
        btnBook = (ImageView) view.findViewById(R.id.btnBook);
        btnBack = (Button) view.findViewById(R.id.btnBack);
        txtFrom = (AutoCompleteTextView) view.findViewById(R.id.txtFrom);
        btnStart = (ImageView) view.findViewById(R.id.btnStart);
        txtTo = (AutoCompleteTextView) view.findViewById(R.id.txtTo);
        btnEnd = (ImageView) view.findViewById(R.id.btnEnd);
        btnLink1 = (TextViewRaleway) view.findViewById(R.id.btnLink1);
        btnLink1.setTypeface(null, Typeface.BOLD);
        btnLink2 = (TextViewRaleway) view.findViewById(R.id.btnLink2);
        btnLink3 = (TextViewRaleway) view.findViewById(R.id.btnLink3);
        viewLink1 = (View) view.findViewById(R.id.viewLink1);
        viewLink2 = (View) view.findViewById(R.id.viewLink2);
        viewLink3 = (View) view.findViewById(R.id.viewLink3);
        lbl_From = (TextViewRaleway) view.findViewById(R.id.lbl_From);
        lbl_To = (TextViewRaleway) view.findViewById(R.id.lbl_To);
        rcvTypeCars = (RecyclerView) view.findViewById(R.id.rcvTypeCars);
    }

    public void createServiceRequest() {
//        Intent intent = new Intent();
//        intent.setAction("com.htcp.taxinear.ACTION_REQUEST");
//        getContext().sendBroadcast(intent);
        getMainActivity().startService(new Intent(getMainActivity(), RequestService.class));
    }

    public void initCarTypes() {
        adapter = new CarTypeAdapter(GlobalValue.getInstance().getListCarTypes());
        LinearLayoutPagerManager horizontalLayoutManagaer
                = new LinearLayoutPagerManager(getActivity(), LinearLayoutManager.HORIZONTAL, false, 3);
        rcvTypeCars.setLayoutManager(horizontalLayoutManagaer);
        rcvTypeCars.setAdapter(adapter);
        Picasso.with(getActivity()).load(listCarTypes.get(adapter.getPositionCheck()).getImageType()).into(btnBook);
        preferencesManager.setCarTypeData(listCarTypes.get(adapter.getPositionCheck()));
        Bitmap bitmap = ImageUtil.createBitmapFromUrl(listCarTypes.get(adapter.getPositionCheck()).getImageMarker());
        // resize
        int size = ImageUtil.getSizeBaseOnDensity(self, 35);
        bitmap = ImageUtil.getResizedBitmap(bitmap, size, size);
        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        adapter.setOnItemClickListener(new CarTypeAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Bitmap bitmap = ImageUtil.createBitmapFromUrl(listCarTypes.get(position).getImageMarker());
                // resize
                int size = ImageUtil.getSizeBaseOnDensity(self, 35);
                bitmap = ImageUtil.getResizedBitmap(bitmap, size, size);
                bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                Picasso.with(getActivity()).load(listCarTypes.get(position).getImageType()).into(btnBook);

                link = "";
                link = listCarTypes.get(position).getId();
                if (!link.equals(preferencesManager.getDataCarType().getId())) {
                    for (int i = 0; i < listMarkers.size(); i++) {
                        listMarkers.get(i).getMarker().remove();
                        listMarkers.remove(i);
                    }
                }

                initDataMap(link);
                preferencesManager.setCarTypeData(listCarTypes.get(position));
            }
        });
    }

    public void initControl(View view) {
        btnBook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (startLocation == null || endLocation == null) {
                    showToast(R.string.message_please_select_start_and_end);
                } else {
//                    if (globalValue.getUser().getPhone().length() > 0) {
//                        if (!checkData) {
                    link = "";
                    int position = adapter.getPositionCheck();
                    for (int i = 0; i < position; i++) {
                        link = link + "I";
                    }
                    bookNowDialog(link, false, "");
                }
            }
        });
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                btnBack.setVisibility(View.GONE);
            }
        });

//        btnLink1.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewLink1.setVisibility(View.VISIBLE);
//                viewLink2.setVisibility(View.GONE);
//                viewLink3.setVisibility(View.GONE);
//                btnBook.setBackgroundResource(R.drawable.ic_cart3);
//                selectPeople(1);
//                initDataMap(link);
//            }
//        });
//
//        btnLink2.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewLink1.setVisibility(View.GONE);
//                viewLink2.setVisibility(View.VISIBLE);
//                viewLink3.setVisibility(View.GONE);
//                btnBook.setBackgroundResource(R.drawable.ic_cart2);
//                selectPeople(2);
//                initDataMap(link);
//            }
//        });
//
//        btnLink3.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewLink1.setVisibility(View.GONE);
//                viewLink2.setVisibility(View.GONE);
//                viewLink3.setVisibility(View.VISIBLE);
//                btnBook.setBackgroundResource(R.drawable.ic_cart1);
//                selectPeople(3);
//                initDataMap(link);
//            }
//        });

//        btnIcGPS.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToMyLocation();
//            }
//        });

        btnRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                txtFrom.getText().clear();
//                txtTo.getText().clear();
//                if (mMarkerEndLocation != null) {
//                    mMarkerStartLocation.remove();
//                }
//                if (mMarkerEndLocation != null) {
//                    mMarkerEndLocation.remove();
//                }
//                startLocation = null;
//
//                if (circle != null) {
//                    circle.remove();
//                }

                refreshDriver();
            }
        });
        btnStart.setOnClickListener(new

                                            OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    final GPSTracker tracker = new GPSTracker(mainActivity);
                                                    if (tracker.canGetLocation() == false) {
                                                        tracker.showSettingsAlert();
                                                        showToast(R.string.message_wait_for_location);
                                                    } else {
                                                        startLocation = new LatLng(tracker.getLatitude(), tracker.getLongitude());
                                                        mMap.animateCamera(CameraUpdateFactory
                                                                .newLatLngZoom(startLocation, 12));
                                                        setStartMarker();
                                                        new MapsUtil.GetAddressByLatLng(new IMaps() {
                                                            @Override
                                                            public void processFinished(Object obj) {
                                                                String address = (String) obj;
                                                                if (!address.isEmpty()) {
                                                                    // Set marker's title
                                                                    selectFromMap = true;
                                                                    txtFrom.setText(address);
                                                                }
                                                            }
                                                        }).execute(startLocation);
                                                    }
                                                }
                                            }

        );

        btnEnd.setOnClickListener(new

                                          OnClickListener() {

                                              @Override
                                              public void onClick(View v) {

                                                  final GPSTracker tracker = new GPSTracker(mainActivity);
                                                  if (tracker.canGetLocation() == false) {
                                                      tracker.showSettingsAlert();
                                                      showToast(R.string.message_wait_for_location);
                                                  } else {
                                                      endLocation = new LatLng(tracker.getLatitude(), tracker.getLongitude());
                                                      mMap.animateCamera(CameraUpdateFactory
                                                              .newLatLngZoom(endLocation, 12));
                                                      setEndMarker();
                                                      new MapsUtil.GetAddressByLatLng(new IMaps() {
                                                          @Override
                                                          public void processFinished(Object obj) {
                                                              String address = (String) obj;
                                                              if (!address.isEmpty()) {
                                                                  // Set marker's title
                                                                  selectFromMap = true;
                                                                  txtTo.setText(address);
                                                              }
                                                          }
                                                      }).execute(endLocation);
                                                  }
                                              }
                                          }

        );
        lbl_From.setOnClickListener(new

                                            OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    txtFrom.requestFocus();
                                                    txtFromIsSelected = true;
                                                    txtToIsSelected = false;
                                                }
                                            }

        );
        lbl_To.setOnClickListener(new

                                          OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  txtTo.requestFocus();
                                                  txtFromIsSelected = false;
                                                  txtToIsSelected = true;
                                              }
                                          }

        );

    }

    public void refreshDriver() {
        final GPSTracker tracker = new GPSTracker(mainActivity);
        if (tracker.canGetLocation() == false) {
            tracker.showSettingsAlert();
            showToast(getResources().getString(R.string.wait_for_location));
        } else {
            LatLng currentLatLng = new LatLng(tracker.getLatitude(), tracker.getLongitude());
            CameraUpdate myLoc = CameraUpdateFactory
                    .newCameraPosition(new CameraPosition.Builder()
                            .target(currentLatLng).zoom(13).build());
            mMap.moveCamera(myLoc);
            link = listCarTypes.get(adapter.getPositionCheck()).getId();
            initDataMap(link);
        }

    }

    private void promocodeDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.promocode_layout);
        dialog.setCancelable(false);

        final EditText edt_promo = (EditText) dialog.findViewById(R.id.edt_promo);
        final TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        final TextView apply = (TextView) dialog.findViewById(R.id.apply);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_promo.getText().toString().equals("")) {
                    showToast(getResources().getString(R.string.enter_promo));
                } else {
                    dialog.dismiss();
                    book_dialog.dismiss();
//                    promo_code_value = edt_promo.getText().toString();
                    applyPromocodeAPi(edt_promo.getText().toString());
                }
            }
        });
        dialog.show();
    }

    private void bookNowDialog(final String link, boolean is_promo_applied, final String promocode_value) {
//        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
//        dialog.setContentView(R.layout.promocode_layout);
//        dialog.setCancelable(false);

        book_dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        book_dialog.setContentView(R.layout.book_dialog);
        book_dialog.setCancelable(false);

        final TextView msg_text_view = (TextView) book_dialog.findViewById(R.id.text);
        final TextView promo_code = (TextView) book_dialog.findViewById(R.id.promo_code);
        final TextView cancel = (TextView) book_dialog.findViewById(R.id.cancel);
        final TextView book_now = (TextView) book_dialog.findViewById(R.id.book_now);

        if (!is_promo_applied) {
            price = 0;
        } else {
            promo_code.setText(getResources().getString(R.string.promo_applied));
            promo_code.setEnabled(false);
        }

        String ppk = "0";
        String ppm = "0";
        String sf = "0";
        String taskRate = "0";
        String tastDefaultTime = "0";
        int linkPosition = 1;
        SettingObj settingObj = preferencesManager.getDataSettings();
        float[] results = new float[1];
        String msg = getString(R.string.msgEstimatedFare);
        Location.distanceBetween(startLocation.latitude, startLocation.longitude, endLocation.latitude, endLocation.longitude,
                results);
        estimateDistance = round(results[0] / 1000, 2) + "";

        book_now.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalValue.getUser().getBalance() != null) {
                    if (globalValue.getUser().getBalance() >= price) {
                        createRequest(link, promocode_value);
                    } else {
                        String msg = getString(R.string.msgValidateBalance);
                        msg = msg.replace("[a]", globalValue.getUser().getBalance() + "");
                        msg = msg.replace("[b]", "$" + round(price, 2) + "");
                        showDialogAddPaymentForRequest(msg);
                    }
                }
            }
        });

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                book_dialog.dismiss();
            }
        });

        promo_code.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                promocodeDialog();
            }
        });


        ppk = listCarTypes.get(adapter.getPositionCheck()).getFee_per_kilometer();
        ppm = listCarTypes.get(adapter.getPositionCheck()).getFee_per_minute();
        sf = listCarTypes.get(adapter.getPositionCheck()).getStart_fare();
        taskRate = listCarTypes.get(adapter.getPositionCheck()).getTaskRate();
        tastDefaultTime = listCarTypes.get(adapter.getPositionCheck()).getTaskDefaultTime();
        linkPosition = 1;

//        if (settingObj != null) {
        if (!is_promo_applied)
            price = Double.parseDouble(sf) + (round(results[0] / 1000, 2) / Double.parseDouble(settingObj.getEstimate_fare_speed())) * 60 * Double.parseDouble(ppm) + (round(results[0] / 1000, 2)) * Double.parseDouble(ppk) + Double.parseDouble(taskRate) * Double.parseDouble(tastDefaultTime);
//        }
        preferencesManager.putStringValue("distanceLocation", round(price, 2) + "");
        msg = msg.replace("%.2f", round(price, 2) + "");
        msg_text_view.setText(msg);
        Log.e("data", "updated_price" + price);
        book_dialog.show();
    }

    /*public void showDialogCreateRequest(final String link) {
        price = 0;
        String ppk = "0";
        String ppm = "0";
        String sf = "0";
        String taskRate = "0";
        String tastDefaultTime = "0";
        int linkPosition = 1;
        SettingObj settingObj = preferencesManager.getDataSettings();
        float[] results = new float[1];
        String msg = getString(R.string.msgEstimatedFare);
        Location.distanceBetween(startLocation.latitude, startLocation.longitude, endLocation.latitude, endLocation.longitude,
                results);
        estimateDistance = round(results[0] / 1000, 2) + "";

        dialog = new MaterialDialog(getActivity());
        dialog.setPositiveButton("Book Now", new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalValue.getUser().getBalance() != null) {
                    if (globalValue.getUser().getBalance() >= price) {
                        createRequest(link);
                    } else {
                        String msg = getString(R.string.msgValidateBalance);
                        msg = msg.replace("[a]", globalValue.getUser().getBalance() + "");
                        msg = msg.replace("[b]", "$" + round(price, 2) + "");
                        showDialogAddPaymentForRequest(msg);
                    }
                }
            }
        }).setNegativeButton("Cancel", new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ppk = listCarTypes.get(adapter.getPositionCheck()).getFee_per_kilometer();
        ppm = listCarTypes.get(adapter.getPositionCheck()).getFee_per_minute();
        sf = listCarTypes.get(adapter.getPositionCheck()).getStart_fare();
        taskRate = listCarTypes.get(adapter.getPositionCheck()).getTaskRate();
        tastDefaultTime = listCarTypes.get(adapter.getPositionCheck()).getTaskDefaultTime();
        linkPosition = 1;

//        if (settingObj != null) {
        price = Double.parseDouble(sf) + (round(results[0] / 1000, 2) / Double.parseDouble(settingObj.getEstimate_fare_speed())) * 60 * Double.parseDouble(ppm) + (round(results[0] / 1000, 2)) * Double.parseDouble(ppk) + Double.parseDouble(taskRate) * Double.parseDouble(tastDefaultTime);
//        }
        preferencesManager.putStringValue("distanceLocation", round(price, 2) + "");
        msg = msg.replace("%.2f", round(price, 2) + "");
        dialog.setMessage(msg);
        dialog.show();
    }*/

    public void showDialogAddPaymentForRequest(String message) {
        dialogPayment = new MaterialDialog(getActivity());
        dialogPayment.setMessage(message);
        dialogPayment.setPositiveButton(getResources().getString(R.string.add_point), new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPayment.dismiss();
//                Intent intent = new Intent(getActivity(), AddPointActivity.class);
//                intent.putExtra("point", round(price, 2));
                startActivity(PaymentPointActivity.class);
            }
        });
        dialogPayment.setNegativeButton(getResources().getString(R.string.cancel), new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPayment.dismiss();
            }
        });
        dialogPayment.show();
    }

    private void refreshData(String carType) {
        final GPSTracker tracker = new GPSTracker(mainActivity);
        if (tracker.canGetLocation() == false) {
            lblAvailableVehicle.setText(0 + "");
        } else {
            ModelManager.getTotalDriversAroundLocation(self, tracker.getLatitude(), tracker.getLongitude(), 3, carType, true, new ModelManagerListener() {
                @Override
                public void onError() {
                    lblAvailableVehicle.setText(0 + "");
                }

                @Override
                public void onSuccess(String json) {
                    if (ParseJsonUtil.isSuccess(json)) {
                        lblAvailableVehicle.setText(ParseJsonUtil.getDriverCount(json));
                    } else {
                        lblAvailableVehicle.setText(0 + "");
                    }
                }
            });
        }
    }

//    String link = "I";

//    private void selectPeople(int num) {
//        if (num == 1) {
//            link = "I";
//            btnLink1.setTypeface(null, Typeface.BOLD);
//            btnLink2.setTypeface(null, Typeface.NORMAL);
//            btnLink3.setTypeface(null, Typeface.NORMAL);
//        } else if (num == 2) {
//            link = "II";
//            btnLink1.setTypeface(null, Typeface.NORMAL);
//            btnLink2.setTypeface(null, Typeface.BOLD);
//            btnLink3.setTypeface(null, Typeface.NORMAL);
//        } else if (num == 3) {
//            link = "III";
//            btnLink1.setTypeface(null, Typeface.NORMAL);
//            btnLink2.setTypeface(null, Typeface.NORMAL);
//            btnLink3.setTypeface(null, Typeface.BOLD);
//        }
//    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private void applyPromocodeAPi(final String promo_code) {
        Log.e("data", "data:" + preferencesManager.getUserID() + " " + promo_code + " " + String.valueOf(price));

//        Log.e("caculate", "caculate:" + getDistanceOnRoad(startLocation.latitude, startLocation.longitude, endLocation.latitude, endLocation.longitude));
        ModelManager.applyPromo(preferencesManager.getUserID(), promo_code, String.valueOf(price), mainActivity, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(String json) {

                        if (ParseJsonUtil.isSuccess(json)) {
                            Log.e("data", "result " + json);
                            try {
                                showToast(ParseJsonUtil.getMessage(json));
                                JSONObject jsonObject1 = new JSONObject(json);
                                price = Double.parseDouble(jsonObject1.getString("data"));
                                bookNowDialog(link, true, promo_code);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                           /* setDataCreateRequest(estimateDistance, preferencesManager.getDataCarType().getId());
                            createServiceRequest();
                            globalValue.setEstimate_fare(ParseJsonUtil.getEstimateFare(json));
                            preferencesManager.putStringValue("countDriver", ParseJsonUtil.getCountDriver(json));
                            preferencesManager.getInstance(getActivity()).putStringValue("estimate", globalValue.getEstimate_fare());
                            mainActivity.gotoActivity(WaitDriverConfirmActivity.class);
                            book_dialog.dismiss();*/
//                            dialog.dismiss();
                        } else {
                            showToast(ParseJsonUtil.getMessage(json));
                        }
                    }

                    @Override
                    public void onError() {

                        showToast(R.string.message_have_some_error);
                    }
                });
    }

    private void createRequest(final String link, final String promocode_value) {
        Log.e("data", "data:" + link);

        if (startLocation == null) {
            startLocation = new LatLng(Double.parseDouble(preferencesManager.getStringValue("STARTLATITUDE")), Double.parseDouble(preferencesManager.getStringValue("STARTLONGITUDE")));
        }
        if (endLocation == null) {
            endLocation = new LatLng(Double.parseDouble(preferencesManager.getStringValue("ENDLATITUDE")), Double.parseDouble(preferencesManager.getStringValue("ENDLONGITUDE")));
        }

        if (estimateDistance == null) {
            estimateDistance = preferencesManager.getStringValue("distanceLocation");
        }
//        Log.e("caculate", "caculate:" + getDistanceOnRoad(startLocation.latitude, startLocation.longitude, endLocation.latitude, endLocation.longitude));
        ModelManager.createRequest(PreferencesManager.getInstance(mainActivity)
                        .getToken(), preferencesManager.getDataCarType().getId(), startLocation.latitude + "",
                startLocation.longitude + "", txtFrom.getText().toString(),
                endLocation.latitude + "", endLocation.longitude + "", txtTo
                        .getText().toString(), estimateDistance, mainActivity, true, promocode_value,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            setDataCreateRequest(estimateDistance, preferencesManager.getDataCarType().getId(), promocode_value);
                            createServiceRequest();
                            globalValue.setEstimate_fare(ParseJsonUtil.getEstimateFare(json));
                            preferencesManager.putStringValue("countDriver", ParseJsonUtil.getCountDriver(json));
                            preferencesManager.getInstance(getActivity()).putStringValue("estimate", globalValue.getEstimate_fare());
                            mainActivity.gotoActivity(WaitDriverConfirmActivity.class);
                            book_dialog.dismiss();
//                            dialog.dismiss();
                        } else {
                            showToast(ParseJsonUtil.getMessage(json));
                        }
                    }

                    @Override
                    public void onError() {

                        showToast(R.string.message_have_some_error);
                    }
                });
    }

    public void setDataCreateRequest(String estimateDistance, String link, String promocode_value) {
        preferencesManager.putStringValue(Constant.KEY_STARTLOCATION_LATITUDE, startLocation.latitude + "");
        preferencesManager.putStringValue(Constant.KEY_STARTLOCATION_LONGITUDE, startLocation.longitude + "");
        preferencesManager.putStringValue(Constant.KEY_ENDLOCATION_LATITUDE, endLocation.latitude + "");
        preferencesManager.putStringValue(Constant.KEY_ENDLOCATION_LONGITUDE, endLocation.latitude + "");
        preferencesManager.putStringValue(Constant.KEY_ADDRESS_START, txtFrom.getText().toString());
        preferencesManager.putStringValue(Constant.KEY_ADDRESS_TO, txtTo.getText().toString());
        preferencesManager.putStringValue(Constant.KEY_LINK, link);
        preferencesManager.putStringValue(Constant.KEY_ESTIMATE_DISTANCE, estimateDistance);
        preferencesManager.putStringValue(Constant.KEY_PROMO_CODE, promocode_value);
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

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        //close when fake location
        mMap.setOnMyLocationChangeListener(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    // ===================== SETUP AUTO COMPLETE ADDRESS =====================
    public void setStartMarker() {
        if (startLocation != null) {
            PreferencesManager.getInstance(getActivity()).putStringValue("STARTLATITUDE", startLocation.latitude + "");
            PreferencesManager.getInstance(getActivity()).putStringValue("STARTLONGITUDE", startLocation.longitude + "");
            if (mMarkerStartLocation != null) {
                mMarkerStartLocation.remove();
            }
            iconMarker = BitmapFactory.decodeResource(
                    mainActivity.getResources(), R.drawable.ic_position_a);
            iconMarker = Bitmap.createScaledBitmap(iconMarker,
                    iconMarker.getWidth(), iconMarker.getHeight(),
                    false);
            mMarkerStartLocation = mMap.addMarker(new MarkerOptions().position(
                    startLocation).icon(
                    BitmapDescriptorFactory.fromBitmap(iconMarker)));
            showDirection();
        }
    }

    public void setStartMarkerAgain() {
        if (startLocation != null) {
            if (mMarkerStartLocation != null) {
                mMarkerStartLocation.remove();
            }
            iconMarker = BitmapFactory.decodeResource(
                    mainActivity.getResources(), R.drawable.ic_position_a);
            iconMarker = Bitmap.createScaledBitmap(iconMarker,
                    iconMarker.getWidth(), iconMarker.getHeight(),
                    false);
            mMarkerStartLocation = mMap.addMarker(new MarkerOptions().position(
                    startLocation).icon(
                    BitmapDescriptorFactory.fromBitmap(iconMarker)));
        }
    }

    public void setEndMarker() {
        if (endLocation != null) {
            PreferencesManager.getInstance(getActivity()).putStringValue("ENDLATITUDE", endLocation.latitude + "");
            PreferencesManager.getInstance(getActivity()).putStringValue("ENDLONGITUDE", endLocation.longitude + "");
            if (mMarkerEndLocation != null) {
                mMarkerEndLocation.remove();
            }
            iconMarker = BitmapFactory.decodeResource(
                    mainActivity.getResources(), R.drawable.ic_position_b);
            iconMarker = Bitmap.createScaledBitmap(iconMarker,
                    iconMarker.getWidth(), iconMarker.getHeight(),
                    false);
            mMarkerEndLocation = mMap.addMarker(new MarkerOptions().position(
                    endLocation).icon(
                    BitmapDescriptorFactory.fromBitmap(iconMarker)));
            showDirection();
        }
    }

    public void setEndMarkerAgain() {
        if (endLocation != null) {
            if (mMarkerEndLocation != null) {
                mMarkerEndLocation.remove();
            }
            iconMarker = BitmapFactory.decodeResource(
                    mainActivity.getResources(), R.drawable.ic_position_b);
            iconMarker = Bitmap.createScaledBitmap(iconMarker,
                    iconMarker.getWidth(), iconMarker.getHeight(),
                    false);
            mMarkerEndLocation = mMap.addMarker(new MarkerOptions().position(
                    endLocation).icon(
                    BitmapDescriptorFactory.fromBitmap(iconMarker)));
        }
    }

    public void setUpMapOnClick() {
        // Click on map to get latitude and longitude.
        mMap.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(LatLng loc) {
                // Hiding the keyboard when tab on map.
//                new MapsUtil.GetAddressByLatLng(new IMaps() {
//                    @Override
//                    public void processFinished(Object obj) {
//                        String address = (String) obj;
//                        if (!address.isEmpty()) {
//                            // Set marker's title
//                            if (txtFromIsSelected) {
//                                txtFrom.setText(address);
//                            } else {
//                                if (txtToIsSelected) {
//                                    txtTo.setText(address);
//                                }
//                            }
//                        }
//                    }
//                }).execute(loc);
                if (txtFromIsSelected) {
                    txtFrom.setText(getCompleteAddressString(loc.latitude, loc.longitude));
                } else {
                    if (txtToIsSelected) {
                        txtTo.setText(getCompleteAddressString(loc.latitude, loc.longitude));
                    }
                }
                closeKeyboard();
                selectFromMap = true;
                if (txtFromIsSelected) {
                    if (circle != null) {
                        circle.remove();
//                        mMap.clear();
                    }
                    startLocation = loc;
                    setStartMarker();
                    circle = mMap.addCircle(new CircleOptions()
                            .center(startLocation)
                            .radius(3000)
                            .strokeWidth(0.5f)
                            .strokeColor(Color.rgb(0, 136, 255))
                            .fillColor(Color.argb(20, 0, 136, 255)));
                    txtFrom.setDropDownHeight(0);
                } else {
                    if (txtToIsSelected) {
                        endLocation = loc;
                        setEndMarker();
                        txtTo.setDropDownHeight(0);
                    }
                }
                // Get address by latlng async

            }
        });
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                if (returnedAddress.getMaxAddressLineIndex() > 0) {
                    for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                        if (i < returnedAddress.getMaxAddressLineIndex() - 1) {
                            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
                        } else {
                            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("");
                        }

                    }
                } else {
                    strReturnedAddress.append(returnedAddress.getAddressLine(0));
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    public void setMarker(ArrayList<DriverUpdate> listMarkers) {
        ArrayList<DriverUpdate> listMarkerDatas = new ArrayList<>();
        listMarkerDatas.addAll(listMarkers);
        listMarkers.clear();
        for (int i = 0; i < listMarkerDatas.size(); i++) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(listMarkerDatas.get(i).getMarker().getPosition()));
            BitmapDescriptor bitmapDescriptor = null;
            Bitmap bitmap = ImageUtil.createBitmapFromUrl(listCarTypes.get(adapter.getPositionCheck()).getImageMarker());
            // resize
            int size = ImageUtil.getSizeBaseOnDensity(self, 35);
            bitmap = ImageUtil.getResizedBitmap(bitmap, size, size);
            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
            marker.setIcon(bitmapDescriptor);
            DriverUpdate driverUpdate = new DriverUpdate();
            driverUpdate.setId(listMarkerDatas.get(i).getId());
            driverUpdate.setMarker(listMarkerDatas.get(i).getMarker());
            listMarkers.add(driverUpdate);
        }
    }

    public void setupAutoComplete(View view) {
        if (mThreadHandler == null) {
            mHandlerThread = new HandlerThread(TAG,
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);
            mHandlerThread.start();

            // Initialize the Handler
            mThreadHandler = new Handler(mHandlerThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        ArrayList<String> results = mAdapter.resultList;

                        if (results != null && results.size() > 0) {
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.notifyDataSetInvalidated();
                        }
                    }
                }
            };
        }
        txtFrom = (AutoCompleteTextView) view.findViewById(R.id.txtFrom);
        txtFrom.setAdapter(new PlacesAutoCompleteAdapter(self,
                R.layout.item_auto_place));
        txtFrom.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txtFromIsSelected = true;
                } else {
                    txtFromIsSelected = false;
                }
            }
        });
        txtFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                closeKeyboard();
// Get data associated with the specified position
// in the list (AdapterView)
                final String description = (String) parent
                        .getItemAtPosition(position);

                // Move camera to new address.
                new MapsUtil.GetLatLngByAddress(new IMaps() {

                    @Override
                    public void processFinished(Object obj) {
                        try {
                            // Move camera smoothly
                            LatLng latLng = (LatLng) obj;
                            if (((LatLng) obj).latitude != 0.0 && ((LatLng) obj).longitude != 0.0) {
                                mMap.animateCamera(CameraUpdateFactory
                                        .newLatLngZoom(latLng, 12));

                                // Add marker
                                startLocation = latLng;
                                setStartMarker();
                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.can_not_find), Toast.LENGTH_SHORT).show();
                            }

                            // Set marker's title
                            // String address = description.replace("%20", " ");
                            // mMarker.setTitle(address);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }).execute(description);
            }
        });

        txtFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                final String value = s.toString();
                if (value.length() > 0) {
                    // Remove all callbacks and messages
                    mThreadHandler.removeCallbacksAndMessages(null);

                    // Now add a new one
                    mThreadHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (mAdapter == null) {
                                mAdapter = new PlacesAutoCompleteAdapter(self,
                                        R.layout.item_auto_place);
                            }
                            // Background thread
                            mAdapter.resultList = mAdapter.mPlaceAPI
                                    .autocomplete(value);

                            // Footer
                            if (mAdapter.resultList.size() > 0) {
                                mAdapter.resultList.add("footer");
                            }

                            // Post to Main Thread
                            mThreadHandler.sendEmptyMessage(1);
                        }
                    }, 500);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (selectFromMap) {
                    selectFromMap = false;
                } else {
                    txtFrom.setDropDownHeight(LayoutParams.WRAP_CONTENT);
                }
            }
        });

        txtTo = (AutoCompleteTextView) view.findViewById(R.id.txtTo);
        txtTo.setAdapter(new PlacesAutoCompleteAdapter(self,
                R.layout.item_auto_place));
        txtTo.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txtToIsSelected = true;
                } else {
                    txtToIsSelected = false;
                }
            }
        });
        txtTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                closeKeyboard();
                // Get data associated with the specified position
                // in the list (AdapterView)
                final String description = (String) parent
                        .getItemAtPosition(position);

                // Move camera to new address.
                new MapsUtil.GetLatLngByAddress(new IMaps() {

                    @Override
                    public void processFinished(Object obj) {
                        try {
                            // Move camera smoothly
                            LatLng latLng = (LatLng) obj;
                            mMap.animateCamera(CameraUpdateFactory
                                    .newLatLngZoom(latLng, 11));

                            // Add marker
                            endLocation = latLng;
                            setEndMarker();

                            // Set marker's title
                            // String address = description.replace("%20", " ");
                            // mMarker.setTitle(address);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }).execute(description);
            }
        });

        txtTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                final String value = s.toString();
                if (value.length() > 0) {
                    // Remove all callbacks and messages
                    mThreadHandler.removeCallbacksAndMessages(null);

                    // Now add a new one
                    mThreadHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (mAdapter == null) {
                                mAdapter = new PlacesAutoCompleteAdapter(self,
                                        R.layout.item_auto_place);
                            }
                            // Background thread
                            mAdapter.resultList = mAdapter.mPlaceAPI
                                    .autocomplete(value);

                            // Footer
                            if (mAdapter.resultList.size() > 0) {
                                mAdapter.resultList.add("footer");
                            }

                            // Post to Main Thread
                            mThreadHandler.sendEmptyMessage(1);
                        }
                    }, 500);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (selectFromMap) {
                    selectFromMap = false;
                } else {
                    txtTo.setDropDownHeight(LayoutParams.WRAP_CONTENT);
                }
            }
        });
    }

//    public void goToMyLocation() {
//        final GPSTracker tracker = new GPSTracker(mainActivity);
//
//        if (tracker.canGetLocation() == false) {
//            tracker.showSettingsAlert();
//            showToast("Wait for location service");
//        } else {
//            LatLng currentLatLng = new LatLng(tracker.getLatitude(), tracker.getLongitude());
//            CameraUpdate myLoc = CameraUpdateFactory
//                    .newCameraPosition(new CameraPosition.Builder()
//                            .target(currentLatLng).zoom(13).build());
//            mMap.moveCamera(myLoc);
////            refreshData();
//        }
//    }

// ===================== DOMAIN =====================

    private void closeKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (txtFrom.hasFocus()) {
                imm.hideSoftInputFromWindow(txtFrom.getWindowToken(), 0);
            } else {
                imm.hideSoftInputFromWindow(txtFrom.getWindowToken(), 0);
            }

            if (txtTo.hasFocus()) {
                imm.hideSoftInputFromWindow(txtTo.getWindowToken(), 0);
            } else {
                imm.hideSoftInputFromWindow(txtTo.getWindowToken(), 0);
            }
        } catch (Exception ex) {

        }
    }

    public void back() {
        clear();
        btnBack.setVisibility(View.GONE);
    }

    public void clear() {
        mMap.clear();
        startLocation = null;
        endLocation = null;
        txtFrom.setText("");
        txtTo.setText("");
    }
}
