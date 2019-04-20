package com.tranxitpro.partner.base;

import android.app.Activity;

import com.tranxitpro.partner.MvpApplication;
import com.tranxitpro.partner.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private final CompositeDisposable mCompositeDisposable;
    private V mMvpView;

    public BasePresenter() {
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public Activity activity() {
        return getMvpView().activity();
    }

    @Override
    public MvpApplication appContext() {
        return MvpApplication.getInstance();
    }

    @Override
    public void attachView(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mCompositeDisposable.dispose();
        mMvpView = null;
    }

    @Override
    public void refreshToken() {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .refreshToken()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccessRefreshToken,
                        getMvpView()::onErrorRefreshToken));
    }

    @Override
    public void logout(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .logout(obj)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccessLogout, getMvpView()::onError));
    }


    public V getMvpView() {
        return mMvpView;
    }


    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }


}
