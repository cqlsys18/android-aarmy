package com.alaryani.aamrny.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.adapters.StateAdapter;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.object.StateObj;
import com.alaryani.aamrny.utility.NetworkUtil;
import com.alaryani.aamrny.widget.CircleImageView;
import com.alaryani.aamrny.widget.TextViewPixeden;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 10/26/2016.
 */
public class SignUpActivity extends BaseActivity implements View.OnClickListener, CropHandler {
    private EditText txtUpdateNameDrive, txtUpdatePhone, txtUpdateEmail, txtPassword, txtUpdateAddress, sp_city, txtUpdatePostCode, txtAccount;
    private Spinner sp_state;
    private CircleImageView imgProfile;
    public static int SELECT_PHOTO = 1000;
    ArrayList<StateObj> listStates;
    private TextViewPixeden btnSave;
    private Bitmap yourSelectedImage;
    private ImageButton btnBackUpdate;
    CropParams mCropParams;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mCropParams = new CropParams();
        initUI();
        initControl();
        initData();
        checkCurrentLanguage();
    }

    public void initUI() {
        txtUpdateNameDrive = (EditText) findViewById(R.id.txtUpdateNameDrive);
        txtUpdatePhone = (EditText) findViewById(R.id.txtUpdatePhone);
        txtAccount = (EditText) findViewById(R.id.txtAccount);
        txtUpdateEmail = (EditText) findViewById(R.id.txtUpdateEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtUpdateAddress = (EditText) findViewById(R.id.txtUpdateAddress);
        sp_city = (EditText) findViewById(R.id.sp_city);
        txtUpdatePostCode = (EditText) findViewById(R.id.txtUpdatePostCode);
        sp_state = (Spinner) findViewById(R.id.sp_state);
        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        btnSave = (TextViewPixeden) findViewById(R.id.btnSave);
        btnBackUpdate = (ImageButton) findViewById(R.id.btnBackUpdate);

    }

    public void initControl() {
        imgProfile.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnBackUpdate.setOnClickListener(this);
    }

    public void initData() {
        setDateStates();
    }

    public boolean validate() {
        if (listStates.size() <= 0) {
            Toast.makeText(self, getString(R.string.messsage_state_empty), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (yourSelectedImage == null) {
            Toast.makeText(SignUpActivity.this, getString(R.string.lblValidateImage), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtUpdateNameDrive.getText().toString().equals("")) {
            txtUpdateNameDrive.requestFocus();
            Toast.makeText(SignUpActivity.this, getString(R.string.message_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtUpdatePhone.getText().toString().equals("")) {
            txtUpdatePhone.requestFocus();
            Toast.makeText(SignUpActivity.this, getString(R.string.message_phone), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtUpdateEmail.getText().toString().equals("")) {
            txtUpdateEmail.requestFocus();
            Toast.makeText(SignUpActivity.this, getString(R.string.message_email), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidEmailAddress(txtUpdateEmail.getText().toString())) {
            txtUpdateEmail.requestFocus();
            Toast.makeText(SignUpActivity.this, getString(R.string.message_email2), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtPassword.getText().toString().equals("")) {
            txtPassword.requestFocus();
            Toast.makeText(SignUpActivity.this, getString(R.string.message_password), Toast.LENGTH_SHORT).show();
            return false;

        }
        if (txtUpdateAddress.getText().toString().equals("")) {
            txtUpdateAddress.requestFocus();
            Toast.makeText(SignUpActivity.this, getString(R.string.message_address), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sp_city.getText().toString().equals("")) {
            Toast.makeText(SignUpActivity.this, getString(R.string.message_State), Toast.LENGTH_SHORT).show();
            sp_city.requestFocus();
            return false;
        }
        if (txtUpdatePostCode.getText().toString().equals("")) {
            txtUpdatePostCode.requestFocus();
            Toast.makeText(SignUpActivity.this, getString(R.string.message_postcode), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!txtAccount.getText().toString().equals("")) {
            if (!isValidEmailAddress(txtAccount.getText().toString())) {
                txtAccount.requestFocus();
                Toast.makeText(SignUpActivity.this, getString(R.string.message_Acount2), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public void sendData() {
        if (validate()) {
            ModelManager.registerAccount(this, txtUpdateNameDrive.getText().toString(), txtUpdatePhone.getText().toString(), txtUpdateEmail.getText().toString(),
                    txtPassword.getText().toString(), txtUpdateAddress.getText().toString(), listStates.get(sp_state.getSelectedItemPosition()).getStateId(), sp_city.getText().toString(), txtUpdatePostCode.getText().toString(), txtAccount.getText().toString(),
                    yourSelectedImage, true, new ModelManagerListener() {
                        @Override
                        public void onError() {

                        }

                        @Override
                        public void onSuccess(String json) {
                            if (ParseJsonUtil.isSuccess(json)) {
                                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.sent_otp), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, OTPActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, ParseJsonUtil.getMessage(json), Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        }
    }


    public void setDateStates() {
        if (NetworkUtil.checkNetworkAvailable(getBaseContext())) {
            ModelManager.getAllSates(this, true, new ModelManagerListener() {
                @Override
                public void onError() {
                    Toast.makeText(self, getString(R.string.loadding_state_error), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String json) {
                    listStates = new ArrayList<StateObj>();
                    listStates = ParseJsonUtil.parseListStates(json);
                    StateAdapter adapter = new StateAdapter(SignUpActivity.this, listStates);
                    sp_state.setAdapter(adapter);
                }
            });
        } else {
            Toast.makeText(self, getString(R.string.no_intenet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgProfile:
                Intent intent = CropHelper.buildCropFromGalleryIntent(mCropParams);
                startActivityForResult(intent, CropHelper.REQUEST_CROP);
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;
            case R.id.btnSave:
                sendData();
                break;
            case R.id.btnBackUpdate:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        CropHelper.handleResult(this, requestCode, resultCode, imageReturnedIntent);
//        switch (requestCode) {
//            case 1000:
//                if (resultCode == RESULT_OK) {
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    InputStream imageStream = null;
//                    try {
//                        imageStream = getContentResolver().openInputStream(selectedImage);
//                        yourSelectedImage = BitmapFactory.decodeStream(imageStream);
//                        AQuery aQuery = new AQuery(this);
//                        aQuery.id(R.id.imgProfile).image(yourSelectedImage);
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//        }
    }

    @Override
    public void onPhotoCropped(Uri uri) {
        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(uri);
            yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            AQuery aQuery = new AQuery(this);
            aQuery.id(R.id.imgProfile).image(yourSelectedImage);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        CropHelper.clearCachedCropFile(uri);
    }

    @Override
    public void onCropCancel() {

    }

    @Override
    public void onCropFailed(String message) {

    }

    @Override
    public CropParams getCropParams() {
        mCropParams = new CropParams();
        return mCropParams;
    }

    @Override
    public Activity getContext() {
        return this;
    }

//    @Override
//    public void handleIntent(Intent intent, int requestCode) {
//        startActivityForResult(intent, requestCode);
//    }

    private void checkCurrentLanguage() {
        if (isCurrentAppLangRtl()) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnBackUpdate.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            btnBackUpdate.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) btnSave.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            btnSave.setLayoutParams(params1);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnBackUpdate.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            btnBackUpdate.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) btnSave.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            btnSave.setLayoutParams(params1);
        }
    }
}
