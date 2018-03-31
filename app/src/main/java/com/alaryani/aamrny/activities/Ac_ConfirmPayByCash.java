package com.alaryani.aamrny.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.ServiceUpdateLocation;
import com.alaryani.aamrny.config.Constant;
import com.alaryani.aamrny.fragment.BeforeOnlineFragment;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;


public class Ac_ConfirmPayByCash extends BaseActivity {
    private TextView btnConfirm, btnCancel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac__confirm_pay_by_cash);

        init();
        initControl();
    }

    public void init() {
        btnConfirm = (TextView) findViewById(R.id.btnConfirm);
        btnCancel = (TextView) findViewById(R.id.btnCancel);
    }

    public void initControl() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAMountDiffDialog();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm(Constant.ACTION_CANCEL, "");
                btnConfirm.setEnabled(false);
            }
        });
    }

    private void showAMountDiffDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.is_payment_diff)
                .setPositiveButton(R.string.lbl_yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                enterAmountDialog();
                            }
                        }).setNegativeButton(R.string.lbl_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirm(Constant.ACTION_CONFIRM, "");
            }
        }).create().show();

    }


    private void enterAmountDialog() {
        final Dialog dialog = new Dialog(Ac_ConfirmPayByCash.this, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.enter_cash_layout);
        dialog.setCancelable(false);

        final EditText edt_amount = (EditText) dialog.findViewById(R.id.edt_amount);
        final TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        final TextView apply = (TextView) dialog.findViewById(R.id.apply);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_amount.getText().toString().equals("")) {
                    Toast.makeText(getMainActivity(), getResources().getString(R.string.enter_amount), Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    confirm(Constant.ACTION_AMOUNT_DIFF, edt_amount.getText().toString());
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void confirm(String action, String paid_amount) {
        ModelManager.confirmPayByCash(preferencesManager.getCurrentOrderId(), action, this, paid_amount, false, new ModelManagerListener() {
            @Override
            public void onError() {
                showToastMessage(getString(R.string.message_have_some_error));
                btnConfirm.setEnabled(true);
            }

            @Override
            public void onSuccess(String json) {
                if (ParseJsonUtil.isSuccess(json)) {

                    preferencesManager.isFromBeforeOnline(false);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

                } else {
                    showToastMessage(ParseJsonUtil.getMessage(json));
                }
                btnConfirm.setEnabled(true);
            }
        });
    }

}
