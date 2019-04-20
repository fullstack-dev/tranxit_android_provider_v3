package com.tranxitpro.partner.ui.activity.invite_friend;

import com.tranxitpro.partner.base.MvpView;
import com.tranxitpro.partner.data.network.model.UserResponse;

public interface InviteFriendIView extends MvpView {

    void onSuccess(UserResponse response);
    void onError(Throwable e);

}
