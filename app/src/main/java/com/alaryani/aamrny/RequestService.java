package com.alaryani.aamrny;

/**
 * Created by Administrator on 2/23/2017.
 */

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alaryani.aamrny.activities.MainActivity;
import com.alaryani.aamrny.config.Constant;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.service.GPSTracker;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class RequestService extends Service {
    public static String ACTION = "SEND_LOCATION_FROM_SERVICE";
    GPSTracker gpsTracker;
    int dem = 0;
    int timeLoop = 0;
    int number = 0;
    boolean checkLoadData = true;
    boolean checkDataFirst = true;
    boolean checkRun = true;
    int count = 0;

    PreferencesManager preferencesManager;

    Handler handler;

    Runnable runnable;
    CountDownTimer countDownTimerFirst;
    CountDownTimer countDownTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        preferencesManager = PreferencesManager.getInstance(getApplicationContext());
        // TODO Auto-generated method stub
        ModelManager.getGeneralSettings(PreferencesManager.getInstance(getApplicationContext())
                        .getToken(),
                this, false, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        number = Integer.parseInt(ParseJsonUtil.getMaxTimeSendRequest(json));
                        timeLoop = Integer.parseInt(ParseJsonUtil.getTimeSendRequestAgain(json));
                        Log.e("number", "number:" + number);
                        Log.e("number", "timeLoop:" + timeLoop);
                        if (ParseJsonUtil.isSuccess(json)) {
                            if (handler != null && runnable != null) {
                                handler.postDelayed(runnable, 1000);
                            } else {
                                handler = new Handler();
                                runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        if (dem < number - 1) {
                                            if (checkDataFirst) {
                                                checkDataFirst = false;
                                                countDownTimerFirst = new CountDownTimer(timeLoop * 1000, 1000) {
                                                    @Override
                                                    public void onTick(long l) {
                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        createRequest();
                                                    }
                                                };
                                                countDownTimerFirst.start();

                                            }
                                            if (!checkLoadData) {
                                                checkLoadData = true;
                                                createRequest();
                                            }
                                        } else {
                                            if (checkRun) {
                                                if (dem == 0) {
                                                    checkRun = false;
                                                    countDownTimer = new CountDownTimer(timeLoop * 1000, 1000) {
                                                        @Override
                                                        public void onTick(long l) {
                                                        }

                                                        @Override
                                                        public void onFinish() {
                                                            cancelRequestByPassenger();
                                                            stopSelf();
                                                        }
                                                    };
                                                    countDownTimer.start();
                                                } else {
                                                    checkRun = false;
                                                    dem = 0;
                                                    cancelRequestByPassenger();
                                                    stopSelf();
                                                }
                                            }


                                        }
                                        handler.postDelayed(runnable, 1000);
                                    }

                                };
                                handler.postDelayed(runnable, 1000);


                            }
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });

        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        handler.removeCallbacks(runnable);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (countDownTimerFirst != null) {
            countDownTimerFirst.cancel();
        }
        this.stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (countDownTimerFirst != null) {
            countDownTimerFirst.cancel();
        }
        this.stopSelf();
//        ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
//        Future longRunningTaskFuture = threadPoolExecutor.submit(runnable);
//        longRunningTaskFuture.cancel(true);
        Log.e("destroy", "destroy");
    }

    private void createRequest() {
//        if (preferencesManager.getStringValue(Constant.KEY_STARTLOCATION_LATITUDE) != null && !preferencesManager.getStringValue(Constant.KEY_STARTLOCATION_LATITUDE).equals("")
//                && preferencesManager.getStringValue(Constant.KEY_ENDLOCATION_LATITUDE) != null && !preferencesManager.getStringValue(Constant.KEY_ENDLOCATION_LATITUDE).equals("")
//                && preferencesManager.getStringValue(Constant.KEY_ENDLOCATION_LONGITUDE) != null && !preferencesManager.getStringValue(Constant.KEY_ENDLOCATION_LONGITUDE).equals("")
//                && preferencesManager.getStringValue(Constant.KEY_STARTLOCATION_LONGITUDE) != null && !preferencesManager.getStringValue(Constant.KEY_STARTLOCATION_LONGITUDE).equals("")
//                && preferencesManager.getStringValue(Constant.KEY_ADDRESS_START) != null && !preferencesManager.getStringValue(Constant.KEY_ADDRESS_START).equals("")
//                && preferencesManager.getStringValue(Constant.KEY_ADDRESS_TO) != null && !preferencesManager.getStringValue(Constant.KEY_ADDRESS_TO).equals("")
//                && preferencesManager.getStringValue(Constant.KEY_LINK) != null && !preferencesManager.getStringValue(Constant.KEY_LINK).equals("")
//                && preferencesManager.getStringValue(Constant.KEY_ESTIMATE_DISTANCE) != null && !preferencesManager.getStringValue(Constant.KEY_ESTIMATE_DISTANCE).equals("")
//                ) {
        ModelManager.createRequest(PreferencesManager.getInstance(getApplicationContext())
                        .getToken(), preferencesManager.getStringValue(Constant.KEY_LINK), preferencesManager.getStringValue(Constant.KEY_STARTLOCATION_LATITUDE),
                preferencesManager.getStringValue(Constant.KEY_STARTLOCATION_LONGITUDE), preferencesManager.getStringValue(Constant.KEY_ADDRESS_START),
                preferencesManager.getStringValue(Constant.KEY_ENDLOCATION_LATITUDE), preferencesManager.getStringValue(Constant.KEY_ENDLOCATION_LONGITUDE),
                preferencesManager.getStringValue(Constant.KEY_ADDRESS_TO), preferencesManager.getStringValue(Constant.KEY_ESTIMATE_DISTANCE),this, false,
                preferencesManager.getStringValue(Constant.KEY_PROMO_CODE), new ModelManagerListener() {

                    @Override
                    public void onSuccess(String json) {
                        Log.e("jsonCreate", "jsonCreate:" + json);
                        if (preferencesManager.getStringValue("countDriver") != null && !preferencesManager.getStringValue("countDriver").equals("")) {
                            int dem = Integer.parseInt(preferencesManager.getStringValue("countDriver")) + Integer.parseInt(ParseJsonUtil.getCountDriver(json));
                            preferencesManager.putStringValue("countDriver", dem + "");
                        } else {
                            preferencesManager.putStringValue("countDriver", ParseJsonUtil.getCountDriver(json));
                        }
                        sendBroadcast(new Intent("com.alaryani.aamrny.COUNTDRIVER"));
                        final Timer T = new Timer();
                        T.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                count++;
                                if (count == timeLoop) {
                                    dem++;
                                    count = 0;
                                    checkLoadData = false;
                                    Log.e("dem", "dem:" + dem);
                                    T.cancel();
                                }
                            }
                        }, 1000, 1000);

//                        }
                    }

                    @Override
                    public void onError() {
                        count = 0;
                        checkLoadData = false;
                        Log.e("chay vao fail", "chay vao failt");
                    }
                });
//        }

    }

    private void cancelRequestByPassenger() {
        ModelManager.cancelRequestByPassenger(
                PreferencesManager.getInstance(getApplicationContext()).getToken(), this,
                false, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            Log.e("TAG", "cancel request success");
                            preferencesManager.putStringValue("checkCancel", "1");
                            if (preferencesManager.appIsShow()) {
                                sendBroadcast(new Intent("com.alaryani.aamrny.FINISH"));
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                        } else {
                            Log.e("TAG", "cancel request false");
                        }
                    }

                    @Override
                    public void onError() {
                        Log.e("TAG", "error");
                    }
                });
    }

    protected Boolean isActivityRunning(Class activityClass) {
        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }

        return false;
    }
}

