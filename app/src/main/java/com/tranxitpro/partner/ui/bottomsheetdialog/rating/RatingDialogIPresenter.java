package com.tranxitpro.partner.ui.bottomsheetdialog.rating;

import com.tranxitpro.partner.base.MvpPresenter;

import java.util.HashMap;

public interface RatingDialogIPresenter<V extends RatingDialogIView> extends MvpPresenter<V> {

    void rate(HashMap<String, Object> obj, Integer id);
}
