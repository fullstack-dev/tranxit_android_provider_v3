package com.tranxitpro.partner.ui.activity.wallet_detail;

import com.tranxitpro.partner.base.MvpPresenter;
import com.tranxitpro.partner.data.network.model.Transaction;

import java.util.ArrayList;

public interface WalletDetailIPresenter<V extends WalletDetailIView> extends MvpPresenter<V> {
    void setAdapter(ArrayList<Transaction> myList);
}
