package com.tranxitpro.partner.ui.activity.help;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.Help;

public interface HelpIView extends MvpView {

    void onSuccess(Help object);

    void onError(Throwable e);
}
