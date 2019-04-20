
package com.tranxitpro.partner.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WalletTransation implements Serializable {

    @SerializedName("transaction_alias")
    @Expose
    private String transactionAlias;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("transactions")
    @Expose
    private List<Transaction> transactions = null;

    public String getTransactionAlias() {
        return transactionAlias;
    }

    public void setTransactionAlias(String transactionAlias) {
        this.transactionAlias = transactionAlias;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

}
