package com.tranxitpro.partner.ui.activity.wallet;

import com.tranxitpro.partner.base.MvpPresenter;

import java.util.HashMap;

public interface WalletIPresenter<V extends WalletIView> extends MvpPresenter<V> {

    void getWalletData();
    void addMoney(HashMap<String, Object> obj);
}
