package com.example.vaccinationapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vaccinationapp.models.VaccineListModel;

public class VaccinationDetailActivity extends AppCompatActivity {

    ImageView mImageBack;
    TextView mTextVaccineName, mTextDetails, mTextSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_detail);

        mInitViews();
        mInitStatements();
        getData();
    }

    private void getData() {
        try {
            Intent intent = getIntent();
            VaccineListModel.Item model = (VaccineListModel.Item) intent.getSerializableExtra("model");
            mTextVaccineName.setText(model.getName());
            mTextDetails.setText(model.getDetails());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mInitViews() {
        mImageBack = findViewById(R.id.back);

        mTextVaccineName = findViewById(R.id.vaccination_name);
        mTextDetails = findViewById(R.id.vaccination_detail);
        mTextSchedule = findViewById(R.id.tobetaken);
    }

    private void mInitStatements() {
        mImageBack.setOnClickListener(v -> onBackPressed());
    }
}