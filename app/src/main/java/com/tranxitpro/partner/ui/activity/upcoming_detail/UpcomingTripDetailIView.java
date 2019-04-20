package com.tranxitpro.partner.ui.activity.upcoming_detail;


import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.HistoryDetail;

public interface UpcomingTripDetailIView extends MvpView {

    void onSuccess(HistoryDetail historyDetail);
    void onError(Throwable e);
}
