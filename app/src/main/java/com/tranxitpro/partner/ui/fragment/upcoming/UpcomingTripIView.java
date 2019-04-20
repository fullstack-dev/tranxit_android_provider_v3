package com.tranxitpro.partner.ui.fragment.upcoming;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.HistoryList;

import java.util.List;

public interface UpcomingTripIView extends MvpView {

    void onSuccess(List<HistoryList> historyList);
    void onError(Throwable e);
}
