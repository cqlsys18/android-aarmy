package com.alaryani.aamrny;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alaryani.aamrny.activities.MainActivity;
import com.alaryani.aamrny.config.Constant;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.utility.AppUtil;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;

public class BaseActivity extends FragmentActivity {
    public static String TAG;
    public static boolean DEBUG_MODE = false;
    protected BaseActivity self;
    protected MainActivity mainActivity;

    protected ProgressDialog progressDialog;
    protected LinearLayout userStatusLayout;
    protected Context context;
    protected PreferencesManager preferencesManager;
    protected GlobalValue globalValue;

    private TextView lblTitle;
    private ImageButton btnBack;


    protected static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(Constant.PAYPAL_CLIENT_APP_ID)
            //.defaultUserEmail(Constant.PAYPAL_RECEIVE_EMAIL_ID)
            .acceptCreditCards(false);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        context = this;
        TAG = self.getClass().getSimpleName();
        preferencesManager = PreferencesManager.getInstance(context);
        globalValue = GlobalValue.getInstance();
        // mainActivity =(MainActivity)this;
    }

    @Override
    public void onResume() {
        preferencesManager.setAppIsShow();
        /* CLEAR PUSH NOTIFICATION */
        NotificationManager notifManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        System.gc();
    }

    @Override
    public void onPause() {
        preferencesManager.setAppIsHide();
        super.onPause();
    }


    // ============ PAYPAL ==============
    protected void requestPaypalPayment(double value, String content,
                                        String currency) {

        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(value),
                currency, content, PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, 1);
    }

    protected void startPaypalService() {
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        startService(intent);
    }

    protected void initUI() {
        lblTitle = (TextView) findViewById(R.id.lblTitle);

    }

    protected void setHeaderTitle(int idString) {
        lblTitle.setText(idString);
    }

    protected void initAndSetHeader(int idString) {
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        lblTitle = (TextView) findViewById(R.id.lblTitle);

        lblTitle.setText(idString);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void initWithoutHeader() {
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    /**
     * Go to other activity
     * <p>
     * //	 * @param context
     * //	 * @param cla
     */

    protected String getPreviousActivityName() {
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle.getString("classFrom") == null) {
                return "";
            } else {
                return bundle.getString("classFrom");
            }
        } catch (Exception e) {
            // TODO: handle exception
            return "";
        }
    }

    public void updateCoordinate(String lat, String lon) {
        if (!lat.isEmpty() && !lon.isEmpty()) {
            ModelManager.updateCoordinate(preferencesManager.getToken(), lat,
                    lon, self, false, new ModelManagerListener() {

                        @Override
                        public void onSuccess(String json) {
                            if (ParseJsonUtil.isSuccess(json)) {

                            } else {
                                showToastMessage(ParseJsonUtil.getMessage(json));
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }

    }

    public void gotoActivity(Class<?> cla) {
        Intent intent = new Intent(this, cla);
        intent.putExtra("classFrom", self.getClass().getSimpleName());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
    }

    public void gotoActivityWithClearTop(Class<?> cla) {
        Intent intent = new Intent(this, cla);
        intent.putExtra("classFrom", self.getClass().getSimpleName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void backActivity(Class<?> cla, Bundle b) {
        Intent intent = new Intent(this, cla);
        intent.putExtras(b);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
    }

    public void backActivity(Class<?> cla) {
        Intent intent = new Intent(this, cla);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public void gotoActivity(Class<?> cla, int flag) {
        Intent intent = new Intent(this, cla);
        intent.setFlags(flag);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
    }

    /**
     * goto view website
     * <p>
     * //	 * @param url
     */
    public void gotoWeb(Uri uri) {
        Intent myIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(myIntent);
    }

    /**
     * Go to other activity
     *
     * @param context
     * @param cla
     */
    public void gotoActivityForResult(Context context, Class<?> cla,
                                      int requestCode) {
        Intent intent = new Intent(context, cla);
        startActivityForResult(intent, requestCode);
    }

    /**
     * Goto activity with bundle
     *
     * @param context
     * @param cla
     * @param bundle
     */
    public void gotoActivity(Context context, Class<?> cla, Bundle bundle) {
        Intent intent = new Intent(context, cla);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);
    }

    /**
     * Goto activity with bundle
     *
     * @param context
     * @param cla
     * @param bundle
     * @param requestCode
     */
    public void gotoActivityForResult(Context context, Class<?> cla,
                                      Bundle bundle, int requestCode) {
        Intent intent = new Intent(context, cla);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    /**
     * Goto web page
     *
     * @param url
     */
    protected void gotoWebPage(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(i);
    }

    /**
     * Goto phone call
     *
     * @param telNumber
     */
    protected void gotoPhoneCallPage(String telNumber) {
        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse(R.string.tel
                + telNumber));
        startActivity(i);
    }

    /**
     * Close activity
     */
    public void closeActivity(View v) {
        finish();
    }

    // ********************* NOTIFICATION *******************

    // ======================= TOAST MANAGER =======================

    /**
     * @param str : alert message
     *            <p>
     *            Show toast message
     */
    public void showToastMessage(String str) {
        Toast.makeText(self, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param strId : alert message
     *              <p>
     *              Show toast message
     */
    public void showToastMessage(int strId) {
        Toast.makeText(self, getString(strId), Toast.LENGTH_SHORT).show();
    }

    /**
     * @param str : alert message
     *            <p>
     *            Show toast message
     */
    public void showShortToastMessage(String str) {
        Toast.makeText(self, str, Toast.LENGTH_SHORT).show();
    }

    public void showShortToastMessage(int str) {
        Toast.makeText(self, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param str : alert message
     *            <p>
     *            Show toast message
     */
    public void showToastMessage(String str, int time) {
        Toast.makeText(self, str, time).show();
    }

    /**
     * //	 * @param str
     * : alert message
     * <p>
     * Show toast message
     */

    public void showToastMessage(int resId, int time) {
        Toast.makeText(self, resId, time).show();
    }

    /**
     * Show comming soon toast message
     */
    public void showComingSoonMessage() {
        showToastMessage("Coming soon!");
    }

    // ======================= PROGRESS DIALOG ======================

    /**
     * Open progress dialog
     * <p>
     * //	 * @param text
     */
    public void showProgressDialog() {
        try {
            if (progressDialog == null) {
                try {
                    progressDialog = new ProgressDialog(self);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                } catch (Exception e) {
                    progressDialog = new ProgressDialog(self.getParent());
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    e.printStackTrace();
                }
            } else {
                if (!progressDialog.isShowing())
                    progressDialog.show();
            }
        } catch (Exception e) {
            progressDialog = new ProgressDialog(self);
            progressDialog.show();
            progressDialog.setCancelable(false);
            e.printStackTrace();
        }
    }

    public void showProgressDialog(Context context) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog.show();
                progressDialog.setCancelable(false);

            } else {
                if (!progressDialog.isShowing())
                    progressDialog.show();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog.cancel();
            progressDialog = null;
        }

    }

    public boolean isCurrentAppLangRtl() {
        if (AppUtil.isConfigRtl(context))
            return true;
        else
            return false;
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
