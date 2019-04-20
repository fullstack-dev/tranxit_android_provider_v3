package com.tranxitpro.partner.ui.activity.password;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tranxitpro.partner.BuildConfig;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseActivity;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.common.SharedHelper;
import com.tranxitpro.partner.common.Utilities;
import com.tranxitpro.partner.data.network.model.ForgotResponse;
import com.tranxitpro.partner.data.network.model.User;
import com.tranxitpro.partner.ui.activity.main.MainActivity;
import com.tranxitpro.partner.ui.activity.regsiter.RegisterActivity;
import com.tranxitpro.partner.ui.activity.reset_password.ResetActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class PasswordActivity extends BaseActivity implements PasswordIView {

    PasswordPresenter presenter = new PasswordPresenter();

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.sign_up)
    TextView signUp;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    @BindView(R.id.next)
    FloatingActionButton next;
    String email = "";
    public static String TAG = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_password;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString(Constants.SharedPref.EMAIL);
            Utilities.printV("EMAIL===>", email);
            Utilities.printV("EMAIL===>", SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN));
            Utilities.printV("EMAIL===>", SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_ID));
        }
        if (BuildConfig.DEBUG) password.setText("112233");


        if (SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN).isEmpty()) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.w("PasswordActivity", "getInstanceId failed", task.getException());
                    return;
                }
                Log.d("FCM_TOKEN", task.getResult().getToken());

                SharedHelper.putKeyFCM(PasswordActivity.this, Constants.SharedPref.DEVICE_TOKEN, task.getResult().getToken());
            });
        }

        @SuppressLint("HardwareIds")
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("DEVICE_ID: ", deviceId);
        SharedHelper.putKeyFCM(this, Constants.SharedPref.DEVICE_ID, deviceId);


    }

    @OnClick({R.id.back, R.id.sign_up, R.id.forgot_password, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                activity().onBackPressed();
                break;
            case R.id.sign_up:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.forgot_password:
                showLoading();
                HashMap<String, Object> map = new HashMap<>();
                map.put("email", email);
                presenter.forgot(map);
//                startActivity(new Intent(this, ForgotActivity.class));
                break;
            case R.id.next:
                login();
                break;
            default:
                break;
        }
    }

    private void login() {
        if (password.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (email.isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
            return;
        }

        deviceToken = SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN);
        deviceId = SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_ID);

        if (TextUtils.isEmpty(deviceToken))
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    deviceToken = instanceIdResult.getToken();
                    SharedHelper.putKeyFCM(PasswordActivity.this, Constants.SharedPref.DEVICE_TOKEN, deviceToken);
                }
            });

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            SharedHelper.putKeyFCM(this, Constants.SharedPref.DEVICE_ID, deviceId);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password.getText().toString());
        map.put("device_id", deviceId);
        map.put("device_type", BuildConfig.DEVICE_TYPE);
        map.put("device_token", deviceToken);
        presenter.login(map);
        showLoading();
    }

    @Override
    public void onSuccess(ForgotResponse forgotResponse) {
        hideLoading();
        SharedHelper.putKey(this, Constants.SharedPref.TXT_EMAIL, email);
        SharedHelper.putKey(this, Constants.SharedPref.OTP, String.valueOf(forgotResponse.getProvider().getOtp()));
        SharedHelper.putKey(this, Constants.SharedPref.ID, String.valueOf(forgotResponse.getProvider().getId()));
        Toasty.success(this, forgotResponse.getMessage(), Toast.LENGTH_SHORT, true).show();
        startActivity(new Intent(this, ResetActivity.class));
    }

    @Override
    public void onSuccess(User user) {
        hideLoading();
        SharedHelper.putKey(this, Constants.SharedPref.ACCESS_TOKEN, user.getAccessToken());
        SharedHelper.putKey(this, Constants.SharedPref.USER_ID,
                String.valueOf(user.getId()));
        SharedHelper.putKey(this, Constants.SharedPref.LOGGGED_IN, "true");
        Toasty.success(activity(), getString(R.string.login_out_success), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();


    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            TAG = "PasswordActivity";
        onErrorBase(e);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
