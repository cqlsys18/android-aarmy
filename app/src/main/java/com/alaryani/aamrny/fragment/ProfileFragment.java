package com.alaryani.aamrny.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.alaryani.aamrny.BaseFragment;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.R.id;
import com.alaryani.aamrny.activities.UpdateProFileActivity;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.object.CarType;
import com.alaryani.aamrny.object.User;
import com.alaryani.aamrny.widget.CircleImageView;
import com.alaryani.aamrny.widget.TextViewRaleway;

import java.util.ArrayList;

public class ProfileFragment extends BaseFragment {
    public static final int REQUEST_CODE_INPUT = 1;
    public static final int RESULT_CODE_SAVE = 2;
    TextView btnUpdate;
    TextView lblNameDriver;
    ImageView imgTaxt, imgphoto1, imgphoto2;
    CircleImageView profile;
    TextViewRaleway lblIDDriver, lblCarPlate, lblBrand, lblModel, lblYear,
            lblPhone, lblEmail, lblAddress, lblDes, lbl_IDDriver, lbl_CarPlace, lbl_Model, lbl_Brand,
            lbl_Year, lbl_Email, lbl_Address, lbl_Phone, lblCity, lblState, lblTypeCar, lblPaypal, lbl_Paypal;
    //TextViewRaleway lbl_BankAcount, lbl_Status, lbl_Description, lblStatus, lblDescription, lblAccount;
    //LinearLayout llStatus
    //RelativeLayout llAccount;
    RelativeLayout lblRate;
    LinearLayout llCarPlate, llBrand, llModel, llYear, imgCar, llPhone, llCity, llTypeCar, lbPaypal;
    RatingBar ratingBar;

    User user = new User();
    // private PreferencesManager preferencesManager;
    private AQuery lstAq;
    static AQuery aq;


    private View view;
    private ArrayList<CarType> listCarTypes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        preferencesManager = PreferencesManager.getInstance(self);
        Log.e("ee", "onResume");
        initUI(view);
        initControl();
        typeUser();
        initMenuButton(view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("ee", "onPause");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("ee", "onStart");
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getData();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getData();
        }
    }

    public void changeLanguage() {
        lbl_CarPlace.setText(R.string.lbl_car_plate);
        lbl_Model.setText(R.string.lbl_model_car);
        lbl_Brand.setText(R.string.lbl_brand_car);
        lbl_Year.setText(R.string.lbl_year_manufacturer);
        //lbl_BankAcount.setText(R.string.lbl_bank_accout);
        //lbl_Status.setText(R.string.lbl_status);
        //lbl_Description.setText(R.string.lbl_description);
        lbl_Email.setText(R.string.lbl_email);
        lbl_Address.setText(R.string.lbl_address);

        lbl_Phone.setText(R.string.lbl_phone);
        btnUpdate.setText(R.string.lbl_update_profile);
    }

    public void initUI(View view) {

        lbl_CarPlace = (TextViewRaleway) view.findViewById(R.id.lbl_CarPlace);
        lblPaypal = (TextViewRaleway) view.findViewById(R.id.lblPaypal);
        lbl_Model = (TextViewRaleway) view.findViewById(R.id.lbl_Model);
        lbl_Brand = (TextViewRaleway) view.findViewById(R.id.lbl_Brand);
        lbl_Year = (TextViewRaleway) view.findViewById(R.id.lbl_Year);
        lbPaypal = (LinearLayout) view.findViewById(R.id.lbPaypal);
        /*lbl_BankAcount = (TextViewRaleway) view
                .findViewById(R.id.lbl_BankAccount);
		lbl_Status = (TextViewRaleway) view.findViewById(R.id.lbl_Status);
		lbl_Description = (TextViewRaleway) view
				.findViewById(R.id.lbl_Description);*/
        lbl_Email = (TextViewRaleway) view.findViewById(R.id.lbl_Email);
        lbl_Address = (TextViewRaleway) view.findViewById(R.id.lbl_Address);
        //lbl_Rate = (TextViewRaleway) view.findViewById(R.id.lbl_Rate);
        lbl_Phone = (TextViewRaleway) view.findViewById(R.id.lbl_Phone);

        //lblTaxiDriver = (TextView) view.findViewById(R.id.lblTaxiDriver);
        btnUpdate = (TextView) view.findViewById(R.id.btnUpdate);
        lblNameDriver = (TextView) view.findViewById(R.id.lblNameDriver);
        profile = (CircleImageView) view.findViewById(R.id.imgProfile);
        lblCarPlate = (TextViewRaleway) view.findViewById(R.id.lblCarPlate);
        lblBrand = (TextViewRaleway) view.findViewById(R.id.lblBrand);
        lblModel = (TextViewRaleway) view.findViewById(R.id.lblModel);
        lblYear = (TextViewRaleway) view.findViewById(R.id.lblYear);
        lblPhone = (TextViewRaleway) view.findViewById(id.lblPhone);
        lblEmail = (TextViewRaleway) view.findViewById(R.id.lblEmail);
        lblAddress = (TextViewRaleway) view.findViewById(R.id.lblAddress);
        lblDes = (TextViewRaleway) view.findViewById(id.lblDestination);
        lblState = (TextViewRaleway) view.findViewById(id.lblState);
        lblCity = (TextViewRaleway) view.findViewById(id.lblCity);
        lblTypeCar = (TextViewRaleway) view.findViewById(id.lblTypeCar);
        lbl_Paypal = (TextViewRaleway) view.findViewById(id.lbl_Paypal);
        // lblDob = (TextViewRaleway) view.findViewById(R.id.lblDob);
        /*lblDescription = (TextViewRaleway) view
                .findViewById(R.id.lblDescription);
		lblStatus = (TextViewRaleway) view.findViewById(R.id.lblStatus);
		lblAccount = (TextViewRaleway) view.findViewById(R.id.lblAccount);*/

        llCarPlate = (LinearLayout) view.findViewById(R.id.llCarplate);
        llBrand = (LinearLayout) view.findViewById(R.id.llBrand);
        llModel = (LinearLayout) view.findViewById(R.id.llModel);
        llYear = (LinearLayout) view.findViewById(R.id.llYear);
        llCity = (LinearLayout) view.findViewById(R.id.llCity);
        llTypeCar = (LinearLayout) view.findViewById(R.id.llTypeCar);
        //llStatus = (LinearLayout) view.findViewById(R.id.llStatus);
        //llAccount = (LinearLayout) view.findViewById(R.id.llAccount);
        llPhone = (LinearLayout) view.findViewById(R.id.llPhone);

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        // typeUser();
        lstAq = new AQuery(self);
        aq = lstAq.recycle(view);
    }

    public void typeUser() {
        if (GlobalValue.getInstance().getUser().getDriverObj().getIsActive() == null
                || GlobalValue.getInstance().getUser().getDriverObj()
                .getIsActive().equals("0")) {
            llCarPlate.setVisibility(View.GONE);
            llTypeCar.setVisibility(View.GONE);
            llBrand.setVisibility(View.GONE);
            llModel.setVisibility(View.GONE);
            llYear.setVisibility(View.GONE);
            lbPaypal.setVisibility(View.VISIBLE);
            lbl_Paypal.setText(getString(R.string.lblPayoutPaypal));
            //llStatus.setVisibility(View.GONE);
            //llAccount.setVisibility(View.GONE);
        } else {
            preferencesManager.setIsDriver();
            preferencesManager.setDriverIsActive();
            llCarPlate.setVisibility(View.VISIBLE);
            llTypeCar.setVisibility(View.VISIBLE);
            llBrand.setVisibility(View.VISIBLE);
            llModel.setVisibility(View.VISIBLE);
            llYear.setVisibility(View.VISIBLE);
            lbPaypal.setVisibility(View.VISIBLE);
            lbl_Paypal.setText(getString(R.string.lblPayoutPaypalBinding));
            //llStatus.setVisibility(View.VISIBLE);
            //llAccount.setVisibility(View.VISIBLE);

        }
    }

    public void initControl() {

        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (preferencesManager.isUser()) {
                    mainActivity.gotoActivity(UpdateProFileActivity.class);
                } else {
                    if (preferencesManager.isActiveDriver()) {
                        mainActivity.gotoActivity(UpdateProFileActivity.class);
                    } else {
                        showToast(R.string.message_your_account_in_wait);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_INPUT) {

        }
    }

    // set data user
    private void setData() {
        if (user != null) {
            lblNameDriver.setText(user.getFullName());
            lblEmail.setText(user.getEmail());
            lblPhone.setText(user.getPhone());
            lblAddress.setText(user.getAddress());
            // lblDob.setText(user.getDob());
            lblDes.setText(user.getDescription());
        }
        if (preferencesManager.isDriver()) {
            if (user.getDriverObj() != null && user.getDriverObj().getBankAccount() != null) {
                lblPaypal.setText(user.getDriverObj().getBankAccount());
            }
        } else {
            lblPaypal.setText(user.getAccount());
        }

        if (user.getCarObj() != null) {
            lblCarPlate.setText(user.getCarObj().getCarPlate());
            lblBrand.setText(user.getCarObj().getBrand());
            lblModel.setText(user.getCarObj().getModel());
            lblYear.setText(user.getCarObj().getYear());
        }
        for (int i = 0; i < GlobalValue.getInstance().getListCarTypes().size(); i++) {
            if (GlobalValue.getInstance().getListCarTypes().get(i).getId() != null && !GlobalValue.getInstance().getListCarTypes().get(i).getId().equals("")) {
                listCarTypes.add(GlobalValue.getInstance().getListCarTypes().get(i));
                if (user.getCarObj() != null && user.getCarObj().getTypeCar() != null && user.getCarObj().getTypeCar().equals(GlobalValue.getInstance().getListCarTypes().get(i).getId())) {
                    lblTypeCar.setText(GlobalValue.getInstance().getListCarTypes().get(i).getName());
                }
            }
        }


        if (user.getCityId() != null && !user.getCityId().equals("") && !user.getCityId().equals("0")) {
            llCity.setVisibility(View.VISIBLE);
            lblCity.setText(user.getCityName());
        } else {
            llCity.setVisibility(View.GONE);
        }
        if (user.getStateId() != null && !user.getStateId().equals("")) {
            lblState.setText(user.getStateName());
        }
        //lblStatus.setText(user.getCarObj().getStatus());
        //lblAccount.setText(user.getDriverObj().getBankAccount());

        if (preferencesManager.isUser()) {
            if (user.getPassengerRate().length() == 0) {
                ratingBar.setRating(0);
            } else {
                ratingBar.setRating(Float.parseFloat(user.getPassengerRate()) / 2);
            }
        } else {
            if (user.getDriverObj().getDriverRate().length() == 0) {
                ratingBar.setRating(0);
            } else {
                ratingBar.setRating(Float.parseFloat(user.getDriverObj()
                        .getDriverRate()) / 2);
            }
        }
    }

    // get data info user
    public void getData() {
        ModelManager.showInfoProfile(
                PreferencesManager.getInstance(mainActivity).getToken(), self,
                true, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParseJsonUtil.isSuccess(json)) {
                            GlobalValue.getInstance().setUser(
                                    ParseJsonUtil.parseInfoProfile(json));
                            typeUser();
                            user = ParseJsonUtil.parseInfoProfile(json);
                            aq.id(profile).image(user.getLinkImage());
                            setData();

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
