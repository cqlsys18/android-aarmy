package com.alaryani.aamrny.activities;

import android.os.Bundle;

import com.alaryani.aamrny.BaseActivity;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.object.ItemTripHistory;
import com.alaryani.aamrny.widget.TextViewRaleway;

import java.util.ArrayList;

public class DetailTripHistoryActivity extends BaseActivity {

	TextViewRaleway tripID;
	TextViewRaleway nameDriver;
	TextViewRaleway timeStart;
	TextViewRaleway timeEnd;
	TextViewRaleway desparture;
	TextViewRaleway destination;
	TextViewRaleway totalTime;
	TextViewRaleway totalDistance;
	TextViewRaleway totalPoint;
	TextViewRaleway linkTrip;
	
	ArrayList<ItemTripHistory> listHistory;
	private ItemTripHistory itemHistory;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_history);
		listHistory = new ArrayList<ItemTripHistory>();
		
		initUI();
		initControl();
		getDataFromGlobal();
		getData();
		initAndSetHeader(R.string.lbl_trip_history);
	}

	private void initControl() {
		
		tripID = (TextViewRaleway)findViewById(R.id.txtTripID);
		linkTrip = (TextViewRaleway)findViewById(R.id.txtLinkTrip);
		timeEnd = (TextViewRaleway)findViewById(R.id.txtTimeEnd);
		desparture = (TextViewRaleway)findViewById(R.id.txtDesparture);
		destination = (TextViewRaleway)findViewById(R.id.txtDestination);
		totalTime = (TextViewRaleway)findViewById(R.id.txtTotalTime);
		totalDistance = (TextViewRaleway)findViewById(R.id.txtTotalDistance);
		totalPoint = (TextViewRaleway)findViewById(R.id.txtTotalPoint);
	}
	private void getDataFromGlobal() {
		itemHistory = GlobalValue.getInstance().currentHistory;
	}
	
	public void getData(){
		tripID.setText(String.valueOf(itemHistory.getTripId()));
		linkTrip.setText(itemHistory.getLink());
		timeEnd.setText(itemHistory.getEndTime());
		desparture.setText(itemHistory.getStartLocaton());
		destination.setText(itemHistory.getEndLocation());
		totalTime.setText(itemHistory.getTotalTime()+"\t"+"Min");
		totalDistance.setText(itemHistory.getDistance().toString()+"\t"+"Km");
		totalPoint.setText(itemHistory.getActualFare().toString());
	}
}
