package com.example.vaccinationapp.fragments;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vaccinationapp.CovidDetailsActivity;
import com.example.vaccinationapp.R;

public class CovidFragment extends Fragment {

    View mView;
    TextView mTextVaccine1, mTextVaccine2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_covid, container, false);

        initView();
        return mView;
    }

    private void initView() {
        mTextVaccine1 = mView.findViewById(R.id.vacc_1);
        mTextVaccine2 = mView.findViewById(R.id.vacc_2);

        mTextVaccine1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CovidDetailsActivity.class);
            intent.putExtra("vaccine_name", "vacc_1");
            startActivity(intent);
        });

        mTextVaccine2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CovidDetailsActivity.class);
            intent.putExtra("vaccine_name", "vacc_2");
            startActivity(intent);
        });
    }
}