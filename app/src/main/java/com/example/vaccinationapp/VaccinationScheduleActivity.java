package com.example.vaccinationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.example.vaccinationapp.adapters.MainChildListAdapter;
import com.example.vaccinationapp.adapters.MemberAdapter;
import com.example.vaccinationapp.clicklisteners.ChildClickListener;
import com.example.vaccinationapp.clicklisteners.GivenStatusListener;
import com.example.vaccinationapp.helpers.APIClient;
import com.example.vaccinationapp.helpers.APIInterface;
import com.example.vaccinationapp.helpers.Constants;
import com.example.vaccinationapp.helpers.PrefManager;
import com.example.vaccinationapp.models.ChildModel;
import com.example.vaccinationapp.models.ScheduleModel;
import com.example.vaccinationapp.models.UserModel;

import java.text.MessageFormat;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VaccinationScheduleActivity extends AppCompatActivity implements ChildClickListener, GivenStatusListener {

    //    https://www.canada.ca/en/public-health/services/publications/healthy-living/canadian-immunization-guide-part-1-key-immunization-information/page-13-recommended-immunization-schedules.html#p1c12a1
    ImageView mImageBack;
    RecyclerView mRecyclerView;
    MainChildListAdapter mAdapter;
    PrefManager manager;
    String UserType;
    ProgressDialog progressDialog;
    APIInterface apiInterface;
    ArrayList<ScheduleModel.Item> mArray = new ArrayList<>();
    ArrayList<UserModel.Item> itemArrayList = new ArrayList<>();
    ChildModel.Item model;
    ProgressBar simpleProgressBar;
    TextView mTextProgressCount;
    int min = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_schedule);
        manager = new PrefManager(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        UserType = manager.getData(Constants.USER_TYPE);

        mInitViews();
        mInitStatements();
        getData();
        getDoctorList();
        getVaccineList();
    }

    private void getDoctorList() {
        progressDialog.show();
        itemArrayList = new ArrayList<>();
        UserModel model = new UserModel("POST", "Doctor");
        Call<UserModel> call = apiInterface.getUserList(model);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                progressDialog.hide();
                try {
                    if (response.code() == 200) {
                        UserModel model1 = response.body();
                        itemArrayList = model1.getItems();
                    } else {
                        Toast.makeText(VaccinationScheduleActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                progressDialog.hide();
                call.cancel();
                Toast.makeText(VaccinationScheduleActivity.this, "Sorry we're facing some technical issue.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        Intent intent = getIntent();
        model = (ChildModel.Item) intent.getSerializableExtra("model");
    }

    private void getVaccineList() {
        progressDialog.show();
        ScheduleModel scModel = new ScheduleModel("POST", model.getId());
        Call<ScheduleModel> call = apiInterface.getScheduleForChild(scModel);
        call.enqueue(new Callback<ScheduleModel>() {
            @Override
            public void onResponse(Call<ScheduleModel> call, Response<ScheduleModel> response) {
                progressDialog.hide();
                try {
                    if (response.code() == 200) {
                        ScheduleModel model1 = response.body();
                        mArray = model1.getItems();
                        setAdapter();
                    } else {
                        Toast.makeText(VaccinationScheduleActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ScheduleModel> call, Throwable t) {
                progressDialog.hide();
                call.cancel();
                Toast.makeText(VaccinationScheduleActivity.this, "Sorry we're facing some technical issue.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        mAdapter = new MainChildListAdapter(this,UserType,this, this, mArray, manager.getData(Constants.CHILD_DOB)){
            @Override
            protected void getDocList(ScheduleModel.Item item) {
                super.getDocList(item);
                if (itemArrayList.size() > 0) {
                    showDocDialogue(item);
                } else {
                    Toast.makeText(VaccinationScheduleActivity.this, "Sorry no doctor available in your area", Toast.LENGTH_SHORT).show();
                }
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (mArray.size() > 0) {
            simpleProgressBar.setMax(mArray.size());
            for (int i = 0; i < mArray.size(); i++) {
                if (mArray.get(i).getStatus().equals("true")) {
                    min += 1;
                }
            }
            simpleProgressBar.setProgress(min);
            mTextProgressCount.setText(MessageFormat.format("{0}/{1}", min, mArray.size()));
        }
    }

    private void showDocDialogue(ScheduleModel.Item item) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.doctor_dialogue);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RecyclerView recyclerView = dialog.findViewById(R.id.rv_recyclerview);

        if (itemArrayList.size() > 0) {
            MemberAdapter mAdapter = new MemberAdapter(VaccinationScheduleActivity.this,"Schedule",null,itemArrayList){
                @Override
                protected void selectDoc(UserModel.Item itemDoc) {
                    super.selectDoc(itemDoc);
                    submitRequest(item, itemDoc);
                    dialog.dismiss();
                }
            };
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(VaccinationScheduleActivity.this));
        }
        dialog.show();
    }

    private void submitRequest(ScheduleModel.Item item, UserModel.Item itemDoc) {
        progressDialog.show();
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("httpMethod", "POST");
            jsonObject.addProperty("cid", manager.getData(Constants.CHILD_ID));
            jsonObject.addProperty("vid", item.getId());
            jsonObject.addProperty("doctorEmail", itemDoc.getEmail());
            jsonObject.addProperty("status", "false");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Call<ResponseBody> call = apiInterface.submitRequest(jsonObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.hide();
                try {
                    if (response.code() == 200) {
                        Toast.makeText(VaccinationScheduleActivity.this, "Request Successfully Submitted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VaccinationScheduleActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.hide();
                call.cancel();
                Toast.makeText(VaccinationScheduleActivity.this, "Sorry we're facing some technical issue.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mInitViews() {
        mImageBack = findViewById(R.id.back);
        mRecyclerView = findViewById(R.id.rv_age_view);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        mTextProgressCount = findViewById(R.id.tv_progresscount);
        mTextProgressCount.setText("0/0");
    }

    private void mInitStatements() {
        mImageBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onItemClick(ScheduleModel.Item model, String dob) {
        Intent intent = new Intent(VaccinationScheduleActivity.this, GenerateReportActivity.class);
        intent.putExtra("model", model);
        intent.putExtra("dob", dob);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position, ScheduleModel.Item model, String httpMethod) {
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("httpMethod", httpMethod);
            jsonObject.addProperty("cid", manager.getData(Constants.CHILD_ID));
            jsonObject.addProperty("vid", model.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<ResponseBody> call = apiInterface.givenStatus(jsonObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.hide();
                try {
                    if (response.code() == 200) {
                        Toast.makeText(VaccinationScheduleActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        updateProgress(httpMethod);
                    } else {
                        Toast.makeText(VaccinationScheduleActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.hide();
                call.cancel();
                Toast.makeText(VaccinationScheduleActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProgress(String httpMethod) {
        if (httpMethod.equals("POST")) {
            min += 1;
        } else {
            min -= 1;
        }
        simpleProgressBar.setProgress(min);
        mTextProgressCount.setText(MessageFormat.format("{0}/{1}", min, mArray.size()));
    }
}