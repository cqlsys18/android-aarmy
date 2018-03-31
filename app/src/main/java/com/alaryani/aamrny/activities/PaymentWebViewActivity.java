package com.alaryani.aamrny.activities;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.network.ProgressDialog;
import com.alaryani.aamrny.util.LocaleHelper;
import com.alaryani.aamrny.utility.AppUtil;

public class PaymentWebViewActivity extends BaseActivity {
    WebView webView;
    ProgressDialog progressDialog;
    String app_lang = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web_view);
        app_lang = getIntent().getStringExtra("app_lang");

        initAndSetHeader(R.string.lbl_payment);


        progressDialog = new ProgressDialog(PaymentWebViewActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        webView = (WebView) findViewById(R.id.webView);
        loadUrl();

    }

    private void loadUrl() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getStringExtra("post_url"));
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    webView.loadUrl(url);
                } else {
                    webView.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("app_lang", "start " + app_lang);
                LocaleHelper.setLocale(PaymentWebViewActivity.this, app_lang);
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing() && progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    public boolean isCurrentAppLangRtl() {
        boolean is_rtl = false;
        if (AppUtil.isConfigRtl(context))
            is_rtl = true;
        else
            is_rtl = false;
        return is_rtl;
    }
}


