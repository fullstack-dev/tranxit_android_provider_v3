package com.tranxitpro.partner.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class EarningsList {

    @SerializedName("rides")
    @Expose
    private List<Ride> rides = null;
    @SerializedName("rides_count")
    @Expose
    private int ridesCount;
    @SerializedName("target")
    @Expose
    private String target;

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }

    public int getRidesCount() {
        return ridesCount;
    }

    public void setRidesCount(int ridesCount) {
        this.ridesCount = ridesCount;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
