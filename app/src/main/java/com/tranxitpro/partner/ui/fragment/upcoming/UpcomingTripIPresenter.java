package com.tranxitpro.partner.ui.fragment.upcoming;


import com.tranxitpro.partner.base.MvpPresenter;

public interface UpcomingTripIPresenter<V extends UpcomingTripIView> extends MvpPresenter<V> {

    void getUpcoming();

}
