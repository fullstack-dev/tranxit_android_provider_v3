package com.tranxitpro.partner.ui.bottomsheetdialog.rating;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.Rating;

public interface RatingDialogIView extends MvpView {

    void onSuccess(Rating rating);
    void onError(Throwable e);
}
