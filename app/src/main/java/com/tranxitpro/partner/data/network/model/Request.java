package com.tranxitpro.partner.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Request {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("request_id")
    @Expose
    private int requestId;
    @SerializedName("provider_id")
    @Expose
    private int providerId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("time_left_to_respond")
    @Expose
    private int timeLeftToRespond;
    @SerializedName("request")
    @Expose
    private Request_ request;
    @SerializedName("payment")
    @Expose
    private Payment payment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTimeLeftToRespond() {
        return timeLeftToRespond;
    }

    public void setTimeLeftToRespond(int timeLeftToRespond) {
        this.timeLeftToRespond = timeLeftToRespond;
    }

    public Request_ getRequest() {
        return request;
    }

    public void setRequest(Request_ request) {
        this.request = request;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
