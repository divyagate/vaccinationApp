package com.example.vaccinationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vaccinationapp.adapters.VaccinationListAdapter;
import com.example.vaccinationapp.clicklisteners.VaccinationListClickListener;
import com.example.vaccinationapp.helpers.APIClient;
import com.example.vaccinationapp.helpers.APIInterface;
import com.example.vaccinationapp.helpers.Constants;
import com.example.vaccinationapp.models.ChildModel;
import com.example.vaccinationapp.models.VaccineListModel;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VaccinationInfoActivity extends AppCompatActivity implements VaccinationListClickListener {

    ImageView mImageBack;
    RecyclerView mRecyclerView;
    VaccinationListAdapter mAdapter;
    ProgressDialog progressDialog;
    APIInterface apiInterface;
    ArrayList<VaccineListModel.Item> mArrayVaccineList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_info);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        mInitViews();
        mInitStatements();
        getVaccineList();
    }

    private void getVaccineList() {
        progressDialog.show();
        VaccineListModel model = new VaccineListModel("POST");
        Call<VaccineListModel> call = apiInterface.getVaccineList(model);
        call.enqueue(new Callback<VaccineListModel>() {
            @Override
            public void onResponse(Call<VaccineListModel> call, Response<VaccineListModel> response) {
                progressDialog.hide();
                try {
                    if (response.code() == 200) {
                        VaccineListModel model1 = response.body();
                        mArrayVaccineList = model1.getItems();
                        setAdapter();
                    } else {
                        Toast.makeText(VaccinationInfoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<VaccineListModel> call, Throwable t) {
                progressDialog.hide();
                call.cancel();
                Toast.makeText(VaccinationInfoActivity.this, "Sorry we're facing some technical issue.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        mAdapter = new VaccinationListAdapter(VaccinationInfoActivity.this,"vaccination_info",this, mArrayVaccineList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(VaccinationInfoActivity.this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void mInitViews() {
        mImageBack = findViewById(R.id.back);
        mRecyclerView = findViewById(R.id.rv_vaccinationlist);
    }

    private void mInitStatements() {
        mImageBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onItemClick(int position, VaccineListModel.Item model) {
        Intent intent = new Intent(VaccinationInfoActivity.this,VaccinationDetailActivity.class);
        intent.putExtra("model", model);
        startActivity(intent);
    }
}