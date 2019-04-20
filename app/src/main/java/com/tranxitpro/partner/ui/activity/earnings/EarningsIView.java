package com.tranxitpro.partner.ui.activity.earnings;


import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.EarningsList;

public interface EarningsIView extends MvpView {

    void onSuccess(EarningsList earningsLists);

    void onError(Throwable e);
}
