package com.tranxitpro.partner.ui.activity.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.tranxitpro.partner.BuildConfig;
import com.tranxitpro.partner.MvpApplication;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseActivity;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.common.SharedHelper;
import com.tranxitpro.partner.common.Utilities;
import com.tranxitpro.partner.data.network.model.UserResponse;
import com.tranxitpro.partner.ui.activity.change_password.ChangePasswordActivtiy;
import com.tranxitpro.partner.ui.activity.main.MainActivity;
import com.tranxitpro.partner.ui.activity.sociallogin.SocialLoginActivity;
import com.tranxitpro.partner.ui.countrypicker.Country;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.tranxitpro.partner.common.Constants.APP_REQUEST_CODE;
import static com.tranxitpro.partner.common.Constants.MULTIPLE_PERMISSION;
import static com.tranxitpro.partner.common.Constants.RC_MULTIPLE_PERMISSION_CODE;

public class ProfileActivity extends BaseActivity implements ProfileIView {

    private ProfilePresenter presenter = new ProfilePresenter();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;
    @BindView(R.id.txtFirstName)
    EditText txtFirstName;
    @BindView(R.id.txtLastName)
    EditText txtLastName;
    @BindView(R.id.txtPhoneNumber)
    TextView txtPhoneNumber;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.txtService)
    EditText txtService;
    @BindView(R.id.txtModel)
    EditText txtModel;
    @BindView(R.id.txtNumber)
    EditText txtNumber;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.lblChangePassword)
    TextView lblChangePassword;
    @BindView(R.id.qr_scan)
    ImageView ivQrScan;

    private File imgFile = null;
    private String countryDialCode = Constants.DEFAULT_COUNTRY_DIAL_CODE;
    private String countryCode = Constants.DEFAULT_COUNTRY_CODE;
    private String qrCodeUrl;

    private AlertDialog mDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_profile;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        showLoading();
        presenter.getProfile();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.profile));

        getUserCountryInfo();
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

    @OnClick({R.id.btnSave, R.id.lblChangePassword, R.id.imgProfile, R.id.qr_scan, R.id.txtPhoneNumber})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                profileUpdate();
                break;
            case R.id.lblChangePassword:
                startActivity(new Intent(ProfileActivity.this, ChangePasswordActivtiy.class));
                break;
            case R.id.imgProfile:
                MultiplePermissionTask();
                break;
            case R.id.qr_scan:
                if (!TextUtils.isEmpty(qrCodeUrl)) showQRCodePopup();
                break;
            case R.id.txtPhoneNumber:
                fbOtpVerify(countryCode, countryDialCode, "");
                break;
        }
    }


    private void getUserCountryInfo() {
        Country country = getDeviceCountry(ProfileActivity.this);
        countryDialCode = country.getDialCode();
        countryCode = country.getCode();
    }

    private void showQRCodePopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_qrcode_image, null);

        ImageView qr_image = view.findViewById(R.id.qr_image);
        ImageView close = view.findViewById(R.id.ivClose);

        Glide.with(ProfileActivity.this)
                .load(qrCodeUrl)
                .apply(RequestOptions
                        .placeholderOf(R.drawable.ic_qr_code)
                        .dontAnimate().error(R.drawable.ic_qr_code))
                .into(qr_image);

        builder.setView(view);
        mDialog = builder.create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        close.setOnClickListener(view1 -> mDialog.dismiss());

        mDialog.show();
    }

    void profileUpdate() {
        if (txtFirstName.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_first_name), Toast.LENGTH_SHORT).show();
        } else if (txtLastName.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_last_name), Toast.LENGTH_SHORT).show();
        } else if (txtPhoneNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_phone_number), Toast.LENGTH_SHORT).show();
        } else if (txtEmail.getText().toString().isEmpty()
                || !Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {
            Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
//        } else if (!phoneNumber.equalsIgnoreCase(txtPhoneNumber.getText().toString())) {
            //Mobile number having both country code or not so we simply sending empty here
//            fbOtpVerify(countryCode, countryDialCode, "");
        } else updateDetails();
    }

    private void updateDetails() {
        Map<String, RequestBody> map = new HashMap<>();

        map.put("first_name", toRequestBody(txtFirstName.getText().toString()));
        map.put("last_name", toRequestBody(txtLastName.getText().toString()));
        map.put("email", toRequestBody(txtEmail.getText().toString()));
        map.put("mobile", toRequestBody(txtPhoneNumber.getText().toString()));

        MultipartBody.Part filePart = null;
        if (imgFile != null)
            try {
                File compressedImageFile = new Compressor(this).compressToFile(imgFile);
                filePart = MultipartBody.Part.createFormData("avatar", compressedImageFile.getName(),
                        RequestBody.create(MediaType.parse("image*//*"), compressedImageFile));
            } catch (IOException e) {
                e.printStackTrace();
            }

        Utilities.printV("Params ===> 2", map.toString());
        showLoading();
        presenter.profileUpdate(map, filePart);
    }

    @Override
    public void onSuccess(UserResponse user) {
        hideLoading();
        Utilities.printV("User===>", user.getFirstName() + user.getLastName());
        Utilities.printV("TOKEN===>", SharedHelper.getKey(MvpApplication.getInstance(),
                Constants.SharedPref.ACCESS_TOKEN, ""));

        String loginBy = user.getLoginBy();

        if (loginBy.equalsIgnoreCase("facebook") || loginBy.equalsIgnoreCase("google"))
            lblChangePassword.setVisibility(View.INVISIBLE);
        else lblChangePassword.setVisibility(View.VISIBLE);

        txtFirstName.setText(user.getFirstName());
        txtLastName.setText(user.getLastName());
        txtPhoneNumber.setText(String.valueOf(user.getMobile()));
        txtNumber.setText(user.getService().getServiceNumber());
        txtModel.setText(user.getService().getServiceModel());

        txtEmail.setText(user.getEmail());
        SharedHelper.putKey(this, Constants.SharedPref.STRIPE_PUBLISHABLE_KEY, user.getStripePublishableKey());
        if (user.getService() != null)
            txtService.setText((user.getService().getServiceType() != null)
                    ? user.getService().getServiceType().getName() : "");
        Glide.with(activity())
                .load(BuildConfig.BASE_IMAGE_URL + user.getAvatar())
                .apply(RequestOptions
                        .placeholderOf(R.drawable.ic_user_placeholder)
                        .dontAnimate()
                        .error(R.drawable.ic_user_placeholder))
                .into(imgProfile);
        Constants.showOTP = user.getRide_otp().equals("1");
        qrCodeUrl = !TextUtils.isEmpty(user.getQrcode_url()) ? BuildConfig.BASE_URL + user.getQrcode_url() : null;
        ivQrScan.setVisibility(TextUtils.isEmpty(qrCodeUrl) ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onSuccessUpdate(UserResponse object) {
        hideLoading();
        Intent profileIntent = new Intent(this, MainActivity.class);
        profileIntent.putExtra("avatar", BuildConfig.BASE_IMAGE_URL + object.getAvatar());
        startActivity(profileIntent);
        Toasty.success(this, getString(R.string.profile_updated), Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, ProfileActivity.this,
                new DefaultCallback() {
                    @Override
                    public void onImagesPicked(@NonNull List<File> imageFiles,
                                               EasyImage.ImageSource source, int type) {
                        imgFile = imageFiles.get(0);
                        Glide.with(activity())
                                .load(Uri.fromFile(imgFile))
                                .apply(RequestOptions
                                        .placeholderOf(R.drawable.ic_user_placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.ic_user_placeholder))
                                .into(imgProfile);
                    }
                });

        if (requestCode == APP_REQUEST_CODE && data != null) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (!loginResult.wasCancelled())
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        Log.d("AccountKit", "onSuccess: Account Kit" + Objects.requireNonNull(AccountKit.getCurrentAccessToken()).getToken());
                        if (Objects.requireNonNull(AccountKit.getCurrentAccessToken()).getToken() != null) {
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            SharedHelper.putKey(ProfileActivity.this, Constants.SharedPref.DIAL_CODE, "+" + phoneNumber.getCountryCode());
                            SharedHelper.putKey(ProfileActivity.this, Constants.SharedPref.MOBILE, phoneNumber.getPhoneNumber());
                            txtPhoneNumber.setText(phoneNumber.getPhoneNumber());
                            presenter.verifyCredentials("+" + phoneNumber.getCountryCode(), phoneNumber.getPhoneNumber());
                        }
                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Log.e("AccountKit", "onError: Account Kit" + accountKitError);
                    }
                });
        }
    }

    private boolean hasMultiplePermission() {
        return EasyPermissions.hasPermissions(this, MULTIPLE_PERMISSION);
    }

    @AfterPermissionGranted(RC_MULTIPLE_PERMISSION_CODE)
    void MultiplePermissionTask() {
        if (hasMultiplePermission()) pickImage();
        else EasyPermissions.requestPermissions(
                this, getString(R.string.please_accept_permission),
                RC_MULTIPLE_PERMISSION_CODE,
                MULTIPLE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onSuccessPhoneNumber(Object object) {
        updateDetails();
    }

    @Override
    public void onVerifyPhoneNumberError(Throwable e) {
        Toasty.error(this, getString(R.string.phone_number_already_exists), Toast.LENGTH_SHORT).show();
        txtPhoneNumber.requestFocus();
    }
}
