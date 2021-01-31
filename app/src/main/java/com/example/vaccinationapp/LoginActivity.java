package com.example.vaccinationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.example.vaccinationapp.helpers.APIClient;
import com.example.vaccinationapp.helpers.APIInterface;
import com.example.vaccinationapp.helpers.Constants;
import com.example.vaccinationapp.helpers.PrefManager;
import com.example.vaccinationapp.models.LoginModel;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = LoginActivity.class.getName();
    EditText mEditEmail, mEditPassword;
    Button mButtonLogin;
    TextView mTextSignUp;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    TextView mForgetPassword;
    RadioGroup mRadioGroup;
    RadioButton mRadioAdmin,mRadioParent,mRadioDoc;
    String mUserType = "Parent";
    PrefManager manager;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        manager = new PrefManager(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        initViews();
        setUpClickListeners();
    }

    private void setUpClickListeners() {
        mForgetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        });

        mTextSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch(checkedId){
                case R.id.rb_admin:
                    // do operations specific to this selection
                    mUserType = "Admin";
                    break;
                case R.id.rb_parent:
                    // do operations specific to this selection
                    mUserType = "Parent";
                    break;
                case R.id.rb_doc:
                    // do operations specific to this selection
                    mUserType = "Doctor";
                    break;
            }
        });

        mButtonLogin.setOnClickListener(view -> {
            if (isDataValid()) {
                progressDialog.show();
                loginUser();
            }
        });
    }

    private void loginUser() {
        LoginModel model = new LoginModel("POST", mEditEmail.getText().toString(),mEditPassword.getText().toString(),mUserType);
        Call<LoginModel> call = apiInterface.loginUser(model);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                progressDialog.hide();
                try {
                    if (response.code() == 200 && response.body().getAuth().toString().equals("true")) {
                        LoginModel newModel = response.body();
                        manager.saveData(Constants.EMAIL_ID, newModel.getItem().getEmail());
                        manager.saveData(Constants.USER_NAME, newModel.getItem().getFirstname() + " " + newModel.getItem().getLastname());
                        manager.saveData(Constants.USER_TYPE, newModel.getItem().getType());
                        manager.saveData(Constants.CITY, newModel.getItem().getCity());
                        manager.saveData(Constants.PASSWORD, newModel.getItem().getPassword());
                        Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to Login user.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                progressDialog.hide();
                call.cancel();
                Toast.makeText(LoginActivity.this, "Sorry we're facing some technical issue.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isDataValid() {
        boolean status = true;

        String email = mEditEmail.getText().toString();
        if (mEditPassword.getText().toString().equalsIgnoreCase("") || mEditPassword.getText().toString().trim().length() < 8) {
            mEditPassword.setError("Enter Valid Password");
            status = false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEditEmail.setError("Enter Valid Email");
            status = false;
        }

        return status;
    }

    private void initViews() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mEditEmail = findViewById(R.id.edt_username);
        mEditPassword = findViewById(R.id.edt_password);

        mButtonLogin = findViewById(R.id.btn_login);

        mTextSignUp = findViewById(R.id.sign_up);
        mForgetPassword = findViewById(R.id.forget_password);

        mRadioGroup = findViewById(R.id.rg_rb);
        mRadioAdmin = findViewById(R.id.rb_admin);
        mRadioParent = findViewById(R.id.rb_parent);
        mRadioDoc = findViewById(R.id.rb_doc);
    }
}
