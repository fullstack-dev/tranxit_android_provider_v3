package com.tranxitpro.partner.ui.activity.sociallogin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.tranxitpro.partner.BuildConfig;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseActivity;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.common.SharedHelper;
import com.tranxitpro.partner.data.network.model.Token;
import com.tranxitpro.partner.ui.activity.main.MainActivity;
import com.tranxitpro.partner.ui.countrypicker.Country;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tranxitpro.partner.common.Constants.APP_REQUEST_CODE;

public class SocialLoginActivity extends BaseActivity implements SocialLoginIView {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lblFacebook)
    TextView lblFacebook;
    @BindView(R.id.lblGoogle)
    TextView lblGoogle;

    private String country_dial_code = Constants.DEFAULT_COUNTRY_DIAL_CODE;
    private String country_code = Constants.DEFAULT_COUNTRY_CODE;

    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    private SocialLoginPresenter<SocialLoginActivity> presenter = new SocialLoginPresenter<>();
    private CallbackManager callbackManager;
    private HashMap<String, Object> map = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_social_login;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);

        callbackManager = CallbackManager.Factory.create();
        presenter.attachView(this);
        map.put("device_token", SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN));
        map.put("device_id", SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_ID));
        map.put("device_type", BuildConfig.DEVICE_TYPE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.google_signin_server_client_id)).
                requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        getUserCountryInfo();

    }

    @OnClick({R.id.back, R.id.lblFacebook, R.id.lblGoogle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.lblFacebook:
                fbLogin();
                break;
            case R.id.lblGoogle:
                showLoading();
                startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
                break;
        }
    }

    private void getUserCountryInfo() {
        Country country = getDeviceCountry(SocialLoginActivity.this);
        country_dial_code = country.getDialCode();
        country_code = country.getCode();
    }

    void fbLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            public void onSuccess(LoginResult loginResult) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    map.put("login_by", Constants.Login.FACEBOOK);
                    map.put("accessToken", loginResult.getAccessToken().getToken());
                    fbOtpVerify(country_code, country_dial_code, "");
                }
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                if (exception instanceof FacebookAuthorizationException)
                    if (AccessToken.getCurrentAccessToken() != null)
                        LoginManager.getInstance().logOut();
                Log.e("Facebook", exception.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            hideLoading();
            String TAG = "Google";
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                map.put("login_by", Constants.Login.GOOGLE);
                Runnable runnable = () -> {
                    try {
                        String scope = "oauth2:" + Scopes.EMAIL + " " + Scopes.PROFILE;
                        String accessToken = GoogleAuthUtil.getToken(getApplicationContext(), account.getAccount(), scope, new Bundle());
                        Log.d(TAG, "accessToken:" + accessToken);
                        map.put("accessToken", accessToken);
                        fbOtpVerify(country_code, country_dial_code, "");
                    } catch (IOException | GoogleAuthException e) {
                        e.printStackTrace();
                    }
                };
                AsyncTask.execute(runnable);

            } catch (ApiException e) {
                Log.w(TAG, "signInResult : failed code = " + e.getStatusCode());
            }
        } else if (requestCode == APP_REQUEST_CODE && data != null) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (!loginResult.wasCancelled()) {
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        Log.d("AccountKit", "onSuccess: Account Kit" + AccountKit.getCurrentAccessToken().getToken());
                        if (AccountKit.getCurrentAccessToken().getToken() != null) {
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            SharedHelper.putKey(SocialLoginActivity.this, Constants.SharedPref.DIAL_CODE,
                                    "+" + phoneNumber.getCountryCode());
                            SharedHelper.putKey(SocialLoginActivity.this, Constants.SharedPref.MOBILE,
                                    phoneNumber.getPhoneNumber());
                            register();
                        }
                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Log.e("AccountKit", "onError: Account Kit" + accountKitError);
                    }
                });
            }
        }
    }

    private void register() {
        map.put("mobile", SharedHelper.getKey(this, Constants.SharedPref.MOBILE));
        map.put("country_code", SharedHelper.getKey(this, Constants.SharedPref.DIAL_CODE));
        if (map.get("login_by").equals("google")) presenter.loginGoogle(map);
        else if (map.get("login_by").equals("facebook")) presenter.loginFacebook(map);
        System.out.println("RRR map = " + map);
        showLoading();
    }

    @Override
    public void onSuccess(Token token) {
        hideLoading();
        String accessToken = token.getTokenType() + " " + token.getAccessToken();
        SharedHelper.putKey(this, Constants.SharedPref.ACCESS_TOKEN, accessToken);
        SharedHelper.putKey(this, Constants.SharedPref.LOGGGED_IN, "true");
        finishAffinity();
        startActivity(new Intent(this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

}
