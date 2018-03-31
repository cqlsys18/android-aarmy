package com.alaryani.aamrny.modelmanager;

import com.alaryani.aamrny.config.LinkApi;
import com.alaryani.aamrny.object.CarType;
import com.alaryani.aamrny.object.CityObj;
import com.alaryani.aamrny.object.CurrentOrder;
import com.alaryani.aamrny.object.DriverOnlineObj;
import com.alaryani.aamrny.object.ItemTransactionHistory;
import com.alaryani.aamrny.object.ItemTripHistory;
import com.alaryani.aamrny.object.LocationDriverObj;
import com.alaryani.aamrny.object.RequestObj;
import com.alaryani.aamrny.object.SettingObj;
import com.alaryani.aamrny.object.StateObj;
import com.alaryani.aamrny.object.Transfer;
import com.alaryani.aamrny.object.User;
import com.alaryani.aamrny.object.UserFacebook;
import com.alaryani.aamrny.object.UserOnlineObj;
import com.alaryani.aamrny.utility.AppUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseJsonUtil {

    private static final String PAYPAY_RESONDE = "response";
    private static final String PAYPAL_RESPONDE_TYPE = "response_type";

    // Get
    public static boolean isSuccess(String json) {
        try {
            JSONObject object = new JSONObject(json);
            if (object.getString(LinkApi.KEY_JSON_STATUS).equalsIgnoreCase(LinkApi.JSON_STATUS_SUCCESS)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    // Check trip status
    public static String pareWaitDriverConfirm(String json) {
        String action = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(LinkApi.KEY_JSON_DATA);
            JSONObject itemJson;
            itemJson = jsonArray.getJSONObject(0);
            return itemJson.getString("isWattingConfirm");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return action;
    }

    public static boolean isSuccessData(String json) {
        try {
            JSONObject object = new JSONObject(json);
            if (object.getString(LinkApi.KEY_JSON_DATA).equalsIgnoreCase(LinkApi.JSON_DATA_ERROR)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    // Get json message
    public static String getMessage(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            return jsonobj.getString(LinkApi.KEY_MESSAGE);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getEstimateFare(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            return jsonobj.getString("estimate_fare");
        } catch (Exception e) {
            return "";
        }
    }

    public static String getIsActiveAccount(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            return jsonobj.getString("isActive");
        } catch (Exception e) {
            return "";
        }
    }

    public static String getCountDriver(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            return jsonobj.getString("count");
        } catch (Exception e) {
            return "";
        }
    }

    public static String getIsActiveAsDriver(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            return jsonobj.getString("is_active");
        } catch (Exception e) {
            return "";
        }
    }

    // Count My Request
    public static int countMyRequest(String json) {
        int count = 0;
        try {
            JSONObject jsonobj = new JSONObject(json);
            return (jsonobj.getJSONArray(LinkApi.KEY_JSON_DATA))
                    .length();
        } catch (Exception e) {
            return count;
        }
    }

    // Get Token from login
    public static String getTokenFromLogin(String json) {
        try {
            JSONObject object = new JSONObject(json);
            return object.getJSONObject(LinkApi.KEY_JSON_DATA)
                    .getString("token");
        } catch (Exception e) {

            return "";
        }

    }

    // Get Id from login
    public static String getIdFromLogin(String json) {
        try {
            JSONObject object = new JSONObject(json);
            return object.getJSONObject(LinkApi.KEY_JSON_DATA)
                    .getString("user_id");
        } catch (Exception e) {

            return "";
        }
    }

    // Get Id from login
    public static boolean isDriverFromLogin(String json) {
        try {
            JSONObject object = new JSONObject(json);
            if (object.getJSONObject(LinkApi.KEY_JSON_DATA)
                    .getString("isDriver").equals("1")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    // Get Driver Is Active from login
    public static boolean driverIsActive(String json) {
        try {
            JSONObject object = new JSONObject(json);
            if (object.getJSONObject(LinkApi.KEY_JSON_DATA)
                    .getString("driverActive").equals("1")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    // Get user information
    public static UserFacebook parseUser(String json) {
        UserFacebook user = new UserFacebook();
        try {
            JSONObject jsonobj = new JSONObject(json)
                    .getJSONObject(LinkApi.KEY_JSON_DATA);
            user.setId(jsonobj.getString("gcm_id"));
            user.setFull_name(jsonobj.getString("name"));
            user.setLinkProfile(jsonobj.getString("image"));
            user.setEmail(jsonobj.getString("email"));
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return user;
    }

    public static UserFacebook parseLogin(String json) {
        UserFacebook user = new UserFacebook();
        try {
            JSONObject jsonobj = new JSONObject(json)
                    .getJSONObject(LinkApi.KEY_JSON_DATA);
            user.setId(jsonobj.getString("gcm_id"));
            user.setEmail(jsonobj.getString("email"));
            user.setIme(jsonobj.getString("ime"));
            user.setType(jsonobj.getString("type"));
            user.setLat(jsonobj.getString("lat"));
            user.setLng(jsonobj.getString("long"));
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return user;
    }

    // Get max page
    public static int getMaxPage(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            return jsonobj.getInt("numpage");
        } catch (Exception e) {
            return 0;
        }
    }

    public static ArrayList<CarType> parseListCarTypes(String json) {
        ArrayList<CarType> arr = new ArrayList<CarType>();
        CarType stateObj;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject
                    .getJSONArray("list_job");
            for (int i = 0; i < jsonArray.length(); i++) {
                stateObj = new CarType();
                JSONObject itemJson = jsonArray.getJSONObject(i);
                stateObj.setLink(itemJson.getString("link"));
                stateObj.setId(itemJson.getString("link"));
                stateObj.setName(itemJson.getString("name"));
                stateObj.setImageType(itemJson.getString("image"));
                stateObj.setStart_fare(itemJson.getString("startFare"));
                stateObj.setFee_per_minute(itemJson.getString("feePerMinute"));
                stateObj.setFee_per_kilometer(itemJson.getString("feePerKilometer"));
                stateObj.setGotoA(itemJson.getString("pickUpAtA"));
                stateObj.setWorkAtB(itemJson.getString("workAtB"));
                stateObj.setImageMarker(itemJson.getString("imgMarker"));
                stateObj.setTaskRate(itemJson.getString("taskRate"));
                stateObj.setTaskDefaultTime(itemJson.getString("taskDefaultTime"));

                arr.add(stateObj);
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return arr;
    }

    // get data trip history
    public static ArrayList<ItemTripHistory> parseTripHistory(String json) {
        ArrayList<ItemTripHistory> arr = new ArrayList<ItemTripHistory>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject
                    .getJSONArray(LinkApi.KEY_JSON_DATA);
            JSONObject itemJson;
            ItemTripHistory itemHistory;

            for (int i = 0; i < jsonArray.length(); i++) {
                itemHistory = new ItemTripHistory();
                itemJson = jsonArray.getJSONObject(i);
                itemHistory.setTripId(itemJson.getInt("id"));
                itemHistory.setDriverId(itemJson.getInt("driverId"));
                itemHistory.setPassengerId(itemJson.getInt("passengerId"));
                itemHistory.setStartTime(itemJson.getString("startTime"));
                itemHistory.setEndTime(itemJson.getString("endTime"));
                itemHistory
                        .setStartLocaton(itemJson.getString("startLocation"));
                itemHistory.setEndLocation(itemJson.getString("endLocation"));
                itemHistory.setDistance(itemJson.getDouble("distance"));
                itemHistory.setActualFare(itemJson.getString("actualFare"));
                itemHistory.setActualReceive(itemJson.getString("actualReceive"));
                itemHistory.setTotalTime(itemJson.getString("totalTime"));
                itemHistory.setLink(itemJson.getString("link"));
                itemHistory.setStartTimeWorking(itemJson.getString("startTimeWorking"));
                itemHistory.setEndTimeWorking(itemJson.getString("endTimeWorking"));
                itemHistory.setPaymentMethod(itemJson.getString("paymentMethod"));

                arr.add(itemHistory);
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return arr;
    }

    public static String parseDistanceFromMap(String json) {
        String str = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject
                    .getJSONArray("routes");
            JSONObject itemJson = jsonArray.getJSONObject(0);
            JSONArray jsonArrayLeg = itemJson.getJSONArray("legs");
            JSONObject object = jsonArrayLeg.getJSONObject(0);
            JSONObject object1 = object.getJSONObject("distance");
            JSONObject object2 = object.getJSONObject("duration");
            str = object1.getString("text");


        } catch (JSONException e) {

            e.printStackTrace();
        }
        return str;
    }

    public static String parseTimeFromMap(String json) {
        String str = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject
                    .getJSONArray("routes");
            JSONObject itemJson = jsonArray.getJSONObject(0);
            JSONArray jsonArrayLeg = itemJson.getJSONArray("legs");
            JSONObject object = jsonArrayLeg.getJSONObject(0);
            JSONObject object2 = object.getJSONObject("duration");
            str = object2.getString("text");


        } catch (JSONException e) {

            e.printStackTrace();
        }
        return str;
    }

    public static ArrayList<StateObj> parseListStates(String json) {
        ArrayList<StateObj> arr = new ArrayList<StateObj>();
        StateObj stateObj;
        ArrayList<CityObj> listCity;
        CityObj cityObj;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject
                    .getJSONArray(LinkApi.KEY_JSON_DATA);

            for (int i = 0; i < jsonArray.length(); i++) {
                stateObj = new StateObj();
                JSONObject itemJson = jsonArray.getJSONObject(i);
                stateObj.setStateId(itemJson.getString("stateId"));
                stateObj.setStateName(itemJson.getString("stateName"));
                JSONArray arrayStates = itemJson.getJSONArray("stateCities");
                listCity = new ArrayList<>();
                if (arrayStates != null && arrayStates.length() > 0) {
                    for (int j = 0; j < arrayStates.length(); j++) {
                        cityObj = new CityObj();
                        JSONObject objectCity = arrayStates.getJSONObject(j);
                        cityObj.setCityId(objectCity.getString("cityId"));
                        cityObj.setCityName(objectCity.getString("cityName"));
                        listCity.add(cityObj);
                    }
                    stateObj.setStateCities(listCity);
                }
                arr.add(stateObj);
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return arr;
    }

    // get data transaction history
    public static ArrayList<ItemTransactionHistory> parseTransactionHistory(
            String json) {
        ArrayList<ItemTransactionHistory> arrTransaction = new ArrayList<ItemTransactionHistory>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject
                    .getJSONArray(LinkApi.KEY_JSON_DATA);
            JSONObject itemJson;
            ItemTransactionHistory itemHistory;

            for (int i = 0; i < jsonArray.length(); i++) {
                itemHistory = new ItemTransactionHistory();
                itemJson = jsonArray.getJSONObject(i);
                itemHistory.setTransactionId(itemJson.getString("id"));
                itemHistory.setDateTimeTransaction(itemJson
                        .getString("dateCreated"));
                itemHistory.setTypeTransaction(itemJson.getString("type"));
                itemHistory.setPointTransaction(itemJson.getString("amount"));
                itemHistory.setNoteTransaction(itemJson.getString("action"));

                String tripId = itemJson.getString("tripId");

                itemHistory.setTripId(tripId);

                arrTransaction.add(itemHistory);
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return arrTransaction;
    }

    // get data profile
    public static User parseInfoProfile(String json) {
        User itemUser = new User();
        try {
            JSONObject jsonObject = new JSONObject(json)
                    .getJSONObject(LinkApi.KEY_JSON_DATA);
            itemUser.setIdUser(jsonObject.getString("id"));
            itemUser.setFullName(jsonObject.getString("fullName"));
            itemUser.setLinkImage(jsonObject.getString("image"));
            itemUser.setBalance(jsonObject.getDouble("balance"));
            itemUser.setEmail(jsonObject.getString("email"));
            itemUser.setAddress(jsonObject.getString("address"));
            itemUser.setPhone(jsonObject.getString("phone"));
            itemUser.setGender(jsonObject.getString("gender"));
            itemUser.setDob(jsonObject.getString("dob"));
            itemUser.setDescription(jsonObject.getString("description"));
            itemUser.setIsOnline(jsonObject.getString("isOnline"));
            itemUser.setIsActive(jsonObject.getString("isActive"));
            itemUser.setPassengerRate(jsonObject.getString("passengerRate"));
            itemUser.setStateId(jsonObject.getString("stateId"));
            itemUser.setStateName(jsonObject.getString("stateName"));
            itemUser.setCityId(jsonObject.getString("cityId"));
            itemUser.setCityName(jsonObject.getString("cityName"));
            itemUser.setTypeAccount(jsonObject.getString("typeAccount"));
            if (!jsonObject.isNull("account")) {
                itemUser.setAccount(
                        jsonObject.getString("account"));
            }
            try {
                JSONObject objDriver = jsonObject.getJSONObject("driver");
                itemUser.getDriverObj()
                        .setStatus(objDriver.getString("status"));
                itemUser.getDriverObj().setIsOnline(
                        objDriver.getString("isOnline"));
                itemUser.getDriverObj().setIsActive(
                        objDriver.getString("isActive"));
                itemUser.getDriverObj().setDriverRate(
                        objDriver.getString("driverRate"));
                itemUser.getDriverObj().setUpdatePending(
                        objDriver.getString("updatePending"));
                itemUser.getDriverObj().setBankAccount(
                        objDriver.getString("bankAccount"));
                itemUser.getDriverObj().setIdentity(
                        objDriver.getString("identity"));
                itemUser.getCarObj().setTypeCar(objDriver.getString("linkType"));
            } catch (Exception e) {
                // TODO: handle exception
            }

            try {
                JSONObject objCar = jsonObject.getJSONObject("car");
                itemUser.getCarObj().setCarPlate(objCar.getString("carPlate"));
                itemUser.getCarObj().setBrand(objCar.getString("brand"));
                itemUser.getCarObj().setModel(objCar.getString("model"));
                itemUser.getCarObj().setYear(objCar.getString("year"));
                itemUser.getCarObj().setStatus(objCar.getString("status"));
                itemUser.getCarObj().setDocument(objCar.getString("document"));


                JSONObject objImage = objCar.getJSONObject("images");
                itemUser.getCarObj().setImageone(objImage.getString("image1"));
                itemUser.getCarObj().setImagetwo(objImage.getString("image2"));
            } catch (Exception e) {
                // TODO: handle exception
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return itemUser;
    }

    // get data transfer
    public static Transfer parseInfoTransfer(String json) {
        Transfer itemTransfer = new Transfer();
        try {
            JSONObject jsonObject = new JSONObject(json)
                    .getJSONObject(LinkApi.KEY_JSON_DATA);

            itemTransfer.setReceiverName(jsonObject.getString("fullName"));
            itemTransfer.setReceiverProfile(jsonObject.getString("image"));
            itemTransfer.setReceiverEmail(jsonObject.getString("email"));
            itemTransfer.setReceiverGender(jsonObject.getString("gender"));

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return itemTransfer;
    }

    // get data trip history
    public static ArrayList<RequestObj> parseMyRequest(String json) {
        ArrayList<RequestObj> arr = new ArrayList<RequestObj>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject
                    .getJSONArray(LinkApi.KEY_JSON_DATA);
            JSONObject itemJson, passengerObj;
            RequestObj itemRequest;

            for (int i = 0; i < jsonArray.length(); i++) {
                itemRequest = new RequestObj();
                itemJson = jsonArray.getJSONObject(i);
                itemRequest.setId(itemJson.getString("id"));
                itemRequest.setPassengerId(itemJson.getString("passengerId"));
                itemRequest.setDriverId(itemJson.getString("driverId"));
                itemRequest.setRequestTime(itemJson.getString("requestTime"));
                itemRequest.setLink(itemJson.getString("link"));
                itemRequest.setStartLat(itemJson.getString("startLat"));
                itemRequest.setStartLong(itemJson.getString("startLong"));
                itemRequest.setStartLat(itemJson.getString("startLat"));
                itemRequest.setStartLocation(itemJson
                        .getString("startLocation"));
                itemRequest.setEndLat(itemJson.getString("endLat"));
                itemRequest.setEndLong(itemJson.getString("endLong"));
                itemRequest.setEndLocation(itemJson.getString("endLocation"));
                itemRequest.setPassengerRate(itemJson
                        .getString("passengerRate"));
                itemRequest.setEstimate_fare(itemJson.getString("estimate_fare"));

                passengerObj = itemJson.getJSONObject("passenger");
                itemRequest.setPassengerImage(passengerObj.getString("image"));
                itemRequest.setPassengerName(passengerObj.getString("fullName"));
                itemRequest.setPassengerphone(passengerObj.getString("phone"));


                arr.add(itemRequest);
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return arr;
    }

    public static CurrentOrder parseCurrentOrder(String json) {
        CurrentOrder currentOrder = new CurrentOrder();
        try {
            JSONObject jsonobj = new JSONObject(json)
                    .getJSONObject(LinkApi.KEY_JSON_DATA);
            currentOrder.setId(jsonobj.getString("id"));
            currentOrder.setPassengerId(jsonobj.getString("passengerId"));
            currentOrder.setLink(jsonobj.getString("link"));
            currentOrder.setStartTime(jsonobj.getString("startTime"));
            currentOrder.setStartLat(jsonobj.getString("startLat"));
            currentOrder.setStartLong(jsonobj.getString("startLong"));
            currentOrder.setEndLat(jsonobj.getString("endLat"));
            currentOrder.setEndLong(jsonobj.getString("endLong"));
            currentOrder.setDateCreated(jsonobj.getString("dateCreated"));
            currentOrder.setDriverId(jsonobj.getString("driverId"));
            currentOrder.setStartLocation(jsonobj.getString("startLocation"));
            currentOrder.setEndLocation(jsonobj.getString("endLocation"));
            currentOrder.setStatus(jsonobj.getString("status"));
            currentOrder.setEndTime(jsonobj.getString("endTime"));
            currentOrder.setDistance(jsonobj.getString("distance"));
            currentOrder.setEstimateFare(jsonobj.getString("estimateFare"));

            currentOrder.setActualFare(jsonobj.getString("actualFare"));
            currentOrder.setStartTimeWorking(jsonobj.getString("startTimeWorking"));
            currentOrder.setEndTimeWorking(jsonobj.getString("endTimeWorking"));
            currentOrder.setDriverRateTrip(jsonobj.getString("driverRate"));
            currentOrder.setPassengerRateTrip(jsonobj.getString("passengerRate"));
            if (!jsonobj.isNull("totalTime")) {
                currentOrder.setTotalTime(jsonobj.getString("totalTime"));
            }
            if (!jsonobj.isNull("pickUpAtA")) {
                currentOrder.setPickUpAtA(jsonobj.getString("pickUpAtA"));
            }
            if (!jsonobj.isNull("workAtB")) {
                currentOrder.setWorkAtB(jsonobj.getString("workAtB"));
            }


            JSONObject driver = jsonobj.getJSONObject("driver");
            currentOrder.setDriverName(driver.getString("driverName"));
            currentOrder.setImageDriver(driver.getString("imageDriver"));
            currentOrder.setCarPlate(driver.getString("carPlate"));
            currentOrder.setCarImage(driver.getString("carImage"));
            currentOrder.setDriver_phone(driver.getString("phone"));
            currentOrder.setDriver_rate(driver.getString("rate"));
            currentOrder.setDriverRate(driver.getString("rate"));
            currentOrder.setIdentity(driver.getString("identity"));
            currentOrder.setCarPlate(driver.getString("carPlate"));

            JSONObject passenger = jsonobj.getJSONObject("passenger");
            currentOrder.setPassengerName(passenger.getString("passengerName"));
            currentOrder.setImagePassenger(passenger
                    .getString("imagePassenger"));
            currentOrder.setPassenger_rate(passenger.getString("rate"));
            currentOrder.setPassengerRate(passenger.getString("rate"));
            currentOrder.setPassenger_phone(passenger.getString("phone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return currentOrder;
    }

    // public static CurrentOrder parseCurrentOrderFromDriverConfirm(String
    // json) {
    // CurrentOrder currentOrder = new CurrentOrder();
    // try {
    // JSONObject jsonobj = new JSONObject(json);
    // currentOrder.setId(jsonobj.getString("tripId"));
    // currentOrder.setDriverName(jsonobj.getString("driverName"));
    // currentOrder.setDriverRate(jsonobj.getString("rate"));
    // currentOrder.setDriverImage(jsonobj.getString("imageDriver"));
    // currentOrder.setDriverPhone(jsonobj.getString("phone"));
    // currentOrder.setCarPlate(jsonobj.getString("carPlate"));
    // currentOrder.setCarImage(jsonobj.getString("carImage"));
    // currentOrder.setStartLocation(jsonobj.getString("startLocation"));
    // currentOrder.setEndLocation(jsonobj.getString("endLocation"));
    // } catch (JSONException e) {
    //
    // e.printStackTrace();
    // }
    // return currentOrder;
    // }

    // Get Actual Fare
    public static String getActualFare(String json) {
        try {
            JSONObject object = new JSONObject(json);
            return object.getString("actualFare");
        } catch (Exception e) {

            return "";
        }
    }

    // Get Actual Fare
    public static String getMissingFare(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            return jsonobj.getString("data");
        } catch (Exception e) {

            return "";
        }
    }

    // Get Driver Is Active from login
    public static boolean paymentIsPaypal(JSONObject json) {
        try {
            if (json.getString(PAYPAL_RESPONDE_TYPE).equals("payment")) {
                return true;
            }

        } catch (Exception e) {

        }
        return false;
    }

    public static String getTransactionFromPaypal(JSONObject json) {
        try {
            JSONObject proof_of_payment = json
                    .getJSONObject(PAYPAY_RESONDE);

            return proof_of_payment.getString("state")
                    + proof_of_payment.getString("id");
        } catch (Exception e) {

        }
        return "";
    }

    public static String getTransactionFromCart(JSONObject json) {
        try {
            JSONObject proof_of_payment = json
                    .getJSONObject("proof_of_payment");
            JSONObject rest_api = proof_of_payment.getJSONObject("rest_api");
            return rest_api.getString("state")
                    + rest_api.getString("payment_id");
        } catch (Exception e) {

        }
        return "";
    }

    // GET DISTANCE
    public static String getDistance(String json) {
        try {
            JSONObject object = new JSONObject(json);
            return object.getString("data");
        } catch (Exception e) {
            return "";
        }
    }

    public static String parseOrderIdFromDriverConfirm(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            return jsonobj.getString("tripId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    // Check trip status
    public static String parseTripStatus(String json) {
        String action = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject
                    .getJSONArray(LinkApi.KEY_JSON_DATA);
            JSONObject itemJson;
            itemJson = jsonArray.getJSONObject(0);
            return itemJson.getString("status");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return action;
    }

    public static String parseTripPassengerRate(String json) {
        String action = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject
                    .getJSONArray(LinkApi.KEY_JSON_DATA);
            JSONObject itemJson;
            itemJson = jsonArray.getJSONObject(0);
            return itemJson.getString("passengerRate");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return action;
    }

    public static String parseTripDriverRate(String json) {
        String action = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject
                    .getJSONArray(LinkApi.KEY_JSON_DATA);
            JSONObject itemJson;
            itemJson = jsonArray.getJSONObject(0);
            return itemJson.getString("driverRate");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return action;
    }

    // Check trip status
    public static String parseTripId(String json) {
        String action = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject
                    .getJSONArray(LinkApi.KEY_JSON_DATA);
            JSONObject itemJson;
            itemJson = jsonArray.getJSONObject(0);
            return itemJson.getString("id");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return action;
    }

    public static String getShareDriverBonus(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONObject data = jsonobj
                    .getJSONObject(LinkApi.KEY_JSON_DATA);
            return data.getString("driver_share_bonus");
        } catch (Exception e) {
            return "";
        }
    }

    public static String getTimeSendRequestAgain(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONObject data = jsonobj
                    .getJSONObject(LinkApi.KEY_JSON_DATA);
            return data.getString("time_to_send_request_again");
        } catch (Exception e) {
            return "";
        }
    }

    public static String getMaxTimeSendRequest(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONObject data = jsonobj
                    .getJSONObject(LinkApi.KEY_JSON_DATA);
            return data.getString("max_time_send_request");
        } catch (Exception e) {
            return "";
        }
    }

    public static String getSharePassengerBonus(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONObject data = jsonobj
                    .getJSONObject(LinkApi.KEY_JSON_DATA);
            return data.getString("passenger_share_bonus");
        } catch (Exception e) {
            return "";
        }
    }

    public static String getPhoneAdmin(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONObject data = jsonobj
                    .getJSONObject(LinkApi.KEY_JSON_DATA);
            return data.getString("admin_phone_number");
        } catch (Exception e) {
            return "";
        }
    }

    public static String getMinRedeem(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONObject data = jsonobj
                    .getJSONObject(LinkApi.KEY_JSON_DATA);
            return data.getString("min_redeem_amount");
        } catch (Exception e) {
            return "";
        }
    }

    public static SettingObj getGeneralSettings(String json) {
        SettingObj settingObj = new SettingObj();
        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONObject data = jsonobj
                    .getJSONObject(LinkApi.KEY_JSON_DATA);
            settingObj.setTime_to_send_request_again(data.getString("time_to_send_request_again"));
            settingObj.setMax_time_send_request(data.getString("max_time_send_request"));
            settingObj.setEstimate_fare_speed(data.getString("estimate_fare_speed"));
            settingObj.setPpm_of_link_i(data.getString("ppm_of_link_i"));
            settingObj.setPpm_of_link_ii(data.getString("ppm_of_link_ii"));
            settingObj.setPpm_of_link_iii(data.getString("ppm_of_link_iii"));
            settingObj.setPpk_of_link_i(data.getString("ppk_of_link_i"));
            settingObj.setPpk_of_link_ii(data.getString("ppk_of_link_ii"));
            settingObj.setPpk_of_link_iii(data.getString("ppk_of_link_iii"));
            settingObj.setSf_of_link_i(data.getString("sf_of_link_i"));
            settingObj.setSf_of_link_ii(data.getString("sf_of_link_ii"));
            settingObj.setSf_of_link_iii(data.getString("sf_of_link_iii"));
            return settingObj;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getMinTranfer(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONObject data = jsonobj
                    .getJSONObject(LinkApi.KEY_JSON_DATA);
            return data.getString("min_transfer_amount");
        } catch (Exception e) {
            return "";
        }
    }

    public static double getDriverEarn(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONObject data = jsonobj
                    .getJSONObject(LinkApi.KEY_JSON_DATA);
            return data.getDouble("driver_earn");
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static boolean isDriverFromSplash(String json) {
        try {
            JSONObject object = new JSONObject(json);
            if (object.getJSONObject(LinkApi.KEY_JSON_DATA)
                    .getJSONObject("driver").getString("isActive") != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public static boolean driverIsActiveFromSplash(String json) {
        try {
            JSONObject object = new JSONObject(json);
            if (object.getJSONObject(LinkApi.KEY_JSON_DATA)
                    .getJSONObject("driver").getString("isActive").equals("1")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    // Get Id drivers number
    public static String getDriverCount(String json) {
        try {
            JSONObject object = new JSONObject(json);
            return object.getString(LinkApi.KEY_JSON_COUNT) + "";
        } catch (Exception e) {
            return "0";
        }
    }

    public static LocationDriverObj parseLocationDriver(String json) {
        LocationDriverObj user = new LocationDriverObj();
        try {
            JSONObject jsonobj = new JSONObject(json)
                    .getJSONObject(LinkApi.KEY_JSON_DATA);
            user.setDriverId(jsonobj.getString("driverId"));
            user.setLatitude(jsonobj.getString("driverLat"));
            user.setLongitude(jsonobj.getString("driverLon"));
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return user;
    }

    public static UserOnlineObj parseDriver(String json) {
        UserOnlineObj userOnlineObj = new UserOnlineObj();
        ArrayList<DriverOnlineObj> listDrivers = new ArrayList<>();
        DriverOnlineObj user;

        try {
            JSONObject jsonobj = new JSONObject(json);
            userOnlineObj.setCount(jsonobj.getString(LinkApi.KEY_JSON_COUNT));

            JSONArray array = jsonobj.getJSONArray(LinkApi.KEY_JSON_DATA);
            for (int i = 0; i < array.length(); i++) {
                user = new DriverOnlineObj();
                JSONObject object = array.getJSONObject(i);
                user.setFullName(object.getString("fullName"));
                user.setDriverId(object.getString("driverId"));
                user.setImage(object.getString("image"));
                user.setGender(object.getString("gender"));
                user.setDescription(object.getString("description"));
                user.setPhone(object.getString("phone"));
                user.setLat(object.getString("lat"));
                user.setLongitude(object.getString("long"));
                user.setRate(object.getString("rate"));
                user.setRateCount(object.getString("rateCount"));
                listDrivers.add(user);
            }
            userOnlineObj.setDriverOnlineObj(listDrivers);
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return userOnlineObj;
    }



    public static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }
}
