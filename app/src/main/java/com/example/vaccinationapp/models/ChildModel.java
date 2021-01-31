package com.example.vaccinationapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChildModel {

    @SerializedName("httpMethod")
    public String httpMethod;
    @SerializedName("parentEmail")
    public String parentEmail;

    public ChildModel(String httpMethod, String parentEmail) {
        this.httpMethod = httpMethod;
        this.parentEmail = parentEmail;
    }

    @SerializedName("Item")
    @Expose
    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public class Item {

        @SerializedName("parentEmail")
        @Expose
        private String parentEmail;
        @SerializedName("childName")
        @Expose
        private String childName;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("dob")
        @Expose
        private String dob;

        public String getParentEmail() {
            return parentEmail;
        }

        public void setParentEmail(String parentEmail) {
            this.parentEmail = parentEmail;
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

    }
}
