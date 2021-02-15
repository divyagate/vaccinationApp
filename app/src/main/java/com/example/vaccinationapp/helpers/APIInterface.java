package com.example.vaccinationapp.helpers;

import com.google.gson.JsonObject;
import com.example.vaccinationapp.models.ChildModel;
import com.example.vaccinationapp.models.LoginModel;
import com.example.vaccinationapp.models.ScheduleModel;
import com.example.vaccinationapp.models.UserModel;
import com.example.vaccinationapp.models.VaccineListModel;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIInterface {

    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8", "Cache-Control: no-cache"})
    @POST("vaccinesApi/register")
    Call<ResponseBody> registerUser(@Body JsonObject mJson);

    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8", "Cache-Control: no-cache"})
    @POST("vaccinesApi/login")
    Call<LoginModel> loginUser(@Body LoginModel mJson);

    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8", "Cache-Control: no-cache"})
    @POST("vaccinesApi/children/add")
    Call<ResponseBody> addChild(@Body JsonObject mJson);

    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8", "Cache-Control: no-cache"})
    @POST("vaccinesApi/children/get")
    Call<ChildModel> getChild(@Body ChildModel mJson);

    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8", "Cache-Control: no-cache"})
    @POST("vaccinesApi/vaccine/get")
    Call<VaccineListModel> getVaccineList(@Body VaccineListModel mJson);

    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8", "Cache-Control: no-cache"})
    @POST("vaccinesApi/vaccine/schedules")
    Call<ScheduleModel> getScheduleForChild(@Body ScheduleModel mJson);

    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8", "Cache-Control: no-cache"})
    @POST("vaccinesApi/users/get")
    Call<UserModel> getUserList(@Body UserModel mJson);

    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8", "Cache-Control: no-cache"})
    @POST("vaccinesApi/users/remove")
    Call<ResponseBody> deleteMember(@Body JsonObject mJson);

    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8", "Cache-Control: no-cache"})
    @POST("vaccinesApi/users/update")
    Call<ResponseBody> updateProfile(@Body JsonObject mJson);

    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8", "Cache-Control: no-cache"})
    @POST("vaccinesApi/vaccine/given")
    Call<ResponseBody> givenStatus(@Body JsonObject mJson);

    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8", "Cache-Control: no-cache"})
    @POST("vaccinesApi/vaccine/doctorAR")
    Call<ResponseBody> submitRequest(@Body JsonObject mJson);
}
