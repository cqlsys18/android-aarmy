package com.alaryani.aamrny.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.alaryani.aamrny.BaseFragment;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.activities.PaymentPointActivity;
import com.alaryani.aamrny.activities.PaymentHistoryActivity;
import com.alaryani.aamrny.activities.PaymentTransferActivity;
import com.alaryani.aamrny.activities.PayoutActivity;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.object.User;
import com.alaryani.aamrny.widget.CircleImageView;

public class PaymentFragment extends BaseFragment implements OnClickListener {
    Button btnPayment, btnPayout, btnTransfers, btnHistory;
    TextView lblTotalPoint, lblName, lblBalance;
    RatingBar ratingBar;
    CircleImageView imgProfile;

    User user = new User();
    static AQuery aq;

    int Total = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        getDataFromGlobal();
        initUI(view);
        initControl();
        initMenuButton(view);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getData();
        }
    }


    public void changeLanguage() {
        lblBalance.setText(R.string.lbl_balance);
        btnTransfers.setText(R.string.lbl_transfers);
        btnHistory.setText(R.string.lbl_payment_history);
        btnPayout.setText(R.string.lbl_payout);
    }

    private void getDataFromGlobal() {
        user = GlobalValue.getInstance().user;
    }

    public void initUI(View view) {
        btnHistory = (Button) view.findViewById(R.id.btnHistory);
        btnPayment = (Button) view.findViewById(R.id.btnPayment);
        btnPayout = (Button) view.findViewById(R.id.btnPayout);
        btnTransfers = (Button) view.findViewById(R.id.btnTrasfers);
        lblTotalPoint = (TextView) view.findViewById(R.id.txtTotalPoint);
        lblName = (TextView) view.findViewById(R.id.lblFullName);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        imgProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
        lblBalance = (TextView) view.findViewById(R.id.lblBalance);

        aq = new AQuery(self);
        // = aq.recycle(view);
    }

    public void initControl() {
        btnHistory.setOnClickListener(this);
        btnPayment.setOnClickListener(this);
        btnPayout.setOnClickListener(this);
        btnTransfers.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPayment:

                showDialogPayment();
                break;
            case R.id.btnPayout:
                showDialogPayout();
                break;
            case R.id.btnTrasfers:
                showTrasfers();
                break;
            case R.id.btnHistory:
                showHistory();
                break;

        }
    }

    private void showTrasfers() {
        startActivity(PaymentTransferActivity.class);
    }

    private void showHistory() {
        startActivity(PaymentHistoryActivity.class);
    }

    private void showDialogPayout() {
        startActivity(PayoutActivity.class);

    }

    //////////////////////////////////
    private void showDialogPayment() {
        startActivity(PaymentPointActivity.class);
    }

    // get data
    public void getData() {
        ModelManager.showInfoProfile(
                PreferencesManager.getInstance(mainActivity).getToken(), self,
                true, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            GlobalValue.getInstance().setUser(
                                    ParseJsonUtil.parseInfoProfile(json));
                            user = ParseJsonUtil.parseInfoProfile(json);

                            lblName.setText(user.getFullName());

                            if (user.getDriverObj() != null) {
                                if (user.getDriverObj().getDriverRate() != null) {
                                    if (!user.getDriverObj().getDriverRate().equals(""))
                                        ratingBar.setRating(Float.parseFloat(user.getDriverObj().getDriverRate()) / 2);
                                } else {
                                    if (!user.getPassengerRate().equals("")) {
                                        ratingBar.setRating(Float.parseFloat(user.getPassengerRate()) / 2);
                                    }
                                }
                            } else {
                                if (!user.getPassengerRate().equals("")) {
                                    ratingBar.setRating(Float.parseFloat(user.getPassengerRate()) / 2);
                                }
                            }
                            lblTotalPoint.setText(getString(R.string.lblCurrency) + String.valueOf(user
                                    .getBalance()));
                            aq.id(imgProfile).image(user.getLinkImage());

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

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        lblTotalPoint.setText(getString(R.string.lblCurrency) + GlobalValue.getInstance().getUser().getBalance()
                + "");
    }
}
