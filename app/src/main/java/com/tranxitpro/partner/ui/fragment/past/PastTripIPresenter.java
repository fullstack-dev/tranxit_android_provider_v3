package com.tranxitpro.partner.ui.fragment.past;


import com.tranxitpro.partner.base.MvpPresenter;

public interface PastTripIPresenter<V extends PastTripIView> extends MvpPresenter<V> {

    void getHistory();

}
