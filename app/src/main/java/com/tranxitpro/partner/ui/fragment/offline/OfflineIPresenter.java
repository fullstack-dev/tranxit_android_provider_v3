package com.tranxitpro.partner.ui.fragment.offline;

import com.tranxitpro.partner.base.MvpPresenter;

import java.util.HashMap;

public interface OfflineIPresenter<V extends OfflineIView> extends MvpPresenter<V> {

    void providerAvailable(HashMap<String, Object> obj);
}
