package com.alaryani.aamrny.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.gcm.MyGcmSharedPrefrences;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.object.User;
import com.alaryani.aamrny.service.GPSTracker;
import com.alaryani.aamrny.social.facebook.FaceBookManager;
import com.alaryani.aamrny.social.facebook.FbUser;
import com.alaryani.aamrny.social.googleplus.GUser;
import com.alaryani.aamrny.social.googleplus.GooglePlusManager;
import com.alaryani.aamrny.utility.AppUtil;
import com.alaryani.aamrny.widget.TextViewFontAwesome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements FaceBookManager.ICallbackLoginFacebook, GooglePlusManager.ICallbackGoogleLogin {

    // ======= FOR THIS ACTIVITY ===========
    // Logcat tag
    private static final String TAG = "LoginActivity";
    private static final String TYPE_FACEBOOK = "0";
    private static final String TYPE_GOOGLEPLUS = "1";
    private String loginType = "";

    // ======= LOGIN BY FACEBOOK ===========

    RelativeLayout btnLoginFacebook, btnLoginGoogle;
    private boolean mIntentInProgress;
    public static final String MY_PREFS_NAME = "LinkApp";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;
    private TextViewFontAwesome txtRegister, txtForgotPassword, txtLogin, or_login;
    private EditText txtUsername, txtPassword;

    public GooglePlusManager googlePlusManager;
    private GPSTracker gps;
    private double latitude = 0, longitude = 0;
    private SparseIntArray mErrorString;
    private static final int REQUEST_PERMISSIONS = 20;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FaceBookManager.initSdk(self);
        googlePlusManager = new GooglePlusManager(self, this);
        // signOutFromGplus();

        setContentView(R.layout.layout_login);
        gps = new GPSTracker(this);
        initView();
        initControl();
        checkCurrentLanguage();
        context = this;
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("custom-event-name"));
//        checkPermissionsForMarshmallow();
    }

    /*private void checkPermissionsForMarshmallow() {
        mErrorString = new SparseIntArray();

        int currentapiVersion = Build.VERSION.SDK_INT;
        // if current version is M or greater than M
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            String[] array = {Manifest.permission.LOCATION_HARDWARE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WAKE_LOCK, Manifest.permission.VIBRATE, Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE
            };
            requestAppPermissions(array, R.string.permission, REQUEST_PERMISSIONS);
        } else {
            onPermissionsGranted(REQUEST_PERMISSIONS);
        }
    }*/

    // ======= GET VIEW ===========
    private void initView() {
        btnLoginFacebook = (RelativeLayout) findViewById(R.id.btnLoginFacebook);
        btnLoginGoogle = (RelativeLayout) findViewById(R.id.btnLoginGoogle);
        txtRegister = (TextViewFontAwesome) findViewById(R.id.txtRegister);
        or_login = (TextViewFontAwesome) findViewById(R.id.or_login);
        txtForgotPassword = (TextViewFontAwesome) findViewById(R.id.txtForgotPassword);
        txtLogin = (TextViewFontAwesome) findViewById(R.id.txtLogin);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtForgotPassword.setPaintFlags(txtForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void initControl() {
        if (!GlobalValue.DEMO_STATUS) {
            btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginType = TYPE_FACEBOOK;
                    loginFacebook();
                }
            });

            btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("eee", "  onClick 4");
                    loginType = TYPE_GOOGLEPLUS;
                    googlePlusManager.login();
                }
            });
        } else {
            btnLoginFacebook.setBackgroundResource(R.drawable.login_driver);
            btnLoginGoogle.setBackgroundResource(R.drawable.login_passenger);
            btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login("nxhars@gmail.com", "Vin Diesel", "male", "https://lh6.googleusercontent.com/-TGQ6hweCZpk/AAAAAAAAAAI/AAAAAAAAAJ0/8IbXpKGLcUg/photo.jpg");
                }
            });
            btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login("fruity.hieunx@gmail.com", "Christ Ho", "male", "https://graph.facebook.com/162035877492930/picture?type=large");
                }
            });
        }
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    String gcmId = MyGcmSharedPrefrences.getToken(LoginActivity.this);
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String ime = telephonyManager.getDeviceId();
                    if (validate()) {
                        loginAccount(txtUsername.getText().toString(), txtPassword.getText().toString(), gcmId, ime, "1", latitude, longitude);
                    }
                }
            }
        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogForgot();
            }
        });

    }

    public void showDialogForgot() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_forgot_password);
        TextViewFontAwesome txtCancel = (TextViewFontAwesome) dialog.findViewById(R.id.txtCancel);
        TextViewFontAwesome txtSend = (TextViewFontAwesome) dialog.findViewById(R.id.txtSend);
        final EditText txtUsernameDialog = (EditText) dialog.findViewById(R.id.txtUsername);
        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtUsernameDialog.getText().toString().equals("")) {
                    sendDataForgot(txtUsernameDialog.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.message_please_enter_full_information), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void sendDataForgot(String email) {
        ModelManager.forgotPassword(this, email, true, new ModelManagerListener() {
            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(String json) {
                if (ParseJsonUtil.isSuccess(json)) {
                    Toast.makeText(LoginActivity.this, ParseJsonUtil.getMessage(json), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean validate() {
        if (txtUsername.getText().toString().equals("")) {
            txtUsername.requestFocus();
            Toast.makeText(LoginActivity.this, getString(R.string.message_email), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtPassword.getText().toString().equals("")) {
            txtPassword.requestFocus();
            Toast.makeText(LoginActivity.this, getString(R.string.message_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void loginAccount(final String email, final String password, final String gcm_id, final String ime, final String type, final double lat, final double longitude) {
        ModelManager.loginAccount(this, email, password, gcm_id, ime, type, lat, longitude, true, new ModelManagerListener() {
            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(String json) {
                Log.e("json", "here " + json);
                if (ParseJsonUtil.isSuccess(json)) {
                    // Check is user or driver
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.getString("status").equals("SUCCESS")) {
                            if (jsonObject.getJSONObject("data").getString("isActive").equals("0")) {
                                Intent intent = new Intent(context, OTPActivity.class);
                                startActivity(intent);
                            } else {
                                if (ParseJsonUtil.isDriverFromLogin(json)) {
                                    preferencesManager.setIsDriver();
                                    // If is driver check active or not
                                    if (ParseJsonUtil.driverIsActive(json)) {
                                        preferencesManager
                                                .setDriverIsActive();
                                    } else {
                                        preferencesManager
                                                .setDriverIsUnActive();
                                    }
                                } else {
                                    preferencesManager.setIsUser();
                                }

                                // Set Login
                                preferencesManager.setLogin();
                                // Set Token
                                preferencesManager.setToken(ParseJsonUtil
                                        .getTokenFromLogin(json));
                                // Set User Id
                                preferencesManager.setUserID(ParseJsonUtil.getIdFromLogin(json));
                                // gotoActivity(MainActivity.class);
                                gotoActivity(SplashActivity.class);
                                finish();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, ParseJsonUtil.getMessage(json), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, ParseJsonUtil.getMessage(json), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // ======= LOGIN BY FACEBOOK ===========

    // Call login Facebook
    private void loginFacebook() {
        FaceBookManager.login(LoginActivity.this, this);
    }


    private void getUserFacebookData(final User user) {
        ModelManager.checkEmailLogin(user.getEmail(),
                context, true, new ModelManagerListener() {

                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil
                                .isSuccessData(json)) {
                            Log.e("Login",
                                    "Email chua online");
                            String token = preferencesManager
                                    .getToken();
                            Log.e("token", token);
                            login(user.getEmail(), user.getFullName(),
                                    user.getGender(),
                                    user.getLinkImage());
                        } else {
                            Log.e("Login",
                                    "Email da online");
                            String token = preferencesManager
                                    .getToken();
                            Log.e("token", token);
                            ModelManager.checkTokenLogin(
                                    token,
                                    context,
                                    true,
                                    new ModelManagerListener() {

                                        @Override
                                        public void onSuccess(
                                                String json) {
                                            if (ParseJsonUtil
                                                    .isSuccessData(json)) {
                                                Log.e("token",
                                                        "token pass");
                                            } else {
                                                if (preferencesManager
                                                        .isAlreadyLogin()) {
                                                    preferencesManager
                                                            .setLogout();
                                                    logout();
                                                } else {
                                                    login(user.getEmail(),
                                                            user.getFullName(),
                                                            user.getGender(),
                                                            user.getLinkImage());
                                                }

                                            }

                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onError() {
                        Log.e("Login", "Email online");

                    }
                });

    }

    // dialog login

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.msg_logout)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                logout();
                            }
                        }).setNegativeButton(android.R.string.cancel, null)
                .create().show();
    }

    private void logout() {
        ModelManager.logout(PreferencesManager.getInstance(context).getToken(),
                this, true, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {

                        if (ParseJsonUtil.isSuccess(json)) {
                            logoutApp();

                        } else {
                            logoutApp();
                        }
                    }

                    @Override
                    public void onError() {

                        showToastMessage(R.string.message_have_some_error);
                    }
                });
    }

    private void logoutApp() {
        PreferencesManager.getInstance(context).setLogout();
        finish();
        gotoActivity(LoginActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    // end

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            showToastMessage(message);
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        gps.stopUsingGPS();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        showQuitDialog();
    }

    private void showQuitDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.msg_quit_app)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                                overridePendingTransition(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_right);
                            }
                        }).setNegativeButton(android.R.string.cancel, null)
                .create().show();
    }

    // ======= LOGIN BY GOOGLE PLUS ===========
    @Override
    public void onActivityResult(int requestCode, int responseCode,
                                 android.content.Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if (loginType.equals(TYPE_FACEBOOK)) {
            // For Facebook
            FaceBookManager.onActivityResult(requestCode, responseCode, data);
        } else {
            if (loginType.equals(TYPE_GOOGLEPLUS)) {
                googlePlusManager.onActivityResult(requestCode, data);
            }
        }
    }

    //getProfileInformation();

    protected void onStart() {
        super.onStart();
        // mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
    }

    // =============================

    // ========= LOGIN TO SERVER =========
    public void login(final String email, final String name, String gender, final String image) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String ime = telephonyManager.getDeviceId();

        if (gender.equals("0") || gender.equals("male")) {
            gender = "Male";
        } else {
            if (gender.equals("1") || gender.equals("female")) {
                gender = "Female";
            }
        }

        if (gps.canGetLocation() == false) {
            gps.showSettingsAlert();
            showToastMessage(R.string.message_wait_for_location);
        } else {
            String lat = gps.getLatitude() + "";
            String lng = gps.getLongitude() + "";
            final String genderData = gender;
            String gcmId = "";
            if (!MyGcmSharedPrefrences.getToken(this).equals("")) {
                gcmId = MyGcmSharedPrefrences.getToken(this);
            }
            Log.e("lat long", "lat long:" + lat + "-" + lng);
            ModelManager.login(gcmId, email, ime, lat, lng, name,
                    genderData, image, this, true, new ModelManagerListener() {
                        @Override
                        public void onSuccess(String json) {

                            if (ParseJsonUtil.isSuccess(json)) {
                                // Check is user or driver
                                if (ParseJsonUtil.isDriverFromLogin(json)) {
                                    preferencesManager.setIsDriver();
                                    // If is driver check active or not
                                    if (ParseJsonUtil.driverIsActive(json)) {
                                        preferencesManager
                                                .setDriverIsActive();
                                    } else {
                                        preferencesManager
                                                .setDriverIsUnActive();
                                    }
                                } else {
                                    preferencesManager.setIsUser();
                                }

                                // Set Login
                                preferencesManager.setLogin();
                                // Set Token
                                preferencesManager.setToken(ParseJsonUtil
                                        .getTokenFromLogin(json));
                                // Set User Id
                                preferencesManager.setUserID(ParseJsonUtil
                                        .getIdFromLogin(json));
                                // gotoActivity(MainActivity.class);
                                gotoActivity(SplashActivity.class);
                                finish();
                            } else {
                                if (ParseJsonUtil.getIsActiveAsDriver(json).equals("1")) {
                                    login(email, name, genderData, image);
                                }

                            }
                        }

                        @Override
                        public void onError() {
                            showToastMessage(R.string.message_have_some_error);
                        }
                    });
        }

    }

    @Override
    public void onLoginFbSuccess(FbUser user) {
        getUserFacebookData(user.toUser());
    }

    @Override
    public void onLoginFbError() {
        showToastMessage(R.string.cannot_login_by_facebook);
    }

    @Override
    public void onLoginFBnoEmailPublic() {
        showToastMessage(R.string.lbl_login_fail_facebook_hidden_email);
    }

    @Override
    public void onLoginFBLoginOrtherUser() {
        showToastMessage(R.string.lbl_login_orther_user);
    }

    @Override
    public void onLoginGgSuccess(GUser user) {
        login(user.getEmail(), user.getFullname(), user.getGender() + "", user.getAvatar());
    }

    @Override
    public void onLoginGgError() {
        showToastMessage(R.string.cannot_login_by_google);
    }

    private void checkCurrentLanguage() {
        if (isCurrentAppLangRtl()) {
            or_login.setGravity(Gravity.RIGHT);
            txtForgotPassword.setGravity(Gravity.LEFT);
        } else {
            txtForgotPassword.setGravity(Gravity.RIGHT);
            or_login.setGravity(Gravity.LEFT);
        }
    }

    /*// check requested permissions are on or off
    public void requestAppPermissions(final String[] requestedPermissions, final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                Snackbar snack = Snackbar.make(findViewById(android.R.id.content), stringId, Snackbar.LENGTH_INDEFINITE);
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snack.setAction("GRANT", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(LoginActivity.this, requestedPermissions, requestCode);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        } else {
            // onPermissionsGranted(requestCode);
        }
    }

    // if permissions granted succesfully
    private void onPermissionsGranted(int requestcode) {

    }*/
}
