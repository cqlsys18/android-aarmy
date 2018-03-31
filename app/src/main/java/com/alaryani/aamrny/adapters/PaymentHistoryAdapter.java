package com.alaryani.aamrny.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alaryani.aamrny.R;
import com.alaryani.aamrny.object.ItemTransactionHistory;

import java.util.ArrayList;

public class PaymentHistoryAdapter extends BaseAdapter {

    private LayoutInflater mInflate;
    private ArrayList<ItemTransactionHistory> arrViews;
    Activity mAct;

    // AQuery aq;

    public PaymentHistoryAdapter(Activity activity,
                                 ArrayList<ItemTransactionHistory> arrViews) {
        this.mAct = activity;
        this.arrViews = arrViews;
        this.mInflate = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<ItemTransactionHistory> getArrViews() {
        return arrViews;
    }

    public void setArrViews(ArrayList<ItemTransactionHistory> arrViews) {
        this.arrViews = arrViews;
    }

    @Override
    public int getCount() {

        return arrViews.size();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        final HolderView holder;
        if (convertView == null) {
            holder = new HolderView();
            convertView = mInflate.inflate(
                    R.layout.layout_item_payment_history, null);

            holder.lblTransactionID = (TextView) convertView
                    .findViewById(R.id.txtTransactionID);
            holder.lblDateTime = (TextView) convertView
                    .findViewById(R.id.txtDateTime);

            holder.lblNote = (TextView) convertView.findViewById(R.id.txtNote);
            holder.lblPoint = (TextView) convertView
                    .findViewById(R.id.lblDestination);
            holder.txtTripID = (TextView) convertView
                    .findViewById(R.id.txtTripID);
            holder.lblTripIdHead = (TextView) convertView.findViewById(R.id.lblTripIdHead);

            convertView.setTag(holder);

        } else {
            holder = (HolderView) convertView.getTag();
        }
        ItemTransactionHistory itemTransaction = arrViews.get(position);
        if (itemTransaction != null) {
            holder.lblTransactionID.setText(itemTransaction.getTransactionId()
                    .toString());
            holder.lblDateTime
                    .setText(itemTransaction.getDateTimeTransaction());


            if (itemTransaction.getTripId().equals("null")) {
                holder.txtTripID.setVisibility(View.GONE);
                //holder.lblTripIdHead.setVisibility(View.GONE);
            } else {
                holder.txtTripID.setText(itemTransaction.getTripId());
                //holder.lblTripIdHead.setVisibility(View.VISIBLE);
            }

            if (itemTransaction.getTypeTransaction().equalsIgnoreCase("-")) {
                holder.lblPoint.setText("-" + itemTransaction.getPointTransaction());
                holder.lblPoint.setBackgroundResource(R.color.from);
            } else {
                if (itemTransaction.getTypeTransaction().equalsIgnoreCase("+")) {
                    holder.lblPoint.setText("+" + itemTransaction
                            .getPointTransaction());
                    holder.lblPoint.setBackgroundResource(R.color.to);
                }

            }

            if (itemTransaction.getNoteTransaction().equals("1")) {
                holder.lblNote.setText(mAct.getResources().getString(
                        R.string.action_cancellation_order_fee));
            } else if (itemTransaction.getNoteTransaction().equals("2")) {
                holder.lblNote.setText(mAct.getResources().getString(
                        R.string.action_exchange_point));
            } else if (itemTransaction.getNoteTransaction().equals("3")) {
                holder.lblNote.setText(mAct.getResources().getString(
                        R.string.action_redeem_point));
            } else if (itemTransaction.getNoteTransaction().equals("4")) {
                holder.lblNote.setText(mAct.getResources().getString(
                        R.string.action_transfer_point));
            } else if (itemTransaction.getNoteTransaction().equals("5")) {
                holder.lblNote.setText(mAct.getResources().getString(
                        R.string.action_trip_payment));
            } else if (itemTransaction.getNoteTransaction().equals("6")) {
                holder.lblNote.setText(mAct.getResources().getString(
                        R.string.action_passenger_share_bonus));
            } else {
                holder.lblNote.setText(mAct.getResources().getString(
                        R.string.action_driver_share_bonus));
            }

        }
        return convertView;
    }

    public class HolderView {
        TextView lblPoint, lblTransactionID, lblDateTime, lblNote,
                txtTripID, lblTripIdHead;
    }
}
