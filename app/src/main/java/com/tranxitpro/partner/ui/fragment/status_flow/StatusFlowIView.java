package com.tranxitpro.partner.ui.fragment.status_flow;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.TimerResponse;

public interface StatusFlowIView extends MvpView {

    void onSuccess(Object object);

    void onWaitingTimeSuccess(TimerResponse object);

    void onError(Throwable e);
}
