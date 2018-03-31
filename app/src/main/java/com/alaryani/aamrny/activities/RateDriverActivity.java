package com.alaryani.aamrny.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.alaryani.aamrny.config.Constant;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.utility.NetworkUtil;
import com.alaryani.aamrny.widget.TextViewFontAwesome;
import com.alaryani.aamrny.widget.TextViewRaleway;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.json.JSONObject;

import java.text.DecimalFormat;

import me.drakeet.materialdialog.MaterialDialog;

public class RateDriverActivity extends BaseActivity {
    private TextView btnSend, btnRate, lblPoint;
    private TextViewRaleway lblNorify;
    private String point;
    private TextViewRaleway lblName, tvSeat;

    private LinearLayout loPayment, loRate, llHelp;
    private TextViewFontAwesome stepNext, stepCircle;

    private TextViewRaleway lblPhone, tvStep;
    private RatingBar ratingBar, ratingBarUser;
    private TextViewRaleway lblStartLocation;
    private TextViewRaleway lblEndlocation, txtIdentity, txtCarPlate, txtTask, txtTrip;
    private TextView txtStar;


    private ImageView imgPassenger;
    //    private ImageView imgCar;
    private AQuery aq;
    private String phoneAdmin = "";
    private boolean checkRate = true;
    private boolean checkPayment = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(isCurrentAppLangRtl())
            setContentView(R.layout.activity_passenger_trip_rtl);
        else
            setContentView(R.layout.activity_passenger_trip);
        aq = new AQuery(self);
        initUI();
        initView();
        initLocalBroadcastManager();
        initData();
        initControl();
//

//		if (preferencesManager.passengerIsHaveDonePayment()) {
//			showRate(true);
//		}

        // Starting PayPal service
        startPaypalService();
    }

    @Override
    public void onResume() {
        preferencesManager.setPassengerCurrentScreen(RateDriverActivity.class
                .getSimpleName());
        super.onResume();
    }

    ;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        LocalBroadcastManager.getInstance(getBaseContext()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
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
        if (!getPreviousActivityName().equals(
                StartTripForPassengerActivity.class.getName())) {
            ModelManager.showTripDetail(preferencesManager.getToken(),
                    preferencesManager.getCurrentOrderId(), context, true,
                    new ModelManagerListener() {

                        @Override
                        public void onSuccess(String json) {
                            Log.e("json", "data " + json);
                            if (ParseJsonUtil.isSuccess(json)) {
                                globalValue.setCurrentOrder(ParseJsonUtil
                                        .parseCurrentOrder(json));
                                lblPoint.setText(getString(R.string.lblCurrency) + globalValue.getCurrentOrder().getActualFare());

                                tvSeat.setText(convertLinkToString(globalValue.getCurrentOrder().getLink() + ""));
                                txtIdentity.setText(globalValue.getCurrentOrder().getIdentity());
                                txtTrip.setText(globalValue.getCurrentOrder().getDistance() + " KM (" + globalValue.getCurrentOrder().getTotalTime() + " "+getResources().getString(R.string.minutes)+")");
                                if (globalValue.getCurrentOrder().getStartTimeWorking() != null && !globalValue.getCurrentOrder().getStartTimeWorking().equals("") && !globalValue.getCurrentOrder().getStartTimeWorking().equals("0")) {
                                    txtTask.setText(getTime(Long.parseLong(globalValue.getCurrentOrder().getStartTimeWorking()), Long.parseLong(globalValue.getCurrentOrder().getEndTimeWorking())));
                                } else {
                                    txtTask.setText("0 "+getResources().getString(R.string.minutes));
                                }
                                txtCarPlate.setText(globalValue.getCurrentOrder().getCarPlate());
                                lblPhone.setText(globalValue.getCurrentOrder()
                                        .getDriver_phone(true));
                                lblName.setText(globalValue.getCurrentOrder()
                                        .getDriverName());
//                                lblCarPlate.setText(globalValue.getCurrentOrder().getCarPlate());
                                lblStartLocation.setText(globalValue
                                        .getCurrentOrder().getStartLocation());
                                lblEndlocation.setText(globalValue
                                        .getCurrentOrder().getEndLocation());
                                if (globalValue.getCurrentOrder()
                                        .getDriverRate().isEmpty()) {
                                    ratingBarUser.setRating(0);
                                    txtStar.setText("0");
                                } else {
                                    ratingBarUser.setRating(Float
                                            .parseFloat(globalValue
                                                    .getCurrentOrder()
                                                    .getDriverRate()) / 2);
                                    txtStar.setText("" + Float
                                            .parseFloat(globalValue
                                                    .getCurrentOrder()
                                                    .getDriverRate()) / 2);
                                }
//                                aq.id(imgCar).image(
//                                        globalValue.getCurrentOrder()
//                                                .getCarImage());
                                Log.e("eeeeeeeeee", "image: " + globalValue.getCurrentOrder()
                                        .getCarImage());
                                aq.id(imgPassenger).image(
                                        globalValue.getCurrentOrder()
                                                .getImageDriver());
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
            lblPoint.setText(getString(R.string.lblCurrency) + globalValue.getCurrentOrder().getActualFare()
            );
            tvSeat.setText(convertLinkToString(globalValue.getCurrentOrder().getLink()));
            txtIdentity.setText(globalValue.getCurrentOrder().getIdentity());
            txtTrip.setText(globalValue.getCurrentOrder().getDistance() + " KM (" + globalValue.getCurrentOrder().getTotalTime() + " minutes)");
            if (globalValue.getCurrentOrder().getStartTimeWorking() != null && !globalValue.getCurrentOrder().getStartTimeWorking().equals("") && !globalValue.getCurrentOrder().getStartTimeWorking().equals("0")) {
                txtTask.setText(getTime(Long.parseLong(globalValue.getCurrentOrder().getStartTimeWorking()), Long.parseLong(globalValue.getCurrentOrder().getEndTimeWorking())));
            } else {
                txtTask.setText("0 minute");
            }
            lblPhone.setText(globalValue.getCurrentOrder()
                    .getDriver_phone(true));
            lblName.setText(globalValue.getCurrentOrder()
                    .getDriverName());
//            lblCarPlate.setText(globalValue.getCurrentOrder().getCarPlate());
            lblStartLocation.setText(globalValue
                    .getCurrentOrder().getStartLocation());
            lblEndlocation.setText(globalValue
                    .getCurrentOrder().getEndLocation());
            if (globalValue.getCurrentOrder()
                    .getDriverRate().isEmpty()) {
                ratingBarUser.setRating(0);
            } else {
                ratingBarUser.setRating(Float
                        .parseFloat(globalValue
                                .getCurrentOrder()
                                .getDriverRate()) / 2);
            }
//            aq.id(imgCar).image(
//                    globalValue.getCurrentOrder()
//                            .getCarImage());
            Log.e("eeeeeeeeee", "image: " + globalValue.getCurrentOrder()
                    .getCarImage());
            aq.id(imgPassenger).image(
                    globalValue.getCurrentOrder()
                            .getImageDriver());
        }
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

    private void initView() {
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnSend = (TextView) findViewById(R.id.btnSend);
        lblPoint = (TextView) findViewById(R.id.lblPoint);
        btnRate = (TextView) findViewById(R.id.btnRate);
        tvStep = (TextViewRaleway) findViewById(R.id.tv_step);
        //lblDrvierId = (TextViewRaleway) findViewById(R.id.lblDrvierId);
        lblPhone = (TextViewRaleway) findViewById(R.id.lblPhone);
        ratingBarUser = (RatingBar) findViewById(R.id.ratingBar_user);
        lblStartLocation = (TextViewRaleway) findViewById(R.id.lblStartLocation);
        lblEndlocation = (TextViewRaleway) findViewById(R.id.lblEndlocation);
        txtIdentity = (TextViewRaleway) findViewById(R.id.txtIdentity);
        txtCarPlate = (TextViewRaleway) findViewById(R.id.txtCarPlate);

        tvSeat = (TextViewRaleway) findViewById(R.id.tvSeat);
        txtTask = (TextViewRaleway) findViewById(R.id.txtTask);
        txtTrip = (TextViewRaleway) findViewById(R.id.txtTrip);
        lblName = (TextViewRaleway) findViewById(R.id.lblName);
//        lblCarPlate = (TextViewRaleway) findViewById(R.id.lblCarPlate);

        stepNext = (TextViewFontAwesome) findViewById(R.id.step_full);
        stepCircle = (TextViewFontAwesome) findViewById(R.id.step_circle);
        txtStar = (TextView) findViewById(R.id.txtStar);

        loPayment = (LinearLayout) findViewById(R.id.lo_payment);
//		loRate = (LinearLayout) findViewById(R.id.lo_rate);

        imgPassenger = (ImageView) findViewById(R.id.imgPassenger);
//        imgCar = (ImageView) findViewById(R.id.imgCar);
        llHelp = (LinearLayout) findViewById(R.id.llHelp);
        lblNorify = (TextViewRaleway) findViewById(R.id.lblNorify);
    }

    private void initControl() {
//        btnSend.setEnabled(false);
        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                btnSend.setEnabled(false);
                if (!checkPayment)
                    showDialogPayment();
                else
                    showToastMessage(getResources().getString(R.string.payment_already));

                /*if (!checkRate) {
                    btnSend.setEnabled(false);
                    showDialogPayment();
                } else {
                    Toast.makeText(RateDriverActivity.this, "You must rate before payment", Toast.LENGTH_SHORT).show();
                }*/

            }
        });
        btnRate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalValue.getCurrentOrder().getDriverRateTrip() != null && !globalValue.getCurrentOrder().getDriverRateTrip().equals("")) {
                    Toast.makeText(self, getResources().getString(R.string.already_rated), Toast.LENGTH_SHORT).show();
                    checkRate = false;
                } else {
                    if (checkPayment) {
                        rate();
                    } else {
                        Toast.makeText(RateDriverActivity.this, getResources().getString(R.string.made_payment_before), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        llHelp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.checkNetworkAvailable(RateDriverActivity.this)) {
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

    public void showDialogPayment() {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_payment, null, false);
        TextView byCash = (TextView) v.findViewById(R.id.btnPayCash);
        TextView byPaypal = (TextView) v.findViewById(R.id.btnPayPal);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(v)
                .create();

        byPaypal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                payment(Constant.PAY_TRIP_BY_BALANCE);
                alertDialog.dismiss();
            }
        });
        byCash.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                payment(Constant.PAY_TRIP_BY_CASH);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void rate() {
        final String rate = ratingBar.getRating() * 2 + "";
        if (Double.parseDouble(rate) != 0) {
            ModelManager.rateDriver(preferencesManager.getToken(), globalValue
                            .getCurrentOrder().getId(), rate, context, true,
                    new ModelManagerListener() {
                        @Override
                        public void onSuccess(String json) {
                            if (ParseJsonUtil.isSuccess(json)) {
                                showToastMessage(getResources().getString(R.string.rating_successfully));
                                preferencesManager.setDriverCurrentScreen("");
                                preferencesManager.setPassengerIsInTrip(false);
                                globalValue.getCurrentOrder().setDriverRateTrip(rate);
                                checkRate = false;
                                gotoActivity(MainActivity.class);
                                finish();
                                if (preferencesManager.IsStartWithOutMain()) {
//								gotoActivity(MainActivity.class);
//								finish();
                                } else {
//								finish();
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
        } else {
            Toast.makeText(self, getString(R.string.lblValidateRate), Toast.LENGTH_SHORT).show();
        }

    }

    private void showRate(boolean isRate) {
        if (isRate) {
//			loRate.setVisibility(View.VISIBLE);
            loPayment.setVisibility(View.GONE);
            stepNext.setVisibility(View.VISIBLE);
            stepCircle.setVisibility(View.GONE);
            tvStep.setText(getString(R.string.lbl_request_by_finished));
            lblNorify.setText(getString(R.string.lbl_notify_pasenger_finish));
        } else {
//			loRate.setVisibility(View.GONE);
            loPayment.setVisibility(View.VISIBLE);
            stepNext.setVisibility(View.GONE);
            stepCircle.setVisibility(View.VISIBLE);
            tvStep.setText(getString(R.string.lbl_request_by_arrived1));
            lblNorify.setText(getString(R.string.lbl_notify_pasenger_arriving));

        }

    }

    private void setPayment(String point, String paymentId, final String paymentMethod) {
        ModelManager.payment(preferencesManager.getToken(), point, paymentId,
                paymentMethod, self, true, new ModelManagerListener() {

                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            payment(Constant.PAY_TRIP_BY_BALANCE);
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

    private void payment(String payBy) {
        ModelManager.tripPayment(preferencesManager.getToken(),
                preferencesManager.getCurrentOrderId(), payBy, this, true,
                new ModelManagerListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onSuccess(String json) {

                        if (ParseJsonUtil.isSuccess(json)) {
//							showRate(true);
                            showToastMessage(getResources().getString(R.string.payment_successfully));
                            preferencesManager.setPassengerHaveDonePayment(true);
                            preferencesManager.setPassengerCurrentScreen("");
                            checkPayment = true;
//                            finishAffinity();
//                            gotoActivity(MainActivity.class);
                        } else {
                            if (ParseJsonUtil.getMessage(json).endsWith("Your balance is short")) {
                                String missingFare = "";
                                missingFare = ParseJsonUtil.getMissingFare(json);
                                buyPoint(missingFare);
                                point = missingFare;
                            } else {
                                showToastMessage(ParseJsonUtil.getMessage(json));
                            }
                        }
//                        btnSend.setEnabled(true);
                    }

                    @Override
                    public void onError() {
                        showToastMessage(R.string.message_have_some_error);
                    }
                });
    }

    private void initLocalBroadcastManager() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_DRIVER_CONFIRM_PAID_STATUS_CONFIRM);
        intentFilter.addAction(Constant.ACTION_DRIVER_CONFIRM_PAID_STATUS_CANCEL);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, intentFilter);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(Constant.KEY_ACTION);
            if (action.equals(Constant.ACTION_DRIVER_CONFIRM_PAID_STATUS_CONFIRM)) {
                showToastMessage(getResources().getString(R.string.payment_successfully));
                preferencesManager.setPassengerHaveDonePayment(true);
                preferencesManager.setPassengerCurrentScreen("");
                checkPayment = true;
//                gotoActivity(MainActivity.class);
//                finish();
            }
        }
    };

    private void buyPoint(final String point) {

//        new AlertDialog.Builder(this)
//                .setTitle(R.string.app_name)
//                .setMessage(title)
//                .setPositiveButton(android.R.string.ok,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                requestPaypalPayment(Double.parseDouble(point),
//                                        "GET POINT",
//                                        getString(R.string.currency));
//                            }
//                        }).create().show();

        final MaterialDialog dialog = new MaterialDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_payment_method, null);
        dialog.setContentView(view);
        String title = getResources().getString(
                R.string.message_your_balance_is_not_eounght)
                + " "
                + point
                + " "
                + getResources().getString(R.string.message_point);
        dialog.setTitle(title);
        TextViewRaleway txtPaypal = (TextViewRaleway) view.findViewById(R.id.txtPaypal);
        TextViewRaleway txtStripe = (TextViewRaleway) view.findViewById(R.id.txtStripe);
        txtPaypal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestPaypalPayment(Double.parseDouble(point),
                        "GET POINT",
                        getString(R.string.currency));
            }
        });
        txtStripe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(RateDriverActivity.this, PaymentStripeFinishActivity.class);
                intent.putExtra("amount", point + "");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data
                    .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample",
                            "Payment OK. Response :" + confirm.toJSONObject());
                    JSONObject json = confirm.toJSONObject();

                    // if (ParseJsonUtil.paymentIsPaypal(json)) {
                    setPayment(point,
                            ParseJsonUtil.getTransactionFromPaypal(json),
                            Constant.PAYMENT_BY_PAYPAL);
                    /*
                     * } else { setPayment(point,
					 * ParseJsonUtil.getTransactionFromCart(json),
					 * Constant.PAYMENT_BY_CART); }
					 */

                } catch (Exception e) {
                    Log.e("paymentExample",
                            "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample",
                    "An invalid payment was submitted. Please see the docs.");
        }
    }


}
