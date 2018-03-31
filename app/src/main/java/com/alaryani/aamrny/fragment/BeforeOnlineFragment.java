package com.alaryani.aamrny.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.alaryani.aamrny.BaseFragment;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.ServiceUpdateLocation;
import com.alaryani.aamrny.activities.Ac_ConfirmPayByCash;
import com.alaryani.aamrny.activities.OnlineActivity;
import com.alaryani.aamrny.config.Constant;
import com.alaryani.aamrny.config.LinkApi;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.network.ProgressDialog;
import com.alaryani.aamrny.object.UserUpdate;
import com.alaryani.aamrny.service.GPSTracker;
import com.alaryani.aamrny.widget.MyLocation;
import com.alaryani.aamrny.widget.TextViewRaleway;

public class BeforeOnlineFragment extends BaseFragment implements
        OnClickListener {

    Button btnOnline;
    private TextView lblTitle;
    TextViewRaleway lbl_Online;
    private GPSTracker tracker;
    private TextViewRaleway lblRequest, lblWaiting;
    Firebase ref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_before_online,
                container, false);
        Firebase.setAndroidContext(getActivity());
        ref = new Firebase(LinkApi.FIREBASE_URL);
        tracker = new GPSTracker(getActivity());
        initUI(view);
        initControl();
        initMenuButton(view);
        return view;
    }

    public void changeLanguage() {
        btnOnline.setText(R.string.lbl_online);
        lblTitle.setText(getResources().getString(R.string.lbl_online));
        lbl_Online.setText(R.string.lbl_guide);
        lblWaiting.setText(R.string.lblClearGps);
    }

    public void initUI(View view) {
        lblTitle = (TextView) view.findViewById(R.id.lblTitle);
        lblTitle.setText(getResources().getString(R.string.lbl_online));
        btnOnline = (Button) view.findViewById(R.id.btnOnline);
        lblRequest = (TextViewRaleway) view.findViewById(R.id.lblRequest);
        lbl_Online = (TextViewRaleway) view.findViewById(R.id.lbl_Online);
        lblWaiting = (TextViewRaleway) view.findViewById(R.id.lblWaiting);
        lblTitle.setVisibility(View.GONE);
    }

    public void initControl() {
        btnOnline.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnOnline:
                new DownloadTask(self).execute();
                break;
        }
    }

    @Override
    public void onDestroy() {
        tracker.stopSelf();
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

        if(!hidden){
            getDriverConfirm();
        }
    }
    public void getDriverConfirm() {
        ModelManager.showTripHistory(preferencesManager.getToken(), "1", getActivity(), true, new ModelManagerListener() {
            @Override
            public void onError() {
                showToast(getString(R.string.message_have_some_error));
            }

            @Override
            public void onSuccess(String json) {
                if (ParseJsonUtil.isSuccess(json)) {
                    String isWaitDriverConfirm = ParseJsonUtil.pareWaitDriverConfirm(json);
                    if (isWaitDriverConfirm.equals(Constant.TRIP_WAIT_DRIVER_NOTCONFIRM)) {
                        mainActivity.gotoActivity(Ac_ConfirmPayByCash.class);
                    }
                } else {
                    showToast(ParseJsonUtil.getMessage(json));
                }
            }
        });
    }

    class DownloadTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pDialog;
        private Context context;

        public DownloadTask(Context context) {
            this.context = context;
            pDialog = new ProgressDialog(context);
            pDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog != null) {
                pDialog.show();
                self.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lblWaiting.setText(getString(R.string.lblWaiting));
                    }
                });

            } else {
                self.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lblWaiting.setText(getString(R.string.lblCanNotgetGps));
                    }
                });

            }


        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(tracker.canGetLocation()){

            }
            final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                    @Override
                    public void gotLocation(Location location) {
                        if (location != null) {
                            online(location);
                        } else {
                            if (pDialog != null) {
                                pDialog.dismiss();
                            }
                            self.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lblWaiting.setText(getString(R.string.lblCanNotgetGps));
                                }
                            });

                        }
                    }
                };
                MyLocation myLocation = new MyLocation();
                myLocation.getLocation(context, locationResult);
            } else {
                if (pDialog != null) {
                    pDialog.dismiss();
                }
                self.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lblWaiting.setText(getString(R.string.lblCanNotgetGps));
                    }
                });
            }
            super.onPostExecute(aVoid);
        }

        public void online(final Location location) {
            ref.child("user").child(preferencesManager.getUserID()).setValue(new UserUpdate(tracker.getLatitude() + "", tracker.getLongitude() + "", "1"));
            ModelManager.online(PreferencesManager.getInstance(context)
                    .getToken(), context, false, new ModelManagerListener() {
                @Override
                public void onSuccess(String json) {

                    if (ParseJsonUtil.isSuccess(json)) {
                        Intent intent1 = new Intent(getActivity(), ServiceUpdateLocation.class);
                        getActivity().startService(intent1);
                        preferencesManager.setDriverOnline();
                        preferencesManager.isFromBeforeOnline(true);
                        mainActivity.gotoActivity(OnlineActivity.class);
                        if (pDialog != null) {
                            pDialog.dismiss();
                        }
//                        updateCoordinate(location.getLatitude() + "", location.getLongitude()
//                                + "");
                    } else {
                        pDialog.dismiss();
                        showToast(ParseJsonUtil.getMessage(json));
                    }
                }

                @Override
                public void onError() {
                    pDialog.dismiss();
                    showToast(R.string.message_have_some_error);
                }
            });
        }

//        public void updateCoordinate(String lat, String lon) {
//            if (!lat.isEmpty() && !lon.isEmpty()) {
//                ModelManager.updateCoordinate(preferencesManager.getToken(), lat,
//                        lon, context, false, new ModelManagerListener() {
//
//                            @Override
//                            public void onSuccess(String json) {
//                                if (ParseJsonUtil.isSuccess(json)) {
//                                    mainActivity.gotoActivity(OnlineActivity.class);
//                                    if (pDialog != null) {
//                                        pDialog.dismiss();
//                                    }
//                                } else {
//                                    Toast.makeText(getActivity(), ParseJsonUtil.getMessage(json), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onError() {
//
//                            }
//                        });
//            }
//
//        }
    }
}
