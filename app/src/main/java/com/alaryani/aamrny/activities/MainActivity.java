package com.alaryani.aamrny.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.alaryani.aamrny.utility.AppUtil;
import com.androidquery.AQuery;
import com.firebase.client.Firebase;
import com.google.android.gms.location.LocationListener;
import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.ServiceUpdateLocation;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.fragment.BeforeOnlineFragment;
import com.alaryani.aamrny.fragment.HelpFragment;
import com.alaryani.aamrny.fragment.HistoryFragment;
import com.alaryani.aamrny.fragment.PassengerPage1Fragment;
import com.alaryani.aamrny.fragment.PaymentFragment;
import com.alaryani.aamrny.fragment.ProfileFragment;
import com.alaryani.aamrny.fragment.RegisterFragment;
import com.alaryani.aamrny.fragment.ShareFragment;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.service.FusedLocationService;
import com.alaryani.aamrny.service.GPSTracker;
import com.alaryani.aamrny.slidingmenu.SlidingMenu;
import com.alaryani.aamrny.utility.ImageUtil;
import com.alaryani.aamrny.widget.CircleImageView;
import com.alaryani.aamrny.widget.TextViewFontAwesome;
import com.alaryani.aamrny.widget.TextViewRaleway;

import java.io.FileNotFoundException;
import java.util.Locale;

public class MainActivity extends BaseActivity implements LocationListener {

    // --------------------------------------------
    // --- Menu layout
    // --------------------------------------------
    private CircleImageView imgAvartar;
    private TextViewRaleway txtPoint;
    private TextViewRaleway txtUserName;
    private RatingBar ratingBar;
    private LinearLayout btnHome;
    private TextViewFontAwesome icHome;
    private TextViewRaleway lblHome;
    private LinearLayout btnProfile;
    private TextViewFontAwesome icProfile;
    private TextViewRaleway lblProfile;
    private LinearLayout btnPayment;
    private TextViewFontAwesome icPayment;
    private TextViewRaleway lblPayment;
    private LinearLayout btnShare;
    private TextViewFontAwesome icShare;
    private TextViewRaleway lblShare;
    private LinearLayout btnHelp;
    private TextViewFontAwesome icHelp;
    private TextViewRaleway lblHelp;
    private LinearLayout btnTripHistory;
    private TextViewFontAwesome icTripHistory;
    private TextViewRaleway lblTripHistory;
    private LinearLayout btnOnline;
    private TextViewFontAwesome icOnline;
    private TextViewRaleway lblOnline;
    private LinearLayout btnRegisterDriver;
    private TextViewFontAwesome icRegisterDriver;
    private TextViewRaleway lblRegisterDriver;
    private LinearLayout btnLanguage;
    private TextViewFontAwesome icLanguage;
    private TextViewRaleway lblLanguage, lbl_logout;
    private LinearLayout btnLogout;

    // ============== For fragment ==========
    public static final int PROFILE = 0;
    public static final int TRIP_HISTORY = 1;
    public static final int PASSENGER_PAGE1 = 2;
    public static final int HELP = 3;
    public static final int REGISTER_AS_DRIVER = 4;
    public static final int WAIT_DRIVER_CONFIRM = 5;
    public static final int ONLINE = 6;
    public static final int PAYMENT = 7;
    public static final int SHARE = 8;

    public static final int TOTAL_FRAGMENT = 10;
    public static final int CHANGELANGUAGE = 9;
    public static final String MY_PREFS_NAME = "LinkApp";

    public final int REQUEST_IMAGE_GALLERY_IMAGE_ONE = 0;
    public final int REQUEST_IMAGE_CAPTURE_IMAGE_ONE = 1;
    public final int REQUEST_IMAGE_GALLERY_IMAGE_TWO = 2;
    public final int REQUEST_IMAGE_CAPTURE_IMAGE_TWO = 3;
    public final int FILE_SELECT_CODE = 4;

    private int currentFragment = PROFILE;
    private Fragment fragments[];
    private FragmentManager fm;
    public SlidingMenu menu;
    public boolean shareFaceBook = false;

    // User user = new User();
    AQuery aq;

    public PreferencesManager preferencesManager;

    LocationManager locationManager;
    public static final String DEFAULT_LAT = "-28.9323405";
    public static final String DEFAULT_LONG = "135.6806593";
    GPSTracker tracker;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        preferencesManager = PreferencesManager.getInstance(context);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        initMenu();
        initFragment();
        initMenuControl();
        getInfoMenu();
        updateMyLocation();
        checkGPS();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (globalValue.getUser() == null || globalValue.getUser().getPhone() == null || globalValue.getUser().getPhone().equals("")) {
            gotoActivity(EditProfileFirstActivity.class);
            finish();
        }
        preferencesManager.setPassengerCurrentScreen("");


    }

    private void checkGPS() {
        if (!locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showSettingsAlert();
        }
    }

    public void updateMyLocation() {
        tracker = new GPSTracker(this);
        if (tracker.canGetLocation() == false) {
            tracker.showSettingsAlert();
            showToastMessage(R.string.message_wait_for_location);
        }
    }

    // --------------------------------------------
    // --- Menu Manager
    // --------------------------------------------

    private void initMenu() {
        // Get header menu
        menu = new SlidingMenu(this);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.5f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.layout_menu);

        // Get content menu
        imgAvartar = (CircleImageView) findViewById(R.id.img_avartar);
        txtPoint = (TextViewRaleway) findViewById(R.id.txt_point);
        txtUserName = (TextViewRaleway) findViewById(R.id.txt_user_name);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnHome = (LinearLayout) findViewById(R.id.btn_home);
        icHome = (TextViewFontAwesome) findViewById(R.id.ic_home);
        lblHome = (TextViewRaleway) findViewById(R.id.lbl_home);
        btnProfile = (LinearLayout) findViewById(R.id.btn_profile);
        icProfile = (TextViewFontAwesome) findViewById(R.id.ic_profile);
        lblProfile = (TextViewRaleway) findViewById(R.id.lbl_profile);
        btnPayment = (LinearLayout) findViewById(R.id.btn_payment);
        icPayment = (TextViewFontAwesome) findViewById(R.id.ic_payment);
        lblPayment = (TextViewRaleway) findViewById(R.id.lbl_payment);
        btnShare = (LinearLayout) findViewById(R.id.btn_share);
        icShare = (TextViewFontAwesome) findViewById(R.id.ic_share);
        lblShare = (TextViewRaleway) findViewById(R.id.lbl_share);
        btnHelp = (LinearLayout) findViewById(R.id.btn_help);
        icHelp = (TextViewFontAwesome) findViewById(R.id.ic_help);
        lblHelp = (TextViewRaleway) findViewById(R.id.lbl_help);
        btnTripHistory = (LinearLayout) findViewById(R.id.btn_trip_history);
        icTripHistory = (TextViewFontAwesome) findViewById(R.id.ic_trip_history);
        lblTripHistory = (TextViewRaleway) findViewById(R.id.lbl_trip_history);
        btnOnline = (LinearLayout) findViewById(R.id.btn_online);
        icOnline = (TextViewFontAwesome) findViewById(R.id.ic_online);
        lblOnline = (TextViewRaleway) findViewById(R.id.lbl_online);
        btnRegisterDriver = (LinearLayout) findViewById(R.id.btn_register_driver);
        icRegisterDriver = (TextViewFontAwesome) findViewById(R.id.ic_register_driver);
        lblRegisterDriver = (TextViewRaleway) findViewById(R.id.lbl_register_driver);
        btnLanguage = (LinearLayout) findViewById(R.id.btn_language);
        icLanguage = (TextViewFontAwesome) findViewById(R.id.ic_language);
        lblLanguage = (TextViewRaleway) findViewById(R.id.lbl_language);
        lbl_logout = (TextViewRaleway) findViewById(R.id.lbl_logout);
        btnLogout = (LinearLayout) findViewById(R.id.btn_logout);
        setMenuByUserType();
    }

    public void setMenuByUserType() {
        if (preferencesManager.isUser()) {
            btnOnline.setVisibility(View.GONE);
        } else {
            if (preferencesManager.isActiveDriver()) {
                btnOnline.setVisibility(View.VISIBLE);
                btnRegisterDriver.setVisibility(View.GONE);
            } else {
                btnOnline.setVisibility(View.GONE);
                btnRegisterDriver.setVisibility(View.GONE);
            }
        }
    }

    public void showMenu() {
        menu.showMenu();

    }

    private void initMenuControl() {
        btnProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(PROFILE);
                menu.showContent(true);
            }
        });

        btnTripHistory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(TRIP_HISTORY);
                menu.showContent(true);
            }
        });

        btnLanguage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // showLanguageDialog();
                showFragment(CHANGELANGUAGE);
                menu.showContent(true);
//                updateLanguage();
            }
        });

        btnHelp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(HELP);
                menu.showContent(true);
            }
        });

        btnRegisterDriver.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(REGISTER_AS_DRIVER);
                menu.showContent(true);
            }
        });

        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(PASSENGER_PAGE1);
                menu.showContent(true);
            }
        });

        btnOnline.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(ONLINE);
                menu.showContent(true);
            }
        });

        btnPayment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(PAYMENT);
                menu.showContent(true);
            }
        });

        btnShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(SHARE);
                menu.showContent(true);
            }
        });

        btnLogout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
    }

    Locale myLocale;

    public void updateLanguage() {
        if (preferencesManager.isChinase()) {
            myLocale = new Locale("en");
            preferencesManager.setIsEnglish();

        } else {
            myLocale = new Locale("zh");
            preferencesManager.setIsChinese();
        }

        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;

        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        lblHome.setText(R.string.lbl_home);
        lblLanguage.setText(R.string.lbl_chinese);
        lblProfile.setText(R.string.lbl_profile);
        lblHelp.setText(R.string.lbl_help);
        lblPayment.setText(R.string.lbl_payment);
        lblShare.setText(R.string.lbl_share);
        lblTripHistory.setText(R.string.lbl_trip_history);
        lblOnline.setText(R.string.lbl_online);
        lblRegisterDriver.setText(R.string.lbl_register_as_driver);
        lbl_logout.setText(R.string.lbl_logout);

        ((ProfileFragment) fragments[PROFILE]).changeLanguage();
        ((PassengerPage1Fragment) fragments[PASSENGER_PAGE1]).changeLanguage();
        ((PaymentFragment) fragments[PAYMENT]).changeLanguage();
        ((RegisterFragment) fragments[REGISTER_AS_DRIVER]).changeLanguage();
        ((ShareFragment) fragments[SHARE]).changeLanguage();
        ((BeforeOnlineFragment) fragments[ONLINE]).changeLanguage();
        ((HelpFragment) fragments[HELP]).changeLanguage();
        ((HistoryFragment) fragments[TRIP_HISTORY]).changeLanguage();

    }

    // logout
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

    public void getDataUser() {
        ModelManager.showInfoProfile(PreferencesManager.getInstance(self)
                .getToken(), self, true, new ModelManagerListener() {
            @Override
            public void onSuccess(String json) {
                if (ParseJsonUtil.isSuccess(json)) {
                    globalValue.setUser(ParseJsonUtil.parseInfoProfile(json));
                    getInfoMenu();
                }
            }

            @Override
            public void onError() {

            }
        });
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

    private void updateMenuUI() {
        if (currentFragment == PROFILE) {
            btnProfile.setBackgroundColor(getResources()
                    .getColor(R.color.primary));
            icProfile.setTextColor(getResources().getColor(
                    R.color.white));
            lblProfile.setTextColor(getResources().getColor(
                    R.color.white));
        } else {
            btnProfile.setBackgroundColor(getResources().getColor(
                    R.color.bg_menu));
            icProfile.setTextColor(getResources().getColor(R.color.white));
            lblProfile.setTextColor(getResources().getColor(R.color.white));
        }

        if (currentFragment == TRIP_HISTORY) {
            btnTripHistory.setBackgroundColor(getResources().getColor(
                    R.color.primary));
            icTripHistory.setTextColor(getResources().getColor(
                    R.color.white));
            lblTripHistory.setTextColor(getResources().getColor(
                    R.color.white));
        } else {
            btnTripHistory.setBackgroundColor(getResources().getColor(
                    R.color.bg_menu));
            icTripHistory.setTextColor(getResources().getColor(R.color.white));
            lblTripHistory.setTextColor(getResources().getColor(R.color.white));
        }

        if (currentFragment == HELP) {
            btnHelp.setBackgroundColor(getResources().getColor(R.color.primary));
            icHelp.setTextColor(getResources().getColor(
                    R.color.white));
            lblHelp.setTextColor(getResources().getColor(
                    R.color.white));
        } else {
            btnHelp.setBackgroundColor(getResources().getColor(
                    R.color.bg_menu));
            icHelp.setTextColor(getResources().getColor(R.color.white));
            lblHelp.setTextColor(getResources().getColor(R.color.white));
        }

        if (currentFragment == ONLINE) {
            btnOnline
                    .setBackgroundColor(getResources().getColor(R.color.primary));
            icOnline.setTextColor(getResources().getColor(
                    R.color.white));
            lblOnline.setTextColor(getResources().getColor(
                    R.color.white));
        } else {
            btnOnline.setBackgroundColor(getResources().getColor(
                    R.color.bg_menu));
            icOnline.setTextColor(getResources().getColor(R.color.white));
            lblOnline.setTextColor(getResources().getColor(R.color.white));
        }

        if (currentFragment == REGISTER_AS_DRIVER) {
            btnRegisterDriver.setBackgroundColor(getResources().getColor(
                    R.color.primary));
            icRegisterDriver.setTextColor(getResources().getColor(
                    R.color.white));
            lblRegisterDriver.setTextColor(getResources().getColor(
                    R.color.white));
        } else {
            btnRegisterDriver.setBackgroundColor(getResources().getColor(
                    R.color.bg_menu));
            icRegisterDriver.setTextColor(getResources()
                    .getColor(R.color.white));
            lblRegisterDriver.setTextColor(getResources().getColor(
                    R.color.white));
        }

        if (currentFragment == PASSENGER_PAGE1) {
            btnHome.setBackgroundColor(getResources().getColor(R.color.primary));
            icHome.setTextColor(getResources().getColor(
                    R.color.white));
            lblHome.setTextColor(getResources().getColor(
                    R.color.white));
        } else {
            btnHome.setBackgroundColor(getResources().getColor(
                    R.color.bg_menu));
            icHome.setTextColor(getResources().getColor(R.color.white));
            lblHome.setTextColor(getResources().getColor(R.color.white));
        }

        if (currentFragment == PAYMENT) {
            btnPayment.setBackgroundColor(getResources()
                    .getColor(R.color.primary));
            icPayment.setTextColor(getResources().getColor(
                    R.color.white));
            lblPayment.setTextColor(getResources().getColor(
                    R.color.white));
        } else {
            btnPayment.setBackgroundColor(getResources().getColor(
                    R.color.bg_menu));
            icPayment.setTextColor(getResources().getColor(R.color.white));
            lblPayment.setTextColor(getResources().getColor(R.color.white));
        }

        if (currentFragment == SHARE) {
            btnShare.setBackgroundColor(getResources().getColor(R.color.primary));
            icShare.setTextColor(getResources().getColor(
                    R.color.white));
            lblShare.setTextColor(getResources().getColor(
                    R.color.white));
        } else {
            btnShare.setBackgroundColor(getResources().getColor(
                    R.color.bg_menu));
            icShare.setTextColor(getResources().getColor(R.color.white));
            lblShare.setTextColor(getResources().getColor(R.color.white));
        }
    }

    // --------------------------------------------
    // --- Fragment Manager
    // --------------------------------------------
    private void initFragment() {
        fm = getSupportFragmentManager();
        fragments = new Fragment[TOTAL_FRAGMENT];
        fragments[PROFILE] = fm.findFragmentById(R.id.fragmentProfileFragment);
        fragments[TRIP_HISTORY] = fm
                .findFragmentById(R.id.fragmentHistoryFragment);
        fragments[HELP] = fm.findFragmentById(R.id.fragmentHelpFragment);
        fragments[REGISTER_AS_DRIVER] = fm
                .findFragmentById(R.id.fragmentRegisterFragment);
        fragments[PASSENGER_PAGE1] = fm
                .findFragmentById(R.id.fragmentPassenger1);
        fragments[WAIT_DRIVER_CONFIRM] = fm
                .findFragmentById(R.id.fragmentWaitDriverConfirmFragment);
        fragments[ONLINE] = fm
                .findFragmentById(R.id.fragmentBeforeOnlineFragment);
        fragments[PAYMENT] = fm.findFragmentById(R.id.fragmentPaymentFragment);
        fragments[SHARE] = fm.findFragmentById(R.id.fragmentShareFragment);
        fragments[CHANGELANGUAGE] = fm.findFragmentById(R.id.fragmentLanguageChange);
        showFragment(PASSENGER_PAGE1);
    }

    public void showFragment(int fragmentIndex) {
        FragmentTransaction transaction = fm.beginTransaction();
        currentFragment = fragmentIndex;
        updateMenuUI();
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        if (tracker != null) {
            tracker.stopUsingGPS();
        }
        stopService(new Intent(MainActivity.this, ServiceUpdateLocation.class));
        super.onDestroy();
    }

    // --------------------------------------------
    // --- Main activity
    // --------------------------------------------
    @Override
    public void onBackPressed() {
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
                                stopService(new Intent(MainActivity.this, FusedLocationService.class));
                                finish();
                                overridePendingTransition(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_right);
                            }
                        }).setNegativeButton(android.R.string.cancel, null)
                .create().show();
    }

    // get data info user menu
    public void getInfoMenu() {
        aq = new AQuery(self);
        txtUserName.setText(globalValue.getUser().getFullName());

        if (globalValue.getUser().getDriverObj() != null) {
            if (globalValue.getUser().getDriverObj().getDriverRate() != null) {
                if (!globalValue.getUser().getDriverObj().getDriverRate().equalsIgnoreCase(""))
                    ratingBar.setRating(Float.parseFloat(globalValue.getUser().getDriverObj().getDriverRate()) / 2);
            } else {
                if (!globalValue.getUser().getPassengerRate().equals("")) {
                    ratingBar.setRating(Float.parseFloat(globalValue.getUser().getPassengerRate()) / 2);
                }
            }

        } else {
            if (!globalValue.getUser().getPassengerRate().equals("")) {
                ratingBar.setRating(Float.parseFloat(globalValue.getUser().getPassengerRate()) / 2);
            }
        }

        String point = getResources().getString(R.string.message_point);
        txtPoint.setText(point + " " + globalValue.getUser().getBalance());
        aq.id(imgAvartar).image(globalValue.getUser().getLinkImage());
        // Check user permission
        if (globalValue.getUser().getDriverObj().getIsActive() == null) {
            preferencesManager.setIsUser();
            preferencesManager.setDriverIsUnActive();
        } else {
            if (globalValue.getUser().getDriverObj().getIsActive().equals("0")) {
                preferencesManager.setIsDriver();
                preferencesManager.setDriverIsUnActive();
            } else {
                preferencesManager.setIsDriver();
                preferencesManager.setDriverIsActive();
            }
        }
        setMenuByUserType();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataUser();
        ((PassengerPage1Fragment) fragments[PASSENGER_PAGE1]).back();
        preferencesManager.setStartWithOutMain(false);
        preferencesManager.setPassengerWaitConfirm(false);
        txtPoint.setText("Point: "
                + GlobalValue.getInstance().getUser().getBalance());
        showHistoryTripIfNotify();

    }

    public int getSquareCropDimensionForBitmap(Bitmap bitmap) {
        return Math.min(bitmap.getWidth(), bitmap.getHeight());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        if (shareFaceBook) {
            ((ShareFragment) fragments[SHARE]).shareFaceBook(requestCode,
                    resultCode, imageReturnedIntent);
        }
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case REQUEST_IMAGE_GALLERY_IMAGE_ONE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    Bitmap image = null;
                    Bitmap imageConvert = null;
                    try {
                        image = ImageUtil.decodeUri(self, selectedImage);
                        Log.e("trangpv", "size 1 decode: " + image.getByteCount());
                        int dimension = getSquareCropDimensionForBitmap(image);
                        imageConvert = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ((RegisterFragment) fragments[REGISTER_AS_DRIVER]).setImageOne(
                            selectedImage, imageConvert);
                }
                break;
            case REQUEST_IMAGE_CAPTURE_IMAGE_ONE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    int dimension = getSquareCropDimensionForBitmap(imageBitmap);
                    Bitmap imageConvert = ThumbnailUtils.extractThumbnail(imageBitmap, dimension, dimension);
                    ((RegisterFragment) fragments[REGISTER_AS_DRIVER])
                            .setImageOne(imageConvert);
                }
                break;
            case REQUEST_IMAGE_GALLERY_IMAGE_TWO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    Bitmap image = null;
                    Bitmap imageConvert = null;
                    try {
                        image = ImageUtil.decodeUri(self, selectedImage);
                        int dimension = getSquareCropDimensionForBitmap(image);
                        imageConvert = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                        Log.d("trangpv", "size 2 decode: " + image.getByteCount());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    ((RegisterFragment) fragments[REGISTER_AS_DRIVER]).setImageTwo(
                            selectedImage, imageConvert);
                }
                break;
            case REQUEST_IMAGE_CAPTURE_IMAGE_TWO:
                if (resultCode == RESULT_OK) {
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageConvert = null;
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    int dimension = getSquareCropDimensionForBitmap(imageBitmap);
                    imageConvert = ThumbnailUtils.extractThumbnail(imageBitmap, dimension, dimension);
                    ((RegisterFragment) fragments[REGISTER_AS_DRIVER])
                            .setImageTwo(imageConvert);
                }
                break;
        }
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }

    public void choiseImageOne() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.select_photo_from)
                .setPositiveButton(R.string.gallery,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // one can be replaced with any action code
                                Intent pickPhoto = new Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto,
                                        REQUEST_IMAGE_GALLERY_IMAGE_ONE);
                            }
                        })
                .setNegativeButton(R.string.camera,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // zero can be replaced with any action code
                                Intent takePictureIntent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent
                                        .resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(takePictureIntent,
                                            REQUEST_IMAGE_CAPTURE_IMAGE_ONE);
                                }
                            }
                        }).create().show();
    }

    public void choiseImageTwo() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.select_photo_from)
                .setPositiveButton(R.string.gallery,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // one can be replaced with any action code
                                Intent pickPhoto = new Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto,
                                        REQUEST_IMAGE_GALLERY_IMAGE_TWO);
                            }
                        })
                .setNegativeButton(R.string.camera,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // zero can be replaced with any action code
                                Intent takePictureIntent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent
                                        .resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(takePictureIntent,
                                            REQUEST_IMAGE_CAPTURE_IMAGE_TWO);
                                }
                            }
                        }).create().show();
    }

    @Override
    public void onLocationChanged(Location location) {
//        updateCoordinate(location.getLatitude() + "", location.getLongitude() + "");
    }

    /**
     * Updated 7-30-15 Show history
     */
    public void showHistoryTripIfNotify() {
        if (preferencesManager.IsHistoryPush()) {
            showFragment(TRIP_HISTORY);
            menu.showContent(true);
            preferencesManager.setHistoryPush(false);
        }

    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.gps_setting));
        // Setting Dialog Message
        alertDialog
                .setMessage(getString(R.string.alert_gps));
        // On pressing Settings button
        alertDialog.setPositiveButton(getResources().getString(R.string.setting),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
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

}
