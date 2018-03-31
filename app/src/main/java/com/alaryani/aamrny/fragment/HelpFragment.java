package com.alaryani.aamrny.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alaryani.aamrny.BaseFragment;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.widget.TextViewRaleway;

public class HelpFragment extends BaseFragment {

    TextViewRaleway txtTitle, txtWebsite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        initControl(view);
        initUI(view);
        initMenuButton(view);
        initData();
        setHeaderTitle(R.string.lbl_help);
        return view;
    }

    public void initData() {
        txtWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://hicomsolutions.com.au"));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initControl(View view) {
        txtTitle = (TextViewRaleway) view.findViewById(R.id.lblTitle);
        txtWebsite = (TextViewRaleway) view.findViewById(R.id.txtWebsite);
    }

    public void changeLanguage() {
        txtTitle.setText(R.string.lbl_help);
    }

}
