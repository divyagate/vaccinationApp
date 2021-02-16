package com.example.vaccinationapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class VaccineListModel {

    String httpMethod;

    public VaccineListModel(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    @SerializedName("Items")
    @Expose
    private ArrayList<Item> items = null;
    @SerializedName("Count")
    @Expose
    private Integer count;
    @SerializedName("ScannedCount")
    @Expose
    private Integer scannedCount;

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getScannedCount() {
        return scannedCount;
    }

    public void setScannedCount(Integer scannedCount) {
        this.scannedCount = scannedCount;
    }

    public class Item implements Serializable {

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

    }
}
