package com.tranxitpro.partner.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import com.tranxitpro.partner.BuildConfig;

public class SharedHelper {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void putKey(Context context, String Key, String Value) {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(Key, Value);
        editor.apply();
    }

    public static String getKey(Context context, String Key) {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Key, "");
    }

    public static void putKey(Context context, String Key, Boolean Value) {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(Key, Value);
        editor.apply();
    }

    public static void putKey(Context context, String Key, Integer value) {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt(Key, value);
        editor.apply();
    }

    public static Integer getIntKey(Context context, String Key) {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(Key, -1);
    }

    public static String getKey(Context context, String Key, String defaultValue) {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Key, defaultValue);
    }

    public static void clearSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    public static void putKeyFCM(Context context, String Key, String Value) {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID + ".fcm", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(Key, Value);
        editor.apply();
    }

    public static String getKeyFCM(Context context, String Key) {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID + ".fcm", Context.MODE_PRIVATE);
        return sharedPreferences.getString(Key, "");
    }

    public static void putCurrentLocation(Context context, LatLng latLng) {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putFloat("current_lat", (float) latLng.latitude);
        editor.putFloat("current_lng", (float) latLng.longitude);
        editor.apply();
    }

    public static LatLng getCurrentLocation(Context context) {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        sharedPreferences.getString("Services", "");
        return new LatLng(sharedPreferences.getFloat("current_lat", 0), sharedPreferences.getFloat("current_lng", 0));
    }

}
