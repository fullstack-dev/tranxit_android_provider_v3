package com.tranxitpro.partner.base;

import android.app.Activity;

import com.tranxitpro.partner.MvpApplication;

import java.util.HashMap;

public interface MvpPresenter<V extends MvpView> {

    Activity activity();

    MvpApplication appContext();

    void attachView(V mvpView);

    void detachView();

    void refreshToken();

    void logout(HashMap<String, Object> obj);

}
