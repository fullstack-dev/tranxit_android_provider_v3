package com.tranxitpro.partner;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.stetho.Stetho;
import com.tranxitpro.partner.common.LocaleHelper;
import com.tranxitpro.partner.data.network.model.HistoryDetail;
import com.tranxitpro.partner.data.network.model.HistoryList;
import com.tranxitpro.partner.data.network.model.Request_;
import com.tranxitpro.partner.data.network.model.TripResponse;

import io.fabric.sdk.android.Fabric;

public class MvpApplication extends Application {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static float DEFAULT_ZOOM = 15;
    public static Location mLastKnownLocation;

    private static MvpApplication mInstance;

    public static synchronized MvpApplication getInstance() {
        return mInstance;
    }

    public static Request_ DATUM = null;
    public static TripResponse tripResponse = null;
    public static Integer time_to_left = 60;
    public static HistoryList DATUM_history = null;
    public static HistoryDetail DATUM_history_detail = null;
    public static boolean isCash = true;
    public static boolean isCard = true;

    public static boolean isPayumoney;
    public static boolean isPaypal;
    public static boolean isPaytm;
    public static boolean isPaypalAdaptive;
    public static boolean isBraintree;

    public static boolean canGoToChatScreen;
    public static boolean isChatScreenOpen;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        mInstance = this;

        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        Crashlytics crashlytics = new Crashlytics.Builder().core(core).build();
        Fabric.with(this, crashlytics);

    }

    public String getNewNumberFormat(double d) {
        //      String text = Double.toString(Math.abs(d));
        String text = Double.toString(d);
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        if (decimalPlaces == 2) return text;
        else if (decimalPlaces == 1) return text + "0";
        else if (decimalPlaces == 0) return text + ".00";
        else if (decimalPlaces > 2) {
            String converted = String.valueOf((double) Math.round(d * 100) / 100);
            int convertedInegers = converted.indexOf('.');
            int convertedDecimals = converted.length() - convertedInegers - 1;
            if (convertedDecimals == 2) return converted;
            else if (convertedDecimals == 1) return converted + "0";
            else if (convertedDecimals == 0) return converted + ".00";
            else return converted;
        } else return text;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
        MultiDex.install(newBase);
    }

}
