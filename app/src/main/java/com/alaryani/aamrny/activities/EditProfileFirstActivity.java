package com.alaryani.aamrny.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.adapters.StateAdapter;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.object.CityObj;
import com.alaryani.aamrny.object.StateObj;
import com.alaryani.aamrny.object.User;
import com.alaryani.aamrny.utility.FileDialog;
import com.alaryani.aamrny.utility.ImageUtil;
import com.alaryani.aamrny.utility.NetworkUtil;
import com.alaryani.aamrny.widget.CircleImageView;
import com.alaryani.aamrny.widget.TextViewPixeden;
import com.alaryani.aamrny.widget.TextViewRaleway;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.provider.Settings.System.AIRPLANE_MODE_ON;

public class EditProfileFirstActivity extends BaseActivity implements
        OnClickListener {
    CircleImageView imgPhoto;
    EditText txtUpdateNameDriver, txtUpdateCarPlate, txtUpdatePhone,
            txtUpdateAddress, txtUpdateDescription, txtUpdateModelCar,
            txtUpdateBankCar, txtUpdateYear, txtUpdateEmail, txtUpdateStatus,
            txtUpdateAccount, txtDocument;
    Spinner sp_state;
    EditText sp_city;

    TextView btnSave;
    ImageButton btnBack;
    ImageView imgphoto1, imgphoto2;
    LinearLayout llCarPlate, llBrand, llModel, llYear, imgCar, llStatus,
            llAccount, llDocument, llPhone, llTypeCar, llIdentity;
    TextViewRaleway txtTapAvatar, txtStatusPaypalOption;

    User user;
    AQuery lstAq;
    ArrayList<StateObj> listStates;

    private Bitmap imgPhoto1, imgPhoto2;
    private File document;
    private PreferencesManager preferencesManager;

    /* FOR DATE TIME */
    private DatePickerDialog datePickerDialog, yearPickerDialog;
    private SimpleDateFormat yearFormatter, dobFormatter;
    private Calendar timeDob, timeYear;
    Calendar newCalendar = Calendar.getInstance();
    ArrayList<CityObj> listCities = new ArrayList<>();

    public final int REQUEST_IMAGE_GALLERY_IMAGE_ONE = 0;
    public final int REQUEST_IMAGE_CAPTURE_IMAGE_ONE = 1;
    public final int REQUEST_IMAGE_GALLERY_IMAGE_TWO = 2;
    public final int REQUEST_IMAGE_CAPTURE_IMAGE_TWO = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_updateprofile);
        preferencesManager = PreferencesManager.getInstance(context);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init();
        initControl();
        getDataFromGlobal();
        setDataProfile();
        setDateStates();
    }

    static boolean isAirplaneModeOn(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        return Settings.System.getInt(contentResolver, AIRPLANE_MODE_ON, 0) != 0;
    }

    private void initControl() {

        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        txtDocument.setOnClickListener(this);
        imgphoto1.setOnClickListener(this);
        imgphoto2.setOnClickListener(this);
        txtUpdateYear.setOnClickListener(this);
        // txtUpdateDob.setOnClickListener(this);

		/* CREATE DATE FORMAT */
        yearFormatter = new SimpleDateFormat("yyyy", Locale.US);
        dobFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

		/* CREATE DIALOG DOB */
        timeDob = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(self, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                timeDob.set(year, monthOfYear, dayOfMonth);
                // txtUpdateDob.setText(dobFormatter.format(timeDob.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

//		/* CREATE DIAILOG YEAR */
//        timeYear = Calendar.getInstance();
//        yearPickerDialog = new MyDatePickerDialog(
//                self,
//                new OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        // TODO Auto-generated method stub
//                        timeYear.set(year, monthOfYear, dayOfMonth);
//                        txtUpdateYear.setText(yearFormatter.format(timeYear
//                                .getTime()));
//                    }
//                }, newCalendar.get(Calendar.YEAR), newCalendar
//                .get(Calendar.MONTH),
//                newCalendar.get(Calendar.DAY_OF_MONTH));
//        ((ViewGroup) yearPickerDialog.getDatePicker()).findViewById(
//                Resources.getSystem().getIdentifier("day", "id", "android"))
//                .setVisibility(View.GONE);
//        ((ViewGroup) yearPickerDialog.getDatePicker()).findViewById(
//                Resources.getSystem().getIdentifier("month", "id", "android"))
//                .setVisibility(View.GONE);
//        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                setDataCityUser(listStates.get(position).getStateId(), user.getCityId());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    public void showYearDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);

        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.yeardialog, null, false);

        TextView set = (TextView) v.findViewById(R.id.button1);
        TextView cancel = (TextView) v.findViewById(R.id.button2);
        final NumberPicker nopicker = (NumberPicker) v.findViewById(R.id.numberPicker1);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(v)
                .create();


        nopicker.setMaxValue(year + 200);
        nopicker.setMinValue(year - 200);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                txtUpdateYear.setText(String.valueOf(nopicker.getValue()));
                alertDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();


    }


    private void getDataFromGlobal() {
        user = GlobalValue.getInstance().getUser();
    }

    public void setDateStates() {
        if(NetworkUtil.checkNetworkAvailable(getBaseContext())){
            ModelManager.getAllSates(this, true, new ModelManagerListener() {
                @Override
                public void onError() {
                    Toast.makeText(self, getString(R.string.loadding_state_error), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String json) {
                    listStates = new ArrayList<StateObj>();
                    listStates = ParseJsonUtil.parseListStates(json);
                    StateAdapter adapter = new StateAdapter(EditProfileFirstActivity.this, listStates);
                    sp_state.setAdapter(adapter);
                    if (user.getStateId() != null && !user.getStateId().equals("")) {
                        for (int i = 0; i < listStates.size(); i++) {
                            if (user.getStateId().equals(listStates.get(i).getStateId())) {
                                sp_state.setSelection(i, true);
//                            setDataCityUser(listStates.get(i).getStateId(), user.getCityId());
                            }
                        }
                    }

                }
            });
        } else {
            Toast.makeText(self, getString(R.string.no_intenet), Toast.LENGTH_SHORT).show();
        }
    }

//    public void setDataCityUser(String id, String cityId) {
//        for (int i = 0; i < listStates.size(); i++) {
//            if (id.equals(listStates.get(i).getStateId())) {
//                if (listStates.get(i).getStateCities() != null && listStates.get(i).getStateCities().size() > 0) {
//                    CityAdapter adapter = new CityAdapter(EditProfileFirstActivity.this, listStates.get(i).getStateCities());
//                    sp_city.setAdapter(adapter);
//                    for (int j = 0; j < listStates.get(i).getStateCities().size(); j++) {
//                        if (listStates.get(i).getStateCities().get(j).getCityId().equals(cityId)) {
//                            sp_city.setSelection(j, true);
//                        }
//                    }
//
//                } else {
//                    listCities = new ArrayList<>();
//                    listCities.add(new CityObj("0", "No City"));
//                    CityAdapter adapter = new CityAdapter(EditProfileFirstActivity.this, listCities);
//                    sp_city.setAdapter(adapter);
//                }
//            }
//        }
//    }

    private void init() {

        btnBack = (ImageButton) findViewById(R.id.btnBackUpdate);
        btnBack.setVisibility(View.GONE);
        btnSave = (TextViewPixeden) findViewById(R.id.btnSave);

        txtUpdateNameDriver = (EditText) findViewById(R.id.txtUpdateNameDrive);
        txtUpdateCarPlate = (EditText) findViewById(R.id.txtUpdateCarPlate);
        txtUpdatePhone = (EditText) findViewById(R.id.txtUpdatePhone);
        txtUpdateAddress = (EditText) findViewById(R.id.txtUpdateAddress);
        // txtUpdateDob = (EditText) findViewById(R.id.txtUpdateDob);
        txtUpdateDescription = (EditText) findViewById(R.id.txtUpdateDescription);
        txtUpdateModelCar = (EditText) findViewById(R.id.txtUpdateModelCar);
        txtUpdateBankCar = (EditText) findViewById(R.id.txtUpdateBankCar);
        txtUpdateYear = (EditText) findViewById(R.id.txtUpdateYear);
        txtUpdateEmail = (EditText) findViewById(R.id.txtUpdateEmail);
        txtUpdateStatus = (EditText) findViewById(R.id.txtUpdateStatus);
        txtUpdateAccount = (EditText) findViewById(R.id.txtUpdateAccount);
        txtDocument = (EditText) findViewById(R.id.txtDocument);
        txtTapAvatar = (TextViewRaleway) findViewById(R.id.txtTapAvatar);
        llIdentity = (LinearLayout) findViewById(R.id.llIdentity);
        llIdentity.setVisibility(View.GONE);
        txtStatusPaypalOption = (TextViewRaleway) findViewById(R.id.txtStatusPaypalOption);
        txtDocument.setFocusable(false);
        txtDocument.setFocusableInTouchMode(false);
        imgphoto1 = (ImageView) findViewById(R.id.imgphoto1);
        imgphoto2 = (ImageView) findViewById(R.id.imgphoto2);

        llCarPlate = (LinearLayout) findViewById(R.id.llCarPlate);
        llModel = (LinearLayout) findViewById(R.id.llModel);
        llBrand = (LinearLayout) findViewById(R.id.llBrand);
        llYear = (LinearLayout) findViewById(R.id.llYear);
        imgCar = (LinearLayout) findViewById(R.id.imgCar);
        llStatus = (LinearLayout) findViewById(R.id.llStatus);
        llAccount = (LinearLayout) findViewById(R.id.llAccount);
        llDocument = (LinearLayout) findViewById(R.id.llDocument);
        llTypeCar = (LinearLayout) findViewById(R.id.llTypeCar);
        llTypeCar.setVisibility(View.GONE);
        llPhone = (LinearLayout) findViewById(R.id.llPhone);
        sp_state = (Spinner) findViewById(R.id.sp_state);
        sp_city = (EditText) findViewById(R.id.sp_city);

        imgPhoto = (CircleImageView) findViewById(R.id.imgProfile);

        typeUser();

    }

    public void typeUser() {
        if (GlobalValue.getInstance().getUser().getDriverObj().getIsActive() == null
                || GlobalValue.getInstance().getUser().getDriverObj()
                .getIsActive().equals("0")) {

            llCarPlate.setVisibility(View.GONE);
            llBrand.setVisibility(View.GONE);
            llModel.setVisibility(View.GONE);
            llYear.setVisibility(View.GONE);
            llStatus.setVisibility(View.GONE);
            llDocument.setVisibility(View.GONE);
            imgCar.setVisibility(View.GONE);
            txtStatusPaypalOption.setText(getString(R.string.lblPayoutPaypal));
        } else {
            preferencesManager.setIsDriver();
            preferencesManager.setDriverIsActive();
            txtStatusPaypalOption.setText(getString(R.string.lblPayoutPaypalBinding));
            llCarPlate.setVisibility(View.VISIBLE);
            llBrand.setVisibility(View.VISIBLE);
            llModel.setVisibility(View.VISIBLE);
            llYear.setVisibility(View.VISIBLE);
            llStatus.setVisibility(View.VISIBLE);
            llDocument.setVisibility(View.VISIBLE);
            imgCar.setVisibility(View.VISIBLE);

            // txtUpdateDob.setEnabled(false);
            // txtUpdateAddress.setEnabled(false);
            // txtUpdateDescription.setEnabled(false);
        }
    }

    // set info data Profile
    private void setDataProfile() {
        Log.e("user", "user:" + user.getLinkImage());
        if (user.getTypeAccount().equals("0")) {
            txtTapAvatar.setVisibility(View.VISIBLE);
        } else {
            txtTapAvatar.setVisibility(View.GONE);
        }
        lstAq = new AQuery(self);
        Picasso.with(EditProfileFirstActivity.this).load(user.getLinkImage()).into(imgPhoto);
        txtUpdateNameDriver.setText(user.getFullName());
        txtUpdateEmail.setText(user.getEmail());
        txtUpdatePhone.setText(user.getPhone());
        txtUpdateAddress.setText(user.getAddress());
        // txtUpdateDob.setText(user.getDob());
        txtUpdateDescription.setText(user.getDescription());

        txtUpdateCarPlate.setText(user.getCarObj().getCarPlate());
        txtUpdateBankCar.setText(user.getCarObj().getBrand());
        txtUpdateModelCar.setText(user.getCarObj().getModel());
        txtUpdateYear.setText(user.getCarObj().getYear());
        txtUpdateStatus.setText(user.getCarObj().getStatus());
        txtUpdateAccount.setText(user.getDriverObj().getBankAccount());
        txtUpdateNameDriver.setEnabled(false);
        txtUpdateEmail.setEnabled(false);

        lstAq.id(imgphoto1).image(user.getCarObj().getImageone());
        lstAq.id(imgphoto2).image(user.getCarObj().getImagetwo());

    }

    // ==================Update profile================
    private boolean validteUser() {
        if (txtUpdatePhone.getText().toString().isEmpty()) {
            txtUpdatePhone.requestFocus();
            Toast.makeText(this, getString(R.string.message_phone), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtUpdateAddress.getText().toString().isEmpty()) {
            txtUpdateAddress.requestFocus();
            Toast.makeText(EditProfileFirstActivity.this, getString(R.string.message_address), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtUpdateDescription.getText().toString().isEmpty()) {
            txtUpdateDescription.requestFocus();
            Toast.makeText(EditProfileFirstActivity.this, getString(R.string.message_description), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sp_city.getText().toString().isEmpty()) {
            Toast.makeText(EditProfileFirstActivity.this, getString(R.string.message_State), Toast.LENGTH_SHORT).show();
            sp_city.requestFocus();
            return false;
        }

        return true;
    }

    public void updateUser() {
        if (validteUser()) {
//            user = new User();
            user.setAddress(txtUpdateAddress.getText().toString());
            user.setDescription(txtUpdateDescription.getText().toString());
            user.setPhone(txtUpdatePhone.getText().toString());
            user.setAddress(txtUpdateAddress.getText().toString());
            user.setDescription(txtUpdateDescription.getText().toString());
            user.setCityName(sp_city.getText().toString());
            user.setPayout(txtUpdateAccount.getText().toString());
//            if (sp_city.getSelectedItem() != null && !sp_city.getSelectedItem().toString().equals("")) {
//                if (listCities != null && listCities.size() > 0) {
//                    user.setCityName(listCities.get(0).getCityName());
//                    user.setCityId(listCities.get(0).getCityId());
//                } else {
//                    user.setCityName(sp_city.getSelectedItem().toString());
//                    user.setCityId(listStates.get(sp_state.getSelectedItemPosition()).getStateCities().get(sp_city.getSelectedItemPosition()).getCityId());
//                }
//
//            } else {
//                user.setCityName("");
//                user.setCityId("");
//            }
            if (sp_state.getSelectedItem() != null && !sp_state.getSelectedItem().toString().equals("")) {
                user.setStateName(sp_state.getSelectedItem().toString());
                user.setStateId(listStates.get(sp_state.getSelectedItemPosition()).getStateId());
            } else {
                user.setStateName("");
                user.setStateId("");
            }
            ModelManager.updateProfileSocial(PreferencesManager.getInstance(
                    self).getToken(), self, true, user.getPhone(), user.getPayout(),
                    user.getDescription(), user.getAddress(), user.getStateId(), user.getCityName(),
                    new ModelManagerListener() {

                        @Override
                        public void onError() {
                            showToastMessage(getResources().getString(
                                    R.string.message_have_some_error));
                        }

                        @Override
                        public void onSuccess(String json) {
                            if (ParseJsonUtil.isSuccess(json)) {
                                Intent intent = new Intent(EditProfileFirstActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                globalValue.setUser(user);
                            } else {
                                showToastMessage(ParseJsonUtil.getMessage(json));
                            }
                        }
                    });
        }
    }

    // =========================================================================================================
    // OnClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackUpdate:
                onBackPressed();
                break;
            case R.id.btnSave:
                if (preferencesManager.isUser()) {
                    updateUser();
                } else {
                    if (Integer.parseInt(user.getDriverObj().getUpdatePending()) == 1) {
                        showToastMessage(R.string.message_update_driver);
                    } else {
                        updateDriver();
                    }
                }
                break;
            case R.id.txtDocument:
                choiseFile();
                break;
            case R.id.imgphoto1:
                choiseImageOne();
                break;
            case R.id.imgphoto2:
                choiseImageTwo();
                break;
            case R.id.txtUpdateYear:
                showYearDialog();
//                yearPickerDialog.show();
                break;
        /*
         * case R.id.txtUpdateDob: datePickerDialog.show(); break;
		 */
            default:
                break;
        }
    }

    /* All Update Profile Driver Car */
    // =======================================================================================================
    public void setImgPhoto1(Uri selectedImage, Bitmap image) {
        imgPhoto1 = image;
        imgphoto1.setImageURI(selectedImage);
    }

    public void setImgPhoto1(Bitmap imageBitmap) {
        imgPhoto1 = imageBitmap;
        imgphoto1.setImageBitmap(imageBitmap);
    }

    public void setImgPhoto2(Uri selectedImage, Bitmap image) {
        imgPhoto2 = image;
        imgphoto2.setImageURI(selectedImage);
    }

    public void setImgPhoto2(Bitmap imageBitmap) {
        imgPhoto2 = imageBitmap;
        imgphoto2.setImageBitmap(imageBitmap);
    }

    // =================Choose document===============================
    private void choiseFile() {
        File mPath = new File(Environment.getExternalStorageDirectory()
                + "//DIR//");
        FileDialog fileDialog = new FileDialog(self, mPath);
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
                Log.d("trangpv", "size file: " + file.length());
                if (file.length() > (2 * 1024 * 1024)) {
                    Toast.makeText(self, getString(R.string.notify_file_upload_size), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    document = file;
                    txtDocument.setText(file.getName());
                }
            }
        });
        fileDialog.showDialog();
    }

    public void updateDriver() {
        if (validate()) {
            if (imgPhoto1 == null || imgPhoto2 == null) {
                showToastMessage(getResources().getString(R.string.choice_new_image));
            } else {
//                user = new User();
                user.setPhone(txtUpdatePhone.getText().toString());
                user.getCarObj().setCarPlate(
                        txtUpdateCarPlate.getText().toString());
                user.getCarObj()
                        .setBrand(txtUpdateBankCar.getText().toString());
                user.getCarObj().setModel(
                        txtUpdateModelCar.getText().toString());
                user.getCarObj().setYear(txtUpdateYear.getText().toString());
                user.getCarObj()
                        .setStatus(txtUpdateStatus.getText().toString());
                user.setCityName(sp_city.getText().toString());
//                if (sp_city.getSelectedItem() != null && !sp_city.getSelectedItem().toString().equals("")) {
//                    if (listCities != null && listCities.size() > 0) {
//                        user.setCityName(listCities.get(0).getCityName());
//                        user.setCityId(listCities.get(0).getCityId());
//                    } else {
//                        user.setCityName(sp_city.getSelectedItem().toString());
//                        user.setCityId(listStates.get(sp_state.getSelectedItemPosition()).getStateCities().get(sp_city.getSelectedItemPosition()).getCityId());
//                    }
//                } else {
//                    user.setCityName("");
//                    user.setCityId("");
//                }
                if (sp_state.getSelectedItem() != null && !sp_state.getSelectedItem().toString().equals("")) {
                    user.setStateName(sp_state.getSelectedItem().toString());
                    user.setStateId(listStates.get(sp_state.getSelectedItemPosition()).getStateId());
                } else {
                    user.setStateName("");
                    user.setStateId("");
                }
                ModelManager.updateProfileDriverSocial(PreferencesManager
                                .getInstance(self).getToken(), user.getCarObj()
                                .getCarPlate(), user.getCarObj().getBrand(), user
                                .getCarObj().getModel(), user.getCarObj().getYear(),
                        user.getCarObj().getStatus(), user.getPhone(), user.getStateId(), user.getCityName(),
                        imgPhoto1, imgPhoto2, document, self, true,
                        new ModelManagerListener() {

                            @Override
                            public void onError() {
                                showToastMessage(getResources().getString(
                                        R.string.message_have_some_error));
                            }

                            @Override
                            public void onSuccess(String json) {
                                if (ParseJsonUtil.isSuccess(json)) {
                                    Intent intent = new Intent(EditProfileFirstActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    globalValue.setUser(user);
                                } else {
                                    showToastMessage(ParseJsonUtil.getMessage(json));
                                }
                            }
                        });
            }
        }
    }
    protected void showToast(String message) {
        Toast.makeText(EditProfileFirstActivity.this, message, Toast.LENGTH_LONG).show();
    }
    // ================ VALIDATE===========================
    public boolean validate() {
        if(listStates.size()<=0){
            Toast.makeText(self, getString(R.string.messsage_state_empty), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtUpdateCarPlate.getText().toString().isEmpty()) {
            txtUpdateCarPlate.requestFocus();
            showToast(getString(R.string.message_carPlate));
            return false;
        }
        if (txtUpdateBankCar.getText().toString().isEmpty()) {
            txtUpdateBankCar.requestFocus();
            showToast(getString(R.string.message_brand));
            return false;
        }
        if (txtUpdateModelCar.getText().toString().isEmpty()) {
            txtUpdateModelCar.requestFocus();
            showToast(getString(R.string.message_model));
            return false;
        }
        if (txtUpdateYear.getText().toString().isEmpty()) {
            txtUpdateYear.requestFocus();
            showToast(getString(R.string.message_year));
            return false;
        }
        if (txtUpdateStatus.getText().toString().isEmpty()) {
            txtUpdateStatus.requestFocus();
            showToast(getString(R.string.message_status));
            return false;
        }
        if (txtUpdatePhone.getText().toString().isEmpty()) {
            txtUpdatePhone.requestFocus();
            showToast(getString(R.string.message_phone));
            return false;
        }
        if (txtDocument.getText().toString().isEmpty()) {
            showToast(getString(R.string.message_document));
            return false;
        }
        // if (imgPhoto1 == null || imgPhoto2 == null) {
        // return false;
        // }
        return true;
    }

    // ====================choise image========================================
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
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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


    // ================================================================================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case REQUEST_IMAGE_GALLERY_IMAGE_ONE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        setImgPhoto1(ImageUtil.decodeUri(self, selectedImage));
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_IMAGE_CAPTURE_IMAGE_ONE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    setImgPhoto1(imageBitmap);
                }
                break;
            case REQUEST_IMAGE_GALLERY_IMAGE_TWO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        setImgPhoto2(ImageUtil.decodeUri(self, selectedImage));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_IMAGE_CAPTURE_IMAGE_TWO:
                if (resultCode == RESULT_OK) {
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    setImgPhoto2(imageBitmap);
                }
                break;
        }
    }
}
