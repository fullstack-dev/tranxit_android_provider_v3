package com.tranxitpro.partner.common;

import android.Manifest;

public class Constants {

    public static final String[] MULTIPLE_PERMISSION = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int RC_MULTIPLE_PERMISSION_CODE = 12224;
    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_ARABIC = "ar";
    public static final int RC_CALL_PHONE = 123;
    public static int APP_REQUEST_CODE = 99;
    public static String Currency = "";
    public static boolean showOTP;
    public static final String userAppPackageName = "com.tranxitpro.user";
    public static final String DEFAULT_COUNTRY_DIAL_CODE = "+1";
    public static final String DEFAULT_COUNTRY_CODE = "US";



    public interface SharedPref {
        String LOGGGED_IN = "LOGGGED_IN";
        String EMAIL = "EMAIL";
        String TXT_EMAIL = "TXTEMAIL";
        String OTP = "OTP";
        String ID = "ID_";
        String USER_ID = "USER_ID";
        String USER_NAME = "USER_NAME";
        String USER_AVATAR = "USER_AVATAR";
        String DEVICE_TOKEN = "DEVICE_TOKEN";
        String DEVICE_ID = "DEVICE_ID";
        String ACCESS_TOKEN = "ACCESS_TOKEN";
        String DIAL_CODE = "DIAL_CODE";
        String MOBILE = "MOBILE";
        String CANCEL_ID = "CANCEL_ID";
        String REQUEST_ID = "REQUEST_ID";
        String CURRENCY = "CURRENCY";
        String SERVICE_TYPE = "SERVICE_TYPE";
        String STRIPE_PUBLISHABLE_KEY = "STRIPE_PUBLISHABLE_KEY";
        String LATITUDE = "LATITUDE";
        String LONGITUDE = "LONGITUDE";
        String PICTURE = "PICTURE";
        String USER_INFO = "USER_INFO";

    }

    public interface ReferalKey {
        String REFERRAL_CODE = "REFERRAL_CODE";
        String REFERRAL_COUNT = "REFERRAL_COUNT";
        String REFERRAL_TEXT = "REFERRAL_TEXT";
        String REFERRAL_TOTAL_TEXT = "REFERRAL_TOTAL_TEXT";

    }

    public interface checkStatus {

        String EMPTY = "EMPTY";
        String SEARCHING = "SEARCHING";
        String ACCEPTED = "ACCEPTED";
        String STARTED = "STARTED";
        String ARRIVED = "ARRIVED";
        String PICKEDUP = "PICKEDUP";
        String DROPPED = "DROPPED";
        String COMPLETED = "COMPLETED";
        String PATCH = "PATCH";
    }

    public interface User {

        interface Account {
            String PENDING_DOCUMENT = "document";
            String PENDING_CARD = "card";
            String ONBOARDING = "onboarding";
            String APPROVED = "approved";
            String BANNED = "banned";
            String BALANCE = "balance";
        }

        interface Service {
            String OFFLINE = "offline";
            String ACTIVE = "active";
            String BALANCE = "balance";
        }
    }

    public interface PaymentMode {
        String CASH = "CASH";
        String CARD = "CARD";
        String PAYPAL = "PAYPAL";
        String WALLET = "WALLET";
    }

    public interface Login {
        String FACEBOOK = "facebook";
        String GOOGLE = "google";
    }

    public interface InvoiceFare {
        String MIN = "MIN";
        String HOUR = "HOUR";
        String DISTANCE = "DISTANCE";
        String DISTANCEMIN = "DISTANCEMIN";
        String DISTANCEHOUR = "DISTANCEHOUR";
    }
}
