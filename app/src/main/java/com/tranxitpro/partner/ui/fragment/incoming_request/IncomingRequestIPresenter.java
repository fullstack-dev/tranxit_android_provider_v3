package com.tranxitpro.partner.ui.fragment.incoming_request;

import com.tranxitpro.partner.base.MvpPresenter;

public interface IncomingRequestIPresenter<V extends IncomingRequestIView> extends MvpPresenter<V> {

    void accept(Integer id);
    void cancel(Integer id);
}
