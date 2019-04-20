package com.tranxitpro.partner.common.fcm;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tranxitpro.partner.MvpApplication;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.common.SharedHelper;
import com.tranxitpro.partner.ui.activity.main.MainActivity;

import java.util.List;

@SuppressLint("InvalidWakeLockTag")
public class MyFireBaseMessagingService extends FirebaseMessagingService {

    public static final String INTENT_FILTER = "INTENT_FILTER";
    private static final String TAG = "RRRR Notification: ";
    private int notificationId = 0;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        @SuppressLint("HardwareIds")
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("DEVICE_ID: ", deviceId);
        Log.d("FCM_TOKEN", s);

        SharedHelper.putKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN, s);
        SharedHelper.putKeyFCM(this, Constants.SharedPref.DEVICE_ID, deviceId);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null) {
            String chat = null;

            System.out.println(TAG + "From: " + remoteMessage.getFrom());
            System.out.println(TAG + "getData: " + remoteMessage.getData());

            String message = remoteMessage.getData().get("message");

            try {
                chat = remoteMessage.getData().get("custom");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (TextUtils.isEmpty(message)) message = remoteMessage.getData().toString();

            if (isLocked(getApplicationContext()) && (message.contains("RRRR")
                    || message.equalsIgnoreCase("New Incoming Ride")))
                sendNotificationProlonged(message);
            else sendNotification(message, !TextUtils.isEmpty(chat));

            System.out.println(TAG
                    + " message = " + message
                    + " status = Background-> " + isBackground(getApplicationContext())
                    + " isLocked ->" + isLocked(getApplicationContext())
                    + " is CallActive -> " + isCallActive(getApplicationContext()));

            if ((message.equalsIgnoreCase("New Incoming Ride") || message.contains("RRRR"))
                    && isBackground(getApplicationContext())
                    && !isLocked(getApplicationContext())
                    && !isCallActive(getApplicationContext())) restartApp();

            sendBroadcast(new Intent(INTENT_FILTER));

        } else System.out.println(TAG + "FCM Notification failed");

    }

    private void restartApp() {
        System.out.println("RRR MyFireBaseMessagingService.restartApp");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void sendNotification(String messageBody, boolean isChat) {

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) ((PowerManager) getSystemService(POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.ON_AFTER_RELEASE, "TAG").acquire();

        MvpApplication.canGoToChatScreen = isChat;
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity
                (this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_push)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(uri)
                        .setContentIntent(pIntent);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channel);
        }

        nm.notify(notificationId++, notificationBuilder.build());
    }

    private void sendNotificationProlonged(String messageBody) {

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            System.out.println("RRR MyFireBaseMessagingService.sendNotificationProlonged");
            PowerManager.WakeLock screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "TAG");
            screenLock.acquire();

        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_push)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        Notification mNotification = notificationBuilder.build();
        mNotification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_SOUND;

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channel);
        }

        nm.notify(notificationId++, mNotification);
    }

    private boolean isBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses)
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                    for (String activeProcess : processInfo.pkgList)
                        if (activeProcess.equals(context.getPackageName())) isInBackground = false;
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName()))
                isInBackground = false;
        }

        return isInBackground;
    }

    public boolean isCallActive(Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return manager.getMode() == AudioManager.MODE_IN_CALL;
    }

    public boolean isLocked(Context context) {
        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return myKM.isKeyguardLocked();
    }
}
