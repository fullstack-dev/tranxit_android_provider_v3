package com.tranxitpro.partner.ui.activity.forgot_password;

import com.tranxitpro.partner.base.MvpPresenter;

import java.util.HashMap;

public interface ForgotIPresenter<V extends ForgotIView> extends MvpPresenter<V> {

    void forgot(HashMap<String, Object> obj);

}
