package com.tranxitpro.partner.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Device {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("provider_id")
    @Expose
    private int providerId;
    @SerializedName("udid")
    @Expose
    private String udid;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("sns_arn")
    @Expose
    private Object snsArn;
    @SerializedName("type")
    @Expose
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getSnsArn() {
        return snsArn;
    }

    public void setSnsArn(Object snsArn) {
        this.snsArn = snsArn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
