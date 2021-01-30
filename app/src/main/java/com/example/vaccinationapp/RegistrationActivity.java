package com.example.vaccinationapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = RegistrationActivity.class.getName();
    EditText mEditEmail, mEditPassword, mEditFirstName, mEditCPassword, mEditLastName;
    Button mButtonSignUp;
    TextView mTextLogin;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    RadioGroup mRadioGroup;
    RadioButton mRadioParent,mRadioDoc;
    String mUserType;
    Spinner mSpinnerCity;

    // create array of Strings
    // and store name of cities
    String[] cities = { "Acton Vale", "Alma",
            "Montreal", "Mount royal",
            "Murdochville", "Nicolet", "Perforated", "Pincourt", "Lorraine", "Louiseville", "Macamic", "Malartic", "Windsor", "Warwick"
            , "Val-d'Or", "Thurso", "Terrebonne", "Sutton", "Sept-Iles", "Schefferville", "Saint-Ours", "St. Gabriel", "Rosemere", "Richmond"
            , "Prevost", "Paspebiac", "Lac-Sergent", "Hampstead", "Hudson", "Joliette", "Thaw", "Coaticook"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        initViews();
        setUpClickListeners();
    }

    private void setUpClickListeners() {
        mTextLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @SuppressLint("NonConstantResourceId")
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
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

        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDataValid()) {
                    progressDialog.show();
                    String email = mEditEmail.getText().toString();
                    String password = mEditPassword.getText().toString().trim();
                    final String name = mEditFirstName.getText().toString().trim() + " " + mEditLastName.getText().toString().trim();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.e(TAG, "createUserWithEmail:success");

                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {

                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(name)
                                                    //as of now dummy user picture
//                                                    .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d(TAG, "User profile updated.");
                                                            }
                                                        }
                                                    });
                                        }

                                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.e(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegistrationActivity.this, "Sign-up failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });

        // Create the instance of ArrayAdapter
        // having the list of cities
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                cities);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        mSpinnerCity.setAdapter(ad);
    }

    private boolean isDataValid() {
        boolean status = true;

        String email = mEditEmail.getText().toString();
        if (mEditPassword.getText().toString().equalsIgnoreCase("") || mEditPassword.getText().toString().trim().length() < 8) {
            mEditPassword.setError("Enter Valid Password");
            status = false;
        }
        if (mEditFirstName.getText().toString().trim().equalsIgnoreCase("")) {
            mEditFirstName.setError("Enter Valid Name");
            status = false;
        }
        if (mEditLastName.getText().toString().trim().equalsIgnoreCase("")) {
            mEditLastName.setError("Enter Valid Name");
            status = false;
        }
        if (!mEditCPassword.getText().toString().trim().equalsIgnoreCase(mEditPassword.getText().toString().trim())) {
            mEditCPassword.setError("Password Not Match");
            status = false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEditEmail.setError("Enter Valid Email");
            status = false;
        }

        return status;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    private void initViews() {

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mEditEmail = findViewById(R.id.edt_email);
        mEditPassword = findViewById(R.id.edt_password);
        mEditFirstName = findViewById(R.id.edt_firstname);
        mEditCPassword = findViewById(R.id.edt_confirmpassword);
        mEditLastName = findViewById(R.id.edt_lastname);

        mSpinnerCity = findViewById(R.id.city_spinner);
        mSpinnerCity.setOnItemSelectedListener(this);

        mButtonSignUp = findViewById(R.id.btn_signup);

        mTextLogin = findViewById(R.id.login);

        mRadioGroup = findViewById(R.id.rg_rb);
        mRadioParent = findViewById(R.id.rb_parent);
        mRadioDoc = findViewById(R.id.rb_doc);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // make toastof name of course
        // which is selected in spinner
        /*Toast.makeText(getApplicationContext(),
                cities[position],
                Toast.LENGTH_LONG)
                .show();*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Auto-generated method stub
    }
}