package com.tranxitpro.partner.ui.activity.invite_friend;

import com.tranxitpro.partner.base.BasePresenter;
import com.tranxitpro.partner.data.network.APIClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InviteFriendPresenter<V extends InviteFriendIView> extends BasePresenter<V> implements InviteFriendIPresenter<V> {
    @Override
    public void profile() {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .getProfile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }
}
