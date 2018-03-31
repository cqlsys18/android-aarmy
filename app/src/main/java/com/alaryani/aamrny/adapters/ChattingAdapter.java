package com.alaryani.aamrny.adapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alaryani.aamrny.R;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.object.ChattingResponse;

import java.util.ArrayList;

/**
 * Created by V on 8/9/2017.
 */

public class ChattingAdapter extends BaseAdapter {
    private Context context;
    ArrayList<ChattingResponse> categoriesArrayList;
    private PreferencesManager preferencesManager;

    public ChattingAdapter(Context context, ArrayList<ChattingResponse> categoriesArrayList) {
        this.context = context;
        this.categoriesArrayList = categoriesArrayList;
        preferencesManager = PreferencesManager.getInstance(context);
    }


    @Override
    public int getCount() {
        return categoriesArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.chatting_row, null, false);

        TextView text_view_right = (TextView) view.findViewById(R.id.text_view_right);
        TextView text_view_left = (TextView) view.findViewById(R.id.text_view_left);

        TextView message_time_right = (TextView) view.findViewById(R.id.message_time_right);
        TextView message_time_left = (TextView) view.findViewById(R.id.message_time_left);

        RelativeLayout left_message_layout = (RelativeLayout) view.findViewById(R.id.left_message_layout);
        RelativeLayout right_message_layout = (RelativeLayout) view.findViewById(R.id.right_message_layout);

        if (preferencesManager.getUserID().equals(categoriesArrayList.get(i).getSender_id())) {
            right_message_layout.setVisibility(View.VISIBLE);
            left_message_layout.setVisibility(View.GONE);

            text_view_right.setText(categoriesArrayList.get(i).getMessage());
            message_time_right.setText(categoriesArrayList.get(i).getCreated_time());
        } else {
            right_message_layout.setVisibility(View.GONE);
            left_message_layout.setVisibility(View.VISIBLE);

            text_view_left.setText(categoriesArrayList.get(i).getMessage());
            message_time_left.setText(categoriesArrayList.get(i).getCreated_time());
        }

        return view;
    }
}
