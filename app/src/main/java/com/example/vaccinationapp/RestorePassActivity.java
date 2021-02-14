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

import com.google.gson.JsonObject;
import com.example.vaccinationapp.helpers.APIClient;
import com.example.vaccinationapp.helpers.APIInterface;
import com.example.vaccinationapp.helpers.Constants;
import com.example.vaccinationapp.models.UserModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestorePassActivity extends AppCompatActivity {

    EditText mEditNewPassword, mEditConfirmNewPassword;
    Button mButtonSubmit;
    UserModel.Item item;
    APIInterface apiInterface;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_restore_pass);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        apiInterface = APIClient.getClient().create(APIInterface.class);

        initView();
        getData();
        setOnClickEvent();
    }

    private void setOnClickEvent() {
        mButtonSubmit.setOnClickListener(v -> {
            if (!mEditConfirmNewPassword.getText().toString().equals(mEditNewPassword.getText().toString())) {
                mEditNewPassword.setError("Password Not Match");
            } else {
                updatePass();
            }
        });
    }

    private void updatePass() {
        progressDialog.show();
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("httpMethod", "POST");
            jsonObject.addProperty("firstname", item.getFirstname());
            jsonObject.addProperty("lastname", item.getLastname());
            jsonObject.addProperty("email", item.getEmail());
            jsonObject.addProperty("password", mEditNewPassword.getText().toString());
            jsonObject.addProperty("country", "Canada");
            jsonObject.addProperty("state", "Quebec");
            jsonObject.addProperty("city",  item.getCity());
            jsonObject.addProperty("type", item.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<ResponseBody> call = apiInterface.updateProfile(jsonObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.hide();
                try {
                    if (response.code() == 200) {
                        Toast.makeText(RestorePassActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RestorePassActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RestorePassActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.hide();
                call.cancel();
                Toast.makeText(RestorePassActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        Intent intent = getIntent();
        item = (UserModel.Item) intent.getSerializableExtra("model");
    }

    private void initView() {
        mEditNewPassword = findViewById(R.id.edt_newpass);
        mEditConfirmNewPassword = findViewById(R.id.edt_confirmnewpass);

        mButtonSubmit = findViewById(R.id.btn_submit);
    }
}