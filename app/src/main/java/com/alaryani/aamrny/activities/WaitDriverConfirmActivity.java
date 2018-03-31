package com.alaryani.aamrny.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.RequestService;
import com.alaryani.aamrny.config.Constant;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.object.CurrentOrder;
import com.alaryani.aamrny.object.SettingObj;
import com.alaryani.aamrny.widget.TextViewRaleway;

public class WaitDriverConfirmActivity extends BaseActivity implements
        OnClickListener {

    private final int SPLASH_DISPLAY_LENGHT = 60000;
    Button btnCancelTrip;
    private TextViewRaleway txtEstimateFare, txtCountDriver;

    // private Handler myHandler;
    // private Runnable myRunnable = new Runnable() {
    // @Override
    // public void run() {
    // cancelRequestByPassenger();
    // }
    // };

    @Override
    public void onResume() {
        if (preferencesManager.getPassengerCurrentScreen().equals("ConfirmActivity")) {
            gotoActivity(ConfirmActivity.class);
            finish();
        } else if (preferencesManager.getPassengerCurrentScreen().equals(
                "StartTripForPassengerActivity")) {
            gotoActivity(StartTripForPassengerActivity.class);
            finish();
        } else if (preferencesManager.getPassengerCurrentScreen().equals(
                "RateDriverActivity")) {
            gotoActivity(RateDriverActivity.class);
            finish();
        } else {
            if (preferencesManager.getStringValue("checkCancel") != null && preferencesManager.getStringValue("checkCancel").equals("1")) {
                preferencesManager.putStringValue("checkCancel", "");
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                preferencesManager.setPassengerWaitConfirm(true);
            }

        }
        super.onResume();
    }

    ;

    @Override
    protected void onStart() {
        super.onStart();
        // myHandler = new Handler();
        // myHandler.postDelayed(myRunnable, SPLASH_DISPLAY_LENGHT);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // myHandler.removeCallbacks(myRunnable);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_driver_confirm);
        preferencesManager.putStringValue("checkCancel", "");
        initWithoutHeader();
        initView();
        initControl();
        initLocalBroadcastManager();
        registerReceiver(finishActivity, new IntentFilter("com.alaryani.aamrny.FINISH"));
        registerReceiver(countDriver, new IntentFilter("com.alaryani.aamrny.COUNTDRIVER"));
        if (globalValue.getInstance().getEstimate_fare() != null) {
//            Double toBeTruncated = new Double(globalValue.getInstance().getEstimate_fare());
//            Double truncatedDouble = new BigDecimal(toBeTruncated).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
//            txtEstimateFare.setText(getString(R.string.pointEstimated) + truncatedDouble + " KM");
            txtEstimateFare.setText(" " + globalValue.getInstance().getEstimate_fare());
        } else {
            if (preferencesManager.getInstance(WaitDriverConfirmActivity.this).getStringValue("estimate") != null && !preferencesManager.getInstance(WaitDriverConfirmActivity.this).getStringValue("estimate").equals("")) {
//                Double toBeTruncated = new Double(preferencesManager.getInstance(WaitDriverConfirmActivity.this).getStringValue("estimate"));
//                Double truncatedDouble = new BigDecimal(toBeTruncated).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
//                txtEstimateFare.setText(getString(R.string.pointEstimated) + truncatedDouble + " KM");
                txtEstimateFare.setText(" " + preferencesManager.getInstance(WaitDriverConfirmActivity.this).getStringValue("estimate"));
            } else {
                txtEstimateFare.setText(" " + globalValue.getInstance().getEstimate_fare());
            }

        }
        String msg = getString(R.string.msgCountDriver);
        SettingObj settingObj = preferencesManager.getDataSettings();
        msg = msg.replace("[a]", preferencesManager.getStringValue("countDriver"));
        msg = msg.replace("[b]", settingObj.getTime_to_send_request_again());
        txtCountDriver.setText(msg);

    }

    private void initView() {
        btnCancelTrip = (Button) findViewById(R.id.btnCancelTrip);
        txtEstimateFare = (TextViewRaleway) findViewById(R.id.txtEstimateFare);
        txtCountDriver = (TextViewRaleway) findViewById(R.id.txtCountDriver);
    }

    private void initControl() {
        btnCancelTrip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuitDialog();
            }
        });
    }

    private void initLocalBroadcastManager() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver,
                new IntentFilter(Constant.ACTION_DRIVER_CONFIRM));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String json = intent.getStringExtra(Constant.KEY_DATA);
            String trip_status = intent.getStringExtra("trip_status");
            if (trip_status.equals(Constant.TRIP_STATUS_IN_PROGRESS)) {
                getTripDetailArrivingB();
            } else {
                CurrentOrder currentOrder = new CurrentOrder();
                currentOrder.setId(ParseJsonUtil.parseOrderIdFromDriverConfirm(json));
                globalValue.setCurrentOrder(currentOrder);
                getTripDetail();
            }

        }
    };

    private void getTripDetailArrivingB() {
        ModelManager.showTripDetail(preferencesManager.getToken(), globalValue
                        .getCurrentOrder().getId(), context, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            try {
                                stopService(new Intent(WaitDriverConfirmActivity.this, RequestService.class));
                            } catch (Exception e) {
                                Log.e("exception", "exption:" + e.getMessage());
                            }
                            globalValue.setCurrentOrder(ParseJsonUtil
                                    .parseCurrentOrder(json));
                            gotoActivity(StartTripForPassengerActivity.class);
                            finish();
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

    private void getTripDetail() {
        ModelManager.showTripDetail(preferencesManager.getToken(), globalValue
                        .getCurrentOrder().getId(), context, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            try {
                                stopService(new Intent(WaitDriverConfirmActivity.this, RequestService.class));
                            } catch (Exception e) {
                                Log.e("exception", "exption:" + e.getMessage());
                            }
                            globalValue.setCurrentOrder(ParseJsonUtil
                                    .parseCurrentOrder(json));
                            gotoActivity(ConfirmActivity.class);
                            finish();
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        unregisterReceiver(finishActivity);
        unregisterReceiver(countDriver);
        super.onDestroy();
        preferencesManager.putStringValue("checkCancel", "");
    }

    private void showQuitDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.msg_do_you_cancel)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                cancelRequestByPassenger();
                            }
                        }).setNegativeButton(android.R.string.cancel, null)
                .create().show();
    }

    private final BroadcastReceiver finishActivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };
    private final BroadcastReceiver countDriver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = getString(R.string.msgCountDriver);
            SettingObj settingObj = preferencesManager.getDataSettings();
            msg = msg.replace("[a]", preferencesManager.getStringValue("countDriver"));
            msg = msg.replace("[b]", settingObj.getTime_to_send_request_again());
            txtCountDriver.setText(msg);
        }
    };

    private void cancelRequestByPassenger() {
        try {
            stopService(new Intent(WaitDriverConfirmActivity.this, RequestService.class));
        } catch (Exception e) {
            Log.e("exception", "exption:" + e.getMessage());
        }
        ModelManager.cancelRequestByPassenger(
                PreferencesManager.getInstance(context).getToken(), context,
                true, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            if (getPreviousActivityName().equals(
                                    MainActivity.class.getSimpleName())) {
                                finish();
                                overridePendingTransition(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_right);
                            } else {
                                gotoActivity(MainActivity.class);
                                finish();
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

    @Override
    public void onBackPressed() {
        showQuitDialog();
    }

    @Override
    public void onClick(View v) {

    }
}
