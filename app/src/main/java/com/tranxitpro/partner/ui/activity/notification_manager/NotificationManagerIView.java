package com.tranxitpro.partner.ui.activity.notification_manager;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.NotificationManager;

import java.util.List;

public interface NotificationManagerIView extends MvpView {

    void onSuccess(List<NotificationManager> managers);

    void onError(Throwable e);

}