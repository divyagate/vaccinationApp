package com.example.vaccinationapp.clicklisteners;

import com.example.vaccinationapp.models.ScheduleModel;

public interface ChildClickListener {
    void onItemClick(ScheduleModel.Item model, String dob);
}