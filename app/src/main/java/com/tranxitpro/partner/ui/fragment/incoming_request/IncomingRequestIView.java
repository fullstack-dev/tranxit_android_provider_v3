package com.tranxitpro.partner.ui.fragment.incoming_request;

import com.tranxitpro.partner.base.MvpView;

public interface IncomingRequestIView extends MvpView {

    void onSuccessAccept(Object responseBody);
    void onSuccessCancel(Object object);
    void onError(Throwable e);
}
