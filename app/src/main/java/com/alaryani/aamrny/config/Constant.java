package com.alaryani.aamrny.config;

public class Constant {
    /* Paypal account config */

    // paypal mock up
    public static final String PAYPAL_CLIENT_APP_ID = "AZPP-iGICCwvB7kHBADv_bs55C5bYyU9gCUmI_dT6ceVLQnkoy3TWNASNUpSr8rkrOQixlQTRbi9FvUc";
    public static final String PAYPAL_RECEIVE_EMAIL_ID = "paypal-facilitator@aamrny.com";

    /* ACTION FROM PUSH FOR DRIVER */
    public static final String ACTION_PASSENGER_CREATE_REQUEST = "createRequest";
    public static final String ACTION_PASSENGER_CANCEL_REQUEST = "cancelRequest";

    /* ACTION FROM PUSH FOR PASSENGER */
    public static final String ACTION_DRIVER_CONFIRM = "driverConfirm";
    public static final String ACTION_DRIVER_START_TRIP = "startTrip";
    public static final String ACTION_CHANGE_STATUS = "changeStatus";
    public static final String ACTION_DRIVER_END_TRIP = "endTrip";
    public static final String ACTION_CANCEL_TRIP = "cancelTrip";

    public static final String ACTION_APROVAL_UPDATE = "updateApproval";
    public static final String ACTION_APROVAL_REDEEM = "redeemApproval";
    public static final String ACTION_APROVAL_TRANFER = "transferApproval";

    /* ACTION INFORMATION PROMOTION */
    public static final String ACTION_INFORMATION = "promotion";
    public static final String ACTION_DRIVER_APPROVED = "driverApproved";
    public static final String ACTION_DRIVER_ARRIVED = "driverArrived";
    public static final String ACTION_TASKER_ARRIVED_B = "taskerArrivedB";
    public static final String ACTION_TASKER_START_TASK = "taskerStartTask";

    //is wait driver confirm
    public static final String TRIP_WAIT_DRIVER_NOTCONFIRM = "1";

	/* Screen driver constant */
    /* Screen passenger constant */

    /* Key constant */
    public static final String KEY_DATA = "KEY_DATA";
    public static final String DATA = "DATA";
    public static final String KEY_ACTION = "KEY_ACTION";

    public static final String PAYMENT_BY_CART = "2";
    public static final String PAYMENT_BY_PAYPAL = "1";
    public static final String PAYMENT_BY_STRIPE = "3";

    public static final String PAY_TRIP_BY_CASH = "2";
    public static final String PAY_TRIP_BY_BALANCE = "1";

    //Action
    public static final String ACTION_AMOUNT_DIFF = "2";
    public static final String ACTION_CONFIRM = "1";
    public static final String ACTION_CANCEL = "0";
    public static final String PAID_AMOUNT = "paid_amount";


    public static final String PUSH_NOTIFY_HISTORY = "pushNotificationHistory";

    // Driver Status
    public static final String STATUS_IDLE = "1";
    public static final String STATUS_BUSY = "0";

    public static final String KEY_IS_ACTIVE = "1";
    public static final String KEY_INACTIVE = "0";

    // Trip Status
    public static final String TRIP_STATUS_APPROACHING = "1";
    public static final String TRIP_STATUS_IN_PROGRESS = "2";
    public static final String TRIP_STATUS_PENDING_PAYMENT = "3";
    public static final String TRIP_STATUS_FINISH = "4";


    public static final String KEY_LINK = "KEY_LINK";
    public static final String KEY_STARTLOCATION_LATITUDE = "KEY_STARTLOCATION_LATITUDE";
    public static final String KEY_STARTLOCATION_LONGITUDE = "KEY_STARTLOCATION_LONGITUDE";
    public static final String KEY_ENDLOCATION_LATITUDE = "KEY_ENDLOCATION_LATITUDE";
    public static final String KEY_ENDLOCATION_LONGITUDE = "KEY_ENDLOCATION_LONGITUDE";
    public static final String KEY_ADDRESS_TO = "KEY_ADDRESS_TO";
    public static final String KEY_ADDRESS_START = "KEY_ADDRESS_START";
    public static final String KEY_ESTIMATE_DISTANCE = "KEY_ESTIMATE_DISTANCE";
    public static final String KEY_PROMO_CODE = "KEY_PROMO_CODE";

    public static final String TYPE_ACCOUNT_NORMAL = "0";
    public static final String TYPE_ACCOUNT_SOCIAL = "1";

    public static final String STATUS_ARRIVED_B = "7";
    public static final String STATUS_ARRIVED_A = "6";
    public static final String STATUS_START_TASK = "8";


    //Interval to wait for GPS signal
    public static final int TIME_GPS_SIGNAL = 120000;

    /* ACTION PAY BY CASH */
    public static final String ACTION_PAY_BY_CASH = "passengerPaymentPending";
    public static final String ACTION_DRIVER_CONFIRM_PAID_STATUS_CONFIRM = "driverConfirmPaymentTripAccept";
    public static final String ACTION_DRIVER_CONFIRM_PAID_STATUS_CANCEL = "driverConfirmPaymentTripCancel";

    public static final String SEND_MESSAGE = "SendMessage";
}
