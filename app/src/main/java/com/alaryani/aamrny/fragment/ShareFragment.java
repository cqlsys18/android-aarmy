package com.alaryani.aamrny.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.alaryani.aamrny.BaseFragment;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.widget.TextViewRaleway;

public class ShareFragment extends BaseFragment implements OnClickListener {
    RelativeLayout btnShareFacebook, btnShareGmail;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    TextViewRaleway lbl_Share, lbl_share_customer;
    ShareFragment demo;

    String TAG = ShareFragment.class.getSimpleName();
    private final int RESULT_CODE_GOOGLE_PLUS = -1;
    String type;
    String social;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        FacebookSdk.sdkInitialize(self.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        initUI(view);
        initControl();
        initMenuButton(view);
        demo = this;
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            ModelManager.getGeneralSettings(preferencesManager.getToken(),
                    self, true, new ModelManagerListener() {
                        @Override
                        public void onSuccess(String json) {
                            if (ParseJsonUtil.isSuccess(json)) {
                                /*String driverPoint = ParseJsonUtil
										.getShareDriverBonus(json);
								String passengerPoint = ParseJsonUtil
										.getSharePassengerBonus(json);
								driverPoint = mainActivity
										.getResources()
										.getString(
												R.string.lbl_share_speak_driver)
										.replace("50", driverPoint);
								passengerPoint = mainActivity
										.getResources()
										.getString(
												R.string.lbl_share_speak_customer)
										.replace("20", passengerPoint);
								lbl_Share.setText(driverPoint);
								lbl_share_customer.setText(passengerPoint);*/
                                lbl_Share.setText(getString(R.string.lbl_share_1));
                                lbl_share_customer.setText("");
                            } else {
                                showToast(ParseJsonUtil.getMessage(json));
                            }
                        }

                        @Override
                        public void onError() {
                            showToast(getResources().getString(
                                    R.string.message_have_some_error));
                        }
                    });
        }
    }

    public void changeLanguage() {
        // lbl_Share.setText(R.string.lbl_share_speak_driver);
        // lbl_share_customer.setText(R.string.lbl_share_speak_customer);
    }

    public void initUI(View view) {
        btnShareFacebook = (RelativeLayout) view
                .findViewById(R.id.btnShareFacebook);
        btnShareGmail = (RelativeLayout) view.findViewById(R.id.btnShareGmail);
        lbl_Share = (TextViewRaleway) view.findViewById(R.id.lbl_Share);
        lbl_share_customer = (TextViewRaleway) view
                .findViewById(R.id.lbl_share_customer);
    }

    public void initControl() {
        btnShareFacebook.setOnClickListener(this);
        btnShareGmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShareFacebook:
                shareFaceBook();
                break;
            case R.id.btnShareGmail:
                shareGmail();
                break;
        }
    }

    // ==============SHARE FACEBOOK====================
    private void shareFaceBook() {
        mainActivity.shareFaceBook = true;
        shareDialog = new ShareDialog(self);
        shareDialog.registerCallback(callbackManager,
                new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        mainActivity.shareFaceBook = false;

                        if (preferencesManager.isUser()) {
                            type = "passenger";
                        } else {
                            type = "driver";
                        }
                        social = "f";
                        shareApp(preferencesManager.getToken(), type, social);
                    }

                    @Override
                    public void onError(FacebookException error) {
                        mainActivity.shareFaceBook = false;
                        showToast("Error");
                    }

                    @Override
                    public void onCancel() {
                        mainActivity.shareFaceBook = false;
                        showToast(getResources().getString(R.string.cancel));
                    }
                });
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent facebookShare = new ShareLinkContent.Builder()
                    .setContentTitle(getResources().getString(R.string.taxi_driver))
                    .setContentDescription(getResources().getString(R.string.this_taxi))
                    .setContentUrl(
                            Uri.parse("http://hicomsolutions.com/"))
                    .build();
            shareDialog.show(facebookShare);
        }
    }

    // ==============SHARE GOOGLE====================
    private void shareGmail() {
        Intent googleShare = new PlusShare.Builder(self).setType("text/plain")
                .setText(getResources().getString(R.string.welcome_upro))
                .setContentUrl(Uri.parse("http://hicomsolutions.com/"))
                .getIntent();
        startActivityForResult(googleShare, 0);
    }

    public void shareFaceBook(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (resultCode == this.RESULT_CODE_GOOGLE_PLUS) {
                if (preferencesManager.isUser()) {
                    type = "passenger";
                } else {
                    type = "driver";
                }
                social = "g";
                shareApp(preferencesManager.getToken(), type, social);
            }
        } else {
            showToast(getResources().getString(R.string.not_share));
        }
    }

    // Share app
    private void shareApp(String token, String type, String social) {
        ModelManager.shareApp(token, type, social, self, true,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            //showToast(ParseJsonUtil.getMessage(json));
                        } else {
                            showToast(ParseJsonUtil.getMessage(json));
                        }
                    }

                    @Override
                    public void onError() {
                        showToast(getResources().getString(
                                R.string.message_have_some_error));
                    }
                });
    }
}
