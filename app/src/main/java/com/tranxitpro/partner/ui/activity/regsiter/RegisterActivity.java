package com.tranxitpro.partner.ui.activity.regsiter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tranxitpro.partner.BuildConfig;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseActivity;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.common.SharedHelper;
import com.tranxitpro.partner.common.Utilities;
import com.tranxitpro.partner.data.network.model.ServiceType;
import com.tranxitpro.partner.data.network.model.SettingsResponse;
import com.tranxitpro.partner.data.network.model.User;
import com.tranxitpro.partner.ui.activity.main.MainActivity;
import com.tranxitpro.partner.ui.countrypicker.Country;
import com.tranxitpro.partner.ui.countrypicker.CountryPicker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.tranxitpro.partner.common.Constants.APP_REQUEST_CODE;

public class RegisterActivity extends BaseActivity implements RegisterIView {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.txtFirstName)
    EditText txtFirstName;
    @BindView(R.id.txtLastName)
    EditText txtLastName;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.txtConfirmPassword)
    EditText txtConfirmPassword;
    @BindView(R.id.chkTerms)
    CheckBox chkTerms;
    @BindView(R.id.spinnerServiceType)
    AppCompatSpinner spinnerServiceType;
    @BindView(R.id.txtVehicleModel)
    EditText txtVehicleModel;
    @BindView(R.id.txtVehicleNumber)
    EditText txtVehicleNumber;
    @BindView(R.id.lnrReferralCode)
    LinearLayout lnrReferralCode;
    @BindView(R.id.txtReferalCode)
    EditText txtReferalCode;
    @BindView(R.id.countryImage)
    ImageView countryImage;
    @BindView(R.id.countryNumber)
    TextView countryNumber;
    @BindView(R.id.phoneNumber)
    EditText phoneNumber;

    private String countryDialCode = "+91";
    private String countryCode = "IN";
    private CountryPicker mCountryPicker;

    private RegisterPresenter presenter;
    private int selected_pos = -1;
    private List<ServiceType> lstServiceTypes = new ArrayList<>();

    private boolean isEmailAvailable = true;
    private boolean isPhoneNumberAvailable = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter = new RegisterPresenter();
        presenter.attachView(this);
        setupSpinner(null);
        presenter.getSettings();
        spinnerServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        clickFunctions();

        setCountryList();

        if (BuildConfig.DEBUG) {
            txtEmail.setText("stevejobs@yopmail.com");
            txtFirstName.setText("steve");
            txtLastName.setText("jobs");
            txtVehicleModel.setText("DUKE");
            txtVehicleNumber.setText("PY 01 AA 1111");
            phoneNumber.setText("9003440134");
            txtPassword.setText("112233");
            txtConfirmPassword.setText("112233");
        }

        if (SharedHelper.getKey(this, Constants.SharedPref.DEVICE_TOKEN).isEmpty()) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.w("PasswordActivity", "getInstanceId failed", task.getException());
                    return;
                }
                Log.d("FCM_TOKEN", task.getResult().getToken());

                SharedHelper.putKey(RegisterActivity.this, Constants.SharedPref.DEVICE_TOKEN, task.getResult().getToken());
            });
        }

        @SuppressLint("HardwareIds")
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("DEVICE_ID: ", deviceId);
        SharedHelper.putKeyFCM(RegisterActivity.this, Constants.SharedPref.DEVICE_ID, deviceId);
    }

    private void setCountryList() {
        mCountryPicker = CountryPicker.newInstance("Select Country");
        List<Country> countryList = Country.getAllCountries();
        Collections.sort(countryList, (s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
        mCountryPicker.setCountriesList(countryList);

        setListener();
    }

    private void setListener() {
        mCountryPicker.setListener((name, code, dialCode, flagDrawableResID) -> {
            countryNumber.setText(dialCode);
            countryDialCode = dialCode;
            countryImage.setImageResource(flagDrawableResID);
            mCountryPicker.dismiss();
        });

        countryImage.setOnClickListener(v -> mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER"));

        countryNumber.setOnClickListener(v -> mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER"));

        getUserCountryInfo();
    }

    private void getUserCountryInfo() {
        Country country = getDeviceCountry(RegisterActivity.this);
        countryImage.setImageResource(country.getFlag());
        countryNumber.setText(country.getDialCode());
        countryDialCode = country.getDialCode();
        countryCode = country.getCode();
    }

    private boolean validation() {
        if (txtEmail.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtFirstName.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_first_name), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtLastName.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_last_name), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (selected_pos == -1) {
            Toasty.error(this, getString(R.string.invalid_service_type), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtVehicleModel.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_car_model), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtVehicleNumber.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_car_number), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (phoneNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_phone_number), Toast.LENGTH_SHORT).show();
            return false;
        } else if (txtPassword.getText().toString().length() < 6) {
            Toasty.error(this, getString(R.string.invalid_password_length), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtConfirmPassword.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_confirm_password), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
            Toasty.error(this, getString(R.string.password_should_be_same), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (!chkTerms.isChecked()) {
            Toasty.error(this, getString(R.string.please_accept_terms_conditions), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (isEmailAvailable) {
            showErrorMessage(txtEmail, getString(R.string.email_already_exist));
            return false;
        } else return true;
    }

    private void showErrorMessage(EditText view, String message) {
        Toasty.error(this, message, Toast.LENGTH_SHORT).show();
        view.requestFocus();
        view.setText(null);
    }

    private void register(String countryCode, String phoneNumber) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("first_name", toRequestBody(txtFirstName.getText().toString()));
        map.put("last_name", toRequestBody(txtLastName.getText().toString()));
        map.put("email", toRequestBody(txtEmail.getText().toString()));
        map.put("mobile", toRequestBody(phoneNumber));
        map.put("country_code", toRequestBody(countryCode));
        map.put("password", toRequestBody(txtPassword.getText().toString()));
        map.put("password_confirmation", toRequestBody(txtConfirmPassword.getText().toString()));
        map.put("device_token", toRequestBody(SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN)));
        map.put("device_id", toRequestBody(SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_ID)));
        map.put("service_type", toRequestBody(lstServiceTypes.get(selected_pos).getId() + ""));
        map.put("service_model", toRequestBody(txtVehicleModel.getText().toString()));
        map.put("service_number", toRequestBody(txtVehicleNumber.getText().toString()));
        map.put("device_type", toRequestBody(BuildConfig.DEVICE_TYPE));
        map.put("referral_unique_id", toRequestBody(txtReferalCode.getText().toString()));

        List<MultipartBody.Part> parts = new ArrayList<>();
        showLoading();
        presenter.register(map, parts);
    }

    @OnClick({R.id.next, R.id.back, R.id.lblTerms})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.next:
                if (validation()) if (Utilities.isConnected())
                    fbPhoneLogin(countryCode, countryDialCode, phoneNumber.getText().toString());
                else showAToast(getString(R.string.no_internet_connection));
                break;
            case R.id.lblTerms:
                showTermsConditionsDialog();
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    private void clickFunctions() {
        txtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            isEmailAvailable = true;
            if (!hasFocus && !TextUtils.isEmpty(txtEmail.getText().toString()))
                if (Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches())
                    presenter.verifyEmail(txtEmail.getText().toString().trim());
        });

        phoneNumber.setOnFocusChangeListener((v, hasFocus) -> {
            isPhoneNumberAvailable = true;
            if (!hasFocus && !TextUtils.isEmpty(phoneNumber.getText().toString()))
                presenter.verifyCredentials(countryDialCode, phoneNumber.getText().toString());
        });
    }

    private void showTermsConditionsDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getText(R.string.terms_and_conditions));
        WebView wv = new WebView(this);
        wv.loadUrl(BuildConfig.TERMS_CONDITIONS);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        alert.setView(wv);
        alert.setNegativeButton("Close", (dialog, id) -> dialog.dismiss());
        alert.show();
    }

    @Override
    public void onSuccess(User user) {
        hideLoading();
        SharedHelper.putKey(this, Constants.SharedPref.USER_ID, String.valueOf(user.getId()));
        SharedHelper.putKey(this, Constants.SharedPref.ACCESS_TOKEN, user.getAccessToken());
        SharedHelper.putKey(this, Constants.SharedPref.LOGGGED_IN, "true");
        Toasty.success(this, getString(R.string.register_success), Toast.LENGTH_SHORT, true).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onSuccess(Object verifyEmail) {
        hideLoading();
        isEmailAvailable = false;
    }

    @Override
    public void onVerifyEmailError(Throwable e) {
        isEmailAvailable = true;
        showErrorMessage(txtEmail, getString(R.string.email_already_exist));
    }

    @Override
    public void onSuccess(SettingsResponse response) {
        lstServiceTypes = response.getServiceTypes();
        lnrReferralCode.setVisibility(response.getReferral().getReferral().equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);
        setupSpinner(response);
    }

    private void setupSpinner(@Nullable SettingsResponse response) {
        ArrayList<String> lstNames = new ArrayList<>(response != null ? response.getServiceTypes().size() : 0);
        if (response != null) for (ServiceType serviceType : response.getServiceTypes())
            lstNames.add(serviceType.getName());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lstNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServiceType.setAdapter(dataAdapter);
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    public void fbPhoneLogin(String strCountryCode, String strCountryISOCode, String strPhoneNumber) {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder mBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE, AccountKitActivity.ResponseType.TOKEN);
        mBuilder.setReadPhoneStateEnabled(true);
        mBuilder.setReceiveSMS(true);
        PhoneNumber phoneNumber = new PhoneNumber(strCountryISOCode, strPhoneNumber, strCountryCode);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                mBuilder.setInitialPhoneNumber(phoneNumber).
                        build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == APP_REQUEST_CODE && data != null) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (!loginResult.wasCancelled()) {
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        Log.d("AccountKit", "onSuccess: Account Kit" + AccountKit.getCurrentAccessToken().getToken());
                        if (AccountKit.getCurrentAccessToken().getToken() != null) {
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            SharedHelper.putKey(RegisterActivity.this, Constants.SharedPref.DIAL_CODE,
                                    "+" + phoneNumber.getCountryCode());
                            SharedHelper.putKey(RegisterActivity.this, Constants.SharedPref.MOBILE,
                                    phoneNumber.getPhoneNumber());
                            register(SharedHelper.getKey(RegisterActivity.this, Constants.SharedPref.DIAL_CODE),
                                    SharedHelper.getKey(RegisterActivity.this, Constants.SharedPref.MOBILE));
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

    @Override
    public void onSuccessPhoneNumber(Object object) {
        isPhoneNumberAvailable = false;
    }

    @Override
    public void onVerifyPhoneNumberError(Throwable e) {
        isPhoneNumberAvailable = true;
        showErrorMessage(phoneNumber, getString(R.string.mobile_number_already_exist));
    }
}
