package com.tranxitpro.partner.ui.activity.card;

import com.tranxitpro.partner.base.MvpPresenter;

public interface CardIPresenter<V extends CardIView> extends MvpPresenter<V> {

    void deleteCard(String cardId);

    void card();

    void changeCard(String cardId);
}
