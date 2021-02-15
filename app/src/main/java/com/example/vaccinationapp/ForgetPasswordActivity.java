package com.example.vaccinationapp;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vaccinationapp.helpers.APIClient;
import com.example.vaccinationapp.helpers.APIInterface;
import com.example.vaccinationapp.models.UserModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText mEditEmail;
    Button mButtonSubmit;
    ProgressDialog progressDialog;
    APIInterface apiInterface;
    ArrayList<UserModel.Item> itemArrayList = new ArrayList<>();
    boolean verifier = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        mEditEmail = findViewById(R.id.edt_email);
        mButtonSubmit = findViewById(R.id.btn_submit);


        getAllDoctor();
        getAllParent();

        mButtonSubmit.setOnClickListener(v -> {
            if (mEditEmail.getText().toString().equals("")) {
                mEditEmail.setError("Enter Valid Email");
            } else {
                for (int i = 0; i < itemArrayList.size(); i++) {
                    if (mEditEmail.getText().toString().equals(itemArrayList.get(i).getEmail())) {
                        Intent intent = new Intent(ForgetPasswordActivity.this, RestorePassActivity.class);
                        intent.putExtra("model", itemArrayList.get(i));
                        startActivity(intent);
                        verifier = true;
                        break;
                    }
                    if (i == itemArrayList.size() - 1 && !verifier) {
                        Toast.makeText(ForgetPasswordActivity.this, "Sorry we couldn't verify you.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getAllParent() {
        progressDialog.show();
        UserModel model = new UserModel("POST", "Parent");
        Call<UserModel> call = apiInterface.getUserList(model);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                progressDialog.hide();
                try {
                    if (response.code() == 200) {
                        UserModel model1 = response.body();
                        itemArrayList.addAll(model1.getItems());
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                progressDialog.hide();
                call.cancel();
            }
        });
    }

    private void getAllDoctor() {
        progressDialog.show();
        UserModel model = new UserModel("POST", "Doctor");
        Call<UserModel> call = apiInterface.getUserList(model);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                progressDialog.hide();
                try {
                    if (response.code() == 200) {
                        UserModel model1 = response.body();
                        itemArrayList.addAll(model1.getItems());
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                progressDialog.hide();
                call.cancel();
            }
        });
    }
}