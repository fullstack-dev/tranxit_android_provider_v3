package com.tranxitpro.partner.ui.activity.card;

import com.tranxitpro.partner.base.BasePresenter;
import com.tranxitpro.partner.data.network.APIClient;
import com.tranxitpro.partner.data.network.model.Card;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class CardPresenter<V extends CardIView> extends BasePresenter<V> implements CardIPresenter<V> {

    @Override
    public void deleteCard(String cardId) {

        getCompositeDisposable().add(APIClient.getAPIClient().deleteCard(cardId, "DELETE")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(deleteResponse -> getMvpView().onSuccess(deleteResponse),
                        throwable -> getMvpView().onError(throwable)));
    }

    @Override
    public void card() {

        getCompositeDisposable().add(APIClient.getAPIClient().card()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Card>>() {
                               @Override
                               public void accept(List<Card> cards) throws Exception {
                                   CardPresenter.this.getMvpView().onSuccess(cards);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                CardPresenter.this.getMvpView().onError(throwable);
                            }
                        }));
    }

    @Override
    public void changeCard(String cardId) {

        getCompositeDisposable().add(APIClient.getAPIClient().changeCard(cardId, "PUT")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(changeCard -> getMvpView().onSuccessChangeCard(changeCard),
                        throwable -> getMvpView().onError(throwable)));
    }

}
