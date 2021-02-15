package com.example.vaccinationapp.fragments;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.example.vaccinationapp.R;
import com.example.vaccinationapp.adapters.MemberAdapter;
import com.example.vaccinationapp.clicklisteners.DeleteMemberClickListener;
import com.example.vaccinationapp.helpers.APIClient;
import com.example.vaccinationapp.helpers.APIInterface;
import com.example.vaccinationapp.helpers.PrefManager;
import com.example.vaccinationapp.models.UserModel;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentFragment extends Fragment implements DeleteMemberClickListener {

    View mView;
    RecyclerView mRecyclerView;
    MemberAdapter mAdapter;

    PrefManager manager;
    ProgressDialog progressDialog;
    APIInterface apiInterface;
    ArrayList<UserModel.Item> itemArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_parent, container, false);

        manager = new PrefManager(getActivity());

        apiInterface = APIClient.getClient().create(APIInterface.class);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        mInitViews();
        mInitStatements();
        getUsers();
        return mView;
    }

    private void getUsers() {
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
                        setAdapter();
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

    private void setAdapter() {
        mAdapter = new MemberAdapter(getActivity(),"Parent",this,itemArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void mInitViews() {
        mRecyclerView = mView.findViewById(R.id.rv_member_parent);
    }

    private void mInitStatements() {
    }

    @Override
    public void onItemClick(int position, UserModel.Item model) {
        itemArrayList.remove(position);
        mAdapter.notifyDataSetChanged();
        deleteUser(model);
    }

    private void deleteUser(UserModel.Item model) {
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("httpMethod","POST");
            jsonObject.addProperty("email",model.getEmail());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Call<ResponseBody> call = apiInterface.deleteMember(jsonObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.hide();
                try {
                    if (response.code() == 200) {
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
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
}