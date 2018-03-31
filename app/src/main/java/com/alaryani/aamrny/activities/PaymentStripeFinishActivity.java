package com.alaryani.aamrny.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.config.Constant;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;


public class PaymentStripeFinishActivity extends BaseActivity {
    TextView btnSubmit, lblTitle;
    EditText cardNumberField, monthField, yearField, cvcField;
    ImageView imgLogo;
    private String amount = "0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_payment);
        btnSubmit = (TextView) findViewById(R.id.submitButton);
        cardNumberField = (EditText) findViewById(R.id.cardNumber);
        monthField = (EditText) findViewById(R.id.month);
        yearField = (EditText) findViewById(R.id.year);
        cvcField = (EditText) findViewById(R.id.cvc);
        imgLogo = (ImageView) findViewById(R.id.btnBack);
        lblTitle = (TextView) findViewById(R.id.lblTitle);
        lblTitle.setText(getString(R.string.credit_card));
        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCard();
            }
        });
        amount = getIntent().getStringExtra("amount");
    }

    public void submitCard() {
        // TODO: replace with your own test key
        final String publishableApiKey = this.getString(R.string.stripe_publishable_key);
        Card card = new Card(cardNumberField.getText().toString(),
                Integer.valueOf(monthField.getText().toString()),
                Integer.valueOf(yearField.getText().toString()),
                cvcField.getText().toString());
        if (card.validateCard() && card.validateCVC() && card.validateExpMonth() && card.validateExpYear()) {
            Stripe stripe = new Stripe(this);
            stripe.createToken(card, publishableApiKey, new TokenCallback() {
                public void onSuccess(final Token token) {
                    // TODO: Send Token information to your backend to initiate a charge
                    Log.e("token", "token:" + token.getId());
                    sendRequest(token.getId(), GlobalValue.getInstance().getUser().getEmail(), amount);

                }

                public void onError(Exception error) {
                    Log.d("Stripe", error.getLocalizedMessage());
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.lblInfoInvalid), Toast.LENGTH_SHORT).show();
        }


    }

    public void sendRequest(final String token, String email, final String amount) {
        ModelManager.sendStripRequest(this, token, amount, email, true, new ModelManagerListener() {
            @Override
            public void onError() {
                Toast.makeText(PaymentStripeFinishActivity.this, getString(R.string.message_have_some_error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String json) {
                if (ParseJsonUtil.isSuccess(json)) {
                    Toast.makeText(PaymentStripeFinishActivity.this, ParseJsonUtil.getMessage(json), Toast.LENGTH_SHORT).show();
                    setPayment(amount, token, Constant.PAYMENT_BY_STRIPE);
                }
            }
        });
    }

    private void payment() {
        ModelManager.tripPayment(PreferencesManager.getInstance(this).getToken(),
                PreferencesManager.getInstance(this).getCurrentOrderId(),Constant.PAY_TRIP_BY_BALANCE, this, true,
                new ModelManagerListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onSuccess(String json) {

                        if (ParseJsonUtil.isSuccess(json)) {
//							showRate(true);
                            PreferencesManager.getInstance(PaymentStripeFinishActivity.this)
                                    .setPassengerHaveDonePayment(true);
                            PreferencesManager.getInstance(PaymentStripeFinishActivity.this).setPassengerCurrentScreen("");
                            finishAffinity();
                            gotoActivity(MainActivity.class);
                            finish();

                        }
                    }

                    @Override
                    public void onError() {

                        showToastMessage(R.string.message_have_some_error);
                    }
                });
    }

    private void setPayment(String point, String paymentId, String paymentMethod) {

        ModelManager.payment(PreferencesManager.getInstance(this).getToken(), point, paymentId, paymentMethod, this, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            if (ParseJsonUtil.getMessage(json)
                                    .equalsIgnoreCase("ok")) {
                                payment();
                            }
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public static double round(double number, int digit) {
        if (digit > 0) {
            int temp = 1, i;
            for (i = 0; i < digit; i++)
                temp = temp * 10;
            number = number * temp;
            number = Math.round(number);
            number = number / temp;
            return number;
        } else
            return 0.0;
    }
}