package com.tranxitpro.partner.ui.activity.setting;

import com.tranxitpro.partner.base.MvpPresenter;

public interface SettingsIPresenter<V extends SettingsIView> extends MvpPresenter<V> {
    void changeLanguage(String languageID);
}
