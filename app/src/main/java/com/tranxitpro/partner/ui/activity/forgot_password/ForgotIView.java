package com.tranxitpro.partner.ui.activity.forgot_password;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.ForgotResponse;

public interface ForgotIView extends MvpView {

    void onSuccess(ForgotResponse forgotResponse);
    void onError(Throwable e);
}
