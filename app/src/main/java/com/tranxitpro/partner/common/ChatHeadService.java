package com.tranxitpro.partner.common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.tranxitpro.partner.R;
import com.tranxitpro.partner.ui.activity.main.MainActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewListener;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewManager;

public class ChatHeadService extends Service implements FloatingViewListener {

    public static final String EXTRA_CUTOUT_SAFE_AREA = "cutout_safe_area";
    private static final String TAG = "ChatHeadService";
    private static final int NOTIFICATION_ID = 9083150;

    private FloatingViewManager mFloatingViewManager;

    private static Notification createNotification(Context context) {
        final String channelId = "default_floatingview_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String channelName = "Default Channel";
            final NotificationChannel defaultChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_MIN);
            final NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(defaultChannel);
            }
        }

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(context.getString(R.string.app_name) + " head");
        builder.setContentText(context.getString(R.string.app_name) + " running.");
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_MIN);
        builder.setCategory(NotificationCompat.CATEGORY_SERVICE);

        return builder.build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mFloatingViewManager != null) {
            return START_STICKY;
        }

        final DisplayMetrics metrics = new DisplayMetrics();
        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        final LayoutInflater inflater = LayoutInflater.from(this);
        final CircleImageView iconView = (CircleImageView) inflater.inflate(R.layout.widget_chathead, null, false);
        iconView.setOnClickListener(v -> {
            if (Utilities.isAppIsInBackground(getApplicationContext())) {
                String isLogged = SharedHelper.getKey(this, Constants.SharedPref.LOGGGED_IN);
                if (isLogged.equalsIgnoreCase("true")) {
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                }
            }
        });

        mFloatingViewManager = new FloatingViewManager(this, this);
        mFloatingViewManager.setFixedTrashIconImage(R.drawable.ic_trash_fixed);
        mFloatingViewManager.setActionTrashIconImage(R.drawable.ic_trash_action);
        mFloatingViewManager.setSafeInsetRect((Rect) intent.getParcelableExtra(EXTRA_CUTOUT_SAFE_AREA));
        final FloatingViewManager.Options options = new FloatingViewManager.Options();
        options.overMargin = (int) (16 * metrics.density);
        mFloatingViewManager.addViewToWindow(iconView, options);

        startForeground(NOTIFICATION_ID, createNotification(this));

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onFinishFloatingView() {
        stopSelf();
    }

    @Override
    public void onTouchFinished(boolean isFinishing, int x, int y) {
        if (isFinishing) {
            Log.d(TAG, "FloatingView will be deleted soon.");
        } else {
            //Log.d(TAG, getString(R.string.touch_finished_position, x, y));
        }
    }

    @Override
    public void onDestroy() {
        if (mFloatingViewManager != null) {
            mFloatingViewManager.removeAllViewToWindow();
            mFloatingViewManager = null;
        }
        super.onDestroy();
    }
}
