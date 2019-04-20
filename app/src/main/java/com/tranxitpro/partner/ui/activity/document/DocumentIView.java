package com.tranxitpro.partner.ui.activity.document;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.DriverDocumentResponse;

public interface DocumentIView extends MvpView {

    void onSuccess(DriverDocumentResponse response);

    void onDocumentSuccess(DriverDocumentResponse response);

    void onError(Throwable e);

    void onSuccessLogout(Object object);

}
