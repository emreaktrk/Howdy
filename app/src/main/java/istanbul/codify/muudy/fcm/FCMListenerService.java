package istanbul.codify.muudy.fcm;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import com.blankj.utilcode.util.ActivityUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.deeplink.DeepLink;
import istanbul.codify.muudy.deeplink.DeepLinkManager;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.NotificationActionType;
import istanbul.codify.muudy.model.event.notification.NotificationEvent;
import istanbul.codify.muudy.ui.splash.SplashActivity;
import org.greenrobot.eventbus.EventBus;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;


public final class FCMListenerService extends FirebaseMessagingService {

    private static final int NOTIFICATION_ID = 876;
    private static final String NOTIFICATION_CHANNEL_ID = "612";
    private static final String NOTIFICATION_CHANNEL_NAME = "Default";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Logcat.d(message.getData().toString());

        sendNotification(message);
    }

    private void sendNotification(RemoteMessage message) {
        NotificationEvent event = getEvent(message);
        if (event != null) {
            DeepLink link = event.getDeepLink();
            if (link != null) {
                DeepLinkManager
                        .getInstance()
                        .setPending(link);
            }

            EventBus
                    .getDefault()
                    .post(event);
        }

        if (isAppInForeground(getApplicationContext())) {
            Logcat.d("App is in foreground");
        } else {
            Logcat.d("App is in background");

            NotificationManager manager = getManager();

            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && manager != null) {
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableLights(true);
                channel.setLightColor(Color.parseColor("#B964EF"));
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                channel.setShowBadge(true);
                manager.createNotificationChannel(channel);
            }

            Notification notification = new NotificationCompat
                    .Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(getTitle(message))
                    .setContentText(getBody(message))
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(getBaseContext(), R.color.blue))
                    .setLights(ContextCompat.getColor(getBaseContext(), R.color.blue), 1000, 1000)
                    .setTicker(getString(R.string.app_name))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setColorized(true)
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .setCategory("Default")
                    .build();

            if (manager != null) {
                manager.notify(NOTIFICATION_ID, notification);
            }
        }
    }

    private boolean isAppInForeground(Context context) {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            return true;
        }

        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        // App is foreground, but screen is locked, so show notification
        return km.inKeyguardRestrictedInputMode();
    }

    private String getTitle(RemoteMessage message) {
        if (message.getNotification() == null
                || TextUtils.isEmpty(message.getNotification().getTitle())) {
            if (message.getData() != null && message.getData().containsKey("title")) {
                return message.getData().get("title");
            } else {
                return "";
            }
        } else {
            return message.getNotification().getTitle();
        }
    }

    private String getBody(RemoteMessage message) {
        if (message.getNotification() == null
                || TextUtils.isEmpty(message.getNotification().getBody())) {
            if (message.getData() != null && message.getData().containsKey("body")) {
                return message.getData().get("body");
            } else {
                return "";
            }
        } else {
            return message.getNotification().getBody();
        }
    }

    private NotificationActionType getNotificationActionType(RemoteMessage message) {
        return NotificationActionType.value(message.getData().get("actiontype"));
    }

    public @Nullable
    NotificationEvent getEvent(RemoteMessage message) {
        NotificationActionType type = getNotificationActionType(message);
        if (type != null) {
            NotificationEvent event = type.getEvent();
            if (event != null) {
                event.message = message;
            }

            return event;
        }

        return null;
    }

    private NotificationManager getManager() {
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }
}