package com.example.vaccinationapp.models;
<<<<<<< HEAD

public class UserModel {
}
=======
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class UserModel {

    String httpMethod;
    String userType;

    public UserModel(String httpMethod, String userType) {
        this.httpMethod = httpMethod;
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
>>>>>>> 7d7bd672fac34a19f9653f02099d8aefcacf8acd
