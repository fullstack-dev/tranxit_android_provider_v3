package com.tranxitpro.partner.ui.fragment.past;


import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.HistoryList;

import java.util.List;

public interface PastTripIView extends MvpView {

    void onSuccess(List<HistoryList> historyList);
    void onError(Throwable e);
}
