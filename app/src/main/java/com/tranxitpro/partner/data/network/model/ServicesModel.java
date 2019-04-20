package com.tranxitpro.partner.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServicesModel {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("service_id")
    @Expose
    private String serviceId;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("fixed")
    @Expose
    private int fixed;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("capacity")
    @Expose
    private int capacity;
    @SerializedName("minute")
    @Expose
    private int minute;
    @SerializedName("distance")
    @Expose
    private int distance;
    @SerializedName("hour")
    @Expose
    private Object hour;
    @SerializedName("calculator")
    @Expose
    private String calculator;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("provider_name")
    @Expose
    private String providerName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public int getFixed() {
        return fixed;
    }

    public void setFixed(int fixed) {
        this.fixed = fixed;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Object getHour() {
        return hour;
    }

    public void setHour(Object hour) {
        this.hour = hour;
    }

    public String getCalculator() {
        return calculator;
    }

    public void setCalculator(String calculator) {
        this.calculator = calculator;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public String toString() {
        return name;
    }
}
