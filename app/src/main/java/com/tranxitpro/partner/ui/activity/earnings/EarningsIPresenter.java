package com.tranxitpro.partner.ui.activity.earnings;


import com.tranxitpro.partner.base.MvpPresenter;

public interface EarningsIPresenter<V extends EarningsIView> extends MvpPresenter<V> {

    void getEarnings();
}
