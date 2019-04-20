package com.tranxitpro.partner.ui.fragment.dispute;

import com.tranxitpro.partner.base.BasePresenter;
import com.tranxitpro.partner.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DisputePresenter<V extends DisputeIView> extends BasePresenter<V> implements DisputeIPresenter<V> {

    @Override
    public void dispute(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .dispute(obj)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void getDispute() {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .getDispute("provider")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccessDispute, getMvpView()::onError));
    }
}
