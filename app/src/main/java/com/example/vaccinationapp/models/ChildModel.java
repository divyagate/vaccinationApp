package com.example.vaccinationapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ChildModel {

    @SerializedName("httpMethod")
    public String httpMethod;
    @SerializedName("parentEmail")
    public String parentEmail;
    @SerializedName("userType")
    public String userType;

    public ChildModel(String httpMethod, String parentEmail,String userType) {
        this.httpMethod = httpMethod;
        this.parentEmail = parentEmail;
        this.userType = userType;
    }

    @SerializedName("Items")
    @Expose
    private ArrayList<Item> items = null;

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public class Item implements Serializable {

        @SerializedName("parentEmail")
        @Expose
        private String parentEmail;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("childName")
        @Expose
        private String childName;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("vaccineName")
        @Expose
        private String vaccineName;
        @SerializedName("vid")
        @Expose
        private String vid;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("docStatus")
        @Expose
        private String docStatus;

        public String getParentEmail() {
            return parentEmail;
        }

        public void setParentEmail(String parentEmail) {
            this.parentEmail = parentEmail;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getVaccineName() {
            return vaccineName;
        }

        public void setVaccineName(String vaccineName) {
            this.vaccineName = vaccineName;
        }

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public String getDocStatus() {
            return docStatus;
        }

        public void setDocStatus(String docStatus) {
            this.docStatus = docStatus;
        }
    }
}
