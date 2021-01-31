package com.example.vaccinationapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.vaccinationapp.adapters.VaccinationListAdapter;
import com.example.vaccinationapp.helpers.PrefManager;

import java.util.Calendar;

public class AddChildActivity extends AppCompatActivity{

    ImageView mImageBack;
    EditText mEditChildName, mEditDOB;
    VaccinationListAdapter mAdapter;
    RecyclerView mRecyclerView;
    RadioGroup mRadioGroup;
    RadioButton mRadioMale,mRadioFemale,mRadioOther;
    String mGender;
    Button mButtonSubmit;
    private int mYear, mMonth, mDay;
    PrefManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        mInitViews();
        mInitStatements();
    }

    private void mInitViews() {
        mImageBack = findViewById(R.id.back);

        mEditChildName = findViewById(R.id.edt_childname);
        mEditDOB = findViewById(R.id.edt_dob);

        mRecyclerView = findViewById(R.id.rv_vaccinationlist);

        mRadioGroup = findViewById(R.id.rg_rb);
        mRadioMale = findViewById(R.id.rb_male);
        mRadioFemale = findViewById(R.id.rb_female);
        mRadioOther = findViewById(R.id.rb_other);

        mButtonSubmit = findViewById(R.id.btn_submit);
    }

    private void mInitStatements() {
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mEditDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddChildActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                mEditDOB.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @SuppressLint("NonConstantResourceId")
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_admin:
                        // do operations specific to this selection
                        mGender = "Male";
                        break;
                    case R.id.rb_parent:
                        // do operations specific to this selection
                        mGender = "Female";
                        break;
                    case R.id.rb_doc:
                        // do operations specific to this selection
                        mGender = "Other";
                        break;
                }
            }
        });

        mAdapter = new VaccinationListAdapter(this,"add",null);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}