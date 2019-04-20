package com.tranxitpro.partner.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by suthakar@appoets.com on 29-06-2018.
 */
public class Summary {

    @SerializedName("rides")
    @Expose
    private int rides;
    @SerializedName("revenue")
    @Expose
    private Float  revenue;
    @SerializedName("cancel_rides")
    @Expose
    private int cancelRides;
    @SerializedName("scheduled_rides")
    @Expose
    private int scheduledRides;

    public int getRides() {
        return rides;
    }

    public void setRides(int rides) {
        this.rides = rides;
    }

    public Float  getRevenue() {
        return revenue;
    }

    public void setRevenue(Float  revenue) {
        this.revenue = revenue;
    }

    public int getCancelRides() {
        return cancelRides;
    }

    public void setCancelRides(int cancelRides) {
        this.cancelRides = cancelRides;
    }

    public int getScheduledRides() {
        return scheduledRides;
    }

    public void setScheduledRides(int scheduledRides) {
        this.scheduledRides = scheduledRides;
    }
}
