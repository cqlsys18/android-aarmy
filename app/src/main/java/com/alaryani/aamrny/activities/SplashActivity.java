package com.alaryani.aamrny.activities;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.util.SparseIntArray;

import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.ServiceUpdateLocation;
import com.alaryani.aamrny.config.Constant;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.gcm.RegistrationIntentService;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.object.CarType;
import com.alaryani.aamrny.object.SettingObj;
import com.alaryani.aamrny.service.GPSTracker;
import com.alaryani.aamrny.utility.AppUtil;
import com.alaryani.aamrny.utility.NetworkUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;

public class SplashActivity extends BaseActivity {

    private final int SPLASH_DISPLAY_LENGHT = 1000;
    Locale myLocale;
    private GPSTracker gps;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        getAppKeyHash();

//        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this,
//                SplashActivity.class));
//        Intent intent = new Intent();
//        intent.setAction("com.htcp.taxinear.ACTION_REQUEST");
//        sendBroadcast(intent);

        if (preferencesManager.driverIsOnline()) {
            Intent intent1 = new Intent(this, ServiceUpdateLocation.class);
            startService(intent1);
        }
        // Registering BroadcastReceiver
        if (AppUtil.checkPlayServices(this)) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        NetworkUtil.enableStrictMode();
//        getLanguage();
    }

    public void getLanguage() {
        if (PreferencesManager.getInstance(context).isChinase()) {
            myLocale = new Locale("zh");
            PreferencesManager.getInstance(context).setIsChinese();
        } else {
            myLocale = new Locale("en");
            PreferencesManager.getInstance(context).setIsEnglish();
        }
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onResume() {
        super.onResume();
        /* CLEAR PUSH NOTIFICATION */
        NotificationManager notifManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
        /*Check network & GPS*/
        gps = new GPSTracker(this);
        checkBaseCondition();
    }

    private void checkBaseCondition() {
        if (NetworkUtil.checkNetworkAvailable(this)) {

            if (!gps.canGetLocation()) {
                gps.showSettingsAlert();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        processNextScreen();
                    }
                }, SPLASH_DISPLAY_LENGHT);
            }
        } else {
            showWifiSetting(this);
        }
    }

    public void showWifiSetting(final Context act) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(act);

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.wifi_setting));

        // Setting Dialog Message
        alertDialog
                .setMessage(getResources().getString(R.string.wifi_disable));

        // On pressing Settings button
        alertDialog.setPositiveButton(getResources().getString(R.string.setting),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_SETTINGS);
                        act.startActivity(intent);
                        dialog.dismiss();
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }


    private void processNextScreen() {
        // Check user login or not
        if ((preferencesManager.isAlreadyLogin())) {
            firstLogin();
        } else {
            gotoActivity(LoginActivity.class);
            finish();
        }
    }

    // Start code check for user
    public void firstLogin() {
        generalSettings();
    }

    public void generalSettings() {
        ModelManager.getGeneralSettings(preferencesManager.getToken(),
                self, true, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            SettingObj settingObj = ParseJsonUtil.getGeneralSettings(json);
                            ArrayList<CarType> listCarTypes = new ArrayList<CarType>();
                            listCarTypes.addAll(ParseJsonUtil.parseListCarTypes(json));
                            GlobalValue.getInstance().setListCarTypes(listCarTypes);
                            preferencesManager.setDataSetting(settingObj);
                            ModelManager.showInfoProfile(PreferencesManager.getInstance(self)
                                    .getToken(), self, true, new ModelManagerListener() {
                                @Override
                                public void onSuccess(String json) {
                                    Log.e("data", "data image:" + ParseJsonUtil.parseInfoProfile(json).getLinkImage());
                                    globalValue.setUser(ParseJsonUtil.parseInfoProfile(json));
                                    if (globalValue.getUser() != null && globalValue.getUser().getIsActive() != null && globalValue.getUser().getIsActive().equals("1")) {
                                        if (ParseJsonUtil.isSuccess(json)) {
                                            if (ParseJsonUtil.isDriverFromSplash(json)) {
                                                preferencesManager.setIsDriver();
                                                // If is driver check active or not
                                                if (ParseJsonUtil.driverIsActiveFromSplash(json)) {
                                                    preferencesManager.setDriverIsActive();
                                                } else {
                                                    preferencesManager.setDriverIsUnActive();
                                                }
                                            } else {
                                                preferencesManager.setIsUser();
                                            }

                                            // Check is normal user or driver user
                                            if (preferencesManager.isDriver()) {
                                                checkDriverOnline();
                                            } else {
                                                checkUserWithOutApi();
                                            }
                                        } else {
                                            PreferencesManager.getInstance(context).setLogout();
                                            gotoActivity(LoginActivity.class);
                                            finish();
                                        }
                                    } else {
                                        showToastMessage(getString(R.string.msgAccountInActive));
                                        PreferencesManager.getInstance(context).setLogout();
                                        gotoActivity(LoginActivity.class);
                                        finish();
                                    }

                                }

                                @Override
                                public void onError() {
                                    PreferencesManager.getInstance(context).setLogout();
                                    gotoActivity(LoginActivity.class);
                                    finish();
                                }
                            });
                        } else {
                            PreferencesManager.getInstance(context).setLogout();
                            gotoActivity(LoginActivity.class);
                            finish();
                        }
                    }

                    @Override
                    public void onError() {
                        PreferencesManager.getInstance(context).setLogout();
                        gotoActivity(LoginActivity.class);
                        finish();
                    }
                });
    }

    // Start code check for user
    public void checkUserWithOutApi() {
        Log.e("token check", "token check:" + preferencesManager.getToken());
        ModelManager.showMyRequestForUser(preferencesManager.getToken(), self,
                true, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        Log.e("json data", "json data checkUserWithOutApi:" + json);
                        if (ParseJsonUtil.isSuccess(json)) {
                            if (ParseJsonUtil.parseMyRequest(json).size() > 0) {
                                preferencesManager.setStartWithOutMain(true);
                                gotoActivity(WaitDriverConfirmActivity.class);
                                finish();
                            } else {
                                checkUserInTrip();
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

    // Start code check for Driver
    public void checkDriverOnline() {
        if (globalValue.getUser().getDriverObj().getIsOnline()
                .equals(Constant.STATUS_IDLE)) {
            if (globalValue.getUser().getDriverObj().getStatus()
                    .equals(Constant.STATUS_IDLE)) {
                ModelManager.showTripHistory(PreferencesManager.getInstance(self)
                        .getToken(), "1", self, true, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            String status = ParseJsonUtil.parseTripStatus(json);
                            String passengerRate = ParseJsonUtil.parseTripPassengerRate(json);
                            String isWaitDriverConfirm = ParseJsonUtil.pareWaitDriverConfirm(json);
                            if (isWaitDriverConfirm.equals(Constant.TRIP_WAIT_DRIVER_NOTCONFIRM)) {
                                gotoActivity(Ac_ConfirmPayByCash.class);
                            } else {
                                if (globalValue.getUser().getDriverObj().getStatus()
                                        .equals(Constant.STATUS_IDLE)) {
                                    countMyRequest();
                                } else {
                                    switch (status) {
                                        case Constant.TRIP_STATUS_PENDING_PAYMENT:
                                            Log.e("driverRate", "driverRate:" + passengerRate);
                                            if (passengerRate != null && !passengerRate.equals("")) {
                                                countMyRequest();
                                            } else {
                                                preferencesManager.setStartWithOutMain(true);
                                                gotoActivity(RatingPassengerActivity.class);
                                                finish();
                                            }
                                            break;
                                        case Constant.TRIP_STATUS_FINISH:
                                            Log.e("driverRate", "driverRate:" + passengerRate);
                                            if (passengerRate != null && !passengerRate.equals("")) {
                                                countMyRequest();
                                            } else {
                                                preferencesManager.setStartWithOutMain(true);
                                                gotoActivity(RatingPassengerActivity.class);
                                                finish();
                                            }
                                            break;
                                        default:
                                            countMyRequest();
                                            break;
                                    }
                                }
                            }
                        } else {
                            showToastMessage(ParseJsonUtil.getMessage(json));
                        }
                    }

                    @Override
                    public void onError() {
                        countMyRequest();
                    }
                });
            } else {
                if (globalValue.getUser().getDriverObj().getStatus()
                        .equals(Constant.STATUS_BUSY)) {
                    Log.e("driverRate", "driverRate busy");
                    checkDriverInTrip();
                }
            }
        } else

        {
//            if (preferencesManager.isDriver()) {
//                checkDriverInTrip();
//            } else {
            checkUserWithOutApi();
//            }

        }

    }

    private void checkDriverInTrip() {
        ModelManager.showTripHistory(PreferencesManager.getInstance(self)
                .getToken(), "1", self, true, new ModelManagerListener() {
            @Override
            public void onSuccess(String json) {
                Log.e("json data", "json data in trip:" + json);
                if (ParseJsonUtil.isSuccess(json)) {
                    String status = ParseJsonUtil.parseTripStatus(json);
                    String passengerRate = ParseJsonUtil.parseTripPassengerRate(json);
                    preferencesManager.setCurrentOrderId(ParseJsonUtil
                            .parseTripId(json));
                    switch (status) {
                        case Constant.TRIP_STATUS_APPROACHING:
                            preferencesManager.setStartWithOutMain(true);
                            gotoActivity(ShowPassengerActivity.class);
                            finish();
                            break;
                        case Constant.TRIP_STATUS_IN_PROGRESS:
                            preferencesManager.setDriverStartTrip(true);
                            preferencesManager.setStartWithOutMain(true);
                            gotoActivity(StartTripForDriverActivity.class);
                            finish();
                            break;
                        case Constant.STATUS_ARRIVED_A:
                            preferencesManager.setStartWithOutMain(true);
                            gotoActivity(StartTripForDriverActivity.class);
                            finish();
                            break;
                        case Constant.STATUS_ARRIVED_B:
                            preferencesManager.setStartWithOutMain(true);
                            gotoActivity(StartTripForDriverActivity.class);
                            finish();
                            break;
                        case Constant.STATUS_START_TASK:
                            preferencesManager.setStartWithOutMain(true);
                            gotoActivity(StartTripForDriverActivity.class);
                            finish();
                            break;
                        case Constant.TRIP_STATUS_PENDING_PAYMENT:
                            Log.e("passengerRate", "passengerRate:" + passengerRate);
                            if (passengerRate != null && !passengerRate.equals("")) {
                                if (preferencesManager.driverIsOnline()) {
                                    gotoActivity(OnlineActivity.class);
                                    finish();
                                } else {
                                    gotoActivity(MainActivity.class);
                                    finish();
                                }

                            } else {
                                preferencesManager.setStartWithOutMain(true);
                                gotoActivity(RatingPassengerActivity.class);
                                finish();
                            }
                            break;
                        case Constant.TRIP_STATUS_FINISH:
                            Log.e("passengerRate", "passengerRate finish:" + passengerRate);
                            if (passengerRate != null && !passengerRate.equals("")) {
                                if (preferencesManager.driverIsOnline()) {
                                    gotoActivity(OnlineActivity.class);
                                    finish();
                                } else {
                                    gotoActivity(MainActivity.class);
                                    finish();
                                }
                            } else {
                                preferencesManager.setStartWithOutMain(true);
                                gotoActivity(RatingPassengerActivity.class);
                                finish();
                            }
                            break;
                        default:
                            if (globalValue.getUser() != null && globalValue.getUser().getPhone() != null && !globalValue.getUser().getPhone().equals("")) {
                                gotoActivity(MainActivity.class);
                                finish();
                            } else {
                                gotoActivity(EditProfileFirstActivity.class);
                                finish();
                            }
                            break;
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

    private void checkUserInTrip() {
        ModelManager.showTripHistory(PreferencesManager.getInstance(self)
                .getToken(), "1", self, true, new ModelManagerListener() {
            @Override
            public void onSuccess(String json) {

                if (ParseJsonUtil.isSuccess(json)) {
                    String status = ParseJsonUtil.parseTripStatus(json);
                    String driverRate = ParseJsonUtil.parseTripDriverRate(json);
                    Log.e("status", "status:" + status);
                    String isWaitDriverConfirm = ParseJsonUtil.pareWaitDriverConfirm(json);
                    preferencesManager.setCurrentOrderId(ParseJsonUtil
                            .parseTripId(json));
                    if (isWaitDriverConfirm.equals(Constant.TRIP_WAIT_DRIVER_NOTCONFIRM)) {
                        preferencesManager.setPassengerCurrentScreen("");
                        gotoActivity(MainActivity.class);
                        finish();
                    } else {
                        switch (status) {
                            case Constant.TRIP_STATUS_APPROACHING:
                                preferencesManager.setStartWithOutMain(true);
                                gotoActivity(ConfirmActivity.class);
                                finish();
                                break;
                            case Constant.STATUS_ARRIVED_A:
                                preferencesManager.setStartWithOutMain(true);
                                Log.e("arrived", "arrived a");
                                preferencesManager.getPassengerCurrentScreen().equals(
                                        "ConfirmActivity");
                                gotoActivity(ConfirmActivity.class);
                                finish();
                                break;
                            case Constant.TRIP_STATUS_IN_PROGRESS:
                                preferencesManager.setStartWithOutMain(true);
                                Log.e("status", "status 2");
                                gotoActivity(StartTripForPassengerActivity.class);
                                finish();
                                break;
                            case Constant.STATUS_ARRIVED_B:
                                preferencesManager.setStartWithOutMain(true);
                                gotoActivity(StartTripForPassengerActivity.class);
                                finish();
                                break;
                            case Constant.STATUS_START_TASK:
                                preferencesManager.setStartWithOutMain(true);
                                gotoActivity(StartTripForPassengerActivity.class);
                                finish();
                                break;
                            case Constant.TRIP_STATUS_PENDING_PAYMENT:
                                Log.e("driverRate", "driverRate:" + driverRate);
                                if (driverRate != null && !driverRate.equals("")) {
                                    preferencesManager.setPassengerCurrentScreen("");
                                    gotoActivity(MainActivity.class);
                                    finish();
                                } else {
                                    preferencesManager.setStartWithOutMain(true);
                                    gotoActivity(RateDriverActivity.class);
                                    finish();
                                }
                                break;
                            default:
                                preferencesManager.setPassengerCurrentScreen("");
                                gotoActivity(MainActivity.class);
                                finish();
                                break;
                        }
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

    private void countMyRequest() {
        ModelManager.showMyRequest(preferencesManager.getToken(), self, true,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            if (ParseJsonUtil.parseMyRequest(json).size() > 0) {
                                preferencesManager.setStartWithOutMain(true);
                                gotoActivity(RequestPassengerActivity.class);
                                finish();
                            } else {
                                preferencesManager.setStartWithOutMain(true);
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

    private void getData() {
        // ModelManager.showTripHistory(PreferencesManager.getInstance(self)
        // .getToken(), "1", self, true, new ModelManagerListener() {
        // @Override
        // public void onSuccess(String json) {
        // if (ParseJsonUtil.isSuccess(json)) {
        // if (!ParseJsonUtil.parseTripHistory(json).isEmpty()) {
        // listTripHistory.addAll(ParseJsonUtil
        // .parseTripHistory(json));
        // historyAdapter.setArrViews(listTripHistory);
        // historyAdapter.notifyDataSetChanged();
        // lsvHistory.onRefreshComplete();
        // currentPage++;
        // } else {
        // lsvHistory.onRefreshComplete();
        // showToast(getResources().getString(
        // R.string.message_have_no_more_data));
        // }
        // } else {
        // showToast(ParseJsonUtil.getMessage(json));
        // lsvHistory.onRefreshComplete();
        // }
        // }
        //
        // @Override
        // public void onError() {
        // showToast(getResources().getString(
        // R.string.message_have_some_error));
        // lsvHistory.onRefreshComplete();
        // }
        // });
    }

    // private void processNextScreen() {
    // /* CHECK STATUS OF THIS USER */
    // if (preferencesManager.isDriver()
    // && preferencesManager.isActiveDriver()
    // && preferencesManager.driverIsOnline()) {
    // if (preferencesManager.driverIsInTrip()) {
    // switch (preferencesManager.getDriverCurrentScreen()) {
    // case "RequestPassengerActivity":
    // preferencesManager.setStartWithOutMain(true);
    // gotoActivity(RequestPassengerActivity.class);
    // break;
    // case "ShowPassengerActivity":
    // preferencesManager.setStartWithOutMain(true);
    // gotoActivity(ShowPassengerActivity.class);
    // break;
    // case "StartTripForDriverActivity":
    // preferencesManager.setStartWithOutMain(true);
    // gotoActivity(StartTripForDriverActivity.class);
    // break;
    // case "RatingPassengerActivity":
    // preferencesManager.setStartWithOutMain(true);
    // gotoActivity(RatingPassengerActivity.class);
    // break;
    // case "OnlineActivity":
    // preferencesManager.setStartWithOutMain(true);
    // gotoActivity(OnlineActivity.class);
    // break;
    // default:
    // gotoActivity(MainActivity.class);
    // break;
    // }
    // } else {
    // if (preferencesManager.driverIsOnline()) {
    // preferencesManager.setStartWithOutMain(true);
    // gotoActivity(OnlineActivity.class);
    // } else {
    // gotoActivity(MainActivity.class);
    // }
    // }
    // } else {
    // if (preferencesManager.isUser()
    // || !preferencesManager.driverIsOnline()) {
    // if (preferencesManager.IsPassengerWaitConfirm()) {
    // preferencesManager.setStartWithOutMain(true);
    // gotoActivity(WaitDriverConfirmActivity.class);
    // } else {
    // if (preferencesManager.passengerIsInTrip()) {
    // switch (preferencesManager.getPassengerCurrentScreen()) {
    // case "ConfirmActivity":
    // preferencesManager.setStartWithOutMain(true);
    // gotoActivity(ConfirmActivity.class);
    // break;
    // case "StartTripForPassengerActivity":
    // preferencesManager.setStartWithOutMain(true);
    // gotoActivity(StartTripForPassengerActivity.class);
    // break;
    // case "RateDriverActivity":
    // preferencesManager.setStartWithOutMain(true);
    // gotoActivity(RateDriverActivity.class);
    // break;
    // default:
    // gotoActivity(MainActivity.class);
    // break;
    // }
    // } else {
    // gotoActivity(MainActivity.class);
    // }
    // }
    // } else {
    // gotoActivity(MainActivity.class);
    // }
    // }
    // }

    private void showDialogEnableNetwork() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.msg_check_net_work)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                Intent intent = new Intent(
                                        Settings.ACTION_WIRELESS_SETTINGS);
                                startActivity(intent);
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                onBackPressed();
                            }
                        }).create().show();
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;

                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key", "HASH KEY : " + something);
            }
        } catch (NameNotFoundException e1) {
            // TODO Auto-generated catch block
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
        } catch (Exception e) {
        }

    }
}
