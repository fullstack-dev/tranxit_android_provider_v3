package com.tranxitpro.partner.ui.activity.notification_manager;

import com.tranxitpro.partner.base.MvpPresenter;

public interface NotificationManagerIPresenter<V extends NotificationManagerIView> extends MvpPresenter<V> {
    void getNotificationManager();
}
