package com.alaryani.aamrny.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.object.Transfer;
import com.alaryani.aamrny.object.User;
import com.alaryani.aamrny.widget.CircleImageView;
import com.alaryani.aamrny.widget.TextViewRaleway;

public class DetailTransferActivity extends BaseActivity {

	ImageButton btnback;
	TextViewRaleway lblBalance, lblDriverId;
	TextView lblName, btnTransfers;
	EditText lblNote, lblEmail, lblGenger, lblAmount;
	CircleImageView imgProfile;

	User user;
	Transfer transfer;
	AQuery aq;
	String minTranfer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_transfers1);
		getDataFromGlobal();
		// hide keyboard with edittext
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initUIInThis();
		getMinTranfer();

	}

	private void getDataFromGlobal() {
		user = GlobalValue.getInstance().user;
		transfer = GlobalValue.getInstance().transfer;
	}

	public void initUIInThis() {

		lblNote = (EditText) findViewById(R.id.lblNote);
		lblEmail = (EditText) findViewById(R.id.lblEmail);
		// lblGenger = (EditText) findViewById(R.id.lblGender);
		lblAmount = (EditText) findViewById(R.id.lblPoint);
		lblName = (TextView) findViewById(R.id.txtNameDriver);
		imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
		btnTransfers = (TextView) findViewById(R.id.btnTransfers);

		btnback = (ImageButton) findViewById(R.id.btnBack);
		btnback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		btnTransfers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lblAmount.getText().length() != 0) {
					Double amount = Double.parseDouble(lblAmount.getText()
							.toString());
					if (amount > user.getBalance()) {
						showToastMessage(R.string.message_point_invalid);
					} else if (amount < Double.parseDouble(minTranfer)) {
						showToastMessage(self.getResources()
								.getString(R.string.message_point_min)
								.replace("500", minTranfer));
					} else {
						transfer();
					}
				} else {
					showToastMessage(R.string.message_please_enter_point);
				}

			}
		});

		// get data
		lblEmail.setText(transfer.getReceiverEmail());
		lblEmail.setEnabled(false);
		// lblGenger.setText(transfer.getReceiverGender());
		// lblGenger.setEnabled(false);

		lblName.setText(transfer.getReceiverName());
		aq = new AQuery(self);
		aq.id(imgProfile).image(transfer.getReceiverProfile());
	}

	public void transfer() {
		ModelManager.transfer(PreferencesManager.getInstance(self).getToken(),
				lblAmount.getText().toString(), transfer.getReceiverEmail(),
				lblNote.getText().toString(), self, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						if (ParseJsonUtil.isSuccess(json)) {
							showToastMessage(R.string.lbl_success);
							onBackPressed();
						} else {
							showToastMessage(ParseJsonUtil.getMessage(json));
						}

					}

					@Override
					public void onError() {
						showToastMessage(getResources().getString(
								R.string.message_have_some_error));
					}
				});
	}

	private void getMinTranfer() {
		ModelManager.getGeneralSettings(preferencesManager.getToken(), self,
				true, new ModelManagerListener() {
					@Override
					public void onSuccess(String json) {
						if (ParseJsonUtil.isSuccess(json)) {
							minTranfer = ParseJsonUtil.getMinTranfer(json);
						} else {
							showToastMessage(ParseJsonUtil.getMessage(json));
						}
					}

					@Override
					public void onError() {
						showToastMessage(getResources().getString(
								R.string.message_have_some_error));
					}
				});
	}

}
