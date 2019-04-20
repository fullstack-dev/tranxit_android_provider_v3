package com.tranxitpro.partner.data.network.model;

import com.google.gson.annotations.SerializedName;

public class RequestedDataItem {

    private int id;

    @SerializedName("alias_id")
    private String aliasId;

    @SerializedName("request_from")
    private String requestFrom;

    @SerializedName("fromId")
    private int from_id;

    @SerializedName("fromDesc")
    private String from_desc;

    private String type;

    private int amount;

    @SerializedName("send_by")
    private String sendBy;

    @SerializedName("send_desc")
    private String sendDesc;

    private int status;

    @SerializedName("created_at")
    private String createdAt;

    public RequestedDataItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAliasId() {
        return aliasId;
    }

    public void setAliasId(String aliasId) {
        this.aliasId = aliasId;
    }

    public String getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(String requestFrom) {
        this.requestFrom = requestFrom;
    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public String getFrom_desc() {
        return from_desc;
    }

    public void setFrom_desc(String from_desc) {
        this.from_desc = from_desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public String getSendDesc() {
        return sendDesc;
    }

    public void setSendDesc(String sendDesc) {
        this.sendDesc = sendDesc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
