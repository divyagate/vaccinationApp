package com.example.vaccinationapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ScheduleModel {

    String httpMethod;
    String id;

    public ScheduleModel(String httpMethod, String id) {
        this.httpMethod = httpMethod;
        this.id = id;
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

    public class Item implements Serializable{

        @SerializedName("details")
        @Expose
        private String details;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("dueDays")
        @Expose
        private String dueDays;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("dueDate")
        @Expose
        private String dueDate;
        @SerializedName("dueMonths")
        @Expose
        private Integer dueMonths;
        @SerializedName("status")
        @Expose
        private String status;

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDueDays() {
            return dueDays;
        }

        public void setDueDays(String dueDays) {
            this.dueDays = dueDays;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }

        public Integer getDueMonths() {
            return dueMonths;
        }

        public void setDueMonths(Integer dueMonths) {
            this.dueMonths = dueMonths;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }
}
