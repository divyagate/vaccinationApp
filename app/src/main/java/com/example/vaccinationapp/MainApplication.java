package com.example.vaccinationapp;

import android.app.Application;
import android.content.Context;

import com.example.vaccinationapp.helpers.LocaleHelper;

public class MainApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}
