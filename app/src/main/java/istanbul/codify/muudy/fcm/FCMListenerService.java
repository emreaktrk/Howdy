package istanbul.codify.muudy.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.ui.splash.SplashActivity;


public final class FCMListenerService extends FirebaseMessagingService {

    private static final int NOTIFICATION_ID = 876;
    private static final String NOTIFICATION_CHANNEL_ID = "612";
    private static final String NOTIFICATION_CHANNEL_NAME = "Default";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        sendNotification(message);
    }

    private void sendNotification(RemoteMessage message) {
        NotificationManager manager = getManager();

        Intent intent = new Intent(this, SplashActivity.class);
        intent.setData(getUri(message));
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

    private Uri getUri(RemoteMessage message) {
        if (message.getNotification() == null
                || message.getNotification().getLink() == null) {
            if (message.getData() != null && message.getData().containsKey("link")) {
                String link = message.getData().get("link");
                return Uri.parse(link);
            } else {
                return Uri.EMPTY;
            }
        } else {
            return message.getNotification().getLink();
        }
    }

    private NotificationManager getManager() {
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }
}