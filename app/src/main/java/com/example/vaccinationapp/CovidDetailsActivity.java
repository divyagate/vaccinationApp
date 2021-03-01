package com.example.vaccinationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CovidDetailsActivity extends AppCompatActivity {

    ImageView mImageBack;
    LinearLayout mLayoutVaccine1, mLayoutVaccine2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_details);

        mImageBack = findViewById(R.id.back);
        Intent intent = getIntent();
        String vaccineName = intent.getStringExtra("vaccine_name");

        mLayoutVaccine1 = findViewById(R.id.layout_vaccine_1);
        mLayoutVaccine2 = findViewById(R.id.layout_vaccine_2);

        mImageBack.setOnClickListener(v -> onBackPressed());

        if (vaccineName.equals("vacc_1")) {
            mLayoutVaccine1.setVisibility(View.VISIBLE);
            mLayoutVaccine2.setVisibility(View.GONE);
        } else {
            mLayoutVaccine1.setVisibility(View.GONE);
            mLayoutVaccine2.setVisibility(View.VISIBLE);
        }
    }
}