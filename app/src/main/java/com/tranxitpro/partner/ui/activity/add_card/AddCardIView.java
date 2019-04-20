package com.tranxitpro.partner.ui.activity.add_card;

import com.tranxitpro.partner.base.MvpView;

public interface AddCardIView extends MvpView {

    void onSuccess(Object card);

    void onError(Throwable e);
}
