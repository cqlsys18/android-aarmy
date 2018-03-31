package com.alaryani.aamrny.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alaryani.aamrny.BaseFragment;
import com.alaryani.aamrny.R;
import com.alaryani.aamrny.activities.MainActivity;
import com.alaryani.aamrny.config.PreferencesManager;
import com.alaryani.aamrny.util.LocaleHelper;
import com.alaryani.aamrny.widget.CircleImageView;
import com.androidquery.AQuery;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeLanguageFragment extends BaseFragment implements View.OnClickListener{
    private ImageView english, arabic;
    private Button confirm;
    private String selected_app_language = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_language, container, false);
        initMenuButton(view);
        initUI(view);
        initControl();
        checkCurrentLanguage();
        return view;
    }

    public void initUI(View view) {
        english = (ImageView) view.findViewById(R.id.english);
        arabic = (ImageView) view.findViewById(R.id.arabic);
        confirm = (Button) view.findViewById(R.id.confirm);
    }

    public void initControl() {
        english.setOnClickListener(this);
        arabic.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    private void checkCurrentLanguage() {
        if (PreferencesManager.getString(getActivity(), "app_language", "en").equals("en")) {
            selected_app_language = "en";
            english.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.radio_selected_seller));
            arabic.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.radio_un_select_seller));
        } else {
            selected_app_language = "ar";
            english.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.radio_un_select_seller));
            arabic.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.radio_selected_seller));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.english:
                selected_app_language = "en";
                updateUI();
                break;
            case R.id.arabic:
                selected_app_language = "ar";
                updateUI();
                break;
            case R.id.confirm:
                changeLanguage();
                break;
        }
    }

    private void updateUI(){
        if (selected_app_language.equals("en")) {
            english.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.radio_selected_seller));
            arabic.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.radio_un_select_seller));
        } else {
            english.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.radio_un_select_seller));
            arabic.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.radio_selected_seller));
        }
    }

    private void changeLanguage(){
        LocaleHelper.setLocale(getActivity(), selected_app_language);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        showToast( getResources().getString(R.string.language_updated));
    }
}
