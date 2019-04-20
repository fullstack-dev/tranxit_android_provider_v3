package com.tranxitpro.partner.ui.fragment.offline;

import com.tranxitpro.partner.base.MvpView;

public interface OfflineIView extends MvpView {

    void onSuccess(Object object);
    void onError(Throwable e);
}
