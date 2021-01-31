package com.example.vaccinationapp.models;

import android.content.ClipData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class LoginModel {

    @SerializedName("httpMethod")
    public String httpMethod;
    @SerializedName("email")
    public String email;
    @SerializedName("password")
    public String password;
    @SerializedName("type")
    public String type;

    @SerializedName("Item")
    @Expose
    private Item item;
    @SerializedName("auth")
    @Expose
    private Boolean auth;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Boolean getAuth() {
        return auth;
    }

    public void setAuth(Boolean auth) {
        this.auth = auth;
    }

    public LoginModel(String httpMethod, String email, String password, String type) {
        this.httpMethod = httpMethod;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public class Item {

        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("lastname")
        @Expose
        private String lastname;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("firstname")
        @Expose
        private String firstname;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("type")
        @Expose
        private String type;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }
}
