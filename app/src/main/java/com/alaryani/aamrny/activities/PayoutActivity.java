package com.alaryani.aamrny.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.object.Transfer;
import com.alaryani.aamrny.object.User;
import com.alaryani.aamrny.widget.TextViewFontAwesome;
import com.alaryani.aamrny.widget.TextViewRaleway;

public class PayoutActivity extends BaseActivity {

    TextViewFontAwesome lbl_information;
    TextView lblError;
    LinearLayout btnContinue, btnStripe;
    TextViewRaleway lblBalance;
    EditText lblPoint;
    User user;
    Transfer payment = new Transfer();
    String minRedeem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout);
        initUI();
        getDataFromGlobal();
        initUIInThis();
        getMinRedeem();
        initAndSetHeader(R.string.title_redeem);

    }

    private void getDataFromGlobal() {
        user = GlobalValue.getInstance().user;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void initUIInThis() {

        lblBalance = (TextViewRaleway) findViewById(R.id.lbl_Balance);
        lblPoint = (EditText) findViewById(R.id.lbl_Point);
        lblError = (TextView) findViewById(R.id.lbl_Error);
        lbl_information = (TextViewFontAwesome) findViewById(R.id.lbl_information);
        btnStripe = (LinearLayout) findViewById(R.id.btnStripe);
        lblBalance.setText(getString(R.string.lblCurrency) + String.valueOf(user.getBalance()));

        btnContinue = (LinearLayout) findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (preferencesManager.isDriver()) {
                    if (user.getDriverObj().getBankAccount() != null && !user.getDriverObj().getBankAccount().equals("")) {
                        if (lblPoint.getText().toString().length() != 0) {
                            Double amount = Double.parseDouble(lblPoint.getText()
                                    .toString().trim());
                            Double balance = user.getBalance();
                            if (amount > balance) {
                                showToastMessage(R.string.message_point_invalid);
                            } else if (amount >= Double.parseDouble(minRedeem)) {
                                payOut();
                            } else {
                                showToastMessage(self.getResources()
                                        .getString(R.string.message_point_min)
                                        .replace("500", minRedeem));
                            }
                        } else {
                            showToastMessage(R.string.message_please_enter_point);
                        }
                    } else {
                        showToastMessage(getResources().getString(R.string.update_paypal));
                    }
                } else {
                    if (user.getPayout() != null && !user.getPayout().equals("")) {
                        if (lblPoint.getText().toString().length() != 0) {
                            Double amount = Double.parseDouble(lblPoint.getText()
                                    .toString().trim());
                            Double balance = user.getBalance();
                            if (amount > balance) {
                                showToastMessage(R.string.message_point_invalid);
                            } else if (amount >= Double.parseDouble(minRedeem)) {
                                payOut();
                            } else {
                                showToastMessage(self.getResources()
                                        .getString(R.string.message_point_min)
                                        .replace("500", minRedeem));
                            }
                        } else {
                            showToastMessage(R.string.message_please_enter_point);
                        }
                    } else {
                        showToastMessage(getResources().getString(R.string.update_paypal));
                    }
                }


            }
        });


        lbl_information.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://www.linkrider.net/#!faq/c1yvs"));
                startActivity(browserIntent);
            }
        });
    }

    private void payOut() {
        ModelManager.payout(PreferencesManager.getInstance(self).getToken(),
                lblPoint.getText().toString(), self, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {

                            showToastMessage(R.string.lblPayoutSuccess);
                            onBackPressed();
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

    private void getMinRedeem() {
        ModelManager.getGeneralSettings(preferencesManager.getToken(), self,
                true, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            minRedeem = ParseJsonUtil.getMinRedeem(json);
                            // minRedeem = mainActivity.getResources()
                            // .getString(R.string.message_point_min)
                            // .replace("500", minRedeem);
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
}
