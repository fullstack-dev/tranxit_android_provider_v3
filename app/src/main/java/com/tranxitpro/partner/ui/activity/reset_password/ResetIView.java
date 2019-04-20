package com.tranxitpro.partner.ui.activity.reset_password;

import com.tranxitpro.partner.base.MvpView;

public interface ResetIView extends MvpView{

    void onSuccess(Object object);
    void onError(Throwable e);
}
