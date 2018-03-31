package com.alaryani.aamrny.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class OTPActivity extends BaseActivity {
    EditText otp_num;
    com.alaryani.aamrny.widget.TextViewFontAwesome submit_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        initialize();
    }

    private void initialize() {
        otp_num = (EditText) findViewById(R.id.otp_num);
        submit_button = (com.alaryani.aamrny.widget.TextViewFontAwesome) findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpapi();
            }
        });
    }

    private void otpapi() {
        ModelManager.otpapi(otp_num.getText().toString(), this, true, new ModelManagerListener() {
            @Override
            public void onSuccess(String json) {
                if (ParseJsonUtil.isSuccess(json)) {
                    // Check is user or driver
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.getString("status").equals("SUCCESS")) {
                            Toast.makeText(self, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OTPActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(OTPActivity.this, ParseJsonUtil.getMessage(json), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(OTPActivity.this, ParseJsonUtil.getMessage(json), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError() {
                       /* showToastMessage(R.string.message_have_some_error);*/
            }
        });
    }
}
