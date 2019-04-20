package com.tranxitpro.partner.ui.activity.summary;


import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.Summary;

public interface SummaryIView extends MvpView {

    void onSuccess(Summary object);

    void onError(Throwable e);
}
