package com.tranxitpro.partner.ui.activity.help;


import com.tranxitpro.partner.base.MvpPresenter;

public interface HelpIPresenter<V extends HelpIView> extends MvpPresenter<V> {

    void getHelp();
}
