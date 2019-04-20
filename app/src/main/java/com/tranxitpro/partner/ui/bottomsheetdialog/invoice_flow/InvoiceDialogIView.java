package com.tranxitpro.partner.ui.bottomsheetdialog.invoice_flow;

import com.tranxitpro.partner.base.MvpView;

public interface InvoiceDialogIView extends MvpView {

    void onSuccess(Object object);
    void onError(Throwable e);
}
