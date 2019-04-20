package com.tranxitpro.partner.ui.activity.invite_friend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseActivity;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.common.SharedHelper;
import com.tranxitpro.partner.data.network.model.UserResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteFriendActivity extends BaseActivity implements InviteFriendIView {

    private InviteFriendPresenter<InviteFriendActivity> inviteFriendPresenter = new InviteFriendPresenter<>();

    @BindView(R.id.invite_friend)
    TextView invite_friend;
    @BindView(R.id.referral_code)
    TextView referral_code;
    @BindView(R.id.referral_amount)
    TextView referral_amount;

    @Override
    public int getLayoutId() {
        return R.layout.activity_invite_friend;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        inviteFriendPresenter.attachView(this);

        if (!SharedHelper.getKey(this, Constants.ReferalKey.REFERRAL_CODE).equalsIgnoreCase(""))
            updateUI();
        else inviteFriendPresenter.profile();
    }

    private void updateUI() {
        referral_code.setText(SharedHelper.getKey(this, Constants.ReferalKey.REFERRAL_CODE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            invite_friend.setText(Html.fromHtml(SharedHelper.getKey(this, Constants.ReferalKey.REFERRAL_TEXT), Html.FROM_HTML_MODE_COMPACT));
            referral_amount.setText(Html.fromHtml(SharedHelper.getKey(this, Constants.ReferalKey.REFERRAL_TOTAL_TEXT), Html.FROM_HTML_MODE_COMPACT));
        } else {
            invite_friend.setText(Html.fromHtml(SharedHelper.getKey(this, Constants.ReferalKey.REFERRAL_TEXT)));
            referral_amount.setText(Html.fromHtml(SharedHelper.getKey(this, Constants.ReferalKey.REFERRAL_TOTAL_TEXT)));
        }
    }

    @Override
    public void onSuccess(UserResponse response) {
        SharedHelper.putKey(this, Constants.ReferalKey.REFERRAL_CODE, response.getReferral_unique_id());
        SharedHelper.putKey(this, Constants.ReferalKey.REFERRAL_COUNT, response.getReferral_count());
        SharedHelper.putKey(this, Constants.ReferalKey.REFERRAL_TEXT, response.getReferral_text());
        SharedHelper.putKey(this, Constants.ReferalKey.REFERRAL_TOTAL_TEXT, response.getReferral_total_text());
        updateUI();
    }

    @Override
    public void onError(Throwable throwable) {
        onErrorBase(throwable);
    }

    @SuppressLint("StringFormatInvalid")
    @OnClick({R.id.share})
    public void onClickAction(View view) {
        switch (view.getId()) {
            case R.id.share:
                try {
                    String appName = getString(R.string.app_name);
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, appName);
                    i.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content_referral, appName, SharedHelper.getKey(this, Constants.ReferalKey.REFERRAL_CODE)));
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
