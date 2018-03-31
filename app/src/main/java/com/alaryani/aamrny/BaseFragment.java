package com.alaryani.aamrny;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alaryani.aamrny.activities.MainActivity;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.utility.AppUtil;

public class BaseFragment extends Fragment {
    private TextView lblTitle;
    protected Activity self;
    protected MainActivity mainActivity;
    protected PreferencesManager preferencesManager;

    // public PreferencesManager preferenceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        self = getActivity();
        mainActivity = (MainActivity) getActivity();
        this.preferencesManager = PreferencesManager.getInstance(self);
    }

    protected void initUI(View view) {
        lblTitle = (TextView) view.findViewById(R.id.lblTitle);
    }

    protected void initMenuButton(View view) {
        view.findViewById(R.id.btnMenu).setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onClick(View v) {
                        if (isCurrentAppLangRtl()) {
                            ((MainActivity) getActivity()).menu.setMode(((MainActivity) getActivity()).menu.RIGHT);
                        } else {
                            ((MainActivity) getActivity()).menu.setMode(((MainActivity) getActivity()).menu.LEFT);
                        }
                        ((MainActivity) getActivity()).menu.showMenu();
                        // hide key
                        View view = self.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) self.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
                });
    }

    protected void initBackButton(View view) {
        // view.findViewById(R.id.btnMenu).setBackgroundResource(
        // R.drawable.btn_back);
        // view.findViewById(R.id.btnMenu).setOnClickListener(
        // new View.OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // getMainActivity().onBackPressed();
        // }
        // });
    }

    protected void initMapButton(View view) {
        // view.findViewById(R.id.icMapHome);
        // view.findViewById(R.id.icMapHome).setOnClickListener(
        // new View.OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // Intent i = new Intent(getActivity(), ActivityMap.class);
        // startActivity(i);
        // self.overridePendingTransition(R.anim.slide_in_left,
        // R.anim.slide_out_left);
        // }
        // });
    }

    // protected void initAdModLayout(View view) {
    // AdView adView = new AdView(getActivity());
    // adView.setAdSize(AdSize.BANNER);
    // adView.setAdUnitId(WebserviceConfig.admob);
    // LinearLayout layout = (LinearLayout) view.findViewById(R.id.layoutAds);
    // if (layout != null) {
    // layout.addView(adView);
    // AdRequest adRequest = new AdRequest.Builder().build();
    // adView.loadAd(adRequest);
    // }
    // }

    protected void setHeaderTitle(int idString) {
        lblTitle.setText(idString);
    }

    protected void setHeaderTitle(String s) {
        lblTitle.setText(s);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_left);
    }

    protected void startActivity(Class<?> cla) {
        startActivity(new Intent(getActivity(), cla));
    }

    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    protected void showToast(String message) {
        Toast.makeText(getMainActivity(), message, Toast.LENGTH_LONG).show();
    }

    protected void showToast(int message) {
        Toast.makeText(getMainActivity(), message, Toast.LENGTH_LONG).show();
    }

    // public boolean isLoggedIn() {
    // // if (preferenceManager.getUserInfo() == null) {
    // // return false;
    // // } else {
    // // return true;
    // // }
    // }

    public boolean isCurrentAppLangRtl() {
        if (AppUtil.isConfigRtl(getActivity()))
            return true;
        else
            return false;
    }

}
