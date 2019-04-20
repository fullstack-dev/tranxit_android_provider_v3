package com.tranxitpro.partner.ui.bottomsheetdialog.cancel;

import com.tranxitpro.partner.base.MvpPresenter;

import java.util.HashMap;

public interface CancelDialogIPresenter<V extends CancelDialogIView> extends MvpPresenter<V> {

    void cancelRequest(HashMap<String, Object> obj);
    void getReasons();
}
