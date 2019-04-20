package com.tranxitpro.partner.ui.fragment.status_flow;

import com.tranxitpro.partner.base.BasePresenter;
import com.tranxitpro.partner.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StatusFlowPresenter<V extends StatusFlowIView> extends BasePresenter<V> implements StatusFlowIPresenter<V> {

    @Override
    public void statusUpdate(HashMap<String, Object> obj, Integer id) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .updateRequest(obj, id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void waitingTime(String time, String requestId) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .waitingTime(time, requestId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onWaitingTimeSuccess, getMvpView()::onError));
    }

    @Override
    public void checkWaitingTime(String requestId) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .CheckWaitingTime(requestId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onWaitingTimeSuccess, getMvpView()::onError));
    }
}
