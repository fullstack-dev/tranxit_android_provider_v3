package com.tranxitpro.partner.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestDataResponse {

    @SerializedName("wallet_balance")
    private double walletBalance;

    @SerializedName("pendinglist")
    private List<RequestedDataItem> pendingList;

    public RequestDataResponse() {
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public List<RequestedDataItem> getPendingList() {
        return pendingList;
    }

    public void setPendingList(List<RequestedDataItem> pendingList) {
        this.pendingList = pendingList;
    }
}
