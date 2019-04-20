package com.tranxitpro.partner.ui.activity.wallet_detail;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.Transaction;

import java.util.ArrayList;

public interface WalletDetailIView extends MvpView {
    void setAdapter(ArrayList<Transaction> myList);
}
