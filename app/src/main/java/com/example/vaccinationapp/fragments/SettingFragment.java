package com.example.vaccinationapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vaccinationapp.GenSettingsActivity;
import com.example.vaccinationapp.MainActivity;
import com.example.vaccinationapp.MembersActivity;
import com.example.vaccinationapp.R;
import com.example.vaccinationapp.helpers.Constants;
import com.example.vaccinationapp.helpers.LocaleHelper;
import com.example.vaccinationapp.helpers.PrefManager;

public class SettingFragment extends Fragment {

    TextView mTextMember,mTextAboutUs,mTextContactUs,mTextTermsAndConditions, mTextEnglish, mTextFrench;
    View mView;
    LinearLayout mLinearMember;
    PrefManager manager;

    Context context;
    Resources resources;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_setting, container, false);
        setHasOptionsMenu(true);
        manager = new PrefManager(getActivity());

        mInit();
        mInitStatements();
        return mView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    private void mInitStatements() {

        if (manager.getData(Constants.USER_TYPE).equals("Admin")) {
            mLinearMember.setVisibility(View.VISIBLE);
        }

        mTextMember.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MembersActivity.class);
            startActivity(intent);
        });

        mTextAboutUs.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GenSettingsActivity.class);
            intent.putExtra("title","About Us");
            startActivity(intent);
        });

        mTextContactUs.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GenSettingsActivity.class);
            intent.putExtra("title","Contact Us");
            startActivity(intent);
        });

        mTextTermsAndConditions.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GenSettingsActivity.class);
            intent.putExtra("title","Terms And Conditions");
            startActivity(intent);
        });

        try {
            if (manager.getData(Constants.SELECTED_LANG).equals("en")) {
                Drawable icUser = getResources().getDrawable(R.drawable.accept);
                mTextEnglish.setCompoundDrawablesWithIntrinsicBounds(null, null, icUser, null);
                mTextFrench.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            } else if (manager.getData(Constants.SELECTED_LANG).equals("fr")){
                Drawable icUser = getResources().getDrawable(R.drawable.accept);
                mTextFrench.setCompoundDrawablesWithIntrinsicBounds(null, null, icUser, null);
                mTextEnglish.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            } else {
                Drawable icUser = getResources().getDrawable(R.drawable.accept);
                mTextEnglish.setCompoundDrawablesWithIntrinsicBounds(null, null, icUser, null);
                mTextFrench.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        mTextEnglish.setOnClickListener(v -> {
            context = LocaleHelper.setLocale(getActivity(), "en");
            resources = context.getResources();
            manager.saveData(Constants.SELECTED_LANG, "en");

            Drawable icUser = getResources().getDrawable(R.drawable.accept);
            mTextEnglish.setCompoundDrawablesWithIntrinsicBounds(null, null, icUser, null);
            mTextFrench.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });

        mTextFrench.setOnClickListener(v -> {
            context = LocaleHelper.setLocale(getActivity(), "fr");
            resources = context.getResources();
            manager.saveData(Constants.SELECTED_LANG, "fr");

            Drawable icUser = getResources().getDrawable(R.drawable.accept);
            mTextFrench.setCompoundDrawablesWithIntrinsicBounds(null, null, icUser, null);
            mTextEnglish.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
    }

    private void mInit() {
        mTextMember = mView.findViewById(R.id.members);
        mTextAboutUs = mView.findViewById(R.id.about);
        mTextContactUs = mView.findViewById(R.id.contactus);
        mTextTermsAndConditions = mView.findViewById(R.id.tandc);
        mTextEnglish = mView.findViewById(R.id.english);
        mTextFrench = mView.findViewById(R.id.french);

        mLinearMember = mView.findViewById(R.id.linear_member);
    }
}