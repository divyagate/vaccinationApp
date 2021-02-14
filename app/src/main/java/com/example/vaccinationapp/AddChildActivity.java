package com.example.vaccinationapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.example.vaccinationapp.adapters.VaccinationListAdapter;
import com.example.vaccinationapp.helpers.APIClient;
import com.example.vaccinationapp.helpers.APIInterface;
import com.example.vaccinationapp.helpers.Constants;
import com.example.vaccinationapp.helpers.PrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddChildActivity extends AppCompatActivity{

    ImageView mImageBack;
    EditText mEditChildName, mEditDOB;
    VaccinationListAdapter mAdapter;
    RecyclerView mRecyclerView;
    RadioGroup mRadioGroup;
    RadioButton mRadioMale,mRadioFemale,mRadioOther;
    String mGender = "Female";
    Button mButtonSubmit;
    private int mYear, mMonth, mDay;
    PrefManager manager;
    ProgressDialog progressDialog;
    APIInterface apiInterface;
    Long dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        manager = new PrefManager(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

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
        mButtonSubmit.setOnClickListener(v -> {
            if (isDataValid()) {
                progressDialog.show();
                addChild();
            }
        });

        mEditDOB.setOnClickListener(v -> {
            // Get Current Date
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(AddChildActivity.this,
                    (view, year, monthOfYear, dayOfMonth) ->
                            mEditDOB.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);

            datePickerDialog.show();
            datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        });

        mEditDOB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
                Date d;
                try {
                    d = f.parse(mEditDOB.getText().toString());
                    assert d != null;
                    dob = d.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        mImageBack.setOnClickListener(v -> onBackPressed());

        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch(checkedId){
                case R.id.rb_male:
                    // do operations specific to this selection
                    mGender = "Male";
                    break;
                case R.id.rb_female:
                    // do operations specific to this selection
                    mGender = "Female";
                    break;
                case R.id.rb_other:
                    // do operations specific to this selection
                    mGender = "Other";
                    break;
            }
        });
/*
        mAdapter = new VaccinationListAdapter(this,"add",null);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));*/
    }

    private void addChild() {
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("httpMethod", "POST");
            jsonObject.addProperty("childName", mEditChildName.getText().toString().trim());
            jsonObject.addProperty("dob", dob);
            jsonObject.addProperty("gender", mGender);
            jsonObject.addProperty("parentEmail", manager.getData(Constants.EMAIL_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<ResponseBody> call = apiInterface.addChild(jsonObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.hide();
                try {
                    if (response.code() == 200) {
                        Toast.makeText(AddChildActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddChildActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Toast.makeText(AddChildActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.hide();
                call.cancel();
                Toast.makeText(AddChildActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isDataValid() {
        boolean status = true;
        if (mEditChildName.getText().toString().equals("")) {
            status = false;
            mEditChildName.setError("Enter Valid Child Name");
        }
        if (mEditDOB.getText().toString().equals("")) {
            status = false;
            mEditDOB.setError("Enter Valid Date Of Birth");
        }
        return  status;
    }
}