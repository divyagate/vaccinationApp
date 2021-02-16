package com.example.vaccinationapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.example.vaccinationapp.AdminChildListActivity;
import com.example.vaccinationapp.R;
import com.example.vaccinationapp.VaccinationScheduleActivity;
import com.example.vaccinationapp.adapters.AdminHomeAdapter;
import com.example.vaccinationapp.adapters.HomeAdapter;
import com.example.vaccinationapp.clicklisteners.HomeClickListener;
import com.example.vaccinationapp.helpers.APIClient;
import com.example.vaccinationapp.helpers.APIInterface;
import com.example.vaccinationapp.helpers.Constants;
import com.example.vaccinationapp.helpers.PrefManager;
import com.example.vaccinationapp.models.ChildModel;
import com.example.vaccinationapp.models.UserModel;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements HomeClickListener {

    View mView;
    TextView mTextNoData, mUpcoming;
    RecyclerView mRecyclerView;
    HomeAdapter mAdapter;
    PrefManager manager;
    ProgressDialog progressDialog;
    APIInterface apiInterface;
    ArrayList<ChildModel.Item> mArrayChild = new ArrayList<>();
    ArrayList<UserModel.Item> itemArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        manager = new PrefManager(getActivity());

        apiInterface = APIClient.getClient().create(APIInterface.class);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");

        mInitViews();
        mInitStatements();
        if (manager.getData(Constants.USER_TYPE).equals("Admin")) {
            getParents();
        } else if (manager.getData(Constants.USER_TYPE).equals("Doctor")) {
            mUpcoming.setVisibility(View.VISIBLE);
            getChild();
        } else {
            getChild();
        }
        return mView;
    }

    private void getParents() {
        progressDialog.show();
        itemArrayList = new ArrayList<>();
        UserModel model = new UserModel("POST", "Parent");
        Call<UserModel> call = apiInterface.getUserList(model);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                progressDialog.hide();
                try {
                    if (response.code() == 200) {
                        UserModel model1 = response.body();
                        itemArrayList = model1.getItems();
                        setAdapterParent();
                    } else {
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                progressDialog.hide();
                call.cancel();
                Toast.makeText(getActivity(), "Sorry we're facing some technical issue.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapterParent() {
        AdminHomeAdapter mAdapter = new AdminHomeAdapter(getActivity(), itemArrayList) {
            @Override
            protected void onClick(UserModel.Item item) {
                super.onClick(item);
                Intent intent = new Intent(getActivity(), AdminChildListActivity.class);
                intent.putExtra("parent_email", item.getEmail());
                startActivity(intent);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getChild() {
        progressDialog.show();
        ChildModel model = new ChildModel("POST", manager.getData(Constants.EMAIL_ID), manager.getData(Constants.USER_TYPE));
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
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Sorry we're facing some technical issue.", Toast.LENGTH_SHORT).show();
                mTextNoData.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void setAdapter() {
        String userType = manager.getData(Constants.USER_TYPE);
        mAdapter = new HomeAdapter(getActivity(),userType,this,mArrayChild) {
            @Override
            protected void submitRequest(ChildModel.Item item, String aTrue) {
                super.submitRequest(item, aTrue);
                updateStatus(item, aTrue);
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void updateStatus(ChildModel.Item item, String aTrue) {
        progressDialog.show();
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("httpMethod", "UPDATE");
            jsonObject.addProperty("cid", item.getId());
            jsonObject.addProperty("vid", item.getVid());
            jsonObject.addProperty("doctorEmail", manager.getData(Constants.EMAIL_ID));
            jsonObject.addProperty("status", aTrue);
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
                        Toast.makeText(getActivity(), "Request Successfully Submitted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.hide();
                call.cancel();
                Toast.makeText(getActivity(), "Sorry we're facing some technical issue.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mInitViews() {
        mTextNoData = mView.findViewById(R.id.no_data);
        mRecyclerView = mView.findViewById(R.id.rv_home);
        mUpcoming = mView.findViewById(R.id.upcoming);
    }

    private void mInitStatements() {
        String userType = manager.getData(Constants.USER_TYPE);
    }

    @Override
    public void onItemClick(int position,ChildModel.Item model) {
        Intent intent = new Intent(getActivity(), VaccinationScheduleActivity.class);
        intent.putExtra("model", model);
        manager.saveData(Constants.CHILD_NAME, model.getChildName());
        manager.saveData(Constants.CHILD_ID, model.getId());
        manager.saveData(Constants.CHILD_DOB, model.getDob());
        startActivity(intent);
    }
}