package com.alaryani.aamrny.modelmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;
import android.util.Log;

import com.alaryani.aamrny.config.Constant;
import com.alaryani.aamrny.config.LinkApi;

//import org.apache.http.client.methods.HttpPost;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.VolleyError;
import com.alaryani.aamrny.volleynetwork.HttpError;
import com.alaryani.aamrny.volleynetwork.HttpGet;
import com.alaryani.aamrny.volleynetwork.HttpListener;
import com.alaryani.aamrny.volleynetwork.HttpPost;
import com.alaryani.aamrny.volleynetwork.HttpRequest;

public class ModelManager {
    private static String TAG = "ModelManager";

    public static void checkEmailLogin(String email, Context context,
                                       boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("email", email);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.PREPARELOGIN), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void getCarTypes(Context context,
                                   boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.SHOW_CAR_TYPE), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void checkTokenLogin(String token, Context context,
                                       boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        LinkApi.getDomain(context);
        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.AUTHORRIZE), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void getDistanceAndTime(String token, Context context, String start_lat, String start_long, String end_lat, String end_long,
                                          boolean isProgress, final ModelManagerListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("origin", start_lat + "," + start_long);
        params.put("destination", end_lat + "," + end_long);
        params.put("sensor", "false");
        params.put("units", "metric");
        params.put("mode", "driving");
        params.put("key", "driving");
        LinkApi.getDomain(context);
        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.GET_DISTANCE_AND_TIME_FROM_MAP), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }
    //Confirm
    public static void confirmPayByCash(String tripId, String action, Context context,String paid_amount,
                                        boolean isProgress, final ModelManagerListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("tripId", tripId);
        params.put("paymentMethod", Constant.PAY_TRIP_BY_CASH);
        params.put("action", action);
        params.put("paid_amount", paid_amount);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.DRIVER_CONFIRM_PAY_BY_CASH), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });

    }

    public static void forgotPassword(Context context, String email,
                                      boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        new HttpGet(context, LinkApi.getLinkApi(context, LinkApi.FORGOTPASSWORD), params, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void loginAccount(Context context, String email, String password, String gcm_id, String ime, String type, double lat, double longitude,
                                    boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("gcm_id", gcm_id);
        params.put("ime", ime);
        params.put("lat", lat + "");
        params.put("long", longitude + "");
        params.put("type", type + "");
        Log.e("params", "params:" + params.toString());

        new HttpGet(context, LinkApi.getLinkApi(context, LinkApi.LOGINACCOUNT), params, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void registerAccount(Context context, String name, String phone, String email, String password, String address, String country, String city, String postcode, String account, Bitmap imgPhoto1, boolean isProgress,
                                       final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        /* ADD PARAM */
        params.put("full_name", name);
        params.put("phone", phone);
        params.put("email", email);
        params.put("password", password);
        params.put("address", address);
        params.put("city", city);
        params.put("country", country);
        params.put("post_code", postcode);
        params.put("account", account);
        Log.e("param", "param:" + params.toString());
        /* ADD IMAGE ONE*/
        params.put("image", convertBitmapListToArrayJson(imgPhoto1));


        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.REGISTERACCOUNT), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void login(String gcm_id, String email, String ime,
                             String lat, String lng, String name, String gender, String image,
                             Context context, boolean isProgress,
                             final ModelManagerListener listener) {


        Map<String, String> params = new HashMap<>();
        params.put("gcm_id", gcm_id);
        params.put("email", email);
        params.put("ime", ime);
        // For Android type = 1, IOS type =2
        params.put("type", "1");
        params.put("lat", lat);
        params.put("long", lng);
        params.put("name", name);
        params.put("gender", gender);
        params.put("image", image);
        LinkApi.getDomain(context);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.LOGIN), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }
    public static void otpapi(String activation_code,
                             Context context, boolean isProgress,
                             final ModelManagerListener listener) {


        Map<String, String> params = new HashMap<>();
        params.put("activation_code", activation_code);
        LinkApi.getDomain(context);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.activation), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // CREATE REQUEST
    public static void createRequest(String token, String link,
                                     String startLat, String startLong, String startLocation,
                                     String endLat, String endLong, String endLocation, String estimateDistance, Context context,
                                     boolean isProgress,String promocode_value, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("link", link);
        params.put("startLat", startLat);
        params.put("startLong", startLong);
        params.put("startLocation", startLocation);
        params.put("endLat", endLat);
        params.put("endLong", endLong);
        params.put("endLocation", endLocation);
        params.put("endLong", endLong);
        params.put("estimateDistance", estimateDistance);
        params.put("promocode", promocode_value);

        Log.e("params", "params:" + params.toString());

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.CREATE_REQUEST), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // APPLY PROMOCODE
    public static void applyPromo( String id,
                                     String promo_code, String amount, Context context,
                                     boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("promo_code", promo_code);
        params.put("amount", amount);

        Log.e("params", "params:" + params.toString());

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.APPLY_PROMO_CODE), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // DRIVER ONLINE
    public static void online(String token, Context context, boolean isProgress,
                              final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("status", "1");

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.UPDATE_STATUS), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void changeStatus(String token, String tripId, String status, Context context, boolean isProgress,
                                    final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("tripId", tripId);
        params.put("status", status);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.CHANGE_STATUS), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // DRIVER UPDATE COORDINATE
    public static void updateCoordinate(String token, String lat, String lon,
                                        Context context, boolean isProgress,
                                        final ModelManagerListener listener) {


        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("lat", lat);
        params.put("long", lon);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.UPDATE_COORDINATE), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // DRIVER START TRIP
    public static void startTrip(String token, String tripId, Context context,
                                 boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("tripId", tripId);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.START_TRIP), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // DRIVER END TRIP
    public static void endTrip(String token, String tripId, String distance,
                               Context context, boolean isProgress,
                               final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("tripId", tripId);
        params.put("distance", distance);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.END_TRIP), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // DRIVER OFFLINE
    public static void offline(String token, Context context,
                               boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("status", "0");

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.UPDATE_STATUS), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // DRIVER OFFLINE
    public static void driverConfirm(String token, String requestId, String startLat, String startLong, String startLocation,
                                     Context context, boolean isProgress,
                                     final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("startLat", startLat);
        params.put("startLong", startLong);
        params.put("startLocation", startLocation);
        params.put("requestId", requestId);
        Log.e("parame", "parame:" + params.toString());

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.DRIVER_CONFIRM), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void driverArrived(String token, String tripId,
                                     Context context, boolean isProgress,
                                     final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("tripId", tripId);

        new HttpGet(context, LinkApi.getLinkApi(context, LinkApi.DRIVER_ARRIVED), params, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // CANCEL TRIP
    public static void cancelTrip(String token, String tripId, Context context,
                                  boolean isProgress, final ModelManagerListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("tripId", tripId);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.CANCEL_TRIP), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // SEND MESSAGE
    public static void sendMessage(String token, String tripId,String receiver_id, String message, Context context,
                                  boolean isProgress, final ModelManagerListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("tripid", tripId);
        params.put("receiver_id", receiver_id);
        params.put("message", message);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.SEND_MESSAGE), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // GET CHAT
    public static void getChat(String token, String tripId,Context context,
                                   boolean isProgress, final ModelManagerListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("tripid", tripId);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.GET_CHAT), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // CANCEL TRIP
    public static void cancelRequestByPassenger(String token, Context context,
                                                boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("driver", "0");

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.CANCEL_REQUEST), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void cancelRequestByPassengerTest(String token, Context context,
                                                    boolean isProgress, final ManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("driver", "0");

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.CANCEL_REQUEST), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError(new VolleyError());
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    // LOGOUT
    public static void logout(String token, Context context, boolean isProgress,
                              final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.LOGOUT), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // TRIP HISTORY
    public static void showTripHistory(String token, String page,
                                       Context context, boolean isProgress,
                                       final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        Log.e("token", "token:" + token);
        params.put("page", page);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.TRIP_HISTORY), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // TRANSACTION HISTORY
    public static void showTransactionHistory(String token, String page,
                                              Context context, boolean isProgress,
                                              final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("page", page);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.TRANSACTION_HISTORY), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // INFO PROFILE
    public static void showInfoProfile(String token, Context context,
                                       boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        Log.e("token", "token:" + token);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.USERINFO), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // UPDAT PROFILE USER
    public static void updateProfileSocial(String token, Context context, boolean isProgress, String phone, String payout,
                                           String description, String address, String stateId, String cityId,
                                           final ModelManagerListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("description", description);
        params.put("account", payout);
        params.put("address", address);
        params.put("phone", phone);
        params.put("cityId", cityId);
        params.put("stateId", stateId);
        Log.e("param", "param:" + params.toString());

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.UPDAT_PROFILE), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void updateProfile(String token, Context context, boolean isProgress, String phone,
                                     String description, String address, String stateId, String cityId, String full_name, String typeDevice, String account, String image,
                                     final ModelManagerListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("description", description);
        params.put("full_name", full_name);
        params.put("address", address);
        params.put("phone", phone);
        params.put("cityId", cityId);
        params.put("stateId", stateId);
        params.put("account", account);
        params.put("type_device", typeDevice);
        params.put("image", image);
        Log.e("param", "param:" + params.toString());

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.UPDAT_PROFILE), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // PAY OUT
    public static void payout(String token, String amount, Context context,
                              boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("amount", amount);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.PAYOUT), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                    Log.e("eee", "" + response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // TRANSFER
    public static void transfer(String token, String amount,
                                String receiverEmail, String note, Context context,
                                boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("amount", amount);
        params.put("receiverEmail", receiverEmail);
        params.put("note", note);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.TRANSFER), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // SEARCH USER TRANSFER
    public static void searchUser(String token, String phone, Context context,
                                  boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("phone", phone);
       /// params.put("email", email);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.SEARCH_USER), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // PAYMENT
    public static void payment(String token, String amount,
                               String transactionID, String PaymentMethod, Context context,
                               boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("amount", amount);
        params.put("transactionId", transactionID);
        params.put("paymentMethod", PaymentMethod);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.PAYMENT), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Tab PAYMENT
    public static void tabPayment(String user_id, String amount, Context context,
                               boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("id", user_id);
        params.put("amount", amount);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.TAB_PAYMENT), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }
    // Show My Request
    public static void showMyRequest(String token, Context context,
                                     boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.SHOW_MY_REQUEST), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Show My Request For User
    public static void showMyRequestForUser(String token, Context context,
                                            boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("driver", "0");

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.SHOW_MY_REQUEST), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    /* RATE DRIVER */
    public static void rateDriver(String token, String tripId, String rate,
                                  Context context, boolean isProgress,
                                  final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("tripId", tripId);
        params.put("rate", rate);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.RATE_DRIVER), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    /* RATE DRIVER */
    public static void rateCustomer(String token, String tripId, String rate,
                                    Context context, boolean isProgress,
                                    final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("tripId", tripId);
        params.put("rate", rate);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.RATE_CUSTOMER), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    /* DRIVER REGISTER */
    public static void driverRegister(String token, String carPlate, String identity,
                                      String brand, String model, String year, String status,
                                      String account, String typeCar, Bitmap imageOne, Bitmap imageTwo, File document, Context context, boolean isProgress,
                                      final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        /* ADD PARAM */
        params.put("token", token);
        params.put("carPlate", carPlate);
        params.put("identity", identity);
        params.put("brand", brand);
        params.put("model", model);
        params.put("year", year);
        params.put("status", status);
        params.put("account", account);
        params.put("link_type", typeCar);
        /* ADD IMAGE ONE*/
        params.put("image", convertBitmapListToArrayJson(imageOne));
        /* ADD IMAGE ONE*/
        params.put("image2", convertBitmapListToArrayJson(imageTwo));
        /* ADD FILE*/
        String encodedBase64 = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(document);
            byte[] bytes = new byte[(int) document.length()];
            fileInputStreamReader.read(bytes);
            encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("document", encodedBase64);
        params.put("document_name", document.getName());

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.REGISTER_DRIVER), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                Log.e("Cuongpm", "Cuongpm" + response);
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Update Profile driver
    public static void updateProfileDriverSocial(String token, String carPlate,
                                                 String brand, String model, String year, String status,
                                                 String phone, String stateId, String cityId, Bitmap imgPhoto1, Bitmap imgPhoto2, File document, Context context, boolean isProgress,
                                                 final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        /* ADD PARAM */
        params.put("token", token);
        params.put("carPlate", carPlate);
        params.put("brand", brand);
        params.put("model", model);
        params.put("year", year);
        params.put("status", status);
        params.put("phone", phone);
        params.put("stateId", stateId);
        params.put("cityId", cityId);
        Log.e("param", "param:" + params.toString());
        /* ADD IMAGE ONE*/
        params.put("image", convertBitmapListToArrayJson(imgPhoto1));
        /* ADD IMAGE TWO*/
        params.put("image2", convertBitmapListToArrayJson(imgPhoto2));
        /* ADD FILE*/
        String encodedBase64 = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(document);
            byte[] bytes = new byte[(int) document.length()];
            fileInputStreamReader.read(bytes);
            encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("document", encodedBase64);
        params.put("document_name", document.getName());


        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.UPDATE_PROFILE_DRIVER), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void updateProfileDriver(String token, String carPlate, String identity,
                                           String brand, String model, String year, String status,
                                           String phone, String stateId, String cityId, String address, String des, String full_name, String typeCar, String account, String image
            , String type_device, Bitmap imgPhoto1, Bitmap imgPhoto2, File document, Context context, boolean isProgress,
                                           final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        /* ADD PARAM */
        params.put("token", token);
        params.put("carPlate", carPlate);
        params.put("brand", brand);
        params.put("model", model);
        params.put("year", year);
        params.put("status", status);
        params.put("phone", phone);
        params.put("stateId", stateId);
        params.put("cityId", cityId);
        params.put("address", address);
        params.put("description", des);
        params.put("full_name", full_name);
        params.put("link_type", typeCar);
        params.put("identity", identity);
        params.put("image_avatar", image);
        params.put("type_device", type_device);
        params.put("account", account);

        /* ADD IMAGE ONE*/
        params.put("image", convertBitmapListToArrayJson(imgPhoto1));
        /* ADD IMAGE TWO*/
        params.put("image2", convertBitmapListToArrayJson(imgPhoto2));
        /* ADD FILE*/
        String encodedBase64 = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(document);
            byte[] bytes = new byte[(int) document.length()];
            fileInputStreamReader.read(bytes);
            encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("document", encodedBase64);
        params.put("document_name", document.getName());
        Log.e("param", "param:" + params.toString());


        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.UPDATE_PROFILE_DRIVER), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    @SuppressWarnings("resource")
    public static byte[] readBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            throw new IOException("Could not completely read file "
                    + file.getName() + " as it is too long (" + length
                    + " bytes, max supported " + Integer.MAX_VALUE + ")");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    // DRIVER END TRIP
    public static void tripPayment(String token, String tripId,String paymentMethod,
                                   Context context, boolean isProgress,
                                   final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("tripId", tripId);
        params.put("paymentMethod",paymentMethod);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.TRIP_PAYMENT), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // SHOW DISTANCE
    public static void showDistance(String token, String tripId,
                                    Context context, boolean isProgress,
                                    final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("tripId", tripId);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.SHOW_DISTANCE), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Show Trip Detail
    public static void showTripDetail(String token, String tripId,
                                      Context context, boolean isProgress,
                                      final ModelManagerListener listener) {
        Log.e("tripId", "tripId:" + tripId + "-" + token);
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("tripId", tripId);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.SHOW_TRIP_DETAIL), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Share app
    public static void shareApp(String token, String type, String social,
                                Context context, boolean isProgress,
                                final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("type", type);
        params.put("social", social);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.SHARE), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void getGeneralSettings(String token, Context context,
                                          boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.GENERAL_SETTINGS), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void sendNeedHelp(String token, String id, Context context,
                                    boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("tripId", id);

        new HttpPost(context, LinkApi.getLinkApi(context, LinkApi.NEED_HELP), params, HttpRequest.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void getTotalDriversAroundLocation(Context context, double lat, double lng, double distance, String carType,
                                                     boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("startLat", lat + "");
        params.put("startLong", lng + "");
        params.put("carType", carType);
        params.put("distance", distance + "");//km

        new HttpGet(context, LinkApi.getLinkApi(context, LinkApi.SEARCH_DRIVER_NUMBER), params, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void getAllSates(Context context,
                                   boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();

        new HttpGet(context, LinkApi.getLinkApi(context, LinkApi.SHOW_STATE_CITY), params, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }


    public static void sendStripRequest(Context context, String token, String amount, String email,
                                        boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("amount", amount);
        params.put("email", email);

        new HttpGet(context, LinkApi.SEND_TRIP_REQUEST, params, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void getLocationDriver(Context context, String driverId,
                                         boolean isProgress, final ModelManagerListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("driverId", driverId);

        new HttpGet(context, LinkApi.getLinkApi(context, LinkApi.GET_DRIVER_LOCATION), params, isProgress, new HttpListener() {
            @Override
            public void onHttpResponse(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static String convertBitmapListToArrayJson(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 90, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }


}
