package com.example.vaccinationapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.example.vaccinationapp.helpers.APIClient;
import com.example.vaccinationapp.helpers.APIInterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = RegistrationActivity.class.getName();
    EditText mEditEmail, mEditPassword, mEditFirstName, mEditCPassword, mEditLastName;
    Button mButtonSignUp;
    TextView mTextLogin;
    ProgressDialog progressDialog;
    RadioGroup mRadioGroup;
    RadioButton mRadioParent, mRadioDoc;
    String mUserType = "Parent";
    Spinner mSpinnerCity;
    APIInterface apiInterface;
    String mSelectedCity;
    // create array of Strings
    // and store name of cities
    String[] cities = {"Acton Vale", "Alma",
            "Montreal", "Mount royal",
            "Murdochville", "Nicolet", "Perforated", "Pincourt", "Lorraine", "Louiseville", "Macamic", "Malartic", "Windsor", "Warwick"
            , "Val-d'Or", "Thurso", "Terrebonne", "Sutton", "Sept-Iles", "Schefferville", "Saint-Ours", "St. Gabriel", "Rosemere", "Richmond"
            , "Prevost", "Paspebiac", "Lac-Sergent", "Hampstead", "Hudson", "Joliette", "Thaw", "Coaticook"};
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        initViews();
        setUpClickListeners();
    }

    private void setUpClickListeners() {
        mTextLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
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

        mButtonSignUp.setOnClickListener(view -> {
            if (isDataValid()) {
                progressDialog.show();
                registerUser();
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
                android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        mSpinnerCity.setAdapter(ad);
    }

    private void registerUser() {

        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("httpMethod", "POST");
            jsonObject.addProperty("firstname", mEditFirstName.getText().toString().trim());
            jsonObject.addProperty("lastname", mEditLastName.getText().toString().trim());
            jsonObject.addProperty("email", mEditEmail.getText().toString().trim());
            jsonObject.addProperty("password", mEditPassword.getText().toString().trim());
            jsonObject.addProperty("country", "Canada");
            jsonObject.addProperty("state", "Quebec");
            jsonObject.addProperty("city", mSelectedCity);
            jsonObject.addProperty("type", mUserType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<ResponseBody> call = apiInterface.registerUser(jsonObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.hide();
                try {
                    if (response.code() == 200) {
                        Toast.makeText(RegistrationActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.hide();
                call.cancel();
                Toast.makeText(RegistrationActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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

    private void initViews() {

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
        // make toast of name of cities
        // which is selected in spinner
        mSelectedCity = cities[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Auto-generated method stub
    }
}