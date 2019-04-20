package com.tranxitpro.partner.ui.activity.forgot_password;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseActivity;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.common.SharedHelper;
import com.tranxitpro.partner.data.network.model.ForgotResponse;
import com.tranxitpro.partner.ui.activity.reset_password.ResetActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ForgotActivity extends BaseActivity implements ForgotIView {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.next)
    FloatingActionButton next;

    ForgotPresenter presenter = new ForgotPresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_forgot;
    }

    @Override
    public void initView() {

        ButterKnife.bind(this);
        presenter.attachView(this);
        txtEmail.setText(SharedHelper.getKey(this, Constants.SharedPref.TXT_EMAIL));

    }

    @OnClick({R.id.back, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                activity().onBackPressed();
                break;
            case R.id.next:

                if (txtEmail.getText().toString().isEmpty()) {
                    Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
                }
                else {
                    showLoading();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("EMAIL", txtEmail.getText().toString());
                    presenter.forgot(map);
                }
                break;
        }
    }

    @Override
    public void onSuccess(ForgotResponse forgotResponse) {
        hideLoading();
        SharedHelper.putKey(this, Constants.SharedPref.TXT_EMAIL, txtEmail.getText().toString());
        SharedHelper.putKey(this, Constants.SharedPref.OTP, String.valueOf(forgotResponse.getProvider().getOtp()));
        SharedHelper.putKey(this, Constants.SharedPref.ID, String.valueOf(forgotResponse.getProvider().getId()));
        Toasty.success(this, forgotResponse.getMessage(), Toast.LENGTH_SHORT, true).show();
        startActivity(new Intent(this, ResetActivity.class));
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if(e!= null)
        onErrorBase(e);
    }
}
