package com.tranxitpro.partner.ui.activity.forgot_password;

import com.tranxitpro.partner.base.BasePresenter;
import com.tranxitpro.partner.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ForgotPresenter<V extends ForgotIView> extends BasePresenter<V> implements ForgotIPresenter<V> {

   @Override
    public void forgot(HashMap<String, Object> obj) {
       getCompositeDisposable().add(
               APIClient
                       .getAPIClient()
                       .forgotPassword(obj)
                       .subscribeOn(Schedulers.computation())
                       .observeOn(AndroidSchedulers.mainThread())
                       .subscribe(
                               trendsResponse -> getMvpView().onSuccess(trendsResponse),
                               throwable -> getMvpView().onError(throwable)
                       )
       );
   }
}
