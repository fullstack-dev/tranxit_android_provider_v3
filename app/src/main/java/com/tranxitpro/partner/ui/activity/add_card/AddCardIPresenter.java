package com.tranxitpro.partner.ui.activity.add_card;

import com.tranxitpro.partner.base.MvpPresenter;

public interface AddCardIPresenter<V extends AddCardIView> extends MvpPresenter<V> {

    void addCard(String stripeToken);
}
