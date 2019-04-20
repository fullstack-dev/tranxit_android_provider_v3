package com.tranxitpro.partner.ui.activity.password;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.ForgotResponse;
import com.tranxitpro.partner.data.network.model.User;

public interface PasswordIView extends MvpView {

    void onSuccess(ForgotResponse forgotResponse);

    void onSuccess(User object);

    void onError(Throwable e);
}
