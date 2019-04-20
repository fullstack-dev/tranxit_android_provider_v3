package com.tranxitpro.partner.ui.activity.instant_ride;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.EstimateFare;
import com.tranxitpro.partner.data.network.model.TripResponse;

public interface InstantRideIView extends MvpView {

    void onSuccess(EstimateFare estimateFare);

    void onSuccess(TripResponse response);

    void onError(Throwable e);

}
