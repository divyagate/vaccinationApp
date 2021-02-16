package com.example.vaccinationapp.clicklisteners;

import com.example.vaccinationapp.models.ScheduleModel;

public interface GivenStatusListener {
    void onItemClick(int position, ScheduleModel.Item model, String httpMethod);
}
