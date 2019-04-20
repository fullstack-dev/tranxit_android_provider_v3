package com.tranxitpro.partner.ui.activity.payment;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.Card;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface PaymentIView extends MvpView {
    void onSuccess(Object card);

    void onSuccess(List<Card> cards);

    void onError(Throwable e);
}
