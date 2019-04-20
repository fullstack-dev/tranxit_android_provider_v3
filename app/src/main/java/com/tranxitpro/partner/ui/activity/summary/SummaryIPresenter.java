package com.tranxitpro.partner.ui.activity.summary;


import com.tranxitpro.partner.base.MvpPresenter;

public interface SummaryIPresenter<V extends SummaryIView> extends MvpPresenter<V> {

    void getSummary();
}
