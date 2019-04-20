package com.tranxitpro.partner.ui.activity.sociallogin;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.Token;

public interface SocialLoginIView extends MvpView {

    void onSuccess(Token token);
    void onError(Throwable e);
}
