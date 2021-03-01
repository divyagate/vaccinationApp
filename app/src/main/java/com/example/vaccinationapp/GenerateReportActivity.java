package com.example.vaccinationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vaccinationapp.adapters.VaccinationListAdapter;
import com.example.vaccinationapp.helpers.Constants;
import com.example.vaccinationapp.helpers.PrefManager;
import com.example.vaccinationapp.models.ScheduleModel;

public class GenerateReportActivity extends AppCompatActivity {

    ImageView mImageBack;
    PrefManager manager;
    String UserType;
    EditText mEditVaccinationName, mEditChildName, mEditGivenDate, mEditParentName, mEditRemarks;
    Button mButtonSubmitReport;
    TextView mTextVaccinationName, mTextGivenDate, mTextChildName, mTextParentName, mTextRemarks;
    LinearLayout mLinearAdminView, mLinearParentView;
    ScheduleModel.Item model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);
        manager = new PrefManager(this);
        UserType = manager.getData(Constants.USER_TYPE);

        mInitViews();
        getData();
        mInitStatements();
    }

    
    private void getData() {
        try {
            Intent intent = getIntent();
            model = (ScheduleModel.Item) intent.getSerializableExtra("model");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void mInitViews() {
        mImageBack = findViewById(R.id.back);

        mEditVaccinationName = findViewById(R.id.edt_vaccine_name);
        mEditChildName = findViewById(R.id.edt_childname);
        mEditGivenDate = findViewById(R.id.edt_date);
        mEditParentName = findViewById(R.id.edt_parentname);
        mEditRemarks = findViewById(R.id.edt_remarks);

        mTextVaccinationName = findViewById(R.id.tv_vaccination_name);
        mTextGivenDate = findViewById(R.id.tv_given_date);
        mTextChildName = findViewById(R.id.tv_child_name);
        mTextParentName = findViewById(R.id.tv_parent_name);
        mTextRemarks = findViewById(R.id.tv_remarks);

        mButtonSubmitReport = findViewById(R.id.btn_submit);

        mLinearAdminView = findViewById(R.id.admin_view);
        mLinearParentView = findViewById(R.id.parent_view);
    }

    private void mInitStatements() {
        mImageBack.setOnClickListener(v -> onBackPressed());

        mLinearAdminView.setVisibility(View.GONE);
        mLinearParentView.setVisibility(View.VISIBLE);

        mTextVaccinationName.setText(String.format("Vaccination Name : %s", model.getName()));
        mTextGivenDate.setText(String.format("Given Date : %s", model.getDueDate()));
        mTextChildName.setText(String.format("Infant Name : %s", manager.getData(Constants.CHILD_NAME)));

        /*if (UserType.equals("Admin")) {
            mLinearAdminView.setVisibility(View.VISIBLE);
            mLinearParentView.setVisibility(View.GONE);

            mEditVaccinationName.setText(model.getName());
            mEditGivenDate.setText(model.getDueDate());
            mEditChildName.setText(manager.getData(Constants.CHILD_NAME));

            mButtonSubmitReport.setOnClickListener(v -> {
                if (isDataValid()) {
                    finish();
                }
            });
        } else {
            mLinearAdminView.setVisibility(View.GONE);
            mLinearParentView.setVisibility(View.VISIBLE);

            mTextVaccinationName.setText(String.format("Vaccination Name : %s", model.getName()));
            mTextGivenDate.setText(String.format("Given Date : %s", model.getDueDate()));
            mTextChildName.setText(String.format("Infant Name : %s", manager.getData(Constants.CHILD_NAME)));
        }*/
    }

    private boolean isDataValid() {
        boolean status = true;

        if (mEditChildName.getText().toString().equals("")) {
            status = false;
            mEditChildName.setError("Enter Child Name");
        }
        if (mEditGivenDate.getText().toString().equals("")) {
            status = false;
            mEditGivenDate.setError("Enter Date");
        }
        if (mEditVaccinationName.getText().toString().equals("")) {
            status = false;
            mEditVaccinationName.setError("Enter Vaccination Name");
        }
        return status;
    }
}