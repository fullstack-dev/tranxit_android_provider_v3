package com.tranxitpro.partner.ui.bottomsheetdialog.invoice_flow;

import com.tranxitpro.partner.base.MvpPresenter;

import java.util.HashMap;

public interface InvoiceDialogIPresenter<V extends InvoiceDialogIView> extends MvpPresenter<V> {

    void statusUpdate(HashMap<String, Object> obj, Integer id);

}
