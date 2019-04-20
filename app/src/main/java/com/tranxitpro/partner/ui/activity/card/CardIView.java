package com.tranxitpro.partner.ui.activity.card;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.Card;

import java.util.List;

public interface CardIView extends MvpView {

    void onSuccess(Object card);

    void onSuccess(List<Card> cards);

    void onError(Throwable e);

    void onSuccessChangeCard(Object card);
}
