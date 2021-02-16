package com.example.vaccinationapp.clicklisteners;

import com.example.vaccinationapp.models.VaccineListModel;

public interface VaccinationListClickListener {
    void onItemClick(int position, VaccineListModel.Item model);
}