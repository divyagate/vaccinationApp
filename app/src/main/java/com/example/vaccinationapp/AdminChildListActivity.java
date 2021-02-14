package com.example.vaccinationapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vaccinationapp.adapters.HomeAdapter;
import com.example.vaccinationapp.clicklisteners.HomeClickListener;
import com.example.vaccinationapp.helpers.APIClient;
import com.example.vaccinationapp.helpers.APIInterface;
import com.example.vaccinationapp.helpers.Constants;
import com.example.vaccinationapp.helpers.PrefManager;
import com.example.vaccinationapp.models.ChildModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminChildListActivity extends AppCompatActivity implements HomeClickListener {

    ImageView mImageBack;
    TextView mTextNoData;
    RecyclerView mRecyclerView;
    HomeAdapter mAdapter;
    PrefManager manager;
    ProgressDialog progressDialog;
    APIInterface apiInterface;
    ArrayList<ChildModel.Item> mArrayChild = new ArrayList<>();
    String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_child_list);

        manager = new PrefManager(AdminChildListActivity.this);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        progressDialog = new ProgressDialog(AdminChildListActivity.this);
        progressDialog.setMessage("Loading...");

        try {
            Intent intent = getIntent();
            mEmail = intent.getStringExtra("parent_email");
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        mInitViews();
        mInitStatements();
        getChild();
    }

    private void mInitViews() {
        mRecyclerView = findViewById(R.id.rv_home);
        mTextNoData = findViewById(R.id.no_data);
        mImageBack = findViewById(R.id.back);
    }

    private void mInitStatements() {
        mImageBack.setOnClickListener(v -> onBackPressed());
    }

    private void getChild() {
        progressDialog.show();
        ChildModel model = new ChildModel("POST", mEmail, "Parent");
        Call<ChildModel> call = apiInterface.getChild(model);
        call.enqueue(new Callback<ChildModel>() {
            @Override
            public void onResponse(Call<ChildModel> call, Response<ChildModel> response) {
                progressDialog.hide();
                try {
                    if (response.code() == 200) {
                        ChildModel model1 = response.body();
                        mArrayChild = model1.getItems();
                        mTextNoData.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        if (mArrayChild.size() > 0) {
                            setAdapter();
                        } else {
                            mTextNoData.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(AdminChildListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        mTextNoData.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mTextNoData.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ChildModel> call, Throwable t) {
                progressDialog.hide();
                call.cancel();
                Toast.makeText(AdminChildListActivity.this, "Sorry we're facing some technical issue.", Toast.LENGTH_SHORT).show();
                mTextNoData.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void setAdapter() {
        String userType = manager.getData(Constants.USER_TYPE);
        mAdapter = new HomeAdapter(AdminChildListActivity.this,userType,this,mArrayChild);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AdminChildListActivity.this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(int position,ChildModel.Item model) {
        Intent intent = new Intent(AdminChildListActivity.this, VaccinationScheduleActivity.class);
        intent.putExtra("model", model);
        manager.saveData(Constants.CHILD_NAME, model.getChildName());
        manager.saveData(Constants.CHILD_ID, model.getId());
        manager.saveData(Constants.CHILD_DOB, model.getDob());
        startActivity(intent);
    }
}