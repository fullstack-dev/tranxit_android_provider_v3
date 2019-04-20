package com.tranxitpro.partner.ui.activity.request_money;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.RequestDataResponse;

public interface RequestMoneyIView extends MvpView {

    void onSuccess(RequestDataResponse response);
    void onSuccess(Object response);
    void onError(Throwable e);

}
