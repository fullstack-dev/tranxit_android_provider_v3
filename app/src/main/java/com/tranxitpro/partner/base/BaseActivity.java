package com.tranxitpro.partner.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.common.LocaleHelper;
import com.tranxitpro.partner.common.SharedHelper;
import com.tranxitpro.partner.common.Utilities;
import com.tranxitpro.partner.data.network.model.User;
import com.tranxitpro.partner.ui.activity.password.PasswordActivity;
import com.tranxitpro.partner.ui.countrypicker.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.HttpException;

import static com.tranxitpro.partner.common.Constants.APP_REQUEST_CODE;

public abstract class BaseActivity extends AppCompatActivity implements MvpView {

    private ProgressDialog progressDialog;
    private Toast mToast;
    private BasePresenter<BaseActivity> presenter = new BasePresenter<>();
    public String deviceToken;
    public String deviceId;

    // This method  converts String to RequestBody
    public RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    public static String getDisplayableTime(long value) {

        long difference;
        long mDate = java.lang.System.currentTimeMillis();

        if (mDate > value) {
            difference = mDate - value;
            final long seconds = difference / 1000;
            final long minutes = seconds / 60;
            final long hours = minutes / 60;
            final long days = hours / 24;
            final long months = days / 31;
            final long years = days / 365;

            if (seconds < 86400)
                return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date(value));
            else if (seconds < 172800) // 48 * 60 * 60
                return "yesterday";
            else if (seconds < 2592000) // 30 * 24 * 60 * 60
                return days + " days ago";
            else if (seconds < 31104000) // 12 * 30 * 24 * 60 * 60
                return months <= 1 ? "one month ago" : days + " months ago";
            else return years <= 1 ? "one year ago" : years + " years ago";
        }
        return null;
    }

    public Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if (json != JSONObject.NULL) retMap = toMap(json);
        return retMap;
    }

    public Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            if (value instanceof JSONArray) value = toList((JSONArray) value);
            else if (value instanceof JSONObject) value = toMap((JSONObject) value);
            map.put(key, value);
        }
        return map;
    }

    public List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) value = toList((JSONArray) value);
            else if (value instanceof JSONObject) value = toMap((JSONObject) value);
            list.add(value);
        }
        return list;
    }

    @Override
    public Activity activity() {
        return this;
    }

    public abstract int getLayoutId();

    public abstract void initView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        initView();
    }

    public void pickImage() {
        EasyImage.openChooserWithGallery(this, "", 0);
    }

    public void showLoading() {
        if (progressDialog != null && !progressDialog.isShowing()) progressDialog.show();
    }

    public void hideLoading() {
        try {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAToast(String message) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void fbOtpVerify(String strCountryCode, String strCountryISOCode, String strPhoneNumber) {

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

    public String getAddress(LatLng currentLocation) {
        String address = null;
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
            if ((addresses != null) && !addresses.isEmpty()) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                if (returnedAddress.getMaxAddressLineIndex() > 0)
                    for (int j = 0; j < returnedAddress.getMaxAddressLineIndex(); j++)
                        strReturnedAddress.append(returnedAddress.getAddressLine(j)).append("");
                else strReturnedAddress.append(returnedAddress.getAddressLine(0)).append("");
                address = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    public void onErrorBase(Throwable e) {

        hideLoading();
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            int responseCode = ((HttpException) e).response().code();
            try {
                JSONObject jsonObject = new JSONObject(responseBody.string());
                if (responseCode == 500)
                    Toasty.error(this, getString(R.string.server_down), Toast.LENGTH_SHORT).show();
                else if (responseCode == 400 || responseCode == 405)
                    Toasty.error(this, getErrorMessage(jsonObject), Toast.LENGTH_SHORT).show();
                else if (responseCode == 404) {
                    if (PasswordActivity.TAG.equals("PasswordActivity")) {
                        Collection<Object> values = jsonToMap(jsonObject).values();
                        printIfContainsValue(values.toString().replaceAll("[\\[\\],]", ""), "Password");
                    } else
                        Toasty.error(this, getErrorMessage(jsonObject), Toast.LENGTH_SHORT).show();
                } else if (responseCode == 401) {
                    if (!SharedHelper.getKey(this, "refresh_token").equalsIgnoreCase(""))
                        refreshToken();
                    else {
                        Toasty.error(this, getString(R.string.unauthenticated), Toast.LENGTH_SHORT).show();
                        Utilities.LogoutApp(this);
                    }
                } else if (responseCode == 422) {
                    Collection<Object> values = jsonToMap(jsonObject).values();
                    printIfContainsValue(values.toString().replaceAll("[\\[\\],]", ""), values.toString()
                            .replaceAll("[\\[\\],]", ""));
                } else if (responseCode == 503)
                    Toasty.error(this, getString(R.string.server_down), Toast.LENGTH_SHORT).show();
                else if (responseCode == 520)
                    Toasty.error(this, getErrorMessage(jsonObject), Toast.LENGTH_SHORT).show();
                else Toasty.error(this, getErrorMessage(jsonObject), Toast.LENGTH_SHORT).show();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void printIfContainsValue(String stringValue, String value) {
        if (value.equals("Password")) {
            String[] parts = stringValue.split("\\.");
            String part1 = parts[0]; // 004
            Toasty.error(this, part1, Toast.LENGTH_LONG).show();
        } else Toasty.error(this, stringValue, Toast.LENGTH_LONG).show();
    }

    private String getErrorMessage(JSONObject jsonObject) {
        try {
            String error;
            if (jsonObject.has("message")) error = jsonObject.getString("message");
            else if (jsonObject.has("error")) error = jsonObject.getString("error");
            else error = getString(R.string.some_thing_wrong);
            return error;
        } catch (Exception e) {
            e.printStackTrace();
            return getString(R.string.some_thing_wrong);
        }
    }

    public String printJSON(Object o) {
        return new Gson().toJson(o);
    }

    public void refreshToken() {
        showLoading();
        presenter.refreshToken();
    }

    @Override
    public void onSuccessRefreshToken(User user) {
        hideLoading();
        SharedHelper.putKey(this, user.getAccessToken(), Constants.SharedPref.ACCESS_TOKEN);
        Toasty.error(this, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorRefreshToken(Throwable throwable) {
        hideLoading();
        if (throwable != null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", SharedHelper.getKey(activity(),
                    Constants.SharedPref.USER_ID) + "");
            showLoading();
            presenter.logout(map);
        }
    }

    @Override
    public void onSuccessLogout(Object object) {
        hideLoading();
        Utilities.LogoutApp(activity(), "");
    }

    @Override
    public void onError(Throwable throwable) {
        hideLoading();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    public Country getDeviceCountry(Context context) {
        return Country.getCountryFromSIM(context) != null
                ? Country.getCountryFromSIM(context)
                : new Country("US", "United States", "+1", R.drawable.flag_us);
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
