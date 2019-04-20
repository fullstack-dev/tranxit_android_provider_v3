package com.tranxitpro.partner.ui.activity.past_detail;


import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.HistoryDetail;

public interface PastTripDetailIView extends MvpView {

    void onSuccess(HistoryDetail historyDetail);
    void onError(Throwable e);
}
