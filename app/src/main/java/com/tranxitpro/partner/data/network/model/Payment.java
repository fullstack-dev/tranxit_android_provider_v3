package com.tranxitpro.partner.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Payment implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("request_id")
    @Expose
    private int requestId;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("provider_id")
    @Expose
    private int providerId;
    @SerializedName("fleet_id")
    @Expose
    private int fleetId;
    @SerializedName("promocode_id")
    @Expose
    private String promocodeId;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("fixed")
    @Expose
    private double fixed;
    @SerializedName("distance")
    @Expose
    private double distance;
    @SerializedName("minute")
    @Expose
    private double minute;
    @SerializedName("hour")
    @Expose
    private double hour;
    @SerializedName("commision")
    @Expose
    private double commision;
    @SerializedName("commision_per")
    @Expose
    private double commisionPer;
    @SerializedName("fleet")
    @Expose
    private double fleet;
    @SerializedName("fleet_per")
    @Expose
    private double fleetPer;
    @SerializedName("discount")
    @Expose
    private double discount;
    @SerializedName("discount_per")
    @Expose
    private double discountPer;
    @SerializedName("tax")
    @Expose
    private double tax;
    @SerializedName("tax_per")
    @Expose
    private double taxPer;
    @SerializedName("wallet")
    @Expose
    private double wallet;
    @SerializedName("is_partial")
    @Expose
    private double isPartial;
    @SerializedName("cash")
    @Expose
    private double cash;
    @SerializedName("card")
    @Expose
    private double card;
    @SerializedName("surge")
    @Expose
    private double surge;
    @SerializedName("tips")
    @Expose
    private double tips;
    @SerializedName("total")
    @Expose
    private double total;
    @SerializedName("payable")
    @Expose
    private double payable;
    @SerializedName("provider_commission")
    @Expose
    private double providerCommission;
    @SerializedName("provider_pay")
    @Expose
    private double providerPay;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public int getFleetId() {
        return fleetId;
    }

    public void setFleetId(int fleetId) {
        this.fleetId = fleetId;
    }

    public String getPromocodeId() {
        return promocodeId;
    }

    public void setPromocodeId(String promocodeId) {
        this.promocodeId = promocodeId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public double getFixed() {
        return fixed;
    }

    public void setFixed(double fixed) {
        this.fixed = fixed;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getMinute() {
        return minute;
    }

    public void setMinute(double minute) {
        this.minute = minute;
    }

    public double getHour() {
        return hour;
    }

    public void setHour(double hour) {
        this.hour = hour;
    }

    public double getCommision() {
        return commision;
    }

    public void setCommision(double commision) {
        this.commision = commision;
    }

    public double getCommisionPer() {
        return commisionPer;
    }

    public void setCommisionPer(double commisionPer) {
        this.commisionPer = commisionPer;
    }

    public double getFleet() {
        return fleet;
    }

    public void setFleet(double fleet) {
        this.fleet = fleet;
    }

    public double getFleetPer() {
        return fleetPer;
    }

    public void setFleetPer(double fleetPer) {
        this.fleetPer = fleetPer;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(double discountPer) {
        this.discountPer = discountPer;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTaxPer() {
        return taxPer;
    }

    public void setTaxPer(double taxPer) {
        this.taxPer = taxPer;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public double getIsPartial() {
        return isPartial;
    }

    public void setIsPartial(double isPartial) {
        this.isPartial = isPartial;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getCard() {
        return card;
    }

    public void setCard(double card) {
        this.card = card;
    }

    public double getSurge() {
        return surge;
    }

    public void setSurge(double surge) {
        this.surge = surge;
    }

    public double getTips() {
        return tips;
    }

    public void setTips(double tips) {
        this.tips = tips;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPayable() {
        return payable;
    }

    public void setPayable(double payable) {
        this.payable = payable;
    }

    public double getProviderCommission() {
        return providerCommission;
    }

    public void setProviderCommission(double providerCommission) {
        this.providerCommission = providerCommission;
    }

    public double getProviderPay() {
        return providerPay;
    }

    public void setProviderPay(double providerPay) {
        this.providerPay = providerPay;
    }
}
