package com.example.vaccinationapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.example.vaccinationapp.LoginActivity;
import com.example.vaccinationapp.MainActivity;
import com.example.vaccinationapp.R;
import com.example.vaccinationapp.RegistrationActivity;
import com.example.vaccinationapp.helpers.APIClient;
import com.example.vaccinationapp.helpers.APIInterface;
import com.example.vaccinationapp.helpers.Constants;
import com.example.vaccinationapp.helpers.PrefManager;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private final String TAG = ProfileFragment.class.getName();
    EditText mEditPassword, mEditFirstName, mEditCPassword, mEditLastName, mEditEmail;
    Button mButtonUpdate;
    ProgressDialog progressDialog;
    Spinner mSpinnerCity;
    View mView;
    PrefManager manager;
    // create array of Strings
    // and store name of cities
    String[] cities = {"Acton Vale", "Alma",
            "Montreal", "Mount royal",
            "Murdochville", "Nicolet", "Perforated", "Pincourt", "Lorraine", "Louiseville", "Macamic", "Malartic", "Windsor", "Warwick"
            , "Val-d'Or", "Thurso", "Terrebonne", "Sutton", "Sept-Iles", "Schefferville", "Saint-Ours", "St. Gabriel", "Rosemere", "Richmond"
            , "Prevost", "Paspebiac", "Lac-Sergent", "Hampstead", "Hudson", "Joliette", "Thaw", "Coaticook"};
    APIInterface apiInterface;
    String mSelectedCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        manager = new PrefManager(getActivity());

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");

        apiInterface = APIClient.getClient().create(APIInterface.class);

        initViews();
        setUpClickListeners();
        return mView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    private void setUpClickListeners() {
        mButtonUpdate.setOnClickListener(view -> {
            if (isDataValid()) {
                updateProfile();
            }
        });

        // Create the instance of ArrayAdapter
        // having the list of cities
        ArrayAdapter ad
                = new ArrayAdapter(
                getActivity(),
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

        ArrayList<String> stringList = new ArrayList<String>(Arrays.asList(cities));
        for (int i = 0; i < stringList.size(); i++) {
            if (manager.getData(Constants.CITY).equals(stringList.get(i))) {
                mSpinnerCity.setSelection(i);
            }
        }
    }

    private void updateProfile() {
        progressDialog.show();
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
            jsonObject.addProperty("type", manager.getData(Constants.USER_TYPE));
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
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        manager.saveData(Constants.USER_NAME, mEditFirstName.getText().toString().trim() + " " + mEditLastName.getText().toString().trim());
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
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
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isDataValid() {
        boolean status = true;

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

        return status;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initViews() {
        mEditEmail = mView.findViewById(R.id.edt_email);
        mEditPassword = mView.findViewById(R.id.edt_password);
        mEditFirstName = mView.findViewById(R.id.edt_firstname);
        mEditCPassword = mView.findViewById(R.id.edt_confirmpassword);
        mEditLastName = mView.findViewById(R.id.edt_lastname);

        mSpinnerCity = mView.findViewById(R.id.city_spinner);
        mSpinnerCity.setOnItemSelectedListener(this);

        mButtonUpdate = mView.findViewById(R.id.btn_signup);

        try {
            String name;
            String mailId;
            String pass;

            // Name, email address, and profile photo Url
            name = manager.getData(Constants.USER_NAME);
            mailId = manager.getData(Constants.EMAIL_ID);
            pass = manager.getData(Constants.PASSWORD);

            String[] separated = new String[0];
            if (name != null) {
                separated = name.split(" ");
            }
            String fName = separated[0];
            String lName = separated[1];

            if (!name.equalsIgnoreCase("")) {
                mEditFirstName.setText(fName);
            }
            if (!name.equalsIgnoreCase("")) {
                mEditLastName.setText(lName);
            }
            if (!mailId.equalsIgnoreCase("")) {
                mEditEmail.setText(mailId);
            }
            if (!pass.equals("")) {
                mEditPassword.setText(pass);
                mEditCPassword.setText(pass);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // make toast of name of city
        // which is selected in spinner
        mSelectedCity = cities[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Auto-generated method stub
    }
}