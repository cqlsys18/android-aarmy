package com.alaryani.aamrny.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.activities.Ac_ConfirmPayByCash;
import com.alaryani.aamrny.activities.SplashActivity;
import com.alaryani.aamrny.config.Constant;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.object.CurrentOrder;

/**
 * {@link IntentService} responsible for handling GCM messages.
 */
public class GCMIntentService extends GcmListenerService {

    private final String TAG = "GCMIntentService";
    public static final String KEY_DATA = "data";
    public static final String KEY_BODY = "body";
    public static final String KEY_ACTION = "action";
    public static final String KEY_STATUS = "KEY_STATUS";
    private static int NOTIFICATION_ID = 0;
    private PreferencesManager preferencesManager = PreferencesManager
            .getInstance(this);

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.e(TAG, "onMessage: " + data);
        processReciverPush(this, data);
    }


    private void processReciverPush(Context context, Bundle data) {
        String action = data.getString(KEY_ACTION);

        String status = "";
        if (null == action) {
            return;
        }
        switch (action) {
            // Done
            case Constant.ACTION_PASSENGER_CREATE_REQUEST:
                //if (preferencesManager.isDriver()) {
                processCreateRequest(context, data);
                //}
                break;
            case Constant.STATUS_ARRIVED_B:
                processTaskerArrivedB(context, data);
                break;
            case Constant.STATUS_START_TASK:
                processTaskerStartTask(context, data);
                break;
            case Constant.STATUS_ARRIVED_A:
                processDriverArrived(context, data);
                break;
            case Constant.ACTION_CANCEL_TRIP:
                processCancelTrip(context, data);
                break;
            case Constant.ACTION_PASSENGER_CANCEL_REQUEST:
                //if (preferencesManager.isDriver()
                //&& preferencesManager.getDriverCurrentScreen().equals(
                //"RequestPassengerActivity")) {
                processCancelRequest(context, data);
                //}
                break;
            case Constant.ACTION_DRIVER_CONFIRM:
                //if (preferencesManager.isUser()
                //|| !preferencesManager.driverIsOnline()) {
                processDriverConfirm(context, data);
                //}
                break;
            case Constant.ACTION_DRIVER_START_TRIP:
                //if (preferencesManager.isUser()
                //|| !preferencesManager.driverIsOnline()) {
                processDriverStartTrip(context, data);
                //}
                break;
            case Constant.ACTION_DRIVER_END_TRIP:
                //if (preferencesManager.isUser()
                // || !preferencesManager.driverIsOnline()) {
                processDriverEndTrip(context, data);
                //}
                break;
            case Constant.ACTION_APROVAL_UPDATE:
                processActionApproval(context, data);
                break;
            case Constant.ACTION_APROVAL_REDEEM:
                processActionApproval(context, data);
                break;
            case Constant.ACTION_APROVAL_TRANFER:
                processActionApproval(context, data);
                break;
            case Constant.ACTION_INFORMATION:
                processInforPromotion(context, data);
                break;
            case Constant.ACTION_DRIVER_APPROVED:
                processInforPromotion(context, data);
                break;
//            case Constant.ACTION_DRIVER_ARRIVED:
//                processDriverArrived(context, data);
//                break;

            case Constant.ACTION_PAY_BY_CASH:
                processActionConfirmPaidByCash(context, data);
                break;

            case Constant.ACTION_DRIVER_CONFIRM_PAID_STATUS_CONFIRM:
                progcessConfirmPaidStatusConfirm(context, data);
                break;
            case Constant.ACTION_DRIVER_CONFIRM_PAID_STATUS_CANCEL:
                progcessConfirmPaidStatusCancel(context, data);
                break;
            case Constant.SEND_MESSAGE:
                Log.e("dataaa","now "+data.getString(KEY_DATA));
                sendMessage(context, data);
                break;

        }
    }

    private void progcessConfirmPaidStatusCancel(Context context, Bundle data) {
        boolean isShow = preferencesManager.appIsShow();
        if (isShow) {
            String msg = data.getString(KEY_BODY);
            showMessage(msg);
            Log.e("push cancel", msg);
        }
    }

    public void showMessage(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void progcessConfirmPaidStatusConfirm(Context context, Bundle data) {
        String msg = data.getString(KEY_BODY);
//        preferencesManager.setPassengerCurrentScreen("RateDriverActivity");
        if (preferencesManager.appIsShow()) {
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            Intent intent = new Intent(Constant.ACTION_DRIVER_CONFIRM_PAID_STATUS_CONFIRM);
            // You can also include some extra data.
            intent.putExtra(Constant.KEY_DATA, data.getString(KEY_DATA));
            intent.putExtra(Constant.KEY_ACTION, data.getString(KEY_ACTION));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
//            preferencesManager.setHaveANotifyPaidConfirm(data.getString("trip_id"), data.getString("payment_method"));
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            /* CREATE NOTIFICATION */
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(context, SplashActivity.class);
            intent.putExtra("pushNotification", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg)
                    .setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    private void processActionConfirmPaidByCash(Context context, Bundle data) {
        String msg = data.getString(KEY_BODY);
        preferencesManager.setCurrentOrderId(data.getString("trip_id"));
        if (preferencesManager.appIsShow()) {
            /* CALL VIBRATE AND RINGTONE */
            if (preferencesManager.driverIsOnline()) {
                vibratorAndRingtone();
                Intent intent = new Intent(context, Ac_ConfirmPayByCash.class);
                // You can also include some extra data.
                intent.putExtra(Constant.KEY_DATA, data);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else {
//            preferencesManager.setHaveANotifyPaidConfirm(data.getString("trip_id"), data.getString("payment_method"));
//            preferencesManager.setCurrentOrderId("trip_id");
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            /* CREATE NOTIFICATION */

            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(context, SplashActivity.class);
            intent.putExtra("pushNotification", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg).setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }


    private void processActionApproval(Context context, Bundle data) {
        String msg = data.getString(KEY_BODY);
        if (preferencesManager.appIsShow()) {
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            Intent intent = new Intent(Constant.PUSH_NOTIFY_HISTORY);
            // You can also include some extra data.
            intent.putExtra(Constant.KEY_DATA, data.getString(KEY_DATA));
            intent.putExtra(Constant.KEY_ACTION,
                    data.getString(KEY_ACTION));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            preferencesManager.setHistoryPush(true);
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            /* CREATE NOTIFICATION */
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(context, SplashActivity.class);
            intent.putExtra(Constant.PUSH_NOTIFY_HISTORY, true);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg).setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    private void processTaskerStartTask(Context context, Bundle data) {
        String msg = data.getString(KEY_BODY);
        preferencesManager.setBeginTask("1");
        if (preferencesManager.appIsShow()) {
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            Intent intent = new Intent(Constant.ACTION_TASKER_START_TASK);
            // You can also include some extra data.
            intent.putExtra(Constant.KEY_DATA, data.getString(KEY_DATA));
            intent.putExtra(Constant.KEY_ACTION,
                    Constant.ACTION_TASKER_START_TASK);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            preferencesManager.setStartWithOutMain(true);
            preferencesManager.setPassengerHavePush(true);
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            /* CREATE NOTIFICATION */
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(context, SplashActivity.class);
            intent.putExtra("pushNotification", true);
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg).setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    private void processTaskerArrivedB(Context context, Bundle data) {
        String msg = data.getString(KEY_BODY);
        preferencesManager.setArrivedB("1");
        if (preferencesManager.appIsShow()) {
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            Intent intent = new Intent(Constant.ACTION_TASKER_ARRIVED_B);
            // You can also include some extra data.
            intent.putExtra(Constant.KEY_DATA, data.getString(KEY_DATA));
            intent.putExtra(Constant.KEY_ACTION,
                    Constant.ACTION_TASKER_ARRIVED_B);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            preferencesManager.setStartWithOutMain(true);
            preferencesManager.setPassengerHavePush(true);
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            /* CREATE NOTIFICATION */
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(context, SplashActivity.class);
            intent.putExtra("pushNotification", true);
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg).setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    private void processDriverArrived(Context context, Bundle data) {
        String msg = data.getString(KEY_BODY);
        preferencesManager
                .setPassengerCurrentScreen("ShowPassengerActivity");
        preferencesManager.setArrived("1");
        if (preferencesManager.appIsShow()) {
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            Intent intent = new Intent(Constant.ACTION_DRIVER_ARRIVED);
            // You can also include some extra data.
            intent.putExtra(Constant.KEY_DATA, data.getString(KEY_DATA));
            intent.putExtra(Constant.KEY_ACTION,
                    Constant.ACTION_DRIVER_ARRIVED);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            preferencesManager.setStartWithOutMain(true);
            preferencesManager.setPassengerHavePush(true);
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            /* CREATE NOTIFICATION */
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(context, SplashActivity.class);
            intent.putExtra("pushNotification", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg).setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    private void sendMessage(Context context, Bundle data) {
        if(preferencesManager.getUserOnChatScreen().equals("1")){
            vibratorAndRingtone();
            Intent intent = new Intent(Constant.SEND_MESSAGE);
            intent.putExtra(Constant.KEY_DATA, data.getString(KEY_DATA));
            intent.putExtra(Constant.KEY_ACTION, Constant.SEND_MESSAGE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }else{
            vibratorAndRingtone();
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(context, SplashActivity.class);
//            intent.putExtra("pushNotification", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(data.getString(KEY_BODY)))
                    .setContentText(data.getString(KEY_BODY)).setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    // new code
    private void processInforPromotion(Context context, Bundle data) {
        String msg = data.getString(KEY_BODY);
        /* CALL VIBRATE AND RINGTONE */
        vibratorAndRingtone();
        /* CREATE NOTIFICATION */
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra(Constant.PUSH_NOTIFY_HISTORY, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg).setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
    // end new code

    private void processDriverConfirm(Context context, Bundle data) {
        String msg = data.getString(KEY_BODY);
        String trip_status = data.getString("trip_status");
        /* PROCESS DATA */
        String json = data.getString(KEY_DATA);
        CurrentOrder currentOrder = new CurrentOrder();
        currentOrder.setId(ParseJsonUtil.parseOrderIdFromDriverConfirm(json));
        Log.e("currentOrder", "currentOrder:" + currentOrder.getStatus() + " ");
        GlobalValue.getInstance().setCurrentOrder(currentOrder);
        preferencesManager.setCurrentOrderId(currentOrder.getId());
        preferencesManager.setPassengerIsInTrip(true);
        preferencesManager.setPassengerCurrentScreen("ConfirmActivity");
        preferencesManager.setPassengerHavePush(true);
        preferencesManager.setPassengerWaitConfirm(false);
        if (preferencesManager.appIsShow()) {
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            Intent intent = new Intent(Constant.ACTION_DRIVER_CONFIRM);
            // You can also include some extra data.
            intent.putExtra(Constant.KEY_DATA, data.getString(KEY_DATA));
            intent.putExtra(Constant.KEY_ACTION,
                    data.getString(KEY_ACTION));
            intent.putExtra("trip_status",
                    trip_status);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            preferencesManager.setStartWithOutMain(true);
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            /* CREATE NOTIFICATION */
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(context, SplashActivity.class);
            intent.putExtra("pushNotification", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg).setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    private void processDriverStartTrip(Context context, Bundle data) {
        String msg = data.getString(KEY_BODY);
        preferencesManager
                .setPassengerCurrentScreen("StartTripForPassengerActivity");
        if (preferencesManager.appIsShow()) {
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            Intent intent = new Intent(Constant.ACTION_DRIVER_START_TRIP);
            // You can also include some extra data.
            intent.putExtra(Constant.KEY_DATA, data.getString(KEY_DATA));
            intent.putExtra(Constant.KEY_ACTION,
                    data.getString(KEY_ACTION));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            preferencesManager.setStartWithOutMain(true);
            preferencesManager.setPassengerHavePush(true);
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            /* CREATE NOTIFICATION */
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(context, SplashActivity.class);
            intent.putExtra("pushNotification", true);
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg).setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    private void processDriverEndTrip(Context context, Bundle data) {
        String msg = data.getString(KEY_BODY);
        preferencesManager.setPassengerCurrentScreen("RateDriverActivity");
        preferencesManager.setPassengerHaveDonePayment(false);
        if (preferencesManager.appIsShow()) {
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            Intent intent = new Intent(Constant.ACTION_DRIVER_END_TRIP);
            // You can also include some extra data.
            intent.putExtra(Constant.KEY_DATA, data.getString(KEY_DATA));
            intent.putExtra(Constant.KEY_ACTION,
                    data.getString(KEY_ACTION));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            preferencesManager.setStartWithOutMain(true);
            preferencesManager.setPassengerHavePush(true);
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            /* CREATE NOTIFICATION */
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(context, SplashActivity.class);
            intent.putExtra("pushNotification", true);
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg).setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    // For driver
    private void processCreateRequest(Context context, Bundle data) {
        String msg = data.getString(KEY_BODY);
        preferencesManager.setDriverCurrentScreen("RequestPassengerActivity");
        preferencesManager.setDriverIsInTrip();
        if (preferencesManager.driverIsOnline()) {
            if (preferencesManager.appIsShow()) {
                /* CALL VIBRATE AND RINGTONE */
                vibratorAndRingtone();
                Intent intent = new Intent(
                        Constant.ACTION_PASSENGER_CREATE_REQUEST);
                // You can also include some extra data.
                intent.putExtra(Constant.KEY_DATA,
                        data.getString(KEY_DATA));
                intent.putExtra(Constant.KEY_ACTION,
                        data.getString(KEY_ACTION));
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            } else {
                preferencesManager.setDriverHavePush();
                /* CALL VIBRATE AND RINGTONE */
                vibratorAndRingtone();
                /* CREATE NOTIFICATION */
                NotificationManager mNotificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent = new Intent(context, SplashActivity.class);
                intent.putExtra("pushNotification", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent contentIntent = PendingIntent.getActivity(
                        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setStyle(
                                new NotificationCompat.BigTextStyle()
                                        .bigText(msg)).setContentText(msg)
                        .setAutoCancel(true);
                mBuilder.setContentIntent(contentIntent);

                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
        }
    }

    private void processCancelRequest(Context context, Bundle data) {
        if (preferencesManager.appIsShow()
                && preferencesManager.getDriverCurrentScreen().equals(
                "RequestPassengerActivity")) {
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            Intent intent = new Intent(Constant.ACTION_PASSENGER_CREATE_REQUEST);
            // You can also include some extra data.
            intent.putExtra(Constant.KEY_DATA, data.getString(KEY_DATA));
            intent.putExtra(Constant.KEY_ACTION,
                    data.getString(KEY_ACTION));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {

        }
    }

    private void processCancelTrip(Context context, Bundle data) {
        String msg = data.getString(KEY_BODY);
        preferencesManager.setDriverIsNotInTrip();
        preferencesManager.setPassengerCurrentScreen("");
        preferencesManager.setDriverCurrentScreen("");
        // preferencesManager.setPassengerIsInTrip(false);
        // preferencesManager.setPassengerWaitConfirm(false);
        if (preferencesManager.appIsShow()) {
            /* CALL VIBRATE AND RINGTONE */
            if (preferencesManager.isUser()
                    || !preferencesManager.driverIsOnline()) {
                vibratorAndRingtone();
                Intent intent = new Intent(Constant.ACTION_DRIVER_START_TRIP);
                // You can also include some extra data.
                intent.putExtra(Constant.KEY_DATA,
                        data.getString(KEY_DATA));
                intent.putExtra(Constant.KEY_ACTION,
                        data.getString(KEY_ACTION));
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            } else {
                if (preferencesManager.driverIsOnline()) {
                    vibratorAndRingtone();
                    Intent intent = new Intent(Constant.ACTION_CANCEL_TRIP);
                    // You can also include some extra data.
                    intent.putExtra(Constant.KEY_DATA,
                            data.getString(KEY_DATA));
                    intent.putExtra(Constant.KEY_ACTION,
                            data.getString(KEY_ACTION));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(
                            intent);
                }
            }

        } else {
            preferencesManager.setStartWithOutMain(true);
            /* CALL VIBRATE AND RINGTONE */
            vibratorAndRingtone();
            /* CREATE NOTIFICATION */
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(context, SplashActivity.class);
            intent.putExtra("pushNotification", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg).setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    private void vibratorAndRingtone() {
        // For Vibrate
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        // For Sound
        Uri notification = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                notification);
        r.play();
    }
}
