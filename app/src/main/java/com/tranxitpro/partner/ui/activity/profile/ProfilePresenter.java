package com.tranxitpro.partner.ui.activity.profile;

import com.tranxitpro.partner.base.BasePresenter;
import com.tranxitpro.partner.data.network.APIClient;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfilePresenter<V extends ProfileIView> extends BasePresenter<V> implements ProfileIPresenter<V> {

    @Override
    public void profileUpdate(Map<String, RequestBody> params, MultipartBody.Part file) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .profileUpdate(params, file)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccessUpdate, getMvpView()::onError));
    }

    @Override
    public void getProfile() {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .getProfile()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void verifyCredentials(String countryCode, String mobile) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .verifyCredentials(countryCode, mobile)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccessPhoneNumber, getMvpView()::onVerifyPhoneNumberError));
    }
}
