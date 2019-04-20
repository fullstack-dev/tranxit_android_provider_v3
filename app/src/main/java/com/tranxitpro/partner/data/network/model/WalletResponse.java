
package com.tranxitpro.partner.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WalletResponse implements Serializable {

    @SerializedName("wallet_transation")
    @Expose
    private List<WalletTransation> walletTransation = null;
    @SerializedName("wallet_balance")
    @Expose
    private double walletBalance;

    public List<WalletTransation> getWalletTransation() {
        return walletTransation;
    }

    public void setWalletTransation(List<WalletTransation> walletTransation) {
        this.walletTransation = walletTransation;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

}
