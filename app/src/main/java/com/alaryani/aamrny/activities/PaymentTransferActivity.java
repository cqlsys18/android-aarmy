package com.alaryani.aamrny.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.modelmanager.ModelManager;
import com.alaryani.aamrny.modelmanager.ModelManagerListener;
import com.alaryani.aamrny.modelmanager.ParseJsonUtil;
import com.alaryani.aamrny.object.Transfer;
import com.alaryani.aamrny.object.User;
import com.alaryani.aamrny.widget.TextViewRaleway;

public class PaymentTransferActivity extends BaseActivity {

	TextView btnContinue, lblError;
	TextViewRaleway lbl_Balance;
	private EditText lblPoint, lblTransferId;
	User user;
	Transfer transfer = new Transfer();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_transfers);
		initAndSetHeader(R.string.title_transfer);
		initUI();
		getDataFromGlobal();
		initUIInThis();
		setHeaderTitle(R.string.lbl_transfers);
	}

	private void getDataFromGlobal() {
		user = GlobalValue.getInstance().user;
		GlobalValue.getInstance().setTransfer(transfer);
	}

	public void initUIInThis() {
		lbl_Balance = (TextViewRaleway) findViewById(R.id.lbl_Balance);
		lblTransferId = (EditText) findViewById(R.id.lblTransferId);
		lblError = (TextView) findViewById(R.id.lbl_Error);

		// get info
		lbl_Balance.setText(getString(R.string.lblCurrency) +String.valueOf(user.getBalance()));
		btnContinue = (TextView) findViewById(R.id.btnContinue);
		btnContinue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchUser();
			}
		});
	}

	private void searchUser() {
		ModelManager.searchUser(
				PreferencesManager.getInstance(self).getToken(), lblTransferId
						.getText().toString(), self, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						if (ParseJsonUtil.isSuccess(json)) {
							GlobalValue.getInstance().setTransfer(
									ParseJsonUtil.parseInfoTransfer(json));
							transfer = ParseJsonUtil.parseInfoTransfer(json);
							showToastMessage(ParseJsonUtil.getMessage(json));
							gotoActivity(DetailTransferActivity.class);
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
