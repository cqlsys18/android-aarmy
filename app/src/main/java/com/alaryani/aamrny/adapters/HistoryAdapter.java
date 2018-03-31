package com.alaryani.aamrny.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alaryani.aamrny.R;
import com.alaryani.aamrny.config.GlobalValue;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.object.ItemTripHistory;
import com.alaryani.aamrny.object.User;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class HistoryAdapter extends BaseAdapter {

    // public ArrayList<NewsObj> arrNews;
    private LayoutInflater mInflate;
    private ArrayList<ItemTripHistory> arrViews;
    Activity mAct;
    User user;

    // AQuery aq;

    public HistoryAdapter(Activity activity, ArrayList<ItemTripHistory> arrViews) {
        this.mAct = activity;
        this.arrViews = arrViews;
        this.mInflate = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrViews.size();
    }

    public ArrayList<ItemTripHistory> getArrViews() {
        return arrViews;
    }

    public void setArrViews(ArrayList<ItemTripHistory> arrViews) {
        this.arrViews = arrViews;
    }

    @Override
    public Object getItem(int position) {

        return arrViews.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        getDataFromGlobal();
        final HolderView holder;
        if (convertView == null) {
            holder = new HolderView();
            convertView = mInflate.inflate(R.layout.layout_item_history, null);

            holder.tripId = (TextView) convertView.findViewById(R.id.txtTripId);
            holder.timeEnd = (TextView) convertView.findViewById(R.id.txtTimeTo);
            holder.departure = (TextView) convertView.findViewById(R.id.txtPlaceGo);
            holder.destination = (TextView) convertView.findViewById(R.id.txtDestination);
            holder.totalTime = (TextView) convertView.findViewById(R.id.txtTime);
            holder.totalDistance = (TextView) convertView.findViewById(R.id.txtLength);
            holder.totalPoint = (TextView) convertView.findViewById(R.id.txtTotalPoint);
            holder.txtLinkTrip = (TextView) convertView.findViewById(R.id.txtLinkTrip);
            holder.txtPaymentMethod = (TextView) convertView.findViewById(R.id.txtPaymentMethod);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }
        ItemTripHistory itemTripHistory = arrViews.get(position);

        if (itemTripHistory != null) {
            holder.tripId.setText(itemTripHistory.getTripId() + "");
            holder.timeEnd.setText(itemTripHistory.getEndTime());
//			holder.fullName.setText("("+itemTripHistory.getDriverId()+")");
            holder.departure.setText(itemTripHistory.getStartLocaton());
            if (itemTripHistory.getStartTimeWorking() != null && !itemTripHistory.getStartTimeWorking().equals("") && !itemTripHistory.getStartTimeWorking().equals("0")) {
                holder.totalDistance.setText(getTime(Long.parseLong(itemTripHistory.getStartTimeWorking()), Long.parseLong(itemTripHistory.getEndTimeWorking())));
            } else {
                holder.totalDistance.setText("0");
            }
            holder.destination.setText(itemTripHistory.getEndLocation());
            if (itemTripHistory.getPaymentMethod().equals("1")) {
                holder.txtPaymentMethod.setText(mAct.getString(R.string.pay_by_paypal));
            } else {
                holder.txtPaymentMethod.setText(mAct.getString(R.string.lbl_paybycash));
            }
            if ((itemTripHistory.getDriverId() + "").equals(PreferencesManager.getInstance(mAct).getUserID())) {
                if (itemTripHistory.getPaymentMethod().equals("2")) {
                    holder.totalPoint.setText("-" + itemTripHistory.getActualReceive() + "");
                    holder.totalPoint.setBackgroundResource(R.color.from);
                } else {
                    holder.totalPoint.setText("+" + itemTripHistory.getActualReceive() + "");
                    holder.totalPoint.setBackgroundResource(R.color.blue);
                }
            } else {
                holder.totalPoint.setText("-" + itemTripHistory.getActualFare() + "");
                holder.totalPoint.setBackgroundResource(R.color.from);
            }

            holder.totalTime.setText(String.valueOf(itemTripHistory.getDistance()) + "Km" + "(" + itemTripHistory.getTotalTime() + " "+ mAct.getResources().getString(R.string.minutes)+")");
            holder.txtLinkTrip.setText(convertLinkToString(itemTripHistory.getLink()) + "");
        }
        return convertView;
    }

    private void getDataFromGlobal() {
        user = GlobalValue.getInstance().user;
    }

    public class HolderView {
        ImageView imgNews;
        TextView tripId, fullName, timeStart, timeEnd, departure, destination,
                totalTime, totalDistance, totalPoint, txtLinkTrip,txtPaymentMethod;
    }

    public String convertLinkToString(String link) {
        for (int i = 0; i < GlobalValue.getInstance().getListCarTypes().size(); i++) {
            if (GlobalValue.getInstance().getListCarTypes().get(i).getId() != null && !GlobalValue.getInstance().getListCarTypes().get(i).getId().equals("")) {
                if (GlobalValue.getInstance().getListCarTypes().get(i).getId().equals(link)) {
                    return GlobalValue.getInstance().getListCarTypes().get(i).getName();
                }
            }
        }
        return link;
    }

    public String getTime(long startTime, long endTime) {
        DateTime today = new DateTime(endTime * 1000L);
        DateTime yesterday = new DateTime(startTime * 1000L);
        Duration duration = new Duration(yesterday, today);
        long timeInMilliseconds = duration.getStandardSeconds();
        long mins = timeInMilliseconds / 60;
        long hour = mins / 60;
        if (mins < 1) {
            return "00 minute";
        } else {
            if (hour < 1) {
                return mins + " minute(s)";
            } else {
                return hour + " hour(s)" + mins + " minute(s)";
            }
        }
    }

    private int convertToInt(String s) {
        switch (s) {
            case "I":
                return 1;

            case "II":
                return 2;

            case "III":
                return 3;
            case "IV":
                return 4;
            case "V":
                return 5;


        }
        return 0;
    }
}
