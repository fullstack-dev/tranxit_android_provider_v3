package com.tranxitpro.partner.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.tranxitpro.partner.MvpApplication;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.ui.activity.welcome.WelcomeActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class Utilities {

    public static boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) MvpApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnected();
    }

    public static void printV(String TAG, String message) {
        System.out.println(TAG + "==>" + message);
    }

    public static void LogoutApp(Activity thisActivity, String logout_text) {

        logout_text = "Loggedout Successfully!";

        Toasty.success(thisActivity, logout_text, Toast.LENGTH_SHORT).show();
        SharedHelper.clearSharedPreferences(thisActivity);
        NotificationManager notificationManager = (NotificationManager) thisActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        thisActivity.finishAffinity();
        Intent goToLogin = new Intent(thisActivity, WelcomeActivity.class);
        goToLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        thisActivity.startActivity(goToLogin);
        thisActivity.finish();

    }

    public static void LogoutApp(Activity thisActivity) {
//        Toasty.success(thisActivity, thisActivity.getString(R.string.logout_successfully), Toast.LENGTH_SHORT).show();
        SharedHelper.clearSharedPreferences(thisActivity);
        NotificationManager notificationManager = (NotificationManager) thisActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        thisActivity.startActivity(new Intent(thisActivity, WelcomeActivity.class));
        thisActivity.finishAffinity();
    }

    public static String convertDate(String receiveDate) throws ParseException {
        SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = in.parse(receiveDate);
        return new SimpleDateFormat("dd MMM").format(date);

    }

    public static void animateTextView(int initialValue, int finalValue, final int target, final TextView textview) {
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(0.8f);
        int start = Math.min(initialValue, finalValue);
        int end = Math.max(initialValue, finalValue);
        int difference = Math.abs(finalValue - initialValue);
        Handler handler = new Handler();
        for (int count = start; count <= end; count++) {
            int time = Math.round(decelerateInterpolator.getInterpolation((((float) count) / difference)) * 100) * count;
            final int finalCount = ((initialValue > finalValue) ? initialValue - count : count);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textview.setText(finalCount + "/" + target);
                }
            }, time);
        }
    }

    public static boolean isPackageExisted(Context context, String targetPackage) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}
