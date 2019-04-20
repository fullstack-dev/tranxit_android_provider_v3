package com.tranxitpro.partner.ui.activity.change_password;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseActivity;
import com.tranxitpro.partner.common.Utilities;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ChangePasswordActivtiy extends BaseActivity
        implements ChangePasswordIView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ChangePasswordPresenter presenter = new ChangePasswordPresenter();
    @BindView(R.id.txtCurrentPassword)
    EditText txtCurrentPassword;
    @BindView(R.id.txtNewPassword)
    EditText txtNewPassword;
    @BindView(R.id.txtConfirmPassword)
    EditText txtConfirmPassword;
    @BindView(R.id.btnChangePassword)
    Button btnChangePassword;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.change_password));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @OnClick(R.id.btnChangePassword)
    public void onViewClicked() {

        if (txtCurrentPassword.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.curr_pwd_val), Toast.LENGTH_SHORT, true).show();
        } else if (txtNewPassword.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_new_password), Toast.LENGTH_SHORT, true).show();
        } else if (txtConfirmPassword.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.confirm_pwd_val), Toast.LENGTH_SHORT, true).show();
        } else if (!txtNewPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
            Toasty.error(this, getString(R.string.password_should_be_same), Toast.LENGTH_SHORT, true).show();
        } else {
            showLoading();
            HashMap<String, Object> map = new HashMap<>();
            map.put("password_old", txtCurrentPassword.getText().toString());
            map.put("password", txtNewPassword.getText().toString());
            map.put("password_confirmation", txtConfirmPassword.getText().toString());
            presenter.changePassword(map);
        }
    }

    @Override
    public void onSuccess(Object object) {
        hideLoading();
        Toasty.success(this, getString(R.string.pass_updated), Toast.LENGTH_SHORT, true).show();
        Utilities.LogoutApp(activity());
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if(e!= null)
        onErrorBase(e);
    }
}
