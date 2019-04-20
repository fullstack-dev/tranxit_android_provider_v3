package com.tranxitpro.partner.ui.fragment.dispute;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.DisputeResponse;

import java.util.List;

public interface DisputeIView extends MvpView {

    void onSuccessDispute(List<DisputeResponse> responseList);

    void onSuccess(Object object);

    void onError(Throwable e);
}
