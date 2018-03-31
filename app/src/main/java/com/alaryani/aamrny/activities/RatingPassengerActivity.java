package com.alaryani.aamrny.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.utility.NetworkUtil;
import com.alaryani.aamrny.widget.TextViewRaleway;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class RatingPassengerActivity extends BaseActivity {

    private TextViewRaleway lblName, lblPhone;
    private TextViewRaleway lblPoint;
    private RatingBar ratingBar, ratingBarUser;
    private TextView btnSend, tvSeat;
    private TextViewRaleway lblStartLocation, txtExplain;
    private TextViewRaleway lblEndLocation, txtIdentity, txtCarPlate, txtTask, txtTrip;
    private ImageView imgPassenger;
    private TextView txtStar;
    private LinearLayout llHelp;
    AQuery aq;
    private String phoneAdmin = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        if (isCurrentAppLangRtl())
            setContentView(R.layout.activity_rating_passenger_rtl);
        else
            setContentView(R.layout.activity_rating_passenger);

        aq = new AQuery(this);

        initUI();
        findViews();
        getDriverEarn();
        initControl();

    }

    private void findViews() {
        lblStartLocation = (TextViewRaleway) findViewById(R.id.lblStartLocation);
        lblEndLocation = (TextViewRaleway) findViewById(R.id.lblEndLocation);
        txtExplain = (TextViewRaleway) findViewById(R.id.txtExplain);
        imgPassenger = (ImageView) findViewById(R.id.imgPassenger);
        tvSeat = (TextView) findViewById(R.id.tvSeat);
        txtStar = (TextView) findViewById(R.id.txtStar);
        lblName = (TextViewRaleway) findViewById(R.id.lblName);
        lblPhone = (TextViewRaleway) findViewById(R.id.lblPhone);
        lblPoint = (TextViewRaleway) findViewById(R.id.lblPoint);
        txtIdentity = (TextViewRaleway) findViewById(R.id.txtIdentity);
        txtCarPlate = (TextViewRaleway) findViewById(R.id.txtCarPlate);
        txtTask = (TextViewRaleway) findViewById(R.id.txtTask);
        txtTrip = (TextViewRaleway) findViewById(R.id.txtTrip);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBarUser = (RatingBar) findViewById(R.id.ratingBarUser);
        llHelp = (LinearLayout) findViewById(R.id.llHelp);
        btnSend = (TextView) findViewById(R.id.btnSend);
        if (globalValue
                .getCurrentOrder() != null && !globalValue
                .getCurrentOrder()
                .getPassengerRate().equals(""))
            ratingBarUser.setRating(Float
                    .parseFloat(globalValue
                            .getCurrentOrder()
                            .getPassengerRate()) / 2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initControl() {
        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingBar.getRating() * 2 != 0) {
                    rateCustomer(globalValue.getCurrentOrder().getId(),
                            ratingBar.getRating() * 2 + "");

                    preferencesManager.setDriverCurrentScreen("");
                    preferencesManager.setDriverIsNotInTrip();
                    preferencesManager.setCurrentOrderId("");
                } else {
                    Toast.makeText(self, getString(R.string.lblValidateRate), Toast.LENGTH_SHORT).show();
                }

            }
        });
        llHelp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.checkNetworkAvailable(RatingPassengerActivity.this)) {
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
    }

    private void initData() {
        ModelManager.getGeneralSettings(preferencesManager.getToken(),
                self, true, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            phoneAdmin = ParseJsonUtil.getPhoneAdmin(json);
                        }
                    }

                    @Override
                    public void onError() {
                    }
                });
        ModelManager.showTripDetail(preferencesManager.getToken(),
                preferencesManager.getCurrentOrderId(), context, true,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        // TODO Auto-generated method stub
                        if (ParseJsonUtil.isSuccess(json)) {
                            globalValue.setCurrentOrder(ParseJsonUtil.parseCurrentOrder(json));

                            lblPoint.setText(getString(R.string.lblCurrency) + globalValue.getCurrentOrder().getActualFare());
                            Locale currentLocale = getResources().getConfiguration().locale;
                            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
                            otherSymbols.setDecimalSeparator('.');
                            DecimalFormat df = new DecimalFormat("#.####",otherSymbols);


                            String temp = df.format(Double.parseDouble(globalValue.getCurrentOrder().getActualFare()) * globalValue.getDriver_earn());
                            String deduct = df.format(Double.parseDouble(globalValue.getCurrentOrder().getActualFare()) - Double.parseDouble(temp));

                            String text = String.format(getString(R.string.explain), deduct, temp);
                            txtExplain.setText(text);

                            tvSeat.setText(convertLinkToString(globalValue.getCurrentOrder().getLink()) + "");
                            txtCarPlate.setText(globalValue.getCurrentOrder().getCarPlate());
                            txtIdentity.setText(globalValue.getCurrentOrder().getIdentity());
                            txtTrip.setText(globalValue.getCurrentOrder().getDistance() + " KM (" + globalValue.getCurrentOrder().getTotalTime() + " " + getResources().getString(R.string.minutes) + ")");
                            if (globalValue.getCurrentOrder().getStartTimeWorking() != null && !globalValue.getCurrentOrder().getStartTimeWorking().equals("") && !globalValue.getCurrentOrder().getStartTimeWorking().equals("0")) {
                                txtTask.setText(getTime(Long.parseLong(globalValue.getCurrentOrder().getStartTimeWorking()), Long.parseLong(globalValue.getCurrentOrder().getEndTimeWorking())));
                            } else {
                                txtTask.setText("0 " + getResources().getString(R.string.minutes));
                            }
                            lblName.setText(globalValue.getCurrentOrder()
                                    .getPassengerName());
                            lblPhone.setText(globalValue.getCurrentOrder().getPassenger_phone(true));
                            lblStartLocation.setText(globalValue
                                    .getCurrentOrder().getStartLocation());
                            lblEndLocation.setText(globalValue
                                    .getCurrentOrder().getEndLocation());
                            aq.id(R.id.imgPassenger).image(
                                    globalValue.getCurrentOrder().getImagePassenger());
                            if (globalValue.getCurrentOrder()
                                    .getPassengerRate().isEmpty()) {
                                txtStar.setText("0");
                            } else {
                                txtStar.setText("" + Float
                                        .parseFloat(globalValue
                                                .getCurrentOrder()
                                                .getPassengerRate()) / 2);
                            }
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

    public String getTime(long startTime, long endTime) {
        DateTime today = new DateTime(endTime * 1000L);
        DateTime yesterday = new DateTime(startTime * 1000L);
        Duration duration = new Duration(yesterday, today);
        long timeInMilliseconds = duration.getStandardSeconds();
        long mins = timeInMilliseconds / 60;
        long hour = mins / 60;
        if (mins < 1) {
            return "0 minute";
        } else {
            if (hour < 1) {
                return mins + " minute(s)";
            } else {
                return hour + " hour(s)" + mins + " minute(s)";
            }
        }
    }

    public String convertLinkToString(String link) {
        for (int i = 0; i < GlobalValue.getInstance().getListCarTypes().size(); i++) {
            if (GlobalValue.getInstance().getListCarTypes().get(i).getId() != null && !GlobalValue.getInstance().getListCarTypes().get(i).getId().equals("")) {
                if (link.equals(GlobalValue.getInstance().getListCarTypes().get(i).getId())) {
                    return GlobalValue.getInstance().getListCarTypes().get(i).getName();
                }
            }

        }
        return link;
    }

    private void rateCustomer(String tripId, String rate) {
        ModelManager.rateCustomer(preferencesManager.getToken(), tripId, rate,
                self, true, new ModelManagerListener() {

                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            preferencesManager.setDriverCurrentScreen("");
                            preferencesManager.setDriverIsNotInTrip();
                            gotoActivity(OnlineActivity.class);
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

    public void getDriverEarn() {
        ModelManager.getGeneralSettings(preferencesManager.getToken(), self,
                true, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            /*if(isCurrentAppLangRtl()){
                                globalValue.setDriver_earn(Double.parseDouble(arabicToDecimal(String.valueOf(ParseJsonUtil.getDriverEarn(json)))));
                            }else{
                                globalValue.setDriver_earn(ParseJsonUtil.getDriverEarn(json));
                            }*/
                            globalValue.setDriver_earn(ParseJsonUtil.getDriverEarn(json));
                            initData();
                        }
                    }

                    @Override
                    public void onError() {
                        showToastMessage(getResources().getString(
                                R.string.message_have_some_error));
                    }
                });
    }

}
