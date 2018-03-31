package com.alaryani.aamrny.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.adapters.StateAdapter;
import com.alaryani.aamrny.adapters.TypeCarAdapter;
import com.alaryani.aamrny.config.Constant;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.object.CarType;
import com.alaryani.aamrny.object.StateObj;
import com.alaryani.aamrny.object.User;
import com.alaryani.aamrny.utility.FileDialog;
import com.alaryani.aamrny.utility.ImageUtil;
import com.alaryani.aamrny.utility.MyDatePickerDialog;
import com.alaryani.aamrny.utility.NetworkUtil;
import com.alaryani.aamrny.widget.CircleImageView;
import com.alaryani.aamrny.widget.TextViewPixeden;
import com.alaryani.aamrny.widget.TextViewRaleway;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.provider.Settings.System.AIRPLANE_MODE_ON;

public class UpdateProFileActivity extends BaseActivity implements
        OnClickListener, CropHandler {
    CircleImageView imgPhoto;
    EditText txtUpdateNameDriver, txtUpdateCarPlate, txtUpdatePhone,
            txtUpdateAddress, txtUpdateDescription, txtUpdateModelCar,
            txtUpdateBankCar, txtUpdateYear, txtUpdateEmail, txtUpdateStatus,
            txtUpdateAccount, txtDocument, txtIdentity;
    TextViewRaleway txtTapAvatar, txtStatusPaypalOption;
    Spinner sp_state, spTypeCar;
    EditText sp_city;

    TextView btnSave;
    ImageButton btnBack;
    ImageView imgphoto1, imgphoto2;
    LinearLayout llCarPlate, llBrand, llModel, llYear, imgCar, llStatus,
            llAccount, llDocument, llPhone, llTypeCar, llIdentity;

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

    public final int REQUEST_IMAGE_GALLERY_IMAGE_ONE = 0;
    public final int REQUEST_IMAGE_CAPTURE_IMAGE_ONE = 1;
    public final int REQUEST_IMAGE_GALLERY_IMAGE_TWO = 2;
    public final int REQUEST_IMAGE_CAPTURE_IMAGE_TWO = 3;
    private Bitmap yourSelectedImage;
    private String image = "", typeCar = "1";
    private CropParams mCropParams;
    private ArrayList<CarType> listCarTypes = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_updateprofile);
        mCropParams = new CropParams();
        preferencesManager = PreferencesManager.getInstance(context);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init();
        initControl();
        getDataFromGlobal();
        setDataProfile();
        setDateStates();
        checkCurrentLanguage();
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
        imgPhoto.setOnClickListener(this);
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

		/* CREATE DIAILOG YEAR */
        timeYear = Calendar.getInstance();
        yearPickerDialog = new MyDatePickerDialog(
                self,
                new OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        timeYear.set(year, monthOfYear, dayOfMonth);
                        txtUpdateYear.setText(yearFormatter.format(timeYear
                                .getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar
                .get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        ((ViewGroup) yearPickerDialog.getDatePicker()).findViewById(
                Resources.getSystem().getIdentifier("day", "id", "android"))
                .setVisibility(View.GONE);
        ((ViewGroup) yearPickerDialog.getDatePicker()).findViewById(
                Resources.getSystem().getIdentifier("month", "id", "android"))
                .setVisibility(View.GONE);
        spTypeCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeCar = listCarTypes.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void initDataTypeCar() {
        for (int i = 0; i < GlobalValue.getInstance().getListCarTypes().size(); i++) {
            if (GlobalValue.getInstance().getListCarTypes().get(i).getId() != null && !GlobalValue.getInstance().getListCarTypes().get(i).getId().equals("")) {
                listCarTypes.add(GlobalValue.getInstance().getListCarTypes().get(i));
            }
        }
        TypeCarAdapter adapter = new TypeCarAdapter(this, listCarTypes);
        spTypeCar.setAdapter(adapter);
    }

    private void getDataFromGlobal() {
        user = GlobalValue.getInstance().user;
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
                    StateAdapter adapter = new StateAdapter(UpdateProFileActivity.this, listStates);
                    sp_state.setAdapter(adapter);
                    if (user.getStateId() != null && !user.getStateId().equals("")) {
                        for (int i = 0; i < listStates.size(); i++) {
                            if (user.getStateId().equals(listStates.get(i).getStateId())) {
                                sp_state.setSelection(i, true);
                            }
                        }
                    }

                }
            });
        } else {
            Toast.makeText(self, getString(R.string.no_intenet), Toast.LENGTH_SHORT).show();
        }

    }

    private void init() {

        btnBack = (ImageButton) findViewById(R.id.btnBackUpdate);
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
        txtIdentity = (EditText) findViewById(R.id.txtIdentity);
        txtTapAvatar = (TextViewRaleway) findViewById(R.id.txtTapAvatar);
        txtStatusPaypalOption = (TextViewRaleway) findViewById(R.id.txtStatusPaypalOption);
        txtDocument = (EditText) findViewById(R.id.txtDocument);
        txtDocument.setFocusable(false);
        txtDocument.setFocusableInTouchMode(false);
        imgphoto1 = (ImageView) findViewById(R.id.imgphoto1);
        imgphoto2 = (ImageView) findViewById(R.id.imgphoto2);

        llCarPlate = (LinearLayout) findViewById(R.id.llCarPlate);
        llModel = (LinearLayout) findViewById(R.id.llModel);
        llBrand = (LinearLayout) findViewById(R.id.llBrand);
        llYear = (LinearLayout) findViewById(R.id.llYear);
        llTypeCar = (LinearLayout) findViewById(R.id.llTypeCar);
        imgCar = (LinearLayout) findViewById(R.id.imgCar);
        llStatus = (LinearLayout) findViewById(R.id.llStatus);
        llAccount = (LinearLayout) findViewById(R.id.llAccount);
        llDocument = (LinearLayout) findViewById(R.id.llDocument);
        llPhone = (LinearLayout) findViewById(R.id.llPhone);
        llIdentity = (LinearLayout) findViewById(R.id.llIdentity);
        sp_state = (Spinner) findViewById(R.id.sp_state);
        spTypeCar = (Spinner) findViewById(R.id.spTypeCar);
        sp_city = (EditText) findViewById(R.id.sp_city);

        imgPhoto = (CircleImageView) findViewById(R.id.imgProfile);

        typeUser();

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
            llStatus.setVisibility(View.GONE);
            llAccount.setVisibility(View.VISIBLE);
            llDocument.setVisibility(View.GONE);
            llIdentity.setVisibility(View.GONE);
            imgCar.setVisibility(View.GONE);
            txtStatusPaypalOption.setText(getString(R.string.lblPayoutPaypal));
        } else {
            preferencesManager.setIsDriver();
            preferencesManager.setDriverIsActive();
            txtStatusPaypalOption.setText(getString(R.string.lblPayoutPaypalBinding));

            llCarPlate.setVisibility(View.VISIBLE);
            llTypeCar.setVisibility(View.VISIBLE);
            llBrand.setVisibility(View.VISIBLE);
            llModel.setVisibility(View.VISIBLE);
            llYear.setVisibility(View.VISIBLE);
            llStatus.setVisibility(View.VISIBLE);
            llAccount.setVisibility(View.VISIBLE);
            llDocument.setVisibility(View.VISIBLE);
            llIdentity.setVisibility(View.VISIBLE);
            imgCar.setVisibility(View.VISIBLE);


            // txtUpdateDob.setEnabled(false);
            // txtUpdateAddress.setEnabled(false);
            // txtUpdateDescription.setEnabled(false);
        }
    }

    // set info data Profile
    private void setDataProfile() {
        initDataTypeCar();
        txtUpdateNameDriver.setText(user.getFullName());
        txtUpdateEmail.setText(user.getEmail());
        txtUpdatePhone.setText(user.getPhone());
        txtUpdateAddress.setText(user.getAddress());
        sp_city.setText(user.getCityName());
        // txtUpdateDob.setText(user.getDob());
        txtUpdateDescription.setText(user.getDescription());

        txtUpdateCarPlate.setText(user.getCarObj().getCarPlate());
        txtUpdateBankCar.setText(user.getCarObj().getBrand());
        txtUpdateModelCar.setText(user.getCarObj().getModel());
        txtUpdateYear.setText(user.getCarObj().getYear());
        txtUpdateStatus.setText(user.getCarObj().getStatus());
        txtIdentity.setText(user.getDriverObj().getIdentity());
        if (preferencesManager.isDriver()) {
            txtUpdateAccount.setText(user.getDriverObj().getBankAccount());
        } else {
            txtUpdateAccount.setText(user.getAccount());
        }
        if (user.getTypeAccount().equals(Constant.TYPE_ACCOUNT_NORMAL)) {
            txtTapAvatar.setVisibility(View.VISIBLE);
        } else {
            txtTapAvatar.setVisibility(View.GONE);
        }
        if (user.getTypeAccount().equals(Constant.TYPE_ACCOUNT_NORMAL)) {
            txtUpdateNameDriver.setEnabled(true);
        } else {
            txtUpdateNameDriver.setEnabled(false);
        }
        if (user.getCarObj() != null && user.getCarObj().getTypeCar() != null) {
            typeCar = user.getCarObj().getTypeCar();
            for (int i = 0; i < listCarTypes.size(); i++) {
                if (typeCar.equals(listCarTypes.get(i).getId())) {
                    spTypeCar.setSelection(i);
                }
            }
        }
        txtUpdateEmail.setEnabled(false);
//        txtUpdateAccount.setEnabled(false);

        lstAq = new AQuery(self);

        lstAq.id(imgPhoto).image(user.getLinkImage());
        lstAq.id(imgphoto1).image(user.getCarObj().getImageone());
        lstAq.id(imgphoto2).image(user.getCarObj().getImagetwo());
//        if (user.getCarObj().getImageone() != null && !user.getCarObj().getImageone().equals("")) {
//            imgPhoto1 = getBitmapFromURL(user.getCarObj().getImageone());
//        }
//        if (user.getCarObj().getImagetwo() != null && !user.getCarObj().getImagetwo().equals("")) {
//            imgPhoto2 = getBitmapFromURL(user.getCarObj().getImagetwo());
//        }
    }

    // ==================Update profile================
    private boolean validteUser() {
        if (sp_city.getText().toString().isEmpty()) {
            return false;
        }
        if (txtUpdatePhone.getText().toString().isEmpty()) {
            txtUpdatePhone.requestFocus();
            showToast(getString(R.string.message_phone));
            return false;
        }
        if (txtUpdateAddress.getText().toString().isEmpty()) {
            txtUpdateAddress.requestFocus();
            showToast(getString(R.string.message_address));
            return false;
        }
        if (txtUpdateNameDriver.getText().toString().isEmpty()) {
            txtUpdateNameDriver.requestFocus();
            showToast(getString(R.string.message_name));
            return false;
        }
        if (!txtUpdateAccount.getText().toString().equals("")) {
            if (!isValidEmailAddress(txtUpdateAccount.getText().toString())) {
                txtUpdateAccount.requestFocus();
                Toast.makeText(this, getString(R.string.message_Acount2), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
//        if (txtUpdateDescription.getText().toString().isEmpty()) {
//            return false;
//        }

        return true;
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public void updateUser() {
        if (validteUser()) {
            user = new User();
            user.setAddress(txtUpdateAddress.getText().toString());
            user.setFullName(txtUpdateNameDriver.getText().toString());
            user.setDescription(txtUpdateDescription.getText().toString());
            user.setPhone(txtUpdatePhone.getText().toString());
            user.setAddress(txtUpdateAddress.getText().toString());
            user.setAccount(txtUpdateAccount.getText().toString());
            if (txtUpdateDescription.getText().toString().equals("")) {
                user.setDescription(" ");
            } else {
                user.setDescription(txtUpdateDescription.getText().toString());
            }
            user.setCityName(sp_city.getText().toString());
            if (sp_state.getSelectedItem() != null && !sp_state.getSelectedItem().toString().equals("")) {
                user.setStateName(sp_state.getSelectedItem().toString());
                user.setStateId(listStates.get(sp_state.getSelectedItemPosition()).getStateId());
            } else {
                user.setStateName("");
                user.setStateId("");
            }
            if (yourSelectedImage != null) {
                image = convertBitmapListToArrayJson(yourSelectedImage);
            } else {
                image = "";
            }
            ModelManager.updateProfile(PreferencesManager.getInstance(
                    self).getToken(), self, true, user.getPhone(),
                    user.getDescription(), user.getAddress(), user.getStateId(), user.getCityName(), user.getFullName(), "1", user.getAccount(), image,
                    new ModelManagerListener() {

                        @Override
                        public void onError() {
                            showToastMessage(getResources().getString(
                                    R.string.message_have_some_error));
                        }

                        @Override
                        public void onSuccess(String json) {
                            if (ParseJsonUtil.isSuccess(json)) {
                                onBackPressed();
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
            case R.id.imgProfile:
                if (user.getTypeAccount().equals(Constant.TYPE_ACCOUNT_NORMAL)) {
//                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                    photoPickerIntent.setType("image/*");
//                    startActivityForResult(photoPickerIntent, 1000);
                    Intent intent = CropHelper.buildCropFromGalleryIntent(mCropParams);
                    startActivityForResult(intent, CropHelper.REQUEST_CROP);
                } else {
                    Toast.makeText(UpdateProFileActivity.this, getString(R.string.lblValidateUpdateImage), Toast.LENGTH_SHORT).show();
                }
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
        int dimension = getSquareCropDimensionForBitmap(imageBitmap);
        Bitmap imageConvert = ThumbnailUtils.extractThumbnail(imageBitmap, dimension, dimension);
        imgPhoto1 = imageConvert;
        imgphoto1.setImageBitmap(imageConvert);
    }

    public int getSquareCropDimensionForBitmap(Bitmap bitmap) {
        return Math.min(bitmap.getWidth(), bitmap.getHeight());
    }

    public void setImgPhoto2(Uri selectedImage, Bitmap image) {
        imgPhoto2 = image;
        imgphoto2.setImageURI(selectedImage);
    }

    public void setImgPhoto2(Bitmap imageBitmap) {
        int dimension = getSquareCropDimensionForBitmap(imageBitmap);
        Bitmap imageConvert = ThumbnailUtils.extractThumbnail(imageBitmap, dimension, dimension);
        imgPhoto2 = imageConvert;
        imgphoto2.setImageBitmap(imageConvert);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
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
                showToastMessage(getResources().getString(R.string.lbl_img_required));
            } else {
                user = new User();
                user.setPhone(txtUpdatePhone.getText().toString());
                user.setFullName(txtUpdateNameDriver.getText().toString());
                user.setAccount(txtUpdateAccount.getText().toString());
                user.getCarObj().setCarPlate(
                        txtUpdateCarPlate.getText().toString());
                user.getCarObj()
                        .setBrand(txtUpdateBankCar.getText().toString());
                user.getCarObj().setModel(
                        txtUpdateModelCar.getText().toString());
                user.getCarObj().setYear(txtUpdateYear.getText().toString());
                user.getCarObj().setTypeCar(typeCar);
                user.getCarObj()
                        .setStatus(txtUpdateStatus.getText().toString());
                user.getDriverObj()
                        .setIdentity(txtIdentity.getText().toString());
                if (txtUpdateDescription.getText().toString().equals("")) {
                    user.setDescription(" ");
                } else {
                    user.setDescription(txtUpdateDescription.getText().toString());
                }
                user.setAddress(txtUpdateAddress.getText().toString());
                user.setCityName(sp_city.getText().toString());
                if (sp_state.getSelectedItem() != null && !sp_state.getSelectedItem().toString().equals("")) {
                    user.setStateName(sp_state.getSelectedItem().toString());
                    user.setStateId(listStates.get(sp_state.getSelectedItemPosition()).getStateId());
                } else {
                    user.setStateName("");
                    user.setStateId("");
                }
                if (yourSelectedImage != null) {
                    image = convertBitmapListToArrayJson(yourSelectedImage);
                } else {
                    image = "";
                }
                ModelManager.updateProfileDriver(PreferencesManager
                                .getInstance(self).getToken(), user.getCarObj()
                                .getCarPlate(), user.getDriverObj().getIdentity(), user.getCarObj().getBrand(), user
                                .getCarObj().getModel(), user.getCarObj().getYear(),
                        user.getCarObj().getStatus(), user.getPhone(), user.getStateId(), user.getCityName(), user.getAddress(), user.getDescription(), user.getFullName(), user.getCarObj().getTypeCar(), user.getAccount(), image, "1",
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
                                    onBackPressed();
                                } else {
                                    showToastMessage(ParseJsonUtil.getMessage(json));
                                }
                            }
                        });
            }
        }
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

    // ================ VALIDATE===========================
    protected void showToast(String message) {
        Toast.makeText(UpdateProFileActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        if (listStates.size() <= 0) {
            showToast(getString(R.string.messsage_state_empty));
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

        if (txtUpdateNameDriver.getText().toString().isEmpty()) {
            txtUpdateNameDriver.requestFocus();
            showToast(getString(R.string.message_name));
            return false;
        }
        if (txtDocument == null) {
            showToast(getString(R.string.message_document));
            return false;
        }
        if (!txtUpdateAccount.getText().toString().equals("")) {
            if (!isValidEmailAddress(txtUpdateAccount.getText().toString())) {
                txtUpdateAccount.requestFocus();
                Toast.makeText(this, getString(R.string.message_Acount2), Toast.LENGTH_SHORT).show();
                return false;
            }
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
        CropHelper.handleResult(this, requestCode, resultCode, imageReturnedIntent);
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
        }
    }

    public static String convertBitmapListToArrayJson(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
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

    private void checkCurrentLanguage() {
        if (isCurrentAppLangRtl()) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnBack.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            btnBack.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) btnSave.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            btnSave.setLayoutParams(params1);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnBack.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            btnBack.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) btnSave.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            btnSave.setLayoutParams(params1);
        }
    }
}
