package com.tranxitpro.partner.base;

import android.app.Activity;

import com.tranxitpro.partner.data.network.model.User;

public interface MvpView {
    Activity activity();

    void showLoading();

    void hideLoading();

    void onErrorRefreshToken(Throwable throwable);

    void onSuccessRefreshToken(User user);

    void onSuccessLogout(Object object);

    void onError(Throwable throwable);
}
