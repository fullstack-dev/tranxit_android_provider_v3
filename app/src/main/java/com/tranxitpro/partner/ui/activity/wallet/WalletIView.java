package com.tranxitpro.partner.ui.activity.wallet;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.WalletMoneyAddedResponse;
import com.tranxitpro.partner.data.network.model.WalletResponse;

public interface WalletIView extends MvpView {

    void onSuccess(WalletResponse response);

    void onSuccess(WalletMoneyAddedResponse response);

    void onError(Throwable e);
}
