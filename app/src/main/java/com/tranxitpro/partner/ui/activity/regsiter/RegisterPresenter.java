package com.tranxitpro.partner.ui.activity.regsiter;

import com.tranxitpro.partner.base.BasePresenter;
import com.tranxitpro.partner.data.network.APIClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public class RegisterPresenter<V extends RegisterIView> extends BasePresenter<V> implements RegisterIPresenter<V> {

    @Override
    public void register(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> files) {
        getCompositeDisposable().add(
                APIClient
                        .getAPIClient()
                        .register(params, files)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void verifyEmail(String email) {
        getCompositeDisposable().add(
                APIClient
                        .getAPIClient()
                        .verifyEmail(email)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getMvpView()::onSuccess, getMvpView()::onVerifyEmailError));
    }


    @Override
    public void getSettings() {
        getCompositeDisposable().add(APIClient
                        .getAPIClient()
                        .getSettings()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void verifyCredentials(String countryCode, String phoneNumber) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .verifyCredentials(countryCode,phoneNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccessPhoneNumber, getMvpView()::onVerifyPhoneNumberError));
    }
}
