package com.tranxitpro.partner.ui.activity.change_password;

import com.tranxitpro.partner.base.MvpPresenter;

import java.util.HashMap;

public interface ChangePasswordIPresenter<V extends ChangePasswordIView> extends MvpPresenter<V> {

    void changePassword(HashMap<String, Object> obj);
}
