package com.example.vaccinationapp;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.vaccinationapp.helpers.Constants;
import com.example.vaccinationapp.helpers.PrefManager;

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
    String mUserType;
    PrefManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        manager = new PrefManager(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        initViews();
        setUpClickListeners();
    }

    private void setUpClickListeners() {
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        mTextSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @SuppressLint("NonConstantResourceId")
            public void onCheckedChanged(RadioGroup group, int checkedId) {
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
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDataValid()) {
                    progressDialog.show();
                    final String email = mEditEmail.getText().toString();
                    String password = mEditPassword.getText().toString();

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        manager.saveData(Constants.EMAIL_ID,email);
                                        if (user != null) {
                                            manager.saveData(Constants.USER_NAME,user.getDisplayName());
                                            manager.saveData(Constants.USER_ID,user.getUid());
                                        }
                                        manager.saveData(Constants.USER_TYPE,mUserType);

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
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