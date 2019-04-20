package com.tranxitpro.partner.ui.activity.past_detail;


import com.tranxitpro.partner.base.MvpPresenter;

public interface PastTripDetailIPresenter<V extends PastTripDetailIView> extends MvpPresenter<V> {

    void getPastTripDetail(String request_id);
}
